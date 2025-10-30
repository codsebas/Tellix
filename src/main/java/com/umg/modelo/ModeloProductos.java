package com.umg.modelo;

import com.umg.vistas.VistaProductos;

public class ModeloProductos {
    VistaProductos vista;

    public ModeloProductos() {
    }

    public ModeloProductos(VistaProductos vista) {
        this.vista = vista;
    }

    public VistaProductos getVista() {
        return vista;
    }

    public void setVista(VistaProductos vista) {
        this.vista = vista;
    }
}
