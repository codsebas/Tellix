package com.umg.modelo;

import com.umg.vistas.VistaProductos;

public class ModeloProducto {
    // Referencia a la vista
    private VistaProductos vista;

    // Atributos del producto
    private int codigo;
    private String nombre;
    private String descripcion;
    private int stockMinimo;
    private int stockActual;
    private double precio;
    private String categoria;
    private String marca;
    private String medida;
    private double cantidad;

    // Constructores
    public ModeloProducto() { }

    public ModeloProducto(VistaProductos vista) {
        this.vista = vista;
    }

    public ModeloProducto(int codigo, String nombre, String descripcion, int stockMinimo, int stockActual,
                          double precio, String categoria, String marca, String medida, double cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockMinimo = stockMinimo;
        this.stockActual = stockActual;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca;
        this.medida = medida;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public VistaProductos getVista() { return vista; }
    public void setVista(VistaProductos vista) { this.vista = vista; }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
}
