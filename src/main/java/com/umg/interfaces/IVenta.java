package com.umg.interfaces;
import com.umg.modelo.ModeloClienteVistaRes;
import com.umg.modelo.ModeloVentaDB;
import com.umg.modelo.ModeloDetalleVentaDB;
import com.umg.modelo.ModeloResumProd;
import java.util.List;

public interface IVenta {
    boolean insertarVenta(ModeloVentaDB modelo, List<ModeloDetalleVentaDB> modeloDetalle);
    boolean eliminarVenta(int secuencia);
    boolean insertarDetalleVenta(List<ModeloDetalleVentaDB> modelo);
    boolean eliminarDetallesVenta(int secuencia);
    boolean eliminarDetalleVenta(int secuencia, int correlativo);
    boolean seleccionarVenta(int secuencia);
    boolean seleccionarDetalleVenta(int seccuencia);
    ModeloResumProd seleccionarProducto(int codigo);
    ModeloClienteVistaRes seleccionarCliente(String codigo);
}
