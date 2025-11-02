package com.umg.modelo;

import com.umg.vistas.VistaMenuCuentas;

public class ModeloMenuCuentas {
    VistaMenuCuentas vista;

    public ModeloMenuCuentas(VistaMenuCuentas vista) {
        this.vista = vista;
    }

    public VistaMenuCuentas getVista() {
        return vista;
    }

    public void setVista(VistaMenuCuentas vista) {
        this.vista = vista;
    }
}
