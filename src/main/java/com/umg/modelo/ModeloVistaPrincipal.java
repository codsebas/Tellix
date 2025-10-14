package com.umg.modelo;

import com.umg.vistas.VistaPrincipal;

public class ModeloVistaPrincipal {
    VistaPrincipal vista;

    public ModeloVistaPrincipal(VistaPrincipal vista) {
        this.vista = vista;
    }

    public VistaPrincipal getVista() {
        return vista;
    }

    public void setVista(VistaPrincipal vista) {
        this.vista = vista;
    }
}
