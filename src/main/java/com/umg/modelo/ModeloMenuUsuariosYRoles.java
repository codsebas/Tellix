package com.umg.modelo;

import com.umg.vistas.VistaMenuUsuariosYRoles;

public class ModeloMenuUsuariosYRoles {
    VistaMenuUsuariosYRoles vista;

    public ModeloMenuUsuariosYRoles(VistaMenuUsuariosYRoles vista) {
        this.vista = vista;
    }

    public VistaMenuUsuariosYRoles getVista() {
        return vista;
    }

    public void setVista(VistaMenuUsuariosYRoles vista) {
        this.vista = vista;
    }
}
