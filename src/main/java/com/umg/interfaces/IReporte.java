package com.umg.interfaces;

import javax.swing.table.DefaultTableModel;

public interface IReporte {
    DefaultTableModel ventasDelDia();
    DefaultTableModel ventasRangoFechas(String fechaInicio, String fechaFin);
    DefaultTableModel ventasMensuales();
    public DefaultTableModel ventasMensualesComparativo();
    public DefaultTableModel comprasDelDia();
    public DefaultTableModel comprasPorRangoFechas(String fechaInicio, String fechaFin);
    public DefaultTableModel comprasPorProveedor();
    public DefaultTableModel mejoresClientesPorMonto(String fechaInicio, String fechaFin);
    public DefaultTableModel mejoresClientesPorFacturas(String inicio, String fin);
    public DefaultTableModel productosMasVendidosCantidad(String ini, String fin);
    public DefaultTableModel productosMasVendidosMonto(String ini, String fin);
    public DefaultTableModel productosStockBajo();
    public DefaultTableModel cuentasPorCobrar();
    public DefaultTableModel cuentasPorPagar();
    public DefaultTableModel movimientosInventario(String ini, String fin, String usuario);
}
