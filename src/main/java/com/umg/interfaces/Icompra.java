package com.umg.interfaces;

import java.sql.SQLException;

public interface Icompra {
    String registrarCompra(CompraDto compra, List<DetalleCompraDto> detalles) throws SQLException;
    void registrarPagoProveedor(String noDocumento, PagoProveedorDto pago) throws SQLException;
}
