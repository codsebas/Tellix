package com.umg.modelo;

import com.umg.vistas.VistaMenuProductos;

public class ModeloMenuProductos {
    VistaMenuProductos vista;

    public ModeloMenuProductos(VistaMenuProductos vista) {
        this.vista = vista;
    }

    public VistaMenuProductos getVista() {
        return vista;
    }

    public void setVista(VistaMenuProductos vista) {
        this.vista = vista;
    }
}
