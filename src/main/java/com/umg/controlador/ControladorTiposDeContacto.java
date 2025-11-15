package com.umg.controlador;

import com.umg.implementacion.TipoContactoImp;
import com.umg.interfaces.ITipoContacto;
import com.umg.modelo.ModeloTiposDeContacto;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorTiposDeContacto implements ActionListener, MouseListener {

    private final ModeloTiposDeContacto modelo;
    private final ITipoContacto.Servicio servicio = new TipoContactoImp();

    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private final Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorTiposDeContacto(ModeloTiposDeContacto modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        // Mapear botones/labels (USANDO tus nombres)
        btnNuevo      = vista.btnNuevo;
        btnActualizar = vista.btnActualizar;
        btnEliminar   = vista.btnEliminar;
        btnBuscar     = vista.btnBuscar;
        btnLimpiar    = vista.btnLimpiar;

        lblNuevo      = vista.lblNuevo;
        lblActualizar = vista.lblActualizar;
        lblEliminar   = vista.lblEliminar;
        lblBuscar     = vista.lblBuscar;
        lblLimpiar    = vista.lblLimpiar;

        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        inicializarIconos();



        // Selección en tabla -> llenar campos
        if (vista.tblTiposDeContacto != null) {
            vista.tblTiposDeContacto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            vista.tblTiposDeContacto.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { onSeleccionFila(); }
            });
            ListSelectionListener ls = ev -> { if (!ev.getValueIsAdjusting()) onSeleccionFila(); };
            vista.tblTiposDeContacto.getSelectionModel().addListSelectionListener(ls);
        }

        // Carga inicial
        refrescarTabla();
    }

    // ======================= Iconos =======================
    private void inicializarIconos() {
        String ruta = "/com/umg/iconos/IconoBoton1.png";
        iconosBotones.put(btnNuevo, ruta);
        iconosBotones.put(btnActualizar, ruta);
        iconosBotones.put(btnEliminar, ruta);
        iconosBotones.put(btnBuscar, ruta);
        iconosBotones.put(btnLimpiar, ruta);
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
            if (comp instanceof JLabel lbl && nombre.equals(lbl.getName())) return lbl;
        }
        return null;
    }

    // ======================= Mouse =======================
    @Override public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if (src == btnNuevo)          onNuevo();
        else if (src == btnActualizar) onActualizar();
        else if (src == btnEliminar)   onEliminar();
        else if (src == btnBuscar)     onBuscar();
        else if (src == btnLimpiar)    onLimpiar();

    }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), true); }
    @Override public void mouseExited(MouseEvent e)  { cambiarIconoBoton((JPanel)e.getSource(), false); }
    @Override public void actionPerformed(ActionEvent e) { }

    // ======================= Acciones =======================
    private void onNuevo() {
        var v = modelo.getVista();
        String desc = safe(v.txtDescripcion.getText());
        if (desc == null || desc.isBlank()) {
            mensaje("La descripción es obligatoria.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (servicio.crear(desc)) {
            mensaje("Tipo de contacto guardado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            refrescarTabla();
        } else {
            mensaje("No se pudo guardar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onActualizar() {
        var v = modelo.getVista();
        Integer cod = parseIntSafe(v.txtCodigo.getText());
        String desc = safe(v.txtDescripcion.getText());
        if (cod == null) { mensaje("Indica el código.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        if (desc == null || desc.isBlank()) { mensaje("La descripción es obligatoria.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }

        if (servicio.actualizar(cod, desc)) {
            mensaje("Tipo de contacto actualizado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } else {
            mensaje("No se pudo actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onEliminar() {
        var v = modelo.getVista();
        Integer cod = parseIntSafe(v.txtCodigo.getText());
        if (cod == null) { mensaje("Indica el código a eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        int r = JOptionPane.showConfirmDialog(v, "¿Eliminar el tipo de contacto " + cod + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        if (servicio.eliminar(cod)) {
            mensaje("Eliminado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            refrescarTabla();
        } else {
            mensaje("No se pudo eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onBuscar() {
        var v = modelo.getVista();
        String f = safe(v.txtBuscar.getText());
        cargarTabla(servicio.buscar(f == null ? "" : f));
    }

    private void onLimpiar() {
        limpiarFormulario();
        refrescarTabla();
    }

    private void onSeleccionFila() {
        var v = modelo.getVista();
        JTable tbl = v.tblTiposDeContacto;
        int row = (tbl == null) ? -1 : tbl.getSelectedRow();
        if (row < 0) return;
        Object oc = tbl.getValueAt(row, 0);
        Object od = tbl.getValueAt(row, 1);
        v.txtCodigo.setText(oc == null ? "" : String.valueOf(oc));
        v.txtDescripcion.setText(od == null ? "" : String.valueOf(od));
    }

    // ======================= Tabla =======================
    private void refrescarTabla() {
        String sel = (String) modelo.getVista().cmbOrdenarPor.getSelectedItem();
        String campo = ("Descripción".equalsIgnoreCase(sel)) ? "descripcion" : "codigo";
        cargarTabla(servicio.listarOrdenadoPor(campo));
    }

    private void cargarTabla(List<ITipoContacto.Registro> data) {
        var v = modelo.getVista();
        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        m.setColumnIdentifiers(new Object[]{"Código", "Descripción"});
        for (var r : data) m.addRow(new Object[]{ r.codigo, r.descripcion });
        v.tblTiposDeContacto.setModel(m);
        actualizarTablaUI(v.tblTiposDeContacto);
    }

    private void actualizarTablaUI(JTable t) {
        if (t == null) return;
        SwingUtilities.invokeLater(() -> {
            if (t.getModel() instanceof DefaultTableModel dm) dm.fireTableDataChanged();
            t.revalidate();
            t.repaint();
        });
    }

    // ======================= Util =======================
    private void limpiarFormulario() {
        var v = modelo.getVista();
        v.txtCodigo.setText("");
        v.txtDescripcion.setText("");
        v.txtBuscar.setText("");
        if (v.cmbOrdenarPor.getItemCount() > 0) v.cmbOrdenarPor.setSelectedIndex(0);
    }
    private void mensaje(String m, String t, int tipo) { JOptionPane.showMessageDialog(modelo.getVista(), m, t, tipo); }
    private String safe(String s) { if (s == null) return null; String t = s.trim(); return t.isEmpty()? null : t; }
    private Integer parseIntSafe(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; } }
}
