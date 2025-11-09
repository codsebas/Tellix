package com.umg.modelo;

import com.umg.vistas.VistaReporteFinanzasEInventarios;

public class ModeloReporteFinanzasEInventarios {
    VistaReporteFinanzasEInventarios vista;

    public ModeloReporteFinanzasEInventarios(VistaReporteFinanzasEInventarios vista) {
        this.vista = vista;
    }

    public VistaReporteFinanzasEInventarios getVista() {
        return vista;
    }

    public void setVista(VistaReporteFinanzasEInventarios vista) {
        this.vista = vista;
    }
}
