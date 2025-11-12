package com.umg.modelo;

public class ModeloDetalleCompraDB {
    private int no_documento;
    private int cod_producto;
    private int cantidad;
    private float precio_bruto;
    private float descuentos;
    private float impuestos;

    public ModeloDetalleCompraDB() {
    }

    public ModeloDetalleCompraDB(int no_documento, int cod_producto, int cantidad, float precio_bruto, float descuentos, float impuestos) {
        this.no_documento = no_documento;
        this.cod_producto = cod_producto;
        this.cantidad = cantidad;
        this.precio_bruto = precio_bruto;
        this.descuentos = descuentos;
        this.impuestos = impuestos;
    }

    public int getNo_documento() {
        return no_documento;
    }

    public void setNo_documento(int no_documento) {
        this.no_documento = no_documento;
    }

    public int getCod_producto() {
        return cod_producto;
    }

    public void setCod_producto(int cod_producto) {
        this.cod_producto = cod_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio_bruto() {
        return precio_bruto;
    }

    public void setPrecio_bruto(float precio_bruto) {
        this.precio_bruto = precio_bruto;
    }

    public float getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(float descuentos) {
        this.descuentos = descuentos;
    }

    public float getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(float impuestos) {
        this.impuestos = impuestos;
    }
}
