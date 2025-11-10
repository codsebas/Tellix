package com.umg.modelo;

import com.umg.vistas.VistaProductos;

public class ModeloProducto {
    private VistaProductos vista;

    private int codigo;
    private String nombre;
    private String descripcion;
    private int stockMinimo;
    private int stockActual;
    private String estado;
    private int fkCategoria;
    private String fkMarca;
    private String fkMedida;
    private double cantidadMedida;

    public ModeloProducto(VistaProductos vista) {
        this.vista = vista;
    }

    public ModeloProducto() {
    }

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

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getFkCategoria() { return fkCategoria; }
    public void setFkCategoria(int fkCategoria) { this.fkCategoria = fkCategoria; }

    public String getFkMarca() { return fkMarca; }
    public void setFkMarca(String fkMarca) { this.fkMarca = fkMarca; }

    public String getFkMedida() { return fkMedida; }
    public void setFkMedida(String fkMedida) { this.fkMedida = fkMedida; }

    public double getCantidadMedida() { return cantidadMedida; }
    public void setCantidadMedida(double cantidadMedida) { this.cantidadMedida = cantidadMedida; }
}
