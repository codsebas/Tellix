package com.umg.modelo;

import com.umg.vistas.VistaEmpleados;

public class ModeloEmpleados {
    VistaEmpleados vista;

    public ModeloEmpleados(VistaEmpleados vista) {
        this.vista = vista;
    }

    public VistaEmpleados getVista() {
        return vista;
    }

    public void setVista(VistaEmpleados vista) {
        this.vista = vista;
    }
}
