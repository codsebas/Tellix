package com.umg.modelo;

import com.umg.vistas.VistaCategorias;

public class ModeloCategoria {
    VistaCategorias vista;

    public ModeloCategoria(VistaCategorias vista) {
        this.vista = vista;
    }

    public VistaCategorias getVista() {
        return vista;
    }

    public void setVista(VistaCategorias vista) {
        this.vista = vista;
    }
}
