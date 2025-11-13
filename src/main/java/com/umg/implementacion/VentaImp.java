package com.umg.implementacion;
import com.umg.interfaces.IVenta;
import com.umg.modelo.ModeloDetalleVentaDB;
import com.umg.modelo.ModeloVentaDB;
import com.umg.seguridad.Sesion;
import sql.*;

import java.sql.PreparedStatement;

public class VentaImp implements IVenta {

    private final Conector con = Sesion.getConexion();
    private final QuerysVentas querys = new QuerysVentas();

    @Override
    public boolean insertarVenta(ModeloVentaDB venta, ModeloDetalleVentaDB detalle) {
        try{
            PreparedStatement ps = con.preparar(querys.getInsertVenta(), true);
            ps.setString(1, venta.getNit());
            ps.setDate(2, venta.getFechaOperacion());
            ps.setTimestamp(3,venta.getHoraOperacion());
            ps.setString(4, venta.getUsuarioSistema());
            ps.setInt(5, venta.getMetodoPago());
            ps.setInt(6, venta.getPlazoCredito());
            ps.setString(7, venta.getTipoPlazo());
            ps.setString(8, venta.getEstado());
            ps.setFloat(9, venta.getTotal());
            int filas = ps.executeUpdate();

            if(filas == 0){
                return false;
            } else
                return true;

        } catch (Exception e){
            System.out.println("No se pudo insertar venta");
            return false;
        }
    }

    @Override
    public boolean eliminarVenta(int secuencia) {
        return false;
    }

    @Override
    public boolean insertarDetalleVenta(ModeloDetalleVentaDB modelo) {
        return false;
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
}
