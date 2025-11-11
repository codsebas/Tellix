package com.umg.controlador;

import com.umg.modelo.ModeloLogin;
import com.umg.modelo.ModeloMenu;
import com.umg.seguridad.Sesion;
import com.umg.vistas.*;

import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class ControladorMenu implements MouseListener {

    ModeloMenu modelo;
    VistaMenu vista;
    VistaPrincipal vistaPrincipal;

    // Colores
    Color celesteFocus = new Color(0,123,255);
    Color grisFondo = new Color(245,247,250);
    Color grisOscuro = new Color(51,51,51);
    Color blancoPuro = new Color(255,255,255);

    public ControladorMenu(ModeloMenu modelo, VistaMenu vista, VistaPrincipal vistaPrincipal) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;

        vista.btnCerrarSesion.addMouseListener(this);
        vista.btnInicio.addMouseListener(this);
        vista.btnClientes.addMouseListener(this);
        vista.btnVentas.addMouseListener(this);
        vista.btnProductos.addMouseListener(this);
        vista.btnCompras.addMouseListener(this);
        vista.btnCuentasPorCoPa.addMouseListener(this);
        vista.btnUsuariosYRoles.addMouseListener(this);
        vista.btnReportes.addMouseListener(this);
        vista.btnInventario.addMouseListener(this);
        vista.btnConfiguracion.addMouseListener(this);
        vista.btnEmpleados.addMouseListener(this);

        btnCerrarSesion = vista.btnCerrarSesion;
        btnInicio = vista.btnInicio;
        btnClientes = vista.btnClientes;
        btnVentas = vista.btnVentas;
        btnProductos = vista.btnProductos;
        btnCompras = vista.btnCompras;
        btnCuentasPorCoPa = vista.btnCuentasPorCoPa;
        btnUsuariosYRoles = vista.btnUsuariosYRoles;
        btnReportes = vista.btnReportes;
        btnInventario = vista.btnInventario;
        btnConfiguracion = vista.btnConfiguracion;
        btnEmpleados = vista.btnEmpleados;

        lblCerrarSesion = vista.lblCerrarSesion;
        lblInicio = vista.lblInicio;
        lblClientes = vista.lblClientes;
        lblVentas = vista.lblVentas;
        lblProductos = vista.lblProductos;
        lblCompras = vista.lblCompras;
        lblCuentasPorCoPa = vista.lblCuentasPorCoPa;
        lblUsuariosYRoles = vista.lblUsuariosYRoles;
        lblReportes = vista.lblReportes;
        lblInventario = vista.lblInventario;
        lblConfiguracion = vista.lblConfiguracion;
        lblEmpleados = vista.lblEmpleados;

        lblCerrarSesion.setName("texto");
        lblInicio.setName("texto");
        lblClientes.setName("texto");
        lblVentas.setName("texto");
        lblProductos.setName("texto");
        lblCompras.setName("texto");
        lblCuentasPorCoPa.setName("texto");
        lblUsuariosYRoles.setName("texto");
        lblReportes.setName("texto");
        lblInventario.setName("texto");
        lblConfiguracion.setName("texto");
        lblEmpleados.setName("texto");

        lblIconoCerrarSesion = vista.lblIconoCerrarSesion;
        lblIconoInicio = vista.lblIconoInicio;
        lblIconoClientes = vista.lblIconClientes;
        lblIconoVentas = vista.lblIconoVentas;
        lblIconoProductos = vista.lblIconoProductos;
        lblIconoCompras = vista.lblIconoCompras;
        lblIconoCuentasPorCoPa = vista.lblIconoCuentasPorCoPa;
        lblIconoUsuariosYRoles = vista.lblIconoUsuarioYRoles;
        lblIconoReportes = vista.lblIconoReportes;
        lblIconoInventario = vista.lblIconoInventario;
        lblIconoConfiguracion = vista.lblIconoConfiguracion;
        lblIconoEmpleados = vista.lblIconoEmpleados;

        lblIconoCerrarSesion.setName("icono");
        lblIconoInicio.setName("icono");
        lblIconoClientes.setName("icono");
        lblIconoVentas.setName("icono");
        lblIconoProductos.setName("icono");
        lblIconoCompras.setName("icono");
        lblIconoCuentasPorCoPa.setName("icono");
        lblIconoUsuariosYRoles.setName("icono");
        lblIconoReportes.setName("icono");
        lblIconoInventario.setName("icono");
        lblIconoConfiguracion.setName("icono");
        lblIconoEmpleados.setName("icono");

        lblCerrarSesionSeleccionado = vista.lblCerrarSesionSeleccionado;
        lblInicioSeleccionado = vista.lblInicioSeleccionado;
        lblClientesSeleccionado = vista.lblClientesSeleccionado;
        lblVentasSeleccionado = vista.lblVentasSeleccionado;
        lblProductosSeleccionado = vista.lblProductosSeleccionado;
        lblComprasSeleccionado = vista.lblComprasSeleccionado;
        lblCuentasPorCoPaSeleccionado = vista.lblCuentasPorCoPaSeleccionado;
        lblUsuariosYRolesSeleccionado = vista.lblUsuariosYRolesSeleccionado;
        lblReportesSeleccionado = vista.lblReportesSeleccionado;
        lblInventarioSeleccionado = vista.lblInventarioSeleccionado;
        lblConfiguracionSeleccionado = vista.lblConfiguracionSeleccionado;
        lblEmpleadosSeleccionado = vista.lblEmpleadosSeleccionado;

        lblCerrarSesionSeleccionado.setName("efecto");
        lblInicioSeleccionado.setName("efecto");
        lblClientesSeleccionado.setName("efecto");
        lblVentasSeleccionado.setName("efecto");
        lblProductosSeleccionado.setName("efecto");
        lblComprasSeleccionado.setName("efecto");
        lblCuentasPorCoPaSeleccionado.setName("efecto");
        lblUsuariosYRolesSeleccionado.setName("efecto");
        lblReportesSeleccionado.setName("efecto");
        lblInventarioSeleccionado.setName("efecto");
        lblConfiguracionSeleccionado.setName("efecto");
        lblEmpleadosSeleccionado.setName("efecto");

        lblCerrarSesionSeleccionado.setVisible(false);
        lblInicioSeleccionado.setVisible(false);
        lblClientesSeleccionado.setVisible(false);
        lblVentasSeleccionado.setVisible(false);
        lblProductosSeleccionado.setVisible(false);
        lblComprasSeleccionado.setVisible(false);
        lblCuentasPorCoPaSeleccionado.setVisible(false);
        lblUsuariosYRolesSeleccionado.setVisible(false);
        lblReportesSeleccionado.setVisible(false);
        lblInventarioSeleccionado.setVisible(false);
        lblConfiguracionSeleccionado.setVisible(false);
        lblEmpleadosSeleccionado.setVisible(false);

        inicializarIconos();
        cargarVistaPanel(vistaInicio);
    }

    // Componentes
    private JPanel btnCerrarSesion;
    private JPanel btnInicio;
    private JPanel btnClientes;
    private JPanel btnVentas;
    private JPanel btnProductos;
    private JPanel btnCompras;
    private JPanel btnCuentasPorCoPa;
    private JPanel btnUsuariosYRoles;
    private JPanel btnReportes;
    private JPanel btnInventario;
    private JPanel btnConfiguracion;
    private JPanel btnEmpleados;

    private JLabel lblCerrarSesion;
    private JLabel lblInicio;
    private JLabel lblClientes;
    private JLabel lblVentas;
    private JLabel lblProductos;
    private JLabel lblCompras;
    private JLabel lblCuentasPorCoPa;
    private JLabel lblUsuariosYRoles;
    private JLabel lblReportes;
    private JLabel lblInventario;
    private JLabel lblConfiguracion;
    private JLabel lblEmpleados;

    private JLabel lblIconoCerrarSesion;
    private JLabel lblIconoInicio;
    private JLabel lblIconoClientes;
    private JLabel lblIconoVentas;
    private JLabel lblIconoProductos;
    private JLabel lblIconoCompras;
    private JLabel lblIconoCuentasPorCoPa;
    private JLabel lblIconoUsuariosYRoles;
    private JLabel lblIconoReportes;
    private JLabel lblIconoInventario;
    private JLabel lblIconoConfiguracion;
    private JLabel lblIconoEmpleados;

    private JLabel lblCerrarSesionSeleccionado;
    private JLabel lblInicioSeleccionado;
    private JLabel lblClientesSeleccionado;
    private JLabel lblVentasSeleccionado;
    private JLabel lblProductosSeleccionado;
    private JLabel lblComprasSeleccionado;
    private JLabel lblCuentasPorCoPaSeleccionado;
    private JLabel lblUsuariosYRolesSeleccionado;
    private JLabel lblReportesSeleccionado;
    private JLabel lblInventarioSeleccionado;
    private JLabel lblConfiguracionSeleccionado;
    private JLabel lblEmpleadosSeleccionado;

    private JPanel botonSeleccionado = null;

    // Vistas
    VistaInicio vistaInicio = new  VistaInicio();
    VistaMenuClientes vistaMenuClientes = new  VistaMenuClientes();
    VistaMenuVentas vistaMenuVentas = new  VistaMenuVentas();
    VistaMenuProductos vistaMenuProductos = new VistaMenuProductos();
    VistaMenuCompras vistaMenuCompras = new  VistaMenuCompras();
    VistaMenuCuentas vistaMenuCuentas = new  VistaMenuCuentas();
    VistaMenuUsuariosYRoles vistaMenuUsuariosYRoles = new  VistaMenuUsuariosYRoles();
    VistaMenuReportes vistaMenuReportes = new  VistaMenuReportes();
    VistaMenuInventario vistaMenuInventario = new  VistaMenuInventario();
    VistaMenuConfiguracion vistaMenuConfiguracion = new  VistaMenuConfiguracion();
    VistaEmpleados vistaEmpleados = new  VistaEmpleados();


    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getComponent();

        if (source.equals(btnInicio)) {
            accionBotones(btnInicio, e, vistaInicio);
        } else if(source.equals(btnClientes)) {
            accionBotones(btnClientes, e, vistaMenuClientes);
        } else if(source.equals(btnVentas)) {
            accionBotones(btnVentas, e, vistaMenuVentas);
        } else if(source.equals(btnProductos)) {
            accionBotones(btnProductos, e, vistaMenuProductos);
        } else if(source.equals(btnCompras)) {
            accionBotones(btnCompras, e, vistaMenuCompras);
        } else if(source.equals(btnCuentasPorCoPa)) {
            accionBotones(btnCuentasPorCoPa, e, vistaMenuCuentas);
        } else if(source.equals(btnUsuariosYRoles)) {
            accionBotones(btnUsuariosYRoles, e, vistaMenuUsuariosYRoles);
        } else if(source.equals(btnReportes)) {
            accionBotones(btnReportes, e, vistaMenuReportes);
        } else if(source.equals(btnInventario)) {
            accionBotones(btnInventario, e, vistaMenuInventario);
        } else if(source.equals(btnConfiguracion)) {
            accionBotones(btnConfiguracion, e, vistaMenuConfiguracion);
        } else if(source.equals(btnEmpleados)) {
            accionBotones(btnEmpleados, e, vistaEmpleados);
        } else if (source.equals(btnCerrarSesion)) {
            reiniciarColores();
            Sesion.cerrarSesion();
            vistaLogin();
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

    // Metodo Cambiar Color a Paneles
    private void colorFondoPanel(JPanel panel, Color color, MouseEvent e) {
        if(e.getComponent().equals(panel)){
            panel.setBackground(color);
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

    private void vistaLogin(){
        ModeloLogin modelo = new ModeloLogin();
        VistaLogin vista = new VistaLogin();
        new ControladorLogin(modelo, vista, vistaPrincipal);
        vistaPrincipal.cambiarPanel(vista);
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
        vista.contenedor.removeAll();
        vista.contenedor.add(panel);
        vista.contenedor.revalidate();
        vista.contenedor.repaint();
    }

    private void reiniciarColores(){

        JPanel[] botones = {
                btnCerrarSesion, btnInicio, btnClientes, btnVentas,
                btnProductos, btnCompras, btnCuentasPorCoPa,
                btnUsuariosYRoles, btnReportes, btnInventario, btnConfiguracion,
                btnEmpleados
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
        iconosBotones.put(btnCerrarSesion, "/com/umg/iconos/IconoCerrarSesion.png");
        iconosBotones.put(btnInicio, "/com/umg/iconos/IconoInicio.png");
        iconosBotones.put(btnClientes, "/com/umg/iconos/IconoClientes.png");
        iconosBotones.put(btnVentas, "/com/umg/iconos/IconoVentas.png");
        iconosBotones.put(btnProductos, "/com/umg/iconos/IconoProductos.png");
        iconosBotones.put(btnCompras, "/com/umg/iconos/IconoCompras.png");
        iconosBotones.put(btnCuentasPorCoPa, "/com/umg/iconos/IconoCuentasPorCoPa.png");
        iconosBotones.put(btnUsuariosYRoles, "/com/umg/iconos/IconoUsuariosYRoles.png");
        iconosBotones.put(btnReportes, "/com/umg/iconos/IconoReportes.png");
        iconosBotones.put(btnInventario, "/com/umg/iconos/IconoInventario.png");
        iconosBotones.put(btnConfiguracion, "/com/umg/iconos/IconoInventario.png");
        iconosBotones.put(btnEmpleados, "/com/umg/iconos/IconoInventario.png");
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
