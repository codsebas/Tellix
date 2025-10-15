package com.umg.controlador;

import com.umg.modelo.ModeloMenu;
import com.umg.vistas.VistaMenu;
import com.umg.vistas.VistaPrincipal;

public class ControladorMenu {
    ModeloMenu modelo;
    VistaMenu vista;
    VistaPrincipal vistaPrincipal;

    public ControladorMenu(ModeloMenu modelo, VistaMenu vista, VistaPrincipal vistaPrincipal) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;
    }
}
