package com.umg.modelo;

import com.umg.vistas.VistaTiposDeCliente;

public class ModeloTiposDeCliente {
    VistaTiposDeCliente vista;

    public ModeloTiposDeCliente(VistaTiposDeCliente vista) {
        this.vista = vista;
    }

    public VistaTiposDeCliente getVista() {
        return vista;
    }

    public void setVista(VistaTiposDeCliente vista) {
        this.vista = vista;
    }
}
