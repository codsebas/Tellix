package com.umg.controlador;

import com.umg.implementacion.ProductoImp;
import com.umg.modelo.ModeloProducto;
import com.umg.vistas.VistaProductos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ControladorProductos implements MouseListener {

    private ModeloProducto modelo;
    private VistaProductos vista;
    private ProductoImp productoDAO;

    // Componentes de la vista
    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorProductos(ModeloProducto modelo) {
        this.modelo = modelo;
        this.vista = modelo.getVista();
        this.productoDAO = new ProductoImp();

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

        // Dar nombre a los labels para manejar iconos
        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        inicializarIconos();
        agregarEventos();

        cargarComboBox(); // Cargar categorías, marcas y medidas
        listarProductos(); // Cargar tabla al inicio
    }

    private void inicializarIconos() {
        iconosBotones.put(btnNuevo, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnActualizar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnEliminar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnBuscar, "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnLimpiar, "/com/umg/iconos/IconoBoton1.png");
    }

    private void agregarEventos() {
        btnNuevo.addMouseListener(this);
        btnActualizar.addMouseListener(this);
        btnEliminar.addMouseListener(this);
        btnBuscar.addMouseListener(this);
        btnLimpiar.addMouseListener(this);
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

    // ------------------- FUNCIONALIDAD CRUD -------------------

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
        }
    }

    private void listarProductos() {
        DefaultTableModel tabla = (DefaultTableModel) vista.tblProductos.getModel();
        tabla.setRowCount(0); // Limpiar tabla

        for (ModeloProducto p : productoDAO.obtenerTodos()) {
            tabla.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getStockMinimo(),
                    p.getStockActual(),
                    p.getCantidad(),
                    p.getCategoria(),
                    p.getMarca(),
                    p.getMedida()
            });
        }
    }

    private void cargarComboBox() {
        // Aquí debes llenar tus combobox desde la base de datos
        // Ejemplo rápido para que no falle:
        vista.cmbCategoria.removeAllItems();
        vista.cmbMarca.removeAllItems();
        vista.cmbMedida.removeAllItems();

        vista.cmbCategoria.addItem("1");
        vista.cmbCategoria.addItem("2");

        vista.cmbMarca.addItem("MARCA1");
        vista.cmbMarca.addItem("MARCA2");

        vista.cmbMedida.addItem("KG");
        vista.cmbMedida.addItem("LT");
    }

    private ModeloProducto obtenerDatosVista() {
        ModeloProducto p = new ModeloProducto();
        try {
            p.setCodigo(Integer.parseInt(vista.txtCodigo.getText()));
        } catch (Exception ex) { p.setCodigo(0); }
        p.setNombre(vista.txtNombre.getText());
        p.setDescripcion(vista.txtDescripcion.getText());
        try { p.setStockMinimo(Integer.parseInt(vista.txtStockMinimo.getText())); } catch (Exception ex) { p.setStockMinimo(0); }
        try { p.setStockActual(Integer.parseInt(vista.txtStockActual.getText())); } catch (Exception ex) { p.setStockActual(0); }
        try { p.setCantidad(Double.parseDouble(vista.txtCantidad.getText())); } catch (Exception ex) { p.setCantidad(0.0); }

        p.setCategoria(vista.cmbCategoria.getSelectedItem() != null ? vista.cmbCategoria.getSelectedItem().toString() : "");
        p.setMarca(vista.cmbMarca.getSelectedItem() != null ? vista.cmbMarca.getSelectedItem().toString() : "");
        p.setMedida(vista.cmbMedida.getSelectedItem() != null ? vista.cmbMedida.getSelectedItem().toString() : "");

        return p;
    }

    private void nuevoProducto() {
        ModeloProducto p = obtenerDatosVista();
        if (productoDAO.insertar(p)) {
            JOptionPane.showMessageDialog(vista, "Producto insertado correctamente");
            listarProductos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al insertar producto");
        }
    }

    private void actualizarProducto() {
        ModeloProducto p = obtenerDatosVista();
        if (productoDAO.actualizar(p)) {
            JOptionPane.showMessageDialog(vista, "Producto actualizado correctamente");
            listarProductos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar producto");
        }
    }

    private void eliminarProducto() {
        try {
            int codigo = Integer.parseInt(vista.txtCodigo.getText());
            int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea eliminar este producto?");
            if (opcion == JOptionPane.YES_OPTION) {
                if (productoDAO.eliminar(codigo)) {
                    JOptionPane.showMessageDialog(vista, "Producto eliminado correctamente");
                    listarProductos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al eliminar producto");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }

    private void buscarProducto() {
        try {
            int codigo = Integer.parseInt(vista.txtBuscar.getText());
            ModeloProducto p = productoDAO.obtenerPorCodigo(codigo);
            if (p != null) {
                vista.txtCodigo.setText(String.valueOf(p.getCodigo()));
                vista.txtNombre.setText(p.getNombre());
                vista.txtDescripcion.setText(p.getDescripcion());
                vista.txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
                vista.txtStockActual.setText(String.valueOf(p.getStockActual()));
                vista.txtCantidad.setText(String.valueOf(p.getCantidad()));

                vista.cmbCategoria.setSelectedItem(p.getCategoria());
                vista.cmbMarca.setSelectedItem(p.getMarca());
                vista.cmbMedida.setSelectedItem(p.getMedida());
            } else {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        vista.txtCodigo.setText("");
        vista.txtNombre.setText("");
        vista.txtDescripcion.setText("");
        vista.txtStockMinimo.setText("");
        vista.txtStockActual.setText("");
        vista.txtCantidad.setText("");
        vista.txtBuscar.setText("");

        if (vista.cmbCategoria.getItemCount() > 0) vista.cmbCategoria.setSelectedIndex(0);
        if (vista.cmbMarca.getItemCount() > 0) vista.cmbMarca.setSelectedIndex(0);
        if (vista.cmbMedida.getItemCount() > 0) vista.cmbMedida.setSelectedIndex(0);
    }

    // ------------------- EVENTOS ICONOS -------------------

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), true);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), false);
    }
}
