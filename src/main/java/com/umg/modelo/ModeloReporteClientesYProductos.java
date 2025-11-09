package com.umg.modelo;

import com.umg.vistas.VistaReporteClientesYProductos;

public class ModeloReporteClientesYProductos {
    VistaReporteClientesYProductos vista;

    public ModeloReporteClientesYProductos(VistaReporteClientesYProductos vista) {
        this.vista = vista;
    }

    public VistaReporteClientesYProductos getVista() {
        return vista;
    }

    public void setVista(VistaReporteClientesYProductos vista) {
        this.vista = vista;
    }
}
