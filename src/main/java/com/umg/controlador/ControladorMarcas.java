package com.umg.controlador;

import com.umg.implementacion.MarcaImp;
import com.umg.modelo.ModeloMarcas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorMarcas implements ActionListener, MouseListener {
    ModeloMarcas modelo;
    private MarcaImp implementacion = new MarcaImp();

    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorMarcas(ModeloMarcas modelo) {
        this.modelo = modelo;
        var vista = modelo.getVista();

        // Inicializar botones y labels
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

        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        inicializarIconos();
        listarMarcas();

        // Listener para tabla
        vista.tblMarca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = vista.tblMarca.getSelectedRow();
                if (fila >= 0) {
                    String codigo = vista.tblMarca.getValueAt(fila, 0).toString();
                    String descripcion = vista.tblMarca.getValueAt(fila, 1).toString();

                    vista.txtCodigo.setText(codigo);
                    vista.txtDescripcion.setText(descripcion);
                    vista.txtCodigo.setEditable(false);
                }
            }
        });

        // ComboBox: ordenar automáticamente
        vista.cmbOrdenarPor.addActionListener(e -> listarMarcas());

        // Búsqueda: actualizar tabla en tiempo real
        vista.txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { listarMarcas(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { listarMarcas(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { listarMarcas(); }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnNuevo) {
            nuevoMarca();
        } else if (e.getSource() == btnActualizar) {
            actualizarMarca();
        } else if (e.getSource() == btnEliminar) {
            eliminarMarca();
        } else if (e.getSource() == btnBuscar) {
            buscarMarca();
        } else if (e.getSource() == btnLimpiar) {
            limpiarCampos();
            modelo.getVista().txtCodigo.setEditable(true);
        }
    }

    private void listarMarcas() {
        DefaultTableModel tabla = (DefaultTableModel) modelo.getVista().tblMarca.getModel();
        tabla.setRowCount(0);

        List<ModeloMarcas> lista;
        String filtro = modelo.getVista().txtBuscar.getText().trim();
        if (!filtro.isEmpty()) {
            lista = implementacion.buscar(filtro); // Debe devolver List<ModeloMarcas>
        } else {
            lista = implementacion.obtenerTodos();
        }

        String orden = (String) modelo.getVista().cmbOrdenarPor.getSelectedItem();
        if ("Código".equals(orden)) {
            lista.sort((a,b) -> a.getMarca().compareToIgnoreCase(b.getMarca()));
        } else if ("Descripción".equals(orden)) {
            lista.sort((a,b) -> a.getDescripcion().compareToIgnoreCase(b.getDescripcion()));
        }

        for (ModeloMarcas m : lista) {
            tabla.addRow(new Object[]{m.getMarca(), m.getDescripcion()});
        }

        modelo.getVista().tblMarca.repaint();
        modelo.getVista().tblMarca.revalidate();
    }

    private ModeloMarcas obtenerDatosVista() {
        ModeloMarcas m = new ModeloMarcas(modelo.getVista());
        m.setMarca(modelo.getVista().txtCodigo.getText().trim());
        m.setDescripcion(modelo.getVista().txtDescripcion.getText().trim());
        return m;
    }

    private boolean validarCampos(ModeloMarcas m, boolean esNuevo) {
        if (m.getMarca().isEmpty()) {
            JOptionPane.showMessageDialog(modelo.getVista(), "El código es obligatorio");
            return false;
        }
        if (m.getDescripcion().isEmpty()) {
            JOptionPane.showMessageDialog(modelo.getVista(), "La descripción es obligatoria");
            return false;
        }
        if (esNuevo && implementacion.obtenerPorCodigo(m.getMarca()) != null) {
            JOptionPane.showMessageDialog(modelo.getVista(), "El código ya existe");
            return false;
        }
        return true;
    }

    private void nuevoMarca() {
        ModeloMarcas m = obtenerDatosVista();
        if (!validarCampos(m, true)) return;
        if (implementacion.insertar(m)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "✅ Marca insertada correctamente");
            listarMarcas();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al insertar marca");
        }
    }

    private void actualizarMarca() {
        ModeloMarcas m = obtenerDatosVista();
        if (!validarCampos(m, false)) return;
        if (implementacion.actualizar(m)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "✅ Marca actualizada correctamente");
            listarMarcas();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al actualizar marca");
        }
    }

    private void eliminarMarca() {
        try {
            String codigo = modelo.getVista().txtCodigo.getText();
            int opcion = JOptionPane.showConfirmDialog(modelo.getVista(), "¿Desea eliminar esta marca?");
            if (opcion == JOptionPane.YES_OPTION) {
                if (implementacion.eliminar(codigo)) {
                    JOptionPane.showMessageDialog(modelo.getVista(), "✅ Marca eliminada correctamente");
                    listarMarcas();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al eliminar marca");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error: " + ex.getMessage());
        }
    }

    private void buscarMarca() {
        try {
            String codigo = modelo.getVista().txtBuscar.getText();
            ModeloMarcas m = implementacion.obtenerPorCodigo(codigo);
            if (m != null) {
                modelo.getVista().txtCodigo.setText(m.getMarca());
                modelo.getVista().txtDescripcion.setText(m.getDescripcion());
            } else {
                JOptionPane.showMessageDialog(modelo.getVista(), "Marca no encontrada");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        modelo.getVista().txtCodigo.setText("");
        modelo.getVista().txtDescripcion.setText("");
        modelo.getVista().txtBuscar.setText("");
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel) e.getSource(), true); }
    @Override public void mouseExited(MouseEvent e) { cambiarIconoBoton((JPanel) e.getSource(), false); }

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
            if (comp instanceof JLabel lbl && nombre.equals(lbl.getName())) return lbl;
        }
        return null;
    }
}
