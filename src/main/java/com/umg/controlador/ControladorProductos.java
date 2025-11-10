package com.umg.controlador;

import com.umg.implementacion.ProductoImp;
import com.umg.modelo.ModeloCategoria;
import com.umg.modelo.ModeloProducto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorProductos implements ActionListener, MouseListener {

    private ModeloProducto modelo;
    private ProductoImp implementacion = new ProductoImp();

    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorProductos(ModeloProducto modelo) {
        this.modelo = modelo;
        var vista = modelo.getVista();

        // Inicializar botones
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
        listarProductos();

        // Tabla: click para llenar campos
        vista.tblProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tblProductos.getSelectedRow();
                if (fila >= 0) {
                    modelo.getVista().txtCodigo.setText(vista.tblProductos.getValueAt(fila, 0).toString());
                    modelo.getVista().txtNombre.setText(vista.tblProductos.getValueAt(fila, 1).toString());
                    modelo.getVista().txtDescripcion.setText(vista.tblProductos.getValueAt(fila, 2).toString());
                    modelo.getVista().txtStockMinimo.setText(vista.tblProductos.getValueAt(fila, 3).toString());
                    modelo.getVista().txtStockActual.setText(vista.tblProductos.getValueAt(fila, 4).toString());
                    modelo.getVista().cmbCategoria.setSelectedItem(vista.tblProductos.getValueAt(fila, 5).toString());
                    modelo.getVista().cmbMarca.setSelectedItem(vista.tblProductos.getValueAt(fila, 6).toString());
                    modelo.getVista().cmbMedida.setSelectedItem(vista.tblProductos.getValueAt(fila, 7).toString());
                    modelo.getVista().txtCantidad1.setText(vista.tblProductos.getValueAt(fila, 8).toString());

                    modelo.getVista().txtCodigo.setEditable(false);
                }
            }
        });
