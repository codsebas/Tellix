package com.umg.modelo;

import com.umg.vistas.VistaMenuConfiguracion;

public class ModeloMenuConfiguracion {
    VistaMenuConfiguracion vista;

    public ModeloMenuConfiguracion(VistaMenuConfiguracion vista) {
        this.vista = vista;
    }

    public VistaMenuConfiguracion getVista() {
        return vista;
    }

    public void setVista(VistaMenuConfiguracion vista) {
        this.vista = vista;
    }
}
