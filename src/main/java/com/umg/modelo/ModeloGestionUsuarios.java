package com.umg.modelo;

import com.umg.vistas.VistaGestionUsuarios;

public class ModeloGestionUsuarios {
    VistaGestionUsuarios vista;

    public ModeloGestionUsuarios(VistaGestionUsuarios vista) {
        this.vista = vista;
    }

    public VistaGestionUsuarios getVista() {
        return vista;
    }

    public void setVista(VistaGestionUsuarios vista) {
        this.vista = vista;
    }
}
