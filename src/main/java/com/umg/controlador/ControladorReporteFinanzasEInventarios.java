package com.umg.controlador;

import com.umg.implementacion.ReporteImp;
import com.umg.modelo.ModeloReporteFinanzasEInventarios;
import com.umg.seguridad.Sesion;
import com.umg.util.Exportador;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ControladorReporteFinanzasEInventarios implements ActionListener, MouseListener {
    ModeloReporteFinanzasEInventarios modelo;

    private JPanel btnGenerarPDF, btnGenerarExcel, btnLimpiar, btnVerReporte;
    private JLabel lblGenerarPDF, lblGenerarExcel, lblLimpiar, lblVerReporte;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    ReporteImp implementacion =  new ReporteImp();

    public ControladorReporteFinanzasEInventarios(ModeloReporteFinanzasEInventarios modelo) {
        this.modelo = modelo;

        // Inicializar botones y labels
        var vista = modelo.getVista();

        btnGenerarPDF = vista.btnGenerarPDF;
        btnGenerarExcel = vista.btnGenerarExcel;
        btnLimpiar = vista.btnLimpiar;
        btnVerReporte = vista.btnVerReporte;

        lblGenerarPDF = vista.lblGenerarPDF;
        lblGenerarExcel = vista.lblGenerarExcel;
        lblLimpiar = vista.lblLimpiar;
        lblVerReporte = vista.lblVerReporte;

        // Dar nombre a los labels para manejar iconos
        lblGenerarPDF.setName("icono");
        lblGenerarExcel.setName("icono");
        lblLimpiar.setName("icono");
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
            reportes();
        } else if (e.getComponent().equals(vista.btnGenerarPDF)) {
            exportarReportePDF();
        } else if (e.getComponent().equals(vista.btnGenerarExcel)) {
            exportarReporteExcel();
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
            vista.tblReporte.setModel(implementacion.cuentasPorCobrar());
        } else if(tipoReporte == 1){
            vista.tblReporte.setModel(implementacion.cuentasPorPagar());
        } else if (tipoReporte == 2){
            vista.tblReporte.setModel(implementacion.movimientosInventario(modelo.getVista().txtFechaInicio.getText(),
                    modelo.getVista().txtFechaFin.getText(), Sesion.getUsuario()));
        }
    }

    private void exportarReportePDF() {
        var vista = modelo.getVista();
        TableModel model = vista.tblReporte.getModel();
        String titulo = vista.cmbTipoReporte.getSelectedItem().toString();

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(titulo.replace(" ", "_") + ".pdf"));
        if (chooser.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            try {
                Exportador.exportarPDF(model, titulo, chooser.getSelectedFile());
                JOptionPane.showMessageDialog(vista, "PDF generado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al generar PDF: " + ex.getMessage());
            }
        }
    }

    private void exportarReporteExcel() {
        var vista = modelo.getVista();
        TableModel model = vista.tblReporte.getModel();
        String titulo = vista.cmbTipoReporte.getSelectedItem().toString();

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(titulo.replace(" ", "_") + ".xlsx"));
        if (chooser.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            try {
                Exportador.exportarExcel(model, titulo, chooser.getSelectedFile());
                JOptionPane.showMessageDialog(vista, "Excel generado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al generar Excel: " + ex.getMessage());
            }
        }
    }

    private void inicializarIconos() {
        iconosBotones.put(btnGenerarPDF, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnGenerarExcel, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnLimpiar, "/com/umg/iconos/IconoBoton1.png");
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
