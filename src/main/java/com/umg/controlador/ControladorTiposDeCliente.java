/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.controlador;

import com.umg.modelo.ModeloTiposDeCliente;

/* ======== NUEVO: imports necesarios (no borres los anteriores) ======== */
import com.umg.interfaces.ITipoCliente;          // interfaz DAO/Servicio (ajusta paquete si difiere)
import com.umg.implementacion.TipoClienteImp;    // implementación DAO (ajusta paquete si difiere)

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorTiposDeCliente implements ActionListener, MouseListener {
    ModeloTiposDeCliente modelo;

    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    /* ======== NUEVO: servicio DAO ======== */
    private final ITipoCliente.DAO servicio = new TipoClienteImp();

    public ControladorTiposDeCliente(ModeloTiposDeCliente modelo) {
        this.modelo = modelo;

        // Inicializar botones y labels
        var vista = modelo.getVista();

        btnNuevo = vista.btnNuevo;
        btnActualizar = vista.btnActualizar;
        btnEliminar = vista.btnEliminar;
        btnBuscar = vista.btnBuscar;
        btnLimpiar = vista.btnLimpiar;

        lblNuevo = vista.lblNuevo;
        lblActualizar = vista.lblActualizar;
        lblEliminar = vista.lblEliminar;
        lblBuscar = vista.lblBuscar;
        lblLimpiar = vista.lblLimpiar;

        // Dar nombre a los labels para manejar iconos
        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        inicializarIconos();



        /* ======== NUEVO: ENTER en caja de búsqueda ======== */
        vista.txtBuscar.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) onBuscar();
            }
        });

        /* ======== NUEVO: selección/clic en la tabla para llenar campos ======== */
        if (vista.tblTiposDeCliente != null) {
            JTable t = vista.tblTiposDeCliente;
            t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            t.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { onSeleccionTipo(); }
            });
            ListSelectionListener ls = ev -> { if (!ev.getValueIsAdjusting()) onSeleccionTipo(); };
            t.getSelectionModel().addListSelectionListener(ls);
        }

        /* ======== NUEVO: carga inicial en tabla ======== */
        refrescarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // (mantenido; normalmente no se usa con panel-botón)
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /* ======== NUEVO: ruteo de acciones de los panel-botón ======== */
        Object src = e.getSource();
        if (src == btnNuevo)      { onNuevo();      return; }
        if (src == btnActualizar) { onActualizar(); return; }
        if (src == btnEliminar)   { onEliminar();   return; }
        if (src == btnBuscar)     { onBuscar();     return; }
        if (src == btnLimpiar)    { onLimpiar();    return; }
    }

    @Override public void mousePressed(MouseEvent e) { }

    @Override public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), false);
    }

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

    /* =========================
       NUEVO: Lógica de negocio
       ========================= */

    private void onSeleccionTipo() {
        var v = modelo.getVista();
        JTable tbl = v.tblTiposDeCliente;
        int row = (tbl == null) ? -1 : tbl.getSelectedRow();
        if (row < 0) return;

        Object c = tbl.getValueAt(row, 0);
        Object d = tbl.getValueAt(row, 1);
        v.txtCodigo.setText(c == null ? "" : String.valueOf(c));
        v.txtDescripcion.setText(d == null ? "" : d.toString());
    }

    private void onNuevo() {
        var v = modelo.getVista();
        String desc = safe(v.txtDescripcion.getText());
        if (desc == null) { msg("Ingrese la descripción.", JOptionPane.WARNING_MESSAGE); return; }
        try {
            int n = servicio.insertar(desc);
            if (n > 0) {
                msg("Tipo de cliente registrado.", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                refrescarTabla();
            } else {
                msg("No se pudo registrar.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onActualizar() {
        var v = modelo.getVista();
        Integer cod = parseIntOrNull(v.txtCodigo.getText());
        String desc = safe(v.txtDescripcion.getText());
        if (cod == null) { msg("Seleccione/indique el código.", JOptionPane.WARNING_MESSAGE); return; }
        if (desc == null) { msg("Ingrese la descripción.", JOptionPane.WARNING_MESSAGE); return; }
        try {
            int n = servicio.actualizar(cod, desc);
            if (n > 0) {
                msg("Actualizado correctamente.", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                refrescarTabla();
            } else {
                msg("No se pudo actualizar.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        var v = modelo.getVista();
        Integer cod = parseIntOrNull(v.txtCodigo.getText());
        if (cod == null) { msg("Seleccione/indique el código.", JOptionPane.WARNING_MESSAGE); return; }
        int ok = JOptionPane.showConfirmDialog(v, "¿Eliminar código " + cod + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try {
            int n = servicio.eliminar(cod);
            if (n > 0) {
                msg("Eliminado.", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                refrescarTabla();
            } else {
                msg("No se pudo eliminar.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBuscar() {
        var v = modelo.getVista();
        String f = v.txtBuscar.getText();
        try {
            cargarTabla(servicio.buscarPorDescripcion(f));
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLimpiar() {
        limpiarFormulario();
        refrescarTabla();
    }

    /* =========================
       NUEVO: Helpers UI/Tabla
       ========================= */

    private void refrescarTabla() {
        try {
            cargarTabla(servicio.listarOrdenadoPor("codigo"));
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla(List<ITipoCliente.Registro> data) {
        var v = modelo.getVista();
        JTable tabla = v.tblTiposDeCliente;
        if (tabla == null) return;

        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        m.setColumnIdentifiers(new Object[]{"Código", "Descripción"});

        if (data != null) {
            for (var r : data) m.addRow(new Object[]{ r.codigo, r.descripcion });
        }
        tabla.setModel(m);
    }

    private void limpiarFormulario() {
        var v = modelo.getVista();
        v.txtCodigo.setText("");
        v.txtDescripcion.setText("");
        v.txtBuscar.setText("");
        if (v.tblTiposDeCliente != null) v.tblTiposDeCliente.clearSelection();
    }

    /* =========================
       NUEVO: Utilidades
       ========================= */
    private void msg(String m, int tipo) {
        JOptionPane.showMessageDialog(modelo.getVista(), m, "Tipos de Cliente", tipo);
    }
    private String  safe(String s) { if (s == null) return null; String t = s.trim(); return t.isEmpty()? null : t; }
    private Integer parseIntOrNull(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; } }
}
