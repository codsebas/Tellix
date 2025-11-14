package com.umg.controlador;

import com.umg.implementacion.ProveedorImp;
import com.umg.interfaces.IProveedor;
import com.umg.modelo.ModeloProveedores;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorProveedores implements ActionListener, MouseListener {

    private final ModeloProveedores modelo;
    private final IProveedor servicio = new ProveedorImp();

    // Botones barra superior
    private final JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private final JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    // Iconos
    private final Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorProveedores(ModeloProveedores modelo) {
        this.modelo = modelo;
        var v = modelo.getVista();

        // Mapear botones superiores
        btnNuevo      = v.btnNuevo;
        btnActualizar = v.btnActualizar;
        btnEliminar   = v.btnEliminar;
        btnBuscar     = v.btnBuscar;
        btnLimpiar    = v.btnLimpiar;

        lblNuevo      = v.lblNuevo;
        lblActualizar = v.lblActualizar;
        lblEliminar   = v.lblEliminar;
        lblBuscar     = v.lblBuscar;
        lblLimpiar    = v.lblLimpiar;

        if (lblNuevo != null)      lblNuevo.setName("icono");
        if (lblActualizar != null) lblActualizar.setName("icono");
        if (lblEliminar != null)   lblEliminar.setName("icono");
        if (lblBuscar != null)     lblBuscar.setName("icono");
        if (lblLimpiar != null)    lblLimpiar.setName("icono");

        inicializarIconos();



        // ===== Datos iniciales: llenar tabla =====
        refrescarTabla();

        // ===== Selección en tabla -> llenar formulario =====
        JTable tbl = modelo.getVista().getTablaClientes();
        if (tbl != null) {
            tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tbl.getSelectionModel().addListSelectionListener(ev -> {
                if (!ev.getValueIsAdjusting()) onSeleccionProveedor();
            });
        }

        // ===== Buscar en tiempo real con txtBuscar =====
        v.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarTabla(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarTabla(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarTabla(); }
        });
    }

    // ===================== Acciones de barra superior =====================

    private void onNuevo() {
        try {
            var v = modelo.getVista();
            String nitProv = safe(v.txtNIT.getText());
            String nomProv = safe(v.txtNombreProveedor.getText());

            if (nitProv == null || nomProv == null) {
                mensaje("NIT Proveedor y Nombre son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ModeloProveedores p = new ModeloProveedores(v);
            p.setNitProveedor(nitProv);
            p.setNombreProveedor(nomProv);
            p.setDireccionFiscal(nvl(safe(v.txtDireccionFiscal.getText())));
            p.setTelefonoProveedor(nvl(safe(v.txtTelefonoProveedor.getText())));

            if (!servicio.insertarProveedor(p)) {
                mensaje("No se pudo guardar el proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            mensaje("Proveedor guardado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            refrescarTabla();
        } catch (Exception ex) {
            mensaje("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onActualizar() {
        try {
            var v  = modelo.getVista();
            String nitProv = safe(v.txtNIT.getText());
            if (nitProv == null) {
                mensaje("Indica el NIT del proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ModeloProveedores p = new ModeloProveedores(v);
            p.setNitProveedor(nitProv);
            p.setNombreProveedor(nvl(safe(v.txtNombreProveedor.getText())));
            p.setDireccionFiscal(nvl(safe(v.txtDireccionFiscal.getText())));
            p.setTelefonoProveedor(nvl(safe(v.txtTelefonoProveedor.getText())));

            if (!servicio.actualizarProveedor(p)) {
                mensaje("No se pudo actualizar el proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            mensaje("Proveedor actualizado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } catch (Exception ex) {
            mensaje("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        var v = modelo.getVista();
        String nitProv = safe(v.txtNIT.getText());
        if (nitProv == null) {
            mensaje("Indica el NIT del proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int r = JOptionPane.showConfirmDialog(v,
                "¿Eliminar proveedor " + nitProv + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        if (servicio.eliminarProveedor(nitProv)) {
            mensaje("Proveedor eliminado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            refrescarTabla();
        } else {
            mensaje("No se pudo eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onBuscar() {
        String txt = safe(modelo.getVista().txtBuscar.getText());
        // Si quieres que el botón Buscar filtre la tabla usando el servicio:
        if (txt == null || txt.isEmpty()) {
            refrescarTabla();
        } else {
            cargarTabla(servicio.buscarProveedores(txt));
        }
    }

    private void onLimpiar() {
        limpiarFormulario();
        refrescarTabla();
        modelo.getVista().txtNIT.setEditable(true);
    }

    // ===================== Selección en tabla =====================

    private void onSeleccionProveedor() {
        JTable tbl = modelo.getVista().getTablaClientes();
        int row = (tbl == null) ? -1 : tbl.getSelectedRow();
        if (row < 0) return;

        row = tbl.convertRowIndexToModel(row);
        String nitProv = valStr(tbl.getModel().getValueAt(row, 0));
        if (nitProv == null) return;

        var p = servicio.obtenerProveedorPorNit(nitProv);
        if (p == null) return;

        var v = modelo.getVista();
        v.txtNIT.setText(nvl(p.getNitProveedor()));
        v.txtNombreProveedor.setText(nvl(p.getNombreProveedor()));
        v.txtDireccionFiscal.setText(nvl(p.getDireccionFiscal()));
        v.txtTelefonoProveedor.setText(nvl(p.getTelefonoProveedor()));
        v.txtNIT.setEditable(false);
    }

    // ===================== Tablas UI =====================

    private void refrescarTabla() {
        // Lista completa desde el servicio (ordenado por NIT)
        cargarTabla(servicio.listarProveedoresOrdenNit());
    }

    private void cargarTabla(List<IProveedor.RowProv> data) {
        JTable t = modelo.getVista().getTablaClientes();
        if (t == null) return;

        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        m.setColumnIdentifiers(new Object[]{
                "NIT Proveedor", "Nombre Proveedor", "Dirección Fiscal", "Teléfono"
        });

        for (var r : data) {
            m.addRow(new Object[]{
                    r.nitProveedor,
                    r.nombreProveedor,
                    nvl(r.direccionFiscal),
                    nvl(r.telefono)
            });
        }

        t.setModel(m);

        // Sorter para que el filtro funcione
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(m);
        t.setRowSorter(sorter);
        SwingUtilities.invokeLater(t::repaint);
    }

    // ===================== Filtro txtBuscar =====================

    private void filtrarTabla() {
        String texto = nvl(modelo.getVista().txtBuscar.getText()).trim();
        JTable t = modelo.getVista().getTablaClientes();
        if (t == null || t.getRowSorter() == null) return;

        @SuppressWarnings("unchecked")
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) t.getRowSorter();

        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    // ===================== Limpiar formulario =====================

    private void limpiarFormulario() {
        var v = modelo.getVista();
        v.txtNIT.setText("");
        v.txtNombreProveedor.setText("");
        v.txtDireccionFiscal.setText("");
        v.txtTelefonoProveedor.setText("");
        v.txtBuscar.setText("");
        JTable t = v.getTablaClientes();
        if (t != null) t.clearSelection();
    }

    // ===================== Util / UI =====================

    private void mensaje(String m, String t, int tipo) {
        JOptionPane.showMessageDialog(modelo.getVista(), m, t, tipo);
    }

    private static String nvl(String s) { return (s == null) ? "" : s; }

    private static String safe(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String valStr(Object o) { return (o == null) ? null : String.valueOf(o).trim(); }

    // ===================== MouseListener / iconos =====================

    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if      (src == btnNuevo)      onNuevo();
        else if (src == btnActualizar) onActualizar();
        else if (src == btnEliminar)   onEliminar();
        else if (src == btnBuscar)     onBuscar();
        else if (src == btnLimpiar)    onLimpiar();
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof JPanel panel) cambiarIconoBoton(panel, true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JPanel panel) cambiarIconoBoton(panel, false);
    }

    @Override public void actionPerformed(ActionEvent e) { }

    // ===================== Iconos =====================

    private void inicializarIconos() {
        String ruta = "/com/umg/iconos/IconoBoton1.png";
        if (btnNuevo     != null) iconosBotones.put(btnNuevo, ruta);
        if (btnActualizar!= null) iconosBotones.put(btnActualizar, ruta);
        if (btnEliminar  != null) iconosBotones.put(btnEliminar, ruta);
        if (btnBuscar    != null) iconosBotones.put(btnBuscar, ruta);
        if (btnLimpiar   != null) iconosBotones.put(btnLimpiar, ruta);
    }

    private void cambiarIconoBoton(JPanel boton, boolean activo) {
        if (boton == null) return;
        JLabel icono = obtenerLabelPorNombre(boton, "icono");
        String rutaBase = iconosBotones.get(boton);
        if (rutaBase != null && icono != null) {
            String rutaFinal = activo
                    ? rutaBase.replace(".png", "_oscuro.png")
                    : rutaBase;
            icono.setIcon(new ImageIcon(getClass().getResource(rutaFinal)));
        }
    }

    private JLabel obtenerLabelPorNombre(JPanel boton, String nombre) {
        for (Component comp : boton.getComponents()) {
            if (comp instanceof JLabel lbl && nombre.equals(lbl.getName())) return lbl;
        }
        return null;
    }
}
