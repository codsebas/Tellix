package com.umg.controlador;

import com.umg.modelo.ModeloLogin;
import com.umg.modelo.ModeloMenu;
import com.umg.vistas.VistaLogin;
import com.umg.vistas.VistaMenu;
import com.umg.vistas.VistaPrincipal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControladorLogin implements MouseListener, KeyListener {
    ModeloLogin modelo;
    VistaLogin vista;
    VistaPrincipal vistaPrincipal;

    public ControladorLogin(ModeloLogin modelo, VistaLogin vista, VistaPrincipal vistaPrincipal) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;

        vista.getBtnIniciarSesion().addMouseListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getComponent().equals(this.vista.getBtnIniciarSesion())){
            vistaAdministrador();
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

    private void vistaAdministrador(){
        ModeloMenu modelo = new ModeloMenu();
        VistaMenu vista = new VistaMenu();
        new ControladorMenu(modelo, vista, vistaPrincipal);
        vistaPrincipal.cambiarPanel(vista);
    }
}
