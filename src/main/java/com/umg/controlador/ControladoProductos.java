package com.umg.controlador;

import com.umg.modelo.ModeloProductos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladoProductos implements MouseListener {
    ModeloProductos modelo;

    public ControladoProductos(ModeloProductos modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();
        btnNuevo =  vista.btnNuevo;
        btnActualizar = vista.btnActualizar;
        btnEliminar = vista.btnEliminar;
        btnBuscar = vista.btnBuscar;
        btnLimpiar =  vista.btnLimpiar;

        lblNuevo = vista.lblNuevo;
        lblActualizar = vista.lblActualizar;
        lblEliminar = vista.lblEliminar;
        lblBuscar = vista.lblBuscar;
        lblLimpiar = vista.lblLimpiar;

        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        inicializarIconos();
    }



    // Componentes
    private JPanel btnNuevo;
    private JPanel btnActualizar;
    private JPanel btnEliminar;
    private JPanel btnBuscar;
    private JPanel btnLimpiar;

    private JLabel lblNuevo;
    private JLabel lblActualizar;
    private JLabel lblEliminar;
    private JLabel lblBuscar;
    private JLabel lblLimpiar;

    private JPanel botonSeleccionado = null;

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
        JPanel boton = (JPanel) e.getSource();
            cambiarIconoBoton(boton, true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JPanel boton = (JPanel) e.getSource();
            cambiarIconoBoton(boton, false);
    }

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    private void inicializarIconos() {
        iconosBotones.put(btnNuevo, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnActualizar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnEliminar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnBuscar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnLimpiar, "/com/umg/iconos/IconoBoton1.png");
    }

    private void cambiarIconoBoton(JPanel boton, boolean activo) {
        JLabel icono = obtenerLabelPorNombre(boton, "icono");
        String rutaBase = iconosBotones.get(boton);
        if (rutaBase != null && icono != null) {
            String rutaFinal;
            if (activo) {

                rutaFinal = rutaBase.replace(".png", "_oscuro.png");
            } else {
                rutaFinal = rutaBase;
            }

            icono.setIcon(new ImageIcon(getClass().getResource(rutaFinal)));
        }
    }

    private JLabel obtenerLabelPorNombre(JPanel boton, String nombre) {
        for (Component comp : boton.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (nombre.equals(lbl.getName())) {
                    return lbl;
                }
            }
        }
        return null;
    }
}
