package com.umg.controlador;

import com.umg.modelo.ModeloMenuCuentas;
import com.umg.vistas.VistaCuentasPorCobrar;
import com.umg.vistas.VistaCuentasPorPagar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladorMenuCuentas implements MouseListener {
    ModeloMenuCuentas modelo;

    // Colores
    Color celesteFocus = new Color(0,123,255);
    Color grisFondo = new Color(245,247,250);
    Color grisOscuro = new Color(51,51,51);
    Color blancoPuro = new Color(255,255,255);

    private JPanel btnCuentasPorCobrar, btnCuetnasPorPagar;
    private JLabel lblCuentasPorCobrar, lblCuentasPorPagar;
    private JLabel lblIconoCuentasPorCobrar, lblIconoCuentasPorPagar;
    private JLabel lblCuentasPorCobrarSeleccionado, lblCuentasPorPagarSeleccionado;

    private JPanel botonSeleccionado = null;

    VistaCuentasPorCobrar vistaCuentasPorCobrar = new VistaCuentasPorCobrar();
    VistaCuentasPorPagar vistaCuentasPorPagar = new VistaCuentasPorPagar();

    public ControladorMenuCuentas(ModeloMenuCuentas modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        vista.btnCuentasPorCobrar.addMouseListener(this);
        vista.btnCuentasPorPagar.addMouseListener(this);

        btnCuentasPorCobrar = vista.btnCuentasPorCobrar;
        btnCuetnasPorPagar = vista.btnCuentasPorPagar;

        lblCuentasPorCobrar =  vista.lblCuentasPorCobrar;
        lblCuentasPorPagar =  vista.lblCuentasPorPagar;

        lblCuentasPorCobrar.setName("texto");
        lblCuentasPorPagar.setName("texto");

        lblIconoCuentasPorCobrar = vista.lblIconoCuentasPorCobrar;
        lblIconoCuentasPorPagar = vista.lblIconCuentasPorPagar;

        lblIconoCuentasPorCobrar.setName("icono");
        lblIconoCuentasPorPagar.setName("icono");

        lblCuentasPorCobrarSeleccionado = vista.lblCuentasPorCobrarSeleccionado;
        lblCuentasPorPagarSeleccionado = vista.lblCuentasPorPagarSeleccionado;

        lblCuentasPorCobrarSeleccionado.setName("efecto");
        lblCuentasPorPagarSeleccionado.setName("efecto");

        lblCuentasPorCobrarSeleccionado.setVisible(false);
        lblCuentasPorPagarSeleccionado.setVisible(false);

        inicializarIconos();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getComponent();

        if (source.equals(btnCuentasPorCobrar)) {
            accionBotones(btnCuentasPorCobrar, e, vistaCuentasPorCobrar);
        } else if(source.equals(btnCuetnasPorPagar)) {
            accionBotones(btnCuetnasPorPagar, e, vistaCuentasPorPagar);
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
                btnCuentasPorCobrar, btnCuetnasPorPagar
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
        iconosBotones.put(btnCuentasPorCobrar, "/com/umg/iconos/IconoInicio.png");
        iconosBotones.put(btnCuetnasPorPagar, "/com/umg/iconos/IconoClientes.png");
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
