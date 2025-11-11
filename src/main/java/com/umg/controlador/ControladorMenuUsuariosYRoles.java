package com.umg.controlador;

import com.umg.modelo.ModeloMenuUsuariosYRoles;
import com.umg.vistas.VistaGestionUsuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladorMenuUsuariosYRoles implements MouseListener {
    ModeloMenuUsuariosYRoles modelo;

    // Colores
    Color celesteFocus = new Color(0,123,255);
    Color grisFondo = new Color(245,247,250);
    Color grisOscuro = new Color(51,51,51);
    Color blancoPuro = new Color(255,255,255);

    private JPanel btnGestionUsuarios, btnRoles, btnAsignacionAcceso;
    private JLabel lblGestionUsuarios, lblRoles, lblAsignacionAcceso;
    private JLabel lblIconoGestionUsuarios, lblIconoRoles, lblIconoAsignacionAcceso;
    private JLabel lblGestionUsuariosSeleccionado, lblRolesSeleccionado, lblAsignacionAccesoSeleccionado;

    private JPanel botonSeleccionado = null;

    VistaGestionUsuarios vistaGestionUsuarios = new VistaGestionUsuarios();

    public ControladorMenuUsuariosYRoles(ModeloMenuUsuariosYRoles modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        btnGestionUsuarios = vista.btnGestionUsuarios;
        btnRoles = vista.btnRoles;
        btnAsignacionAcceso = vista.btnAsignacionAcceso;

        lblGestionUsuarios =  vista.lblGestionUsuarios;
        lblRoles =  vista.lblRoles;
        lblAsignacionAcceso =  vista.lblAsignacionAcceso;

        lblGestionUsuarios.setName("texto");
        lblRoles.setName("texto");
        lblAsignacionAcceso.setName("texto");

        lblIconoGestionUsuarios = vista.lblIconoGestionUsuarios;
        lblIconoRoles = vista.lblIconoRoles;
        lblIconoAsignacionAcceso = vista.lblIconoAsignacionAcceso;

        lblIconoGestionUsuarios.setName("icono");
        lblIconoRoles.setName("icono");
        lblIconoAsignacionAcceso.setName("icono");

        lblGestionUsuariosSeleccionado = vista.lblGestionUsuariosSeleccionado;
        lblRolesSeleccionado = vista.lblRolesSeleccionado;
        lblAsignacionAccesoSeleccionado = vista.lblAsignacionAccesoSeleccionado;

        lblGestionUsuariosSeleccionado.setName("efecto");
        lblRolesSeleccionado.setName("efecto");
        lblAsignacionAccesoSeleccionado.setName("efecto");

        lblGestionUsuariosSeleccionado.setVisible(false);
        lblRolesSeleccionado.setVisible(false);
        lblAsignacionAccesoSeleccionado.setVisible(false);

        inicializarIconos();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getComponent();

        if (source.equals(btnGestionUsuarios)) {
            accionBotones(btnGestionUsuarios, e, vistaGestionUsuarios);
        } else if(source.equals(btnRoles)) {
//            accionBotones(btnRoles, e, null);
        } else if(source.equals(btnAsignacionAcceso)) {
//            accionBotones(btnAsignacionAcceso, e, null);
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
        JPanel boton = (JPanel) e.getSource();
        if (boton != botonSeleccionado) {
            JLabel icono = obtenerLabelPorNombre(boton, "icono");
            JLabel texto = obtenerLabelPorNombre(boton, "texto");
            JLabel efecto = obtenerLabelPorNombre(boton, "efecto");

            cambiarIconoBoton(boton, true);

            if (efecto != null) {
                efecto.setVisible(true);
            }

            colorFondoPanelYTexto(boton, icono, texto, grisFondo, blancoPuro, e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JPanel boton = (JPanel) e.getSource();
        if (boton != botonSeleccionado) {
            JLabel icono = obtenerLabelPorNombre(boton, "icono");
            JLabel texto = obtenerLabelPorNombre(boton, "texto");
            JLabel efecto = obtenerLabelPorNombre(boton, "efecto");

            cambiarIconoBoton(boton, false);

            if (efecto != null) {
                efecto.setVisible(false);
            }

            colorFondoPanelYTexto(boton, icono, texto, grisFondo, grisOscuro, e);
        }
    }

    // Metodo cambiar Color a Panel y Texto
    private void colorFondoPanelYTexto(
            JPanel panel,
            JLabel labelIcono,
            JLabel labelTexto,
            Color colorPanel,
            Color colorTexto,
            MouseEvent e) {

        if (e.getComponent().equals(panel)) {

            panel.setBackground(colorPanel);

            if (labelTexto != null) {
                labelTexto.setForeground(colorTexto);
            }
        }
    }

    private void accionBotones(JPanel boton,MouseEvent e, JPanel vista) {
        reiniciarColores();
        JLabel icono = obtenerLabelPorNombre(boton, "icono");
        JLabel texto = obtenerLabelPorNombre(boton, "texto");
        JLabel efecto = obtenerLabelPorNombre(boton, "efecto");

        cambiarIconoBoton(boton, true);

        if (efecto != null) {
            efecto.setVisible(true);
        }

        colorFondoPanelYTexto(boton, icono, texto, grisFondo, blancoPuro, e);
        cargarVistaPanel(vista);
        botonSeleccionado = boton;
    }

    private void cargarVistaPanel(JPanel panel){
        var vista = modelo.getVista();
        vista.contenedor.removeAll();
        vista.contenedor.add(panel);
        vista.contenedor.revalidate();
        vista.contenedor.repaint();
    }

    private void reiniciarColores(){

        JPanel[] botones = {
                btnGestionUsuarios, btnRoles, btnAsignacionAcceso
        };

        for (JPanel boton : botones) {

            JLabel icono = obtenerLabelPorNombre(boton, "icono");
            JLabel texto = obtenerLabelPorNombre(boton, "texto");
            JLabel efecto = obtenerLabelPorNombre(boton, "efecto");

            boton.setBackground(grisFondo);

            if (texto != null) {
                texto.setForeground(grisOscuro);
            }

            if (efecto != null) efecto.setVisible(false);

            cambiarIconoBoton(boton, false);
        }

        botonSeleccionado = null;
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

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    private void inicializarIconos() {
        iconosBotones.put(btnGestionUsuarios, "/com/umg/iconos/IconoInicio.png");
        iconosBotones.put(btnRoles, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnAsignacionAcceso, "/com/umg/iconos/IconoClientes.png");
    }

    private void cambiarIconoBoton(JPanel boton, boolean activo) {
        JLabel icono = obtenerLabelPorNombre(boton, "icono");
        String rutaBase = iconosBotones.get(boton);

        if (rutaBase != null && icono != null) {
            String rutaFinal;
            if (activo) {
                // Aquí asumo que tus versiones blancas tienen _blanco en el nombre
                rutaFinal = rutaBase.replace(".png", "_blanco.png");
            } else {
                rutaFinal = rutaBase;
            }

            icono.setIcon(new ImageIcon(getClass().getResource(rutaFinal)));
        }
    }
}
