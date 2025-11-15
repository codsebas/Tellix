package com.umg.interfaces;
import com.umg.modelo.*;

import java.util.List;

public interface ICompra {
    boolean insertarCompra(ModeloComprasDB compra, List<ModeloDetalleCompraDB> detalle, ModeloCuentasXPagarDB cuentas);
    boolean insertarDetalleCompra(List<ModeloDetalleCompraDB> detalle);
    ModeloResumProd seleccionarProducto(int codigo);
    boolean insertarInventario(int codigo, int cantidad);
    boolean actualizarStock(int codigo, int cantidad);
    List<ModeloProveedoresDB> seleccionarProveedores();
    List<ModeloRepresentanteDB> seleccionarRepresentantes(String proveedor);
    List<ModeloMetodoPagoDB> seleccionarMetodosPago();
    boolean insertarCuentaxPagar(ModeloCuentasXPagarDB modelo);
}
