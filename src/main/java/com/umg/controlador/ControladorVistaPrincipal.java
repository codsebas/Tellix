package com.umg.controlador;

import com.umg.implementacion.ClienteImp;
import com.umg.modelo.ModeloVistaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorVistaPrincipal implements ActionListener {
    ModeloVistaPrincipal modelo;
    ClienteImp implementacion = new  ClienteImp();

    public ControladorVistaPrincipal(ModeloVistaPrincipal modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(modelo.getVista().btnHola.getActionCommand())){
            modelo.getVista().lblHola.setText(implementacion.decirHola());
        }
    }
}
