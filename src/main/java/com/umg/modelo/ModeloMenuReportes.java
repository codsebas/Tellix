package com.umg.modelo;

import com.umg.vistas.VistaMenuReportes;

public class ModeloMenuReportes {
    VistaMenuReportes vista;

    public ModeloMenuReportes(VistaMenuReportes vista) {
        this.vista = vista;
    }

    public VistaMenuReportes getVista() {
        return vista;
    }

    public void setVista(VistaMenuReportes vista) {
        this.vista = vista;
    }
}
