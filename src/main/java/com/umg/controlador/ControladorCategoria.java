package com.umg.controlador;

import com.umg.implementacion.CategoriaImp;
import com.umg.modelo.ModeloCategoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorCategoria implements ActionListener, MouseListener {

    private ModeloCategoria modelo;
    private CategoriaImp implementacion = new CategoriaImp();

    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorCategoria(ModeloCategoria modelo) {
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

        // Registrar MouseListener
        //btnNuevo.addMouseListener(this);
        //btnActualizar.addMouseListener(this);
        //btnEliminar.addMouseListener(this);
        //btnBuscar.addMouseListener(this);
        btnLimpiar.addMouseListener(this);

        // Dar nombre a los labels para iconos
        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        inicializarIconos();
        listarCategorias();

        // Listener para tabla
        vista.tblCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = vista.tblCategorias.getSelectedRow();
                if (fila >= 0) {
                    String codigo = vista.tblCategorias.getValueAt(fila, 0).toString();
                    String descripcion = vista.tblCategorias.getValueAt(fila, 1).toString();
                    vista.txtCodigo.setText(codigo);
                    vista.txtDescripcion.setText(descripcion);
                    vista.txtCodigo.setEditable(false);
                }
            }
        });

        // ComboBox: ordenar automáticamente
        vista.cmbOrdenarPor.addActionListener(e -> listarCategorias());

        // Búsqueda en tiempo real
        vista.txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { listarCategorias(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { listarCategorias(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { listarCategorias(); }
        });
    }

    // ------------------- CRUD -------------------
    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnNuevo) nuevoCategoria();
        else if (e.getSource() == btnActualizar) actualizarCategoria();
        else if (e.getSource() == btnEliminar) eliminarCategoria();
        else if (e.getSource() == btnBuscar) buscarCategoria();
        else if (e.getSource() == btnLimpiar) {
            limpiarCampos();
            modelo.getVista().txtCodigo.setEditable(true);
        }
    }

    private void listarCategorias() {
        DefaultTableModel tabla = (DefaultTableModel) modelo.getVista().tblCategorias.getModel();
        tabla.setRowCount(0);

        List<ModeloCategoria> lista;
        String filtro = modelo.getVista().txtBuscar.getText().trim();
        lista = filtro.isEmpty() ? implementacion.obtenerTodos() : implementacion.buscar(filtro);

        String orden = (String) modelo.getVista().cmbOrdenarPor.getSelectedItem();
        if ("Código".equals(orden)) lista.sort((a,b) -> Integer.compare(a.getCodigo(), b.getCodigo()));
        else if ("Descripción".equals(orden)) lista.sort((a,b) -> a.getDescripcion().compareToIgnoreCase(b.getDescripcion()));

        for (ModeloCategoria c : lista) tabla.addRow(new Object[]{c.getCodigo(), c.getDescripcion()});
    }

    private ModeloCategoria obtenerDatosVista() {
        ModeloCategoria c = new ModeloCategoria(modelo.getVista());
        try {
            c.setCodigo(Integer.parseInt(modelo.getVista().txtCodigo.getText().trim()));
        } catch (NumberFormatException ex) {
            c.setCodigo(0);
        }
        c.setDescripcion(modelo.getVista().txtDescripcion.getText().trim());
        return c;
    }

    private boolean validarCampos(ModeloCategoria c, boolean esNuevo) {

        if (c.getDescripcion().isEmpty()) {
            JOptionPane.showMessageDialog(modelo.getVista(), "La descripción es obligatoria");
            return false;
        }
        if (esNuevo && implementacion.obtenerPorCodigo(c.getCodigo()) != null) {
            JOptionPane.showMessageDialog(modelo.getVista(), "El código ya existe");
            return false;
        }
        return true;
    }

    private void nuevoCategoria() {
        ModeloCategoria c = obtenerDatosVista();
        if (!validarCampos(c, true)) return;
        if (implementacion.insertar(c)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "✅ Categoría insertada correctamente");
            listarCategorias();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al insertar categoría");
        }
    }

    private void actualizarCategoria() {
        ModeloCategoria c = obtenerDatosVista();
        if (!validarCampos(c, false)) return;
        if (implementacion.actualizar(c)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "✅ Categoría actualizada correctamente");
            listarCategorias();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al actualizar categoría");
        }
    }

    private void eliminarCategoria() {
        try {
            int codigo = Integer.parseInt(modelo.getVista().txtCodigo.getText());
            int opcion = JOptionPane.showConfirmDialog(modelo.getVista(), "¿Desea eliminar esta categoría?");
            if (opcion == JOptionPane.YES_OPTION) {
                if (implementacion.eliminar(codigo)) {
                    JOptionPane.showMessageDialog(modelo.getVista(), "✅ Categoría eliminada correctamente");
                    listarCategorias();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al eliminar categoría");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error: " + ex.getMessage());
        }
    }

    private void buscarCategoria() {
        String texto = modelo.getVista().txtBuscar.getText().trim();
        if (texto.isEmpty()) return;

        List<ModeloCategoria> resultados = implementacion.buscar(texto);
        if (!resultados.isEmpty()) {
            ModeloCategoria c = resultados.get(0);
            modelo.getVista().txtCodigo.setText(String.valueOf(c.getCodigo()));
            modelo.getVista().txtDescripcion.setText(c.getDescripcion());
            modelo.getVista().txtCodigo.setEditable(false);
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "Categoría no encontrada");
        }
    }

    private void limpiarCampos() {
        modelo.getVista().txtCodigo.setText("");
        modelo.getVista().txtDescripcion.setText("");
        modelo.getVista().txtBuscar.setText("");
    }

    // ------------------- Iconos -------------------
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), true); }
    @Override public void mouseExited(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), false); }

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