cargarCategorias();
        // ComboBox: ordenar automáticamente
        vista.cmbOrdenarPor.addActionListener(e -> listarProductos());

        // Búsqueda en tiempo real
        vista.txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { listarProductos(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { listarProductos(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { listarProductos(); }
        });
    }

    // ------------------- CRUD -------------------

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnNuevo) {
            nuevoProducto();
        } else if (e.getSource() == btnActualizar) {
            actualizarProducto();
        } else if (e.getSource() == btnEliminar) {
            eliminarProducto();
        } else if (e.getSource() == btnBuscar) {
            buscarProducto();
        } else if (e.getSource() == btnLimpiar) {
            limpiarCampos();
            modelo.getVista().txtCodigo.setEditable(true);
        }
    }

    private void listarProductos() {
        DefaultTableModel tabla = (DefaultTableModel) modelo.getVista().tblProductos.getModel();
        tabla.setRowCount(0);

        List<ModeloProducto> lista;
        String filtro = modelo.getVista().txtBuscar.getText().trim();
        if (!filtro.isEmpty()) {
            lista = implementacion.buscar(filtro);
        } else {
            lista = implementacion.obtenerTodos();
        }

        String orden = (String) modelo.getVista().cmbOrdenarPor.getSelectedItem();
        if ("Código".equals(orden)) {
            lista.sort((a,b) -> Integer.compare(a.getCodigo(), b.getCodigo()));
        } else if ("Nombre".equals(orden)) {
            lista.sort((a,b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        }

        for (ModeloProducto p : lista) {
            tabla.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getStockMinimo(),
                    p.getStockActual(),
                    p.getFkCategoria(),
                    p.getFkMarca(),
                    p.getFkMedida(),
                    p.getCantidadMedida()
            });
        }
    }

    private ModeloProducto obtenerDatosVista() {
        ModeloProducto p = new ModeloProducto(modelo.getVista());
        try { p.setCodigo(Integer.parseInt(modelo.getVista().txtCodigo.getText())); } catch (Exception ex) { p.setCodigo(0); }
        p.setNombre(modelo.getVista().txtNombre.getText());
        p.setDescripcion(modelo.getVista().txtDescripcion.getText());
        try { p.setStockMinimo(Integer.parseInt(modelo.getVista().txtStockMinimo.getText())); } catch (Exception ex) { p.setStockMinimo(0); }
        try { p.setStockActual(Integer.parseInt(modelo.getVista().txtStockActual.getText())); } catch (Exception ex) { p.setStockActual(0); }
        p.setFkCategoria(modelo.getVista().cmbCategoria.getSelectedIndex());
        p.setFkMarca(modelo.getVista().cmbMarca.getSelectedItem().toString());
        p.setFkMedida(modelo.getVista().cmbMedida.getSelectedItem().toString());
        try { p.setCantidadMedida(Double.parseDouble(modelo.getVista().txtCantidad1.getText())); } catch (Exception ex) { p.setCantidadMedida(0); }
        p.setEstado("A"); // por defecto activo
        return p;
    }

    private void nuevoProducto() {
        ModeloProducto p = obtenerDatosVista();
        if (implementacion.insertar(p)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Producto insertado correctamente");
            listarProductos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error al insertar producto");
        }
    }

    private void actualizarProducto() {
        ModeloProducto p = obtenerDatosVista();
        if (implementacion.actualizar(p)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Producto actualizado correctamente");
            listarProductos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error al actualizar producto");
        }
    }

    private void eliminarProducto() {
        try {
            int codigo = Integer.parseInt(modelo.getVista().txtCodigo.getText());
            int opcion = JOptionPane.showConfirmDialog(modelo.getVista(), "¿Desea eliminar este producto?");
            if (opcion == JOptionPane.YES_OPTION) {
                if (implementacion.eliminar(codigo)) {
                    JOptionPane.showMessageDialog(modelo.getVista(), "Producto eliminado correctamente");
                    listarProductos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(modelo.getVista(), "Error al eliminar producto");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error: " + ex.getMessage());
        }
    }
    private void cargarCategorias() {
        try {
            List<ModeloCategoria> listaCategorias = implementacion.obtenerCategorias(); // Método que debes crear en ProductoImp
            JComboBox<String> cmb = modelo.getVista().cmbCategoria;

            cmb.removeAllItems(); // Limpiar ComboBox
            for (ModeloCategoria c : listaCategorias) {
                cmb.addItem(c.getCodigo() + " - " + c.getDescripcion());
            }
        } catch (Exception e) {
            System.out.println("Error cargarCategorias: " + e.getMessage());
        }
    }

    private void buscarProducto() {
        try {
            int codigo = Integer.parseInt(modelo.getVista().txtBuscar.getText());
            ModeloProducto p = implementacion.obtenerPorCodigo(codigo);
            if (p != null) {
                modelo.getVista().txtCodigo.setText(String.valueOf(p.getCodigo()));
                modelo.getVista().txtNombre.setText(p.getNombre());
                modelo.getVista().txtDescripcion.setText(p.getDescripcion());
                modelo.getVista().txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
                modelo.getVista().txtStockActual.setText(String.valueOf(p.getStockActual()));
                modelo.getVista().cmbCategoria.setSelectedItem(p.getFkCategoria());
                modelo.getVista().cmbMarca.setSelectedItem(p.getFkMarca());
                modelo.getVista().cmbMedida.setSelectedItem(p.getFkMedida());
                modelo.getVista().txtCantidad1.setText(String.valueOf(p.getCantidadMedida()));
            } else {
                JOptionPane.showMessageDialog(modelo.getVista(), "Producto no encontrado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        modelo.getVista().txtCodigo.setText("");
        modelo.getVista().txtNombre.setText("");
        modelo.getVista().txtDescripcion.setText("");
        modelo.getVista().txtStockMinimo.setText("");
        modelo.getVista().txtStockActual.setText("");
        modelo.getVista().txtCantidad1.setText("");
        modelo.getVista().txtBuscar.setText("");
        modelo.getVista().cmbCategoria.setSelectedIndex(0);
        modelo.getVista().cmbMarca.setSelectedIndex(0);
        modelo.getVista().cmbMedida.setSelectedIndex(0);
    }

    // ------------------- ICONOS -------------------

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel) e.getSource(), true); }
    @Override
    public void mouseExited(MouseEvent e) { cambiarIconoBoton((JPanel) e.getSource(), false); }

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
}
