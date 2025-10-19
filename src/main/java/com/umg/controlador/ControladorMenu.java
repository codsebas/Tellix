package com.umg.controlador;

import com.umg.modelo.ModeloLogin;
import com.umg.modelo.ModeloMenu;
import com.umg.vistas.VistaLogin;
import com.umg.vistas.VistaMenu;
import com.umg.vistas.VistaPrincipal;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControladorMenu implements MouseListener {

    ModeloMenu modelo;
    VistaMenu vista;
    VistaPrincipal vistaPrincipal;

    public ControladorMenu(ModeloMenu modelo, VistaMenu vista, VistaPrincipal vistaPrincipal) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;

        vista.getBtnCerrarSesion().addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getComponent().equals(this.vista.getBtnCerrarSesion())){
            vistaLogin();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void vistaLogin(){
        ModeloLogin modelo = new ModeloLogin();
        VistaLogin vista = new VistaLogin();
        new ControladorLogin(modelo, vista, vistaPrincipal);
        vistaPrincipal.cambiarPanel(vista);
    }

}
