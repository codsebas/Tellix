package com.umg.controlador;

import com.umg.modelo.ModeloMenuConfiguracion;
import com.umg.vistas.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladorMenuConfiguracion implements MouseListener {
    ModeloMenuConfiguracion modelo;

    // Colores
    Color celesteFocus = new Color(0,123,255);
    Color grisFondo = new Color(245,247,250);
    Color grisOscuro = new Color(51,51,51);
    Color blancoPuro = new Color(255,255,255);

    private JPanel btnCategorias, btnMarcas, btnMedidas, btnTipoDeCliente, btnTipoDeContacto, btnBancos,
                    btnMetodosDeLiquidacion;
    private JLabel lblCategorias, lblMarcas, lblMedidas, lblTipoDeCliente, lblTipoDeContacto, lblBancos,
                    lblMetodosDeLiquidancion;
    private JLabel lblIconoCategorias, lblIconoMarcas, lblIconoMedidas, lblIconoTipoDeCliente, lblIconoTipoDeContacto,
                    lblIconoBancos, lblIconoMetodosDeLiquidancion;
    private JLabel lblCategoriasSeleccionado, lblMarcasSeleccionado, lblMedidasSeleccionado, lblTipoDeClienteSeleccionado,
                    lblTipoDeContactoSeleccionado, lblBancosSeleccionado, lblMetodosDeLiquidancionSeleccionado;

    private JPanel botonSeleccionado = null;

    VistaCategorias vistaCategorias = new VistaCategorias();
    VistaMarcas vistaMarcas = new VistaMarcas();
    VistaMedidas vistaMedidas = new VistaMedidas();
    VistaTiposDeCliente vistaTiposDeCliente = new VistaTiposDeCliente();
    VistaTiposDeContacto vistaTiposDeContacto = new VistaTiposDeContacto();
    VistaBancos vistaBancos = new VistaBancos();
    VistaMetodosDeLiquidacion vistaMetodosDeLiquidacion = new VistaMetodosDeLiquidacion();

    public ControladorMenuConfiguracion(ModeloMenuConfiguracion modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        btnCategorias = vista.btnCategorias;
        btnMarcas = vista.btnMarcas;
        btnMedidas = vista.btnMedidas;
        btnTipoDeCliente = vista.btnTipoDeCliente;
        btnTipoDeContacto = vista.btnTipoDeContacto;
        btnBancos = vista.btnBancos;
        btnMetodosDeLiquidacion = vista.btnMetodosDeLiquidacion;

        lblCategorias =  vista.lblCategorias;
        lblMarcas =  vista.lblMarcas;
        lblMedidas =  vista.lblMedidas;
        lblTipoDeCliente =  vista.lblTipoDeCliente;
        lblTipoDeContacto =  vista.lblTipoDeContacto;
        lblBancos =  vista.lblBancos;
        lblMetodosDeLiquidancion =  vista.lblMetodosDeLiquidacion;

        lblCategorias.setName("texto");
        lblMarcas.setName("texto");
        lblMedidas.setName("texto");
        lblTipoDeCliente.setName("texto");
        lblTipoDeContacto.setName("texto");
        lblBancos.setName("texto");
        lblMetodosDeLiquidancion.setName("texto");

        lblIconoCategorias = vista.lblIconoCategorias;
        lblIconoMarcas = vista.lblIconoMarcas;
        lblIconoMedidas = vista.lblIconoMedidas;
        lblIconoTipoDeCliente = vista.lblIconoTipoDeCliente;
        lblIconoTipoDeContacto = vista.lblIconoTipoDeContacto;
        lblIconoBancos = vista.lblIconoBancos;
        lblIconoMetodosDeLiquidancion = vista.lblIconoMetodosDeLiquidacion;

        lblIconoCategorias.setName("icono");
        lblIconoMarcas.setName("icono");
        lblIconoMedidas.setName("icono");
        lblIconoTipoDeCliente.setName("icono");
        lblIconoTipoDeContacto.setName("icono");
        lblIconoBancos.setName("icono");
        lblIconoMetodosDeLiquidancion.setName("icono");

        lblCategoriasSeleccionado = vista.lblCategoriasSeleccionado;
        lblMarcasSeleccionado = vista.lblMarcasSeleccionado;
        lblMedidasSeleccionado = vista.lblMedidasSeleccionado;
        lblTipoDeClienteSeleccionado = vista.lblTipoDeClienteSeleccionado;
        lblTipoDeContactoSeleccionado = vista.lblTipoDeContactoSeleccionado;
        lblBancosSeleccionado = vista.lblBancosSeleccionado;
        lblMetodosDeLiquidancionSeleccionado = vista.lblMetodosDeLiquidacionSeleccionado;

        lblCategoriasSeleccionado.setName("efecto");
        lblMarcasSeleccionado.setName("efecto");
        lblMedidasSeleccionado.setName("efecto");
        lblTipoDeClienteSeleccionado.setName("efecto");
        lblTipoDeContactoSeleccionado.setName("efecto");
        lblBancosSeleccionado.setName("efecto");
        lblMetodosDeLiquidancionSeleccionado.setName("efecto");

        lblCategoriasSeleccionado.setVisible(false);
        lblMarcasSeleccionado.setVisible(false);
        lblMedidasSeleccionado.setVisible(false);
        lblTipoDeClienteSeleccionado.setVisible(false);
        lblTipoDeContactoSeleccionado.setVisible(false);
        lblBancosSeleccionado.setVisible(false);
        lblMetodosDeLiquidancionSeleccionado.setVisible(false);

        inicializarIconos();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getComponent();

        if (source.equals(btnCategorias)) {
            accionBotones(btnCategorias, e, vistaCategorias);
        } else if(source.equals(btnMarcas)) {
            accionBotones(btnMarcas, e, vistaMarcas);
        } else if(source.equals(btnMedidas)) {
            accionBotones(btnMedidas, e, vistaMedidas);
        } else if(source.equals(btnTipoDeCliente)) {
            accionBotones(btnTipoDeCliente, e, vistaTiposDeCliente);
        } else if(source.equals(btnTipoDeContacto)) {
            accionBotones(btnTipoDeContacto, e, vistaTiposDeContacto);
        } else if(source.equals(btnBancos)) {
            accionBotones(btnBancos, e, vistaBancos);
        } else if(source.equals(btnMetodosDeLiquidacion)) {
            accionBotones(btnMetodosDeLiquidacion, e, vistaMetodosDeLiquidacion);
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
                btnCategorias, btnMarcas, btnMedidas, btnTipoDeCliente, btnTipoDeContacto, btnBancos,
                btnMetodosDeLiquidacion
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
        iconosBotones.put(btnCategorias, "/com/umg/iconos/IconoInicio.png");
        iconosBotones.put(btnMarcas, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnMedidas, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnTipoDeCliente, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnTipoDeContacto, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnBancos, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnMetodosDeLiquidacion, "/com/umg/iconos/IconoClientes.png");
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
