package com.umg.implementacion;
import com.umg.interfaces.IVenta;
import com.umg.modelo.*;
import com.umg.seguridad.Sesion;
import sql.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VentaImp implements IVenta {

    private final Conector con = Sesion.getConexion();
    private final QuerysVentas querys = new QuerysVentas();
    private final query_vistas_db vistas = new query_vistas_db();

    @Override
    public boolean insertarVenta(ModeloVentaDB venta, List<ModeloDetalleVentaDB> detalle) {
        boolean resultado = false;

        try {
            PreparedStatement ps = con.prepararVenta(querys.getInsertVenta(), true);

            ps.setString(1, venta.getNit());
            ps.setDate(2, venta.getFechaOperacion());
            ps.setTimestamp(3, venta.getHoraOperacion());
            ps.setString(4, venta.getUsuarioSistema());
            ps.setInt(5, venta.getMetodoPago());
            ps.setInt(6, venta.getPlazoCredito());
            ps.setString(7, venta.getTipoPlazo());
            ps.setString(8, venta.getEstado());
            ps.setFloat(9, venta.getTotal());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                resultado = false;
            } else {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int sec = rs.getInt(1);

                        for (ModeloDetalleVentaDB det : detalle) {
                            det.setSecuencia(sec);
                        }

                        resultado = insertarDetalleVenta(detalle);
                    } else {
                        resultado = false;
                    }
                } catch (Exception e) {
                    System.out.println("Error al obtener llave generada: " + e.getMessage());
                    resultado = false;
                }
            }

        } catch (Exception e) {
            System.out.println("No se pudo insertar venta: " + e.getMessage());
            resultado = false;
        }

        return resultado;
    }


    @Override
    public boolean eliminarVenta(int secuencia) {
        return false;
    }

    @Override
    public boolean insertarDetalleVenta(List<ModeloDetalleVentaDB> modelo) {
        if (modelo == null || modelo.isEmpty()) {
            return false;
        }

        boolean resultado = true;

        String sql = querys.getInsertDetalleVenta();

        try {
            PreparedStatement ps = con.preparar(sql);

            for (ModeloDetalleVentaDB det : modelo) {
                ps.setInt(1, det.getSecuencia());
                ps.setInt(2, det.getCodigo_producto());
                ps.setInt(3, det.getCantidad());
                ps.setFloat(4, det.getPrecio_bruto());
                ps.setFloat(5, det.getDescuentos());
                ps.setFloat(6, det.getImpuestos());

                ps.addBatch();
            }

            int[] filas = ps.executeBatch();

            for (int f : filas) {
                if (f == PreparedStatement.EXECUTE_FAILED || f == 0) {
                    resultado = false;
                    break;
                }
            }

            if(!resultado){
                return false;
            }

            for(ModeloDetalleVentaDB det : modelo){
                int codigoProd = det.getCodigo_producto();
                int cantidad = det.getCantidad();

                boolean invOk = insertarInventario(codigoProd, cantidad);
                boolean stockOk = actualizarStock(codigoProd, cantidad);

                if(!invOk || !stockOk){
                    resultado =  false;
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("No se pudo insertar detalle de venta: " + e.getMessage());
            resultado = false;
        }

        return resultado;
    }


    @Override
    public boolean eliminarDetallesVenta(int secuencia) {
        return false;
    }

    @Override
    public boolean eliminarDetalleVenta(int secuencia, int correlativo) {
        return false;
    }

    @Override
    public boolean seleccionarVenta(int secuencia) {
        return false;
    }

    @Override
    public boolean seleccionarDetalleVenta(int seccuencia) {
        return false;
    }

    @Override
    public ModeloResumProd seleccionarProducto(int codigo) {
        try {
            PreparedStatement ps = con.preparar(vistas.getSELECT_APLICACION());
            ps.setInt(1, codigo);
            ps.setString(2, "V");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ModeloResumProd modelo = new ModeloResumProd();

                modelo.setCodigo(rs.getInt("codigo"));
                modelo.setNombre(rs.getString("nombre"));
                modelo.setStockDisponible(rs.getInt("stock_disponible"));
                modelo.setStockMinimo(rs.getInt("stock_minimo"));
                modelo.setStockBajoMinimo(rs.getString("stock_bajo_minimo"));
                modelo.setAplicacion(rs.getString("aplicacion"));
                modelo.setPrecioBase(rs.getFloat("precio_base"));
                modelo.setTotalImpuestos(rs.getFloat("total_impuestos"));
                modelo.setTotalDescuentos(rs.getFloat("total_descuentos"));
                modelo.setPrecioFinal(rs.getFloat("precio_final"));

                return modelo;
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al seleccionar producto con código " + codigo, e);
        }
    }

    @Override
    public ModeloClienteVistaRes seleccionarCliente(String codigo) {
        try {
            PreparedStatement ps = con.preparar(vistas.getSELECT_CLIENTE_NIT());
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ModeloClienteVistaRes modelo = new ModeloClienteVistaRes();

                modelo.setNit(rs.getString("NIT"));
                modelo.setNombre(rs.getString("nombre_completo"));

                return modelo;
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al seleccionar producto con código " + codigo, e);
        }
    }

    @Override
    public boolean insertarInventario(int codigo, int cantidad) {
        boolean resultado = false;
        try{
            PreparedStatement ps = con.preparar(querys.getINSERTAR_INVENTARIO());
            ps.setInt(1, codigo);
            ps.setInt(2, cantidad);
            ps.setString(3, "Venta de productos");
            ps.setString(4, "V");
            ps.setString(5, Sesion.getUsuario());
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
        } catch(Exception e){
        }
        return resultado;
    }

    @Override
    public boolean actualizarStock(int codigo, int stock) {
        boolean resultado = false;
        try{
            ModeloResumProd producto = seleccionarProducto(codigo);
            int stockNuevo = producto.getStockDisponible() - stock;

            PreparedStatement ps = con.preparar(querys.getACTUALIZAR_STOCK_PRODUCTO());
            ps.setInt(1, stockNuevo);
            ps.setInt(2, codigo);
            int files = ps.executeUpdate();
            resultado = (files > 0);
        } catch(Exception e){
            throw new RuntimeException("Error al actualizar stock", e);
        }
        return resultado;
    }

    @Override
    public boolean insertarCuentaPorCobrar() {
        return false;
    }

    @Override
    public List<ModeloMetodoPagoDB> seleccionarMetodosPago() {
        List<ModeloMetodoPagoDB> lista = new ArrayList<>();

        try {
            PreparedStatement ps = con.preparar(querys.getSELECT_ALL_METODOS_PAGO());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ModeloMetodoPagoDB modelo = new ModeloMetodoPagoDB();

                modelo.setCodigo(rs.getInt("codigo"));
                modelo.setMetodo_pago(rs.getString("descripcion"));

                lista.add(modelo);
            }

            return lista;

        } catch (Exception e) {
            throw new RuntimeException("Error al seleccionar métodos de pago", e);
        }
    }

}
