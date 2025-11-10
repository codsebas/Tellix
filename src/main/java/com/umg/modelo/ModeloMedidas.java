package com.umg.modelo;

import com.umg.vistas.VistaMarcas;

public class ModeloMedidas {
    VistaMarcas vista;

    private String codigo;
    private String descripcion;

    public ModeloMedidas(VistaMarcas vista) {
        this.vista = vista;
    }
    public ModeloMedidas(){

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
