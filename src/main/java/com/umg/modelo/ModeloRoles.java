package com.umg.modelo;

import com.umg.vistas.VistaRoles;

public class ModeloRoles {
    VistaRoles vista;

    public ModeloRoles(VistaRoles vista) {
        this.vista = vista;
    }

    public VistaRoles getVista() {
        return vista;
    }

    public void setVista(VistaRoles vista) {
        this.vista = vista;
    }
}
