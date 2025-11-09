package com.umg.modelo;

import com.umg.vistas.VistaReporteVentasYCompras;

public class ModeloReporteVentasYCompras {
    VistaReporteVentasYCompras vista;

    public ModeloReporteVentasYCompras(VistaReporteVentasYCompras vista) {
        this.vista = vista;
    }

    public VistaReporteVentasYCompras getVista() {
        return vista;
    }

    public void setVista(VistaReporteVentasYCompras vista) {
        this.vista = vista;
    }
}
