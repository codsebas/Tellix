package com.umg.modelo;

import com.umg.vistas.VistaClientes;

public class ModeloCliente {
    VistaClientes vista;

    public ModeloCliente() {
    }

    public ModeloCliente(VistaClientes vista) {
        this.vista = vista;
    }

    public VistaClientes getVista() {
        return vista;
    }

    public void setVista(VistaClientes vista) {
        this.vista = vista;
    }
}
