package com.umg.implementacion;

import com.umg.interfaces.IReporte;
import com.umg.modelo.ModeloCategoria;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReporteImp implements IReporte {

    private Conector con = Sesion.getConexion(); // usuario y contraseña
    private Sql sql = new Sql();

    @Override
    public DefaultTableModel ventasDelDia() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"No.", "Cliente", "Fecha", "Producto", "Cantidad", "Precio", "Subtotal", "Totales"});
        try {
            PreparedStatement ps = con.preparar("""
    SELECT 
        v.secuencia,
        MAX(v.cliente) AS cliente,
        MAX(v.fecha_operacion) AS fecha_operacion,
        dv.codigo_producto,
        SUM(dv.cantidad) AS cantidad,
        MAX(dv.precio_bruto) AS precio,
        SUM(dv.cantidad * dv.precio_bruto) AS subtotal,
        CASE 
            WHEN GROUPING(v.secuencia) = 1 AND GROUPING(dv.codigo_producto) = 1 THEN 'TOTAL GENERAL'
            WHEN GROUPING(dv.codigo_producto) = 1 THEN 'TOTAL VENTA'
            ELSE NULL
        END AS tipo_fila
    FROM tellix.venta v
    JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
    WHERE TRUNC(v.fecha_operacion) = TRUNC(SYSDATE)
    GROUP BY ROLLUP(v.secuencia, dv.codigo_producto)
    ORDER BY MAX(v.fecha_operacion), v.secuencia, dv.codigo_producto
""");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),      // v.secuencia
                        rs.getString(2),   // v.cliente
                        rs.getDate(3),     // v.fecha_operacion
                        rs.getInt(4),      // dv.codigo_producto
                        rs.getInt(5),      // dv.cantidad
                        rs.getDouble(6),   // dv.precio_bruto
                        rs.getDouble(7),    // importe
                        rs.getString(8)    // totales
                });
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los datos: " + e.getMessage());
        }
        return model;
    }

    @Override
    public DefaultTableModel ventasRangoFechas(String fechaInicio, String fechaFin) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"No.", "Cliente", "Fecha", "Producto", "Cantidad", "Precio", "Subtotal", "Totales"});
        try {
            PreparedStatement ps = con.preparar("""
               SELECT
                   v.secuencia,
                   MAX(v.cliente) AS cliente,
                   MAX(v.fecha_operacion) AS fecha_operacion,
                   dv.codigo_producto,
                   SUM(dv.cantidad) AS cantidad,
                   MAX(dv.precio_bruto) AS precio,
                   SUM(dv.cantidad * dv.precio_bruto) AS subtotal,
                   CASE
                       WHEN GROUPING(v.secuencia) = 1 AND GROUPING(dv.codigo_producto) = 1 THEN 'TOTAL GENERAL'
                       WHEN GROUPING(dv.codigo_producto) = 1 THEN 'TOTAL VENTA'
                       ELSE NULL
                   END AS tipo_fila
               FROM tellix.venta v
               JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
               WHERE v.fecha_operacion BETWEEN ? AND ?
               GROUP BY ROLLUP(v.secuencia, dv.codigo_producto)
               ORDER BY MAX(v.fecha_operacion), v.secuencia, dv.codigo_producto
            """);
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),      // v.secuencia
                        rs.getString(2),   // v.cliente
                        rs.getDate(3),     // v.fecha_operacion
                        rs.getInt(4),      // dv.codigo_producto
                        rs.getInt(5),      // dv.cantidad
                        rs.getDouble(6),   // dv.precio_bruto
                        rs.getDouble(7),
                        rs.getString(8)
                });
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los datos: " + e.getMessage());
        }
        return model;
    }

    @Override
    public DefaultTableModel ventasMensuales() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mes", "Total"});
        try {
            PreparedStatement ps = con.preparar(sql.getREPORTE_VENTAS_MENSUALES());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getDouble(2)
                });
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los datos: " + e.getMessage());
        }
        return model;
    }

    @Override
    public DefaultTableModel ventasMensualesComparativo() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mes", "Total", "Mes Anterior", "% Variación"});

        try {
            PreparedStatement ps = con.preparar("""
            WITH ventas AS (
                SELECT
                    TO_CHAR(v.fecha_operacion, 'YYYY-MM') AS mes,
                    SUM(dv.cantidad * dv.precio_bruto) AS total
                FROM tellix.venta v
                JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
                GROUP BY TO_CHAR(v.fecha_operacion, 'YYYY-MM')
            )
            SELECT 
                mes,
                total,
                LAG(total, 1) OVER (ORDER BY mes) AS total_mes_anterior,
                ROUND(
                    ( (total - LAG(total,1) OVER (ORDER BY mes)) / 
                      NULLIF(LAG(total,1) OVER (ORDER BY mes), 0) ) * 100, 
                2) AS variacion_porcentual
            FROM ventas
            ORDER BY mes
        """);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("mes"),
                        rs.getDouble("total"),
                        rs.getDouble("total_mes_anterior"),
                        rs.getDouble("variacion_porcentual")
                });
            }

        } catch (Exception e) {
            System.out.println("Error en ventasMensualesComparativo: " + e.getMessage());
        }

        return model;
    }
    @Override
    public DefaultTableModel comprasDelDia() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"No.", "Proveedor", "Fecha", "Producto", "Cantidad", "Precio", "Subtotal", "Totales"});

        try {
            PreparedStatement ps = con.preparar("""
SELECT 
    c.no_documento,
    MAX(c.proveedor) AS proveedor,
    MAX(c.fecha_operacion) AS fecha_operacion,
    dc.codigo_producto,
    SUM(dc.cantidad) AS cantidad,
    MAX(dc.precio_bruto) AS precio,
    SUM(dc.cantidad * dc.precio_bruto) AS subtotal,
    CASE 
        WHEN GROUPING(c.no_documento) = 1 AND GROUPING(dc.codigo_producto) = 1 THEN 'TOTAL GENERAL'
        WHEN GROUPING(dc.codigo_producto) = 1 THEN 'TOTAL COMPRA'
        ELSE NULL
    END AS tipo_fila
FROM tellix.compra c
JOIN tellix.detalle_compra dc ON dc.no_documento = c.no_documento
WHERE TRUNC(c.fecha_operacion) = TRUNC(SYSDATE)
GROUP BY ROLLUP(c.no_documento, dc.codigo_producto)
ORDER BY MAX(c.fecha_operacion), c.no_documento, dc.codigo_producto
""");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getDouble(6),
                        rs.getDouble(7),
                        rs.getString(8)
                });
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo compras del día: " + e.getMessage());
        }

        return model;
    }


    @Override
    public DefaultTableModel comprasPorRangoFechas(String fechaInicio, String fechaFin) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "No. Documento", "Proveedor", "Fecha",
                "Producto", "Cantidad", "Precio", "Subtotal", "Totales"
        });

        try {
            PreparedStatement ps = con.preparar("""
            SELECT
                c.no_documento,
                MAX(c.proveedor) AS proveedor,
                MAX(c.fecha_operacion) AS fecha_operacion,
                dc.codigo_producto,
                SUM(dc.cantidad) AS cantidad,
                MAX(dc.precio_bruto) AS precio,
                SUM(dc.cantidad * dc.precio_bruto) AS subtotal,
                CASE
                    WHEN GROUPING(c.no_documento) = 1 AND GROUPING(dc.codigo_producto) = 1 THEN 'TOTAL GENERAL'
                    WHEN GROUPING(dc.codigo_producto) = 1 THEN 'TOTAL COMPRA'
                    ELSE NULL
                END AS tipo_fila
            FROM tellix.compra c
            JOIN tellix.detalle_compra dc ON dc.no_documento = c.no_documento
            WHERE c.fecha_operacion BETWEEN ? AND ?
            GROUP BY ROLLUP(c.no_documento, dc.codigo_producto)
            ORDER BY MAX(c.fecha_operacion), c.no_documento, dc.codigo_producto
        """);

            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("no_documento"),
                        rs.getString("proveedor"),
                        rs.getDate("fecha_operacion"),
                        rs.getObject("codigo_producto"), // null en filas de totales
                        rs.getObject("cantidad"),
                        rs.getObject("precio"),
                        rs.getObject("subtotal"),
                        rs.getString("tipo_fila")
                });
            }

        } catch (Exception e) {
            System.out.println("Error en comprasPorRangoFechas: " + e.getMessage());
        }

        return model;
    }


    @Override
    public DefaultTableModel comprasPorProveedor() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Proveedor", "Nombre", "Total Compras", "Total Pagado", "Saldo Pendiente"});

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                p.nit_proveedor,
                p.nombre AS proveedor,
                SUM(dc.cantidad * dc.precio_bruto) AS total_compras,
                SUM(cp.valor_pagado) AS total_pagado,
                SUM(dc.cantidad * dc.precio_bruto) - SUM(cp.valor_pagado) AS saldo_pendiente
            FROM tellix.proveedor p
            JOIN tellix.compra c ON c.proveedor = p.nit_proveedor
            JOIN tellix.detalle_compra dc ON dc.no_documento = c.no_documento
            JOIN tellix.cuenta_por_pagar cp ON cp.no_documento = c.no_documento
            GROUP BY p.nit_proveedor, p.nombre
            ORDER BY saldo_pendiente DESC
        """);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nit_proveedor"),
                        rs.getString("proveedor"),
                        rs.getDouble("total_compras"),
                        rs.getDouble("total_pagado"),
                        rs.getDouble("saldo_pendiente")
                });
            }

        } catch (Exception e) {
            System.out.println("Error en comprasPorProveedor: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel mejoresClientesPorMonto(String fechaInicio, String fechaFin) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"NIT", "Nombre", "Total Comprado"});

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                c.nit,
                c.nombre_1 || ' ' || c.apellido_1 AS nombre,
                SUM(dv.cantidad * dv.precio_bruto) AS total_comprado
            FROM tellix.cliente c
            JOIN tellix.venta v ON v.cliente = c.nit
            JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
            WHERE v.fecha_operacion BETWEEN ? AND ?
            GROUP BY c.nit, c.nombre_1, c.apellido_1
            ORDER BY total_comprado DESC
        """);

            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nit"),
                        rs.getString("nombre"),
                        rs.getDouble("total_comprado")
                });
            }

        } catch (Exception e) {
            System.out.println("Error en mejoresClientesPorMonto: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel mejoresClientesPorFacturas(String inicio, String fin) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"NIT", "Nombre", "Cantidad Facturas"});

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                c.nit,
                c.nombre_1 || ' ' || c.apellido_1 AS nombre,
                COUNT(v.secuencia) AS total_facturas
            FROM tellix.cliente c
            JOIN tellix.venta v ON v.cliente = c.nit
            WHERE v.fecha_operacion BETWEEN ? AND ?
            GROUP BY c.nit, c.nombre_1, c.apellido_1
            ORDER BY total_facturas DESC
        """);

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nit"),
                        rs.getString("nombre"),
                        rs.getInt("total_facturas")
                });
            }

        } catch (Exception e) {
            System.out.println("Error mejoresClientesPorFacturas: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel productosMasVendidosCantidad(String ini, String fin) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Código", "Producto", "Cantidad Vendida"});

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                p.codigo,
                p.nombre,
                SUM(dv.cantidad) AS total_vendida
            FROM tellix.producto p
            JOIN tellix.detalle_venta dv ON dv.codigo_producto = p.codigo
            JOIN tellix.venta v ON v.secuencia = dv.secuencia
            WHERE v.fecha_operacion BETWEEN ? AND ?
            GROUP BY p.codigo, p.nombre
            ORDER BY total_vendida DESC
        """);

            ps.setDate(1, Date.valueOf(ini));
            ps.setDate(2, Date.valueOf(fin));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("total_vendida")
                });
            }

        } catch (Exception e) {
            System.out.println("Error productosMasVendidosCantidad: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel productosMasVendidosMonto(String ini, String fin) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Código", "Producto", "Monto Generado"});

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                p.codigo,
                p.nombre,
                SUM(dv.cantidad * dv.precio_bruto) AS total_generado
            FROM tellix.producto p
            JOIN tellix.detalle_venta dv ON dv.codigo_producto = p.codigo
            JOIN tellix.venta v ON v.secuencia = dv.secuencia
            WHERE v.fecha_operacion BETWEEN ? AND ?
            GROUP BY p.codigo, p.nombre
            ORDER BY total_generado DESC
        """);

            ps.setDate(1, Date.valueOf(ini));
            ps.setDate(2, Date.valueOf(fin));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("total_generado")
                });
            }

        } catch (Exception e) {
            System.out.println("Error productosMasVendidosMonto: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel productosStockBajo() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Código", "Producto", "Stock Actual", "Stock Mínimo"});

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                codigo,
                nombre,
                stock_actual,
                stock_minimo
            FROM tellix.producto
            WHERE stock_actual <= stock_minimo
            ORDER BY stock_actual ASC
        """);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo")
                });
            }

        } catch (Exception e) {
            System.out.println("Error productosStockBajo: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel cuentasPorCobrar() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "Correlativo", "Venta", "NIT", "Cliente",
                "Total", "Pagado", "Fecha Límite", "Estado"
        });

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                cxc.correlativo,
                cxc.secuencia,
                cxc.cliente_nit,
                c.nombre_1 || ' ' || c.apellido_1 AS cliente,
                cxc.valor_total,
                cxc.valor_pagado,
                cxc.fecha_limite,
                CASE 
                    WHEN cxc.fecha_limite < TRUNC(SYSDATE) THEN 'VENCIDA'
                    WHEN cxc.fecha_limite BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE + 3) THEN 'POR VENCER'
                    ELSE 'AL DÍA'
                END AS estado_plazo
            FROM tellix.cuenta_por_cobrar cxc
            JOIN tellix.cliente c ON cxc.cliente_nit = c.nit
            ORDER BY cxc.fecha_limite
        """);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("correlativo"),
                        rs.getInt("secuencia"),
                        rs.getString("cliente_nit"),
                        rs.getString("cliente"),
                        rs.getDouble("valor_total"),
                        rs.getDouble("valor_pagado"),
                        rs.getDate("fecha_limite"),
                        rs.getString("estado_plazo")
                });
            }

        } catch (Exception e) {
            System.out.println("Error cuentasPorCobrar: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel cuentasPorPagar() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "Correlativo", "Compra", "Proveedor", "Total", "Pagado", "Fecha Límite", "Estado"
        });

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                cxp.correlativo,
                cxp.no_documento,
                p.nombre AS proveedor,
                cxp.valor_total,
                cxp.valor_pagado,
                cxp.fecha_limite,
                CASE 
                    WHEN cxp.fecha_limite < TRUNC(SYSDATE) THEN 'VENCIDA'
                    WHEN cxp.fecha_limite BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE + 3) THEN 'POR VENCER'
                    ELSE 'AL DÍA'
                END AS estado_plazo
            FROM tellix.cuenta_por_pagar cxp
            JOIN tellix.proveedor p ON p.nit_proveedor = (
                SELECT proveedor FROM tellix.compra WHERE no_documento = cxp.no_documento
            )
            ORDER BY cxp.fecha_limite
        """);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("correlativo"),
                        rs.getInt("no_documento"),
                        rs.getString("proveedor"),
                        rs.getDouble("valor_total"),
                        rs.getDouble("valor_pagado"),
                        rs.getDate("fecha_limite"),
                        rs.getString("estado_plazo")
                });
            }

        } catch (Exception e) {
            System.out.println("Error cuentasPorPagar: " + e.getMessage());
        }

        return model;
    }

    @Override
    public DefaultTableModel movimientosInventario(String ini, String fin, String usuario) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "Producto", "Nombre", "Mov.", "Cantidad", "Motivo",
                "Operación", "Usuario", "Fecha", "Hora"
        });

        try {
            PreparedStatement ps = con.preparar("""
            SELECT 
                i.codigo_producto,
                p.nombre,
                i.correlativo,
                i.cantidad_afectada,
                i.motivo,
                i.operacion,
                i.usuario,
                i.fecha_operacion,
                i.hora_operacion
            FROM tellix.inventario i
            JOIN tellix.producto p ON p.codigo = i.codigo_producto
            WHERE i.fecha_operacion BETWEEN ? AND ?
              AND (i.usuario = ? OR ? = 'TODOS')
            ORDER BY i.fecha_operacion, i.hora_operacion
        """);

            ps.setDate(1, Date.valueOf(ini));
            ps.setDate(2, Date.valueOf(fin));
            ps.setString(3, usuario);
            ps.setString(4, usuario);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("codigo_producto"),
                        rs.getString("nombre"),
                        rs.getInt("correlativo"),
                        rs.getInt("cantidad_afectada"),
                        rs.getString("motivo"),
                        rs.getString("operacion"),
                        rs.getString("usuario"),
                        rs.getDate("fecha_operacion"),
                        rs.getTimestamp("hora_operacion")
                });
            }

        } catch (Exception e) {
            System.out.println("Error movimientosInventario: " + e.getMessage());
        }

        return model;
    }


}
