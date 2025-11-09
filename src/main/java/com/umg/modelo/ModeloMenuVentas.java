package com.umg.modelo;

import com.umg.vistas.VistaMenuVentas;

public class ModeloMenuVentas {
    VistaMenuVentas vista;

    public ModeloMenuVentas(VistaMenuVentas vista) {
        this.vista = vista;
    }

    public VistaMenuVentas getVista() {
        return vista;
    }

    public void setVista(VistaMenuVentas vista) {
        this.vista = vista;
    }
}
