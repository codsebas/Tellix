package com.umg.modelo;

import com.umg.vistas.VistaMenuClientes;

public class ModeloMenuClientes {
    VistaMenuClientes vista;

    public ModeloMenuClientes(VistaMenuClientes vista) {
        this.vista = vista;
    }

    public VistaMenuClientes getVista() {
        return vista;
    }

    public void setVista(VistaMenuClientes vista) {
        this.vista = vista;
    }
}
