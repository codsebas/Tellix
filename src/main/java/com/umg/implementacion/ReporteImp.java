package com.umg.implementacion;

import com.umg.interfaces.IReporte;
import com.umg.modelo.ModeloCategoria;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReporteImp implements IReporte {

    private Conector con = Sesion.getConexion(); // usuario y contraseña
    private Sql sql = new Sql();

    @Override
    public DefaultTableModel ventasDelDia() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"No.", "Cliente", "Fecha", "Producto", "Cantidad", "Precio", "Subtotal"});
        try {
            PreparedStatement ps = con.preparar(sql.getREPORTE_VENTAS_DIA());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),      // v.secuencia
                        rs.getString(2),   // v.cliente
                        rs.getDate(3),     // v.fecha_operacion
                        rs.getInt(4),      // dv.codigo_producto
                        rs.getInt(5),      // dv.cantidad
                        rs.getDouble(6),   // dv.precio_bruto
                        rs.getDouble(7)    // importe
                });
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los datos: " + e.getMessage());
        }
        return model;
    }
}
