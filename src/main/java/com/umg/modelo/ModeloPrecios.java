package com.umg.modelo;

import com.umg.vistas.VistaPrecios;

public class ModeloPrecios {
    VistaPrecios vista;

    public ModeloPrecios(VistaPrecios vista) {
        this.vista = vista;
    }

    public VistaPrecios getVista() {
        return vista;
    }

    public void setVista(VistaPrecios vista) {
        this.vista = vista;
    }
}
