package com.umg.modelo;

import com.umg.vistas.VistaRepresentantes;

public class ModeloRepresentates {
    VistaRepresentantes vista;

    public ModeloRepresentates(VistaRepresentantes vista) {
        this.vista = vista;
    }

    public VistaRepresentantes getVista() {
        return vista;
    }

    public void setVista(VistaRepresentantes vista) {
        this.vista = vista;
    }
}
