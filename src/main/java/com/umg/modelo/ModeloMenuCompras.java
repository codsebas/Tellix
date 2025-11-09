package com.umg.modelo;

import com.umg.vistas.VistaMenuCompras;

public class ModeloMenuCompras {
    VistaMenuCompras vista;

    public ModeloMenuCompras(VistaMenuCompras vista) {
        this.vista = vista;
    }

    public VistaMenuCompras getVista() {
        return vista;
    }

    public void setVista(VistaMenuCompras vista) {
        this.vista = vista;
    }
}
