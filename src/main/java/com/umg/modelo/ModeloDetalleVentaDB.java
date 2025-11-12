package com.umg.modelo;

public class ModeloDetalleVentaDB {
    private int secuencia;
    private int codigo_producto;
    private int cantidad;
    private float precio_bruto;
    private float descuentos;
    private float impuestos;

    public ModeloDetalleVentaDB() {
    }

    public ModeloDetalleVentaDB(int secuencia, int codigo_producto, int cantidad, float precio_bruto, float descuentos, float impuestos) {
        this.secuencia = secuencia;
        this.codigo_producto = codigo_producto;
        this.cantidad = cantidad;
        this.precio_bruto = precio_bruto;
        this.descuentos = descuentos;
        this.impuestos = impuestos;
    }

    
}
