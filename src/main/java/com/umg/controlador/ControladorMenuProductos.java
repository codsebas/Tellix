package com.umg.controlador;

import com.umg.modelo.ModeloMenuProductos;
import com.umg.vistas.VistaPrecios;
import com.umg.vistas.VistaProductos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladorMenuProductos implements MouseListener {
    ModeloMenuProductos modelo;

    // Colores
    Color celesteFocus = new Color(0,123,255);
    Color grisFondo = new Color(245,247,250);
    Color grisOscuro = new Color(51,51,51);
    Color blancoPuro = new Color(255,255,255);

    private JPanel btnGestionProductos, btnPrecios, btnAsigImpuestos, btnAsigDescuentos;
    private JLabel lblGestionProductos, lblPrecios, lblAsigImpuestos, lblAsigDescuentos;
    private JLabel lblIconoGestionProductos, lblIconoPrecios, lblIconoAsigImpuestos, lblIconoAsigDescuentos;
    private JLabel lblGestionProductosSeleccionado, lblPreciosSeleccionado, lblAsigImpuestosSeleccionado, lblAsigDescuentosSeleccionado;

    private JPanel botonSeleccionado = null;

    VistaProductos vistaProductos = new VistaProductos();
    VistaPrecios vistaPrecios = new VistaPrecios();

    public ControladorMenuProductos(ModeloMenuProductos modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        btnGestionProductos = vista.btnGestionProductos;
        btnPrecios = vista.btnPrecios;
        btnAsigImpuestos = vista.btnAsigImpuestos;
        btnAsigDescuentos = vista.btnAsigDescuentos;

        lblGestionProductos =  vista.lblGestionProductos;
        lblPrecios =  vista.lblPrecios;
        lblAsigImpuestos =  vista.lblAsigImpuestos;
        lblAsigDescuentos =  vista.lblAsigDescuentos;

        lblGestionProductos.setName("texto");
        lblPrecios.setName("texto");
        lblAsigImpuestos.setName("texto");
        lblAsigDescuentos.setName("texto");

        lblIconoGestionProductos = vista.lblIconoGestionProductos;
        lblIconoPrecios = vista.lblIconoPrecios;
        lblIconoAsigImpuestos = vista.lblIconoAsigImpuestos;
        lblIconoAsigDescuentos = vista.lblIconoAsigDescuentos;

        lblIconoGestionProductos.setName("icono");
        lblIconoPrecios.setName("icono");
        lblIconoAsigImpuestos.setName("icono");
        lblIconoAsigDescuentos.setName("icono");

        lblGestionProductosSeleccionado = vista.lblGestionProductosSeleccionado;
        lblPreciosSeleccionado = vista.lblPreciosSeleccionado;
        lblAsigImpuestosSeleccionado = vista.lblAsigImpuestosSeleccionado;
        lblAsigDescuentosSeleccionado = vista.lblAsigDescuentosSeleccionado;

        lblGestionProductosSeleccionado.setName("efecto");
        lblPreciosSeleccionado.setName("efecto");
        lblAsigImpuestosSeleccionado.setName("efecto");
        lblAsigDescuentosSeleccionado.setName("efecto");

        lblGestionProductosSeleccionado.setVisible(false);
        lblPreciosSeleccionado.setVisible(false);
        lblAsigImpuestosSeleccionado.setVisible(false);
        lblAsigDescuentosSeleccionado.setVisible(false);

        inicializarIconos();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getComponent();

        if (source.equals(btnGestionProductos)) {
            accionBotones(btnGestionProductos, e, vistaProductos);
        } else if(source.equals(btnPrecios)) {
            accionBotones(btnPrecios, e, vistaPrecios);
        }
//        else if(source.equals(btnMedidas)) {
//            accionBotones(btnMedidas, e, vistaMedidas);
//        } else if(source.equals(btnTipoDeCliente)) {
//            accionBotones(btnTipoDeCliente, e, vistaTiposDeCliente);
//        } else if(source.equals(btnTipoDeContacto)) {
//            accionBotones(btnTipoDeContacto, e, vistaTiposDeContacto);
//        } else if(source.equals(btnBancos)) {
//            accionBotones(btnBancos, e, vistaBancos);
//        } else if(source.equals(btnMetodosDeLiquidacion)) {
//            accionBotones(btnMetodosDeLiquidacion, e, vistaMetodosDeLiquidacion);
//        }
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
                btnGestionProductos, btnPrecios, btnAsigImpuestos, btnAsigDescuentos
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
        iconosBotones.put(btnGestionProductos, "/com/umg/iconos/IconoInicio.png");
        iconosBotones.put(btnPrecios, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnAsigImpuestos, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnAsigDescuentos, "/com/umg/iconos/IconoClientes.png");
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
