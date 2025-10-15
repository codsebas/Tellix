package com.umg.controlador;

import com.umg.modelo.ModeloLogin;
import com.umg.modelo.ModeloVistaPrincipal;
import com.umg.vistas.VistaLogin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ControladorVistaPrincipal implements ActionListener, WindowListener {


    ModeloVistaPrincipal modelo;

    public ControladorVistaPrincipal(ModeloVistaPrincipal modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public void windowOpened(WindowEvent e) {
        mostrarVistaLogin();
    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    private void mostrarVistaLogin(){
        ModeloLogin model = new ModeloLogin();
        VistaLogin vista = new VistaLogin();
        new ControladorLogin(model, vista, modelo.getVista());
        modelo.getVista().cambiarPanel(vista);
    }
}
