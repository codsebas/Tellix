package com.umg.modelo;

public class ModeloResumProd {
    private int codigo;
    private String nombre;
    private int stockDisponible;
    private int stockMinimo;
    private String stockBajoMinimo; // 'S' / 'N'
    private String aplicacion;      // 'V' o 'C'
    private float precioBase;
    private float totalImpuestos;
    private float totalDescuentos;
    private float precioFinal;

    public ModeloResumProd() {
    }

    public ModeloResumProd(int codigo, String nombre, int stockDisponible, int stockMinimo, String stockBajoMinimo, String aplicacion, float precioBase, float totalImpuestos, float totalDescuentos, float precioFinal) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stockDisponible = stockDisponible;
        this.stockMinimo = stockMinimo;
        this.stockBajoMinimo = stockBajoMinimo;
        this.aplicacion = aplicacion;
        this.precioBase = precioBase;
        this.totalImpuestos = totalImpuestos;
        this.totalDescuentos = totalDescuentos;
        this.precioFinal = precioFinal;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getStockBajoMinimo() {
        return stockBajoMinimo;
    }

    public void setStockBajoMinimo(String stockBajoMinimo) {
        this.stockBajoMinimo = stockBajoMinimo;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public float getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(float precioBase) {
        this.precioBase = precioBase;
    }

    public float getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(float totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public float getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(float totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public float getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(float precioFinal) {
        this.precioFinal = precioFinal;
    }
}
