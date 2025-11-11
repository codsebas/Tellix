package com.umg.controlador;

import com.umg.implementacion.ReporteImp;
import com.umg.modelo.ModeloReporteVentasYCompras;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladorReporteVentasYCompras implements ActionListener, MouseListener {
    ModeloReporteVentasYCompras modelo;

    private JPanel btnGenerarPDF, btnGenerarExcel, btnLimpiar, btnBuscar, btnVerReporte;
    private JLabel lblGenerarPDF, lblGenerarExcel, lblLimpiar, lblBuscar, lblVerReporte;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    ReporteImp implementacion = new ReporteImp();

    public ControladorReporteVentasYCompras(ModeloReporteVentasYCompras modelo) {
        this.modelo = modelo;

        // Inicializar botones y labels
        var vista = modelo.getVista();

        btnGenerarPDF = vista.btnGenerarPDF;
        btnGenerarExcel = vista.btnGenerarExcel;
        btnLimpiar = vista.btnLimpiar;
        btnBuscar = vista.btnBuscar;
        btnVerReporte = vista.btnVerReporte;

        lblGenerarPDF = vista.lblGenerarPDF;
        lblGenerarExcel = vista.lblGenerarExcel;
        lblLimpiar = vista.lblLimpiar;
        lblBuscar = vista.lblBuscar;
        lblVerReporte = vista.lblVerReporte;

        // Dar nombre a los labels para manejar iconos
        lblGenerarPDF.setName("icono");
        lblGenerarExcel.setName("icono");
        lblLimpiar.setName("icono");
        lblBuscar.setName("icono");
        lblVerReporte.setName("icono");

        inicializarIconos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        var vista = modelo.getVista();
        if(e.getComponent().equals(vista.btnVerReporte)) {
            System.out.println("click");
            reportes();
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
        cambiarIconoBoton((JPanel) e.getSource(), true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), false);
    }

    private void reportes(){
        var vista = modelo.getVista();
        int tipoReporte = vista.cmbTipoReporte.getSelectedIndex();
        if(tipoReporte == 0){
            vista.tblReporte.setModel(implementacion.ventasDelDia());
        } else if(tipoReporte == 1){
            vista.tblReporte.setModel(implementacion.ventasRangoFechas(modelo.getVista().txtFechaInicio.getText(),
                    modelo.getVista().txtFechaFin.getText()));
        }
    }

    private void inicializarIconos() {
        iconosBotones.put(btnGenerarPDF, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnGenerarExcel, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnLimpiar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnBuscar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnVerReporte, "/com/umg/iconos/IconoBoton1.png");
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
