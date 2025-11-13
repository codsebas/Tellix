package com.umg.interfaces;
import com.umg.modelo.ModeloVentaDB;
import com.umg.modelo.ModeloDetalleVentaDB;
import oracle.sql.TIMESTAMP;
import java.util.Date;

public interface IVenta {
    boolean insertarVenta(ModeloVentaDB modelo, ModeloDetalleVentaDB modeloDetalle);
    boolean eliminarVenta(int secuencia);
    boolean insertarDetalleVenta(ModeloDetalleVentaDB modelo);
    boolean eliminarDetallesVenta(int secuencia);
    boolean eliminarDetalleVenta(int secuencia, int correlativo);
    boolean seleccionarVenta(int secuencia);
    boolean seleccionarDetalleVenta(int seccuencia);
}
