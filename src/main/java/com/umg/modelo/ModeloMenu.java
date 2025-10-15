package com.umg.modelo;

import com.umg.vistas.VistaMenu;

public class ModeloMenu {
    VistaMenu vista;

    public ModeloMenu() {
    }

    public ModeloMenu(VistaMenu vista) {
        this.vista = vista;
    }

    public VistaMenu getVista() {
        return vista;
    }

    public void setVista(VistaMenu vista) {
        this.vista = vista;
    }
}
