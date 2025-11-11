package com.umg.modelo;

import com.umg.vistas.VistaMenuInventario;

public class ModeloMenuInventario {
    VistaMenuInventario vista;

    public ModeloMenuInventario(VistaMenuInventario vista) {
        this.vista = vista;
    }

    public VistaMenuInventario getVista() {
        return vista;
    }

    public void setVista(VistaMenuInventario vista) {
        this.vista = vista;
    }
}
