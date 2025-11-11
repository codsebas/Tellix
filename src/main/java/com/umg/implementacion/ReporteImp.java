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
}
