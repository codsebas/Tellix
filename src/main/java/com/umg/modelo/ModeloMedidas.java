package com.umg.modelo;

import com.umg.vistas.VistaMedidas;

public class ModeloMedidas {
    VistaMedidas vista;

    private String codigo;
    private String descripcion;

    public ModeloMedidas(VistaMedidas vista) {
        this.vista = vista;
    }
    public ModeloMedidas(){

    }
    public VistaMedidas getVista() {
        return vista;
    }

    public void setVista(VistaMedidas vista) {
        this.vista = vista;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
