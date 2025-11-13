package com.umg.controlador;

import com.umg.modelo.ModeloMenuCompras;
import com.umg.vistas.VistaCompras;
import com.umg.vistas.VistaCuentasPorPagar;
import com.umg.vistas.VistaProveedores;
import com.umg.vistas.VistaRepresentantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladorMenuCompras implements MouseListener {
    ModeloMenuCompras modelo;

    // Colores
    Color celesteFocus = new Color(0,123,255);
    Color grisFondo = new Color(245,247,250);
    Color grisOscuro = new Color(51,51,51);
    Color blancoPuro = new Color(255,255,255);

    private JPanel btnRegistrarCompra, btnConsultaCompras, btnProveedores, btnCuentasPorPagar, btnRepresentantes;
    private JLabel lblRegistrarCompra, lblConsultaCompras, lblProveedores, lblCuentasPorPagar, lblRepresentantes;
    private JLabel lblIconoRegistrarCompra, lblIconoConsultaCompras, lblIconoProveedores, lblIconoCuentasPorPagar,
                    lblIconoRepresentantes;
    private JLabel lblRegistrarCompraSeleccionado, lblConsultaComprasSeleccionado, lblProveedoresSeleccionado,
                    lblCuentasPorPagarSeleccionado, lblRepresentantesSeleccionado;

    private JPanel botonSeleccionado = null;

    VistaCompras vistaCompras = new VistaCompras();

    VistaProveedores vistaProveedores = new VistaProveedores();
    VistaCuentasPorPagar vistaCuentasPorPagar = new VistaCuentasPorPagar();
    VistaRepresentantes vistaRepresentantes = new VistaRepresentantes();

    public ControladorMenuCompras(ModeloMenuCompras modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        btnRegistrarCompra = vista.btnRegistrarCompra;
        btnConsultaCompras = vista.btnConsultaCompras;
        btnProveedores = vista.btnProveedores;
        btnCuentasPorPagar = vista.btnCuentasPorPagar;
        btnRepresentantes = vista.btnRepresentantes;

        lblRegistrarCompra =  vista.lblRegistrarCompra;
        lblConsultaCompras =  vista.lblConsultaCompras;
        lblProveedores =  vista.lblProveedores;
        lblCuentasPorPagar =  vista.lblCuentasPorPagar;
        lblRepresentantes =  vista.lblRepresentantes;

        lblRegistrarCompra.setName("texto");
        lblConsultaCompras.setName("texto");
        lblProveedores.setName("texto");
        lblCuentasPorPagar.setName("texto");
        lblRepresentantes.setName("texto");

        lblIconoRegistrarCompra = vista.lblIconoRegistrarCompra;
        lblIconoConsultaCompras = vista.lblIconoConsultaCompras;
        lblIconoProveedores = vista.lblIconoProveedores;
        lblIconoCuentasPorPagar = vista.lblIconoCuentasPorPagar;
        lblIconoRepresentantes = vista.lblIconoRepresentantes;

        lblIconoRegistrarCompra.setName("icono");
        lblIconoConsultaCompras.setName("icono");
        lblIconoProveedores.setName("icono");
        lblIconoCuentasPorPagar.setName("icono");
        lblIconoRepresentantes.setName("icono");

        lblRegistrarCompraSeleccionado = vista.lblRegistrarCompraSeleccionado;
        lblConsultaComprasSeleccionado = vista.lblConsultaComprasSeleccionado;
        lblProveedoresSeleccionado = vista.lblProveedoresSeleccionado;
        lblCuentasPorPagarSeleccionado = vista.lblCuentasPorPagarSeleccionado;
        lblRepresentantesSeleccionado = vista.lblRepresentantesSeleccionado;

        lblRegistrarCompraSeleccionado.setName("efecto");
        lblConsultaComprasSeleccionado.setName("efecto");
        lblProveedoresSeleccionado.setName("efecto");
        lblCuentasPorPagarSeleccionado.setName("efecto");
        lblRepresentantesSeleccionado.setName("efecto");

        lblRegistrarCompraSeleccionado.setVisible(false);
        lblConsultaComprasSeleccionado.setVisible(false);
        lblProveedoresSeleccionado.setVisible(false);
        lblCuentasPorPagarSeleccionado.setVisible(false);
        lblRepresentantesSeleccionado.setVisible(false);

        inicializarIconos();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getComponent();

        if (source.equals(btnRegistrarCompra)) {
            accionBotones(btnRegistrarCompra, e, vistaCompras);
        } else if(source.equals(btnConsultaCompras)) {
//            accionBotones(btnPrecios, e, vistaPrecios);
        } else if(source.equals(btnProveedores)) {
            accionBotones(btnProveedores, e, vistaProveedores);
        } else if(source.equals(btnRepresentantes)) {
            accionBotones(btnRepresentantes, e, vistaRepresentantes);
        } else if(source.equals(btnCuentasPorPagar)) {
            accionBotones(btnCuentasPorPagar, e, vistaCuentasPorPagar);
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
                btnRegistrarCompra, btnConsultaCompras, btnProveedores, btnCuentasPorPagar
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
        iconosBotones.put(btnRegistrarCompra, "/com/umg/iconos/IconoInicio.png");
        iconosBotones.put(btnConsultaCompras, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnProveedores, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnCuentasPorPagar, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnRepresentantes, "/com/umg/iconos/IconoClientes.png");
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
