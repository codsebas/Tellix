package com.umg.modelo;

import com.umg.vistas.VistaMarcas;

public class ModeloMarcas {
    VistaMarcas vista;

    private String marca;
    private String descripcion;

    public ModeloMarcas(VistaMarcas vista) {
        this.vista = vista;
    }
    public ModeloMarcas(){

    }
    public VistaMarcas getVista() {
        return vista;
    }

    public void setVista(VistaMarcas vista) {
        this.vista = vista;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}
