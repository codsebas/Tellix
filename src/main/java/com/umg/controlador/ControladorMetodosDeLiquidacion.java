package com.umg.controlador;

import com.umg.implementacion.MetodosDeLiquidacionImp;
import com.umg.interfaces.IMetodosDeLiquidacion;
import com.umg.modelo.ModeloMetodosDeLiquidacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ControladorMetodosDeLiquidacion implements ActionListener, MouseListener {

    private final ModeloMetodosDeLiquidacion modelo;
    private final IMetodosDeLiquidacion servicio = new MetodosDeLiquidacionImp();

    // Botones
    private final JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private final JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private final JComboBox<String> cmbOrdenarPor;

    private final Map<JPanel,String> iconosBotones = new HashMap<>();

    public ControladorMetodosDeLiquidacion(ModeloMetodosDeLiquidacion modelo) {
        this.modelo = modelo;
        var v = modelo.getVista();

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

        cmbOrdenarPor = v.cmbOrdenarPor;

        inicializarIconos();

//        // Listeners de botones
//        if (btnNuevo != null)      btnNuevo.addMouseListener(this);
//        if (btnActualizar != null) btnActualizar.addMouseListener(this);
//        if (btnEliminar != null)   btnEliminar.addMouseListener(this);
//        if (btnBuscar != null)     btnBuscar.addMouseListener(this);
//        if (btnLimpiar != null)    btnLimpiar.addMouseListener(this);

        if (cmbOrdenarPor != null) {
            cmbOrdenarPor.addActionListener(e -> refrescarTabla());
        }

        // Selección en tabla
        JTable t = v.tblMetodosDeLiquidacion;
        if (t != null) {
            t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            t.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onSeleccionTabla();
                }
            });
        }

        // Datos iniciales
        refrescarTabla();
    }

    // =========================================================
    // CRUD
    // =========================================================
    private void onNuevo() {
        var v = modelo.getVista();
        String desc = safe(v.txtDescripcion.getText());
        if (desc == null) {
            mensaje("La descripción es obligatoria.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelo.setDescripcion(desc);

        if (!servicio.insertar(modelo)) {
            mensaje("No se pudo guardar el método de liquidación.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mensaje("Método de liquidación guardado.", "OK", JOptionPane.INFORMATION_MESSAGE);
        limpiarFormulario();
        refrescarTabla();
    }

    private void onActualizar() {
        var v = modelo.getVista();
        Integer cod = parseIntSafe(v.txtCodigo.getText());
        String desc = safe(v.txtDescripcion.getText());

        if (cod == null) {
            mensaje("Selecciona un registro o indica un código válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (desc == null) {
            mensaje("La descripción es obligatoria.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelo.setCodigo(cod);
        modelo.setDescripcion(desc);

        if (!servicio.actualizar(modelo)) {
            mensaje("No se pudo actualizar el método de liquidación.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mensaje("Método de liquidación actualizado.", "OK", JOptionPane.INFORMATION_MESSAGE);
        limpiarFormulario();
        refrescarTabla();
    }

    private void onEliminar() {
        var v = modelo.getVista();
        Integer cod = parseIntSafe(v.txtCodigo.getText());
        if (cod == null) {
            mensaje("Selecciona un registro o indica un código válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int r = JOptionPane.showConfirmDialog(v,
                "¿Eliminar el método con código " + cod + " ?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        if (!servicio.eliminar(cod)) {
            mensaje("No se pudo eliminar el método de liquidación.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mensaje("Método de liquidación eliminado.", "OK", JOptionPane.INFORMATION_MESSAGE);
        limpiarFormulario();
        refrescarTabla();
    }

    private void onBuscar() {
        var v = modelo.getVista();
        String filtro = safe(v.txtBuscar.getText());
        String orden = getCampoOrdenSeleccionado();

        if (filtro == null || filtro.isEmpty()) {
            refrescarTabla();
        } else {
            cargarTabla(servicio.buscar(filtro, orden));
        }
    }

    private void onLimpiar() {
        limpiarFormulario();
    }

    // =========================================================
    // Tabla
    // =========================================================
    private String getCampoOrdenSeleccionado() {
        if (cmbOrdenarPor == null) return "CODIGO";
        Object sel = cmbOrdenarPor.getSelectedItem();
        if (sel == null) return "CODIGO";
        String s = sel.toString().toUpperCase();
        if (s.contains("DESC")) return "DESCRIPCION";
        return "CODIGO";
    }

    private void refrescarTabla() {
        cargarTabla(servicio.listarOrdenadoPor(getCampoOrdenSeleccionado()));
    }

    private void cargarTabla(java.util.List<IMetodosDeLiquidacion.RowMetodo> data) {
        var v = modelo.getVista();
        JTable t = v.tblMetodosDeLiquidacion;
        if (t == null) return;

        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        m.setColumnIdentifiers(new Object[]{"Código","Descripción"});

        for (var row : data) {
            m.addRow(new Object[]{ row.codigo, row.descripcion });
        }

        t.setModel(m);
        t.revalidate();
        t.repaint();
    }

    private void onSeleccionTabla() {
        var v = modelo.getVista();
        JTable t = v.tblMetodosDeLiquidacion;
        if (t == null) return;

        int row = t.getSelectedRow();
        if (row < 0) return;

        row = t.convertRowIndexToModel(row);

        Object cod  = t.getModel().getValueAt(row, 0);
        Object desc = t.getModel().getValueAt(row, 1);

        v.txtCodigo.setText(valStr(cod));
        v.txtDescripcion.setText(valStr(desc));
    }

    // =========================================================
    // Util
    // =========================================================
    private void limpiarFormulario() {
        var v = modelo.getVista();
        v.txtCodigo.setText("");
        v.txtDescripcion.setText("");
        v.txtBuscar.setText("");

        JTable t = v.tblMetodosDeLiquidacion;
        if (t != null) t.clearSelection();
    }

    private void mensaje(String m, String t, int tipo) {
        JOptionPane.showMessageDialog(modelo.getVista(), m, t, tipo);
    }

    private static String safe(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String valStr(Object o) {
        return (o == null) ? "" : String.valueOf(o).trim();
    }

    private static Integer parseIntSafe(String s) {
        try {
            if (s == null) return null;
            s = s.trim();
            if (s.isEmpty()) return null;
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // =========================================================
    // MouseListener / iconos
    // =========================================================
    @Override public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if      (src == btnNuevo)      onNuevo();
        else if (src == btnActualizar) onActualizar();
        else if (src == btnEliminar)   onEliminar();
        else if (src == btnBuscar)     onBuscar();
        else if (src == btnLimpiar)    onLimpiar();
    }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), true); }
    @Override public void mouseExited (MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), false); }
    @Override public void actionPerformed(ActionEvent e) { }

    private void inicializarIconos() {
        String ruta = "/com/umg/iconos/IconoBoton1.png";
        if (btnNuevo!=null)      iconosBotones.put(btnNuevo, ruta);
        if (btnActualizar!=null) iconosBotones.put(btnActualizar, ruta);
        if (btnEliminar!=null)   iconosBotones.put(btnEliminar, ruta);
        if (btnBuscar!=null)     iconosBotones.put(btnBuscar, ruta);
        if (btnLimpiar!=null)    iconosBotones.put(btnLimpiar, ruta);
    }

    private void cambiarIconoBoton(JPanel boton, boolean activo) {
        if (boton == null) return;
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
}
