package com.umg.modelo;

import com.umg.vistas.VistaLogin;

public class ModeloLogin {
    VistaLogin vista;

    public ModeloLogin() {
    }

    public ModeloLogin(VistaLogin vista) {
        this.vista = vista;
    }

    public VistaLogin getVista() {
        return vista;
    }

    public void setVista(VistaLogin vista) {
        this.vista = vista;
    }
}
