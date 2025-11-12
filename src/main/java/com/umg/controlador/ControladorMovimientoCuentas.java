/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.controlador;
import com.umg.modelo.ModeloMovimientoCuentas;
import com.umg.modelo.ModeloMovimientoStock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Lisi
 */
public class ControladorMovimientoCuentas implements ActionListener, MouseListener {
    ModeloMovimientoCuentas modelo;

    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorMovimientoCuentas(ModeloMovimientoCuentas modelo) {
        this.modelo = modelo;
        // Inicializar botones y labels
        var vista = modelo.getVista();
   
        btnBuscar = vista.btnBuscar;


       
     
      
        lblBuscar = vista.lblBuscar;
      

        // Dar nombre a los labels para manejar iconos
     
        
        lblBuscar.setName("icono");
     

        inicializarIconos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), false);
    }

    private void inicializarIconos() {

  
        iconosBotones.put(btnBuscar, "/com/umg/iconos/IconoBoton1.png");
 
    }

    private void cambiarIconoBoton(JPanel boton, boolean activo) {
        JLabel icono = obtenerLabelPorNombre(boton, "icono");
        String rutaBase = iconosBotones.get(boton);
        if (rutaBase != null && icono != null) {
            String rutaFinal = activo ? rutaBase.replace(".png", "_oscuro.png") : rutaBase;
            icono.setIcon(new ImageIcon(getClass().getResource(rutaFinal)));
        }
    }

    private JLabel obtenerLabelPorNombre(JPanel boton, String nombre) {
        for (Component comp : boton.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (nombre.equals(lbl.getName())) return lbl;
            }
        }
        return null;
    }     
}
