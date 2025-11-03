package com.umg.implementacion;

import com.umg.interfaces.Icompra;
import sql.Sql;

import java.math.BigDecimal;
import java.sql.*;

public class CompraImp implements Icompra {

    /**
     * Crea la compra (contado o crédito), inserta detalles, afecta inventario (bitácora + stock_actual)
     * y genera CxP si es crédito. Devuelve el no_documento.
     */
    public String crearCompra(CompraDto compra, List<DetalleCompraDto> detalles) throws SQLException {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La compra debe tener al menos un detalle.");
        }

        try (Connection cn = Conector.getConnection()) {
            cn.setAutoCommit(false);
            try {
                // 1) Cabecera compra
                try (PreparedStatement ps = cn.prepareStatement(Sql.getInsertCompra())) {
                    ps.setString(1, compra.noDocumento());
                    ps.setString(2, compra.nitProveedor());
                    ps.setString(3, compra.nitRepresentante());
                    ps.setDate  (4, Date.valueOf(compra.fecha()));
                    ps.setLong  (5, compra.usuarioSistema());
                    ps.setLong  (6, compra.metodoPago());
                    ps.setObject(7, compra.plazoCredito());
                    ps.setString(8, compra.tipoPlazo());
                    ps.setString(9, compra.esCredito()? "P" : "G"); // P=pendiente, G=pagada
                    ps.executeUpdate();
                }

                // 2) Detalles + inventario + stock
                for (DetalleCompraDto d : detalles) {
                    // correlativo de detalle
                    int corr;
                    try (PreparedStatement ps = cn.prepareStatement(Sql.getNextCorrDetalle())) {
                        ps.setString(1, compra.noDocumento());
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next(); corr = rs.getInt(1);
                        }
                    }

                    // inserta detalle
                    try (PreparedStatement ps = cn.prepareStatement(Sql.getInsertDetalleCompra())) {
                        ps.setString(1, compra.noDocumento());
                        ps.setInt   (2, corr);
                        ps.setLong  (3, d.codigoProducto());
                        ps.setBigDecimal(4, d.cantidad());
                        ps.setBigDecimal(5, d.precioBruto());
                        ps.setBigDecimal(6, d.descuentos());
                        ps.setBigDecimal(7, d.impuestos());
                        ps.executeUpdate();
                    }

                    // correlativo inventario por producto
                    int corrInv;
                    try (PreparedStatement ps = cn.prepareStatement(Sql.getNextCorrInventario())) {
                        ps.setLong(1, d.codigoProducto());
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next(); corrInv = rs.getInt(1);
                        }
                    }

                    // bitácora inventario (entrada por compra)
                    try (PreparedStatement ps = cn.prepareStatement(Sql.getInsertMovInventario())) {
                        ps.setLong(1, d.codigoProducto());
                        ps.setInt (2, corrInv);
                        ps.setBigDecimal(3, d.cantidad());
                        ps.setString(4, "COMPRA " + compra.noDocumento());
                        ps.setString(5, "ENTRADA");
                        ps.setLong(6, compra.usuarioSistema());
                        ps.executeUpdate();
                    }

                    // stock_actual += cantidad
                    try (PreparedStatement ps = cn.prepareStatement(Sql.getUpdateStockProducto())) {
                        ps.setBigDecimal(1, d.cantidad());
                        ps.setLong(2, d.codigoProducto());
                        ps.executeUpdate();
                    }
                }

                // 3) Si es CRÉDITO => crear CxP con el total calculado
                if (compra.esCredito()) {
                    BigDecimal total;
                    try (PreparedStatement ps = cn.prepareStatement(Sql.getTotalCompra())) {
                        ps.setString(1, compra.noDocumento());
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next(); total = rs.getBigDecimal(1);
                        }
                    }

                    try (PreparedStatement ps = cn.prepareStatement(Sql.getInsertCxp())) {
                        ps.setString(1, compra.noDocumento());
                        ps.setString(2, "P"); // pendiente
                        ps.setLong  (3, compra.metodoPago());
                        ps.setBigDecimal(4, total);
                        Date fechaLim = Date.valueOf(
                                compra.fecha().plusDays(compra.plazoCredito() == null ? 0 : compra.plazoCredito()));
                        ps.setDate(5, fechaLim);
                        ps.setString(6, compra.numeroCuenta());
                        ps.setString(7, compra.banco());
                        ps.executeUpdate();
                    }
                } else {
                    // contado -> asegurar estado 'G' en CxP si existiera un registro
                    try (PreparedStatement ps = cn.prepareStatement(Sql.getUpdateCxpEstado())) {
                        ps.setString(1, "G");
                        ps.setString(2, compra.noDocumento());
                        ps.executeUpdate();
                    }
                }

                cn.commit();
                return compra.noDocumento();

            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    /**
     * Registra un pago a proveedor (movimiento bancario tipo 'CXP') y actualiza el estado de la CxP:
     * G=pagada cuando los abonos >= total compra; A=parcial en caso contrario.
     */
    public void abonarCompra(String noDocumento, PagoProveedorDto pago) throws SQLException {
        if (pago == null || pago.monto() == null || pago.monto().signum() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser positivo.");
        }

        try (Connection cn = Conector.getConnection()) {
            cn.setAutoCommit(false);
            try {
                // 1) Registrar movimiento de salida
                try (PreparedStatement ps = cn.prepareStatement(Sql.getInsertMovimientoCuenta())) {
                    ps.setString(1, pago.cuentaNumero());
                    ps.setString(2, "CXP");
                    ps.setBigDecimal(3, pago.monto());
                    ps.setString(4, noDocumento); // guardamos el no_documento en 'descripcion'
                    ps.executeUpdate();
                }

                // 2) Obtener total pagado y total compra
                BigDecimal totalPagado;
                try (PreparedStatement ps = cn.prepareStatement(Sql.getSumPagosCompra())) {
                    ps.setString(1, noDocumento);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next(); totalPagado = rs.getBigDecimal(1);
                    }
                }

                BigDecimal totalCompra;
                try (PreparedStatement ps = cn.prepareStatement(Sql.getTotalCompra())) {
                    ps.setString(1, noDocumento);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next(); totalCompra = rs.getBigDecimal(1);
                    }
                }

                // 3) Actualizar estado de CxP
                String estado = (totalPagado.compareTo(totalCompra) >= 0) ? "G" : "A"; // G=pagada, A=parcial
                try (PreparedStatement ps = cn.prepareStatement(Sql.getUpdateCxpEstado())) {
                    ps.setString(1, estado);
                    ps.setString(2, noDocumento);
                    ps.executeUpdate();
                }

                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    @Override
    public String registrarCompra(CompraDto compra, List<DetalleCompraDto> detalles) throws SQLException {
        return "";
    }

    @Override
    public void registrarPagoProveedor(String noDocumento, PagoProveedorDto pago) throws SQLException {

    }
}