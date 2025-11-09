package com.umg.modelo;

import com.umg.vistas.VistaProveedores;

public class ModeloProveedores {
    VistaProveedores vista;

    public ModeloProveedores(VistaProveedores vista) {
        this.vista = vista;
    }

    public VistaProveedores getVista() {
        return vista;
    }

    public void setVista(VistaProveedores vista) {
        this.vista = vista;
    }
}
