package com.umg.controlador;

import com.umg.implementacion.ProductoImp;
import com.umg.modelo.ModeloCategoria;
import com.umg.modelo.ModeloMarcas;
import com.umg.modelo.ModeloMedidas;
import com.umg.modelo.ModeloProducto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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

        // Cargar combos
        cargarCategorias();
        cargarMarcas();
        cargarMedidas();

        // Cargar tabla inicial
        listarProductoss();

        // Eventos de botones
        //btnNuevo.addMouseListener(this);
        //btnActualizar.addMouseListener(this);
        //btnEliminar.addMouseListener(this);
        ///btnBuscar.addMouseListener(this);
        btnLimpiar.addMouseListener(this);

        // Clic en tabla → llenar datos
        vista.tblProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tblProductos.getSelectedRow();
                if (fila >= 0) {
                    // Convertir índice de fila de vista a modelo
                    fila = vista.tblProductos.convertRowIndexToModel(fila);
                    int codigo = Integer.parseInt(vista.tblProductos.getModel().getValueAt(fila, 0).toString());
                    ModeloProducto p = implementacion.obtenerPorCodigo(codigo);

                    if (p != null) {
                        vista.txtCodigo.setText(String.valueOf(p.getCodigo()));
                        vista.txtNombre.setText(p.getNombre());
                        vista.txtDescripcion.setText(p.getDescripcion());
                        vista.txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
                        vista.txtStockActual.setText(String.valueOf(p.getStockActual()));
                        vista.txtCantidad1.setText(String.valueOf(p.getCantidad()));
                        vista.txtCodigo.setEditable(false);

                        // Seleccionar CATEGORÍA
                        for (int i = 0; i < vista.cmbCategoria.getItemCount(); i++) {
                            String item = vista.cmbCategoria.getItemAt(i);
                            String id = item.split(" - ")[0].trim();
                            if (id.equals(String.valueOf(p.getCategoria()))) {
                                vista.cmbCategoria.setSelectedIndex(i);
                                break;
                            }
                        }

                        // Seleccionar MARCA
                        for (int i = 0; i < vista.cmbMarca.getItemCount(); i++) {
                            String item = vista.cmbMarca.getItemAt(i);
                            String id = item.split(" - ")[0].trim();
                            if (id.equalsIgnoreCase(p.getMarca())) {
                                vista.cmbMarca.setSelectedIndex(i);
                                break;
                            }
                        }

                        // Seleccionar MEDIDA
                        for (int i = 0; i < vista.cmbMedida.getItemCount(); i++) {
                            String item = vista.cmbMedida.getItemAt(i);
                            String id = item.split(" - ")[0].trim();
                            if (id.equals(String.valueOf(p.getMedida()))) {
                                vista.cmbMedida.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                }
            }
        });

        // Buscar en tiempo real usando TableRowSorter
        vista.txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarTabla(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarTabla(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarTabla(); }
        });
    }

    // ==================== FILTRADO ====================
    private void filtrarTabla() {
        String texto = modelo.getVista().txtBuscar.getText().trim();
        JTable table = modelo.getVista().tblProductos;
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();

        if (texto.isEmpty()) {
            sorter.setRowFilter(null); // mostrar todo
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // filtra sin duplicar filas
        }
    }

    // ==================== EVENTOS BOTONES ====================
    @Override public void actionPerformed(ActionEvent e) {}

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

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel) e.getSource(), true); }
    @Override public void mouseExited(MouseEvent e) { cambiarIconoBoton((JPanel) e.getSource(), false); }

    // ==================== CARGA TABLA ====================
    private void listarProductoss() {
        DefaultTableModel tabla = (DefaultTableModel) modelo.getVista().tblProductos.getModel();
        tabla.setRowCount(0);
        tabla.setColumnIdentifiers(new Object[]{"Código", "Nombre", "Categoría", "Stock", "Precio"});

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tabla);
        modelo.getVista().tblProductos.setRowSorter(sorter);

        List<ModeloProducto> lista = implementacion.obtenerTodos();

        for (ModeloProducto p : lista) {
            tabla.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getCategoria(),
                    p.getStockActual(),
                    p.getPrecio() // asegúrate de que exista en tu modelo
            });
        }
    }

    // ==================== CRUD ====================
    private ModeloProducto obtenerDatosVista() {
        ModeloProducto p = new ModeloProducto(modelo.getVista());
        try { p.setCodigo(Integer.parseInt(modelo.getVista().txtCodigo.getText())); } catch (Exception ex) { p.setCodigo(0); }
        p.setNombre(modelo.getVista().txtNombre.getText());
        p.setDescripcion(modelo.getVista().txtDescripcion.getText());
        try { p.setStockMinimo(Integer.parseInt(modelo.getVista().txtStockMinimo.getText())); } catch (Exception ex) { p.setStockMinimo(0); }
        try { p.setStockActual(Integer.parseInt(modelo.getVista().txtStockActual.getText())); } catch (Exception ex) { p.setStockActual(0); }
        try { p.setCategoria(Integer.parseInt(modelo.getVista().cmbCategoria.getSelectedItem().toString().split(" - ")[0])); } catch (Exception ex) { p.setCategoria(0); }
        try { p.setMarca(modelo.getVista().cmbMarca.getSelectedItem().toString().split(" - ")[0]); } catch (Exception ex) { p.setMarca(""); }
        try { p.setMedida(modelo.getVista().cmbMedida.getSelectedItem().toString().split(" - ")[0]); } catch (Exception ex) { p.setMedida(""); }
        try { p.setCantidad(Integer.parseInt(modelo.getVista().txtCantidad1.getText())); } catch (Exception ex) { p.setCantidad(0); }
        p.setEstado("A");
        return p;
    }

    private void nuevoProducto() {
        ModeloProducto p = obtenerDatosVista();
        if (implementacion.insertar(p)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "✅ Producto insertado correctamente");
            listarProductoss();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al insertar producto");
        }
    }

    private void actualizarProducto() {
        System.out.println("🔁 Ejecutando actualizarProducto()");

        ModeloProducto p = obtenerDatosVista();
        if (implementacion.actualizar(p)) {
            JOptionPane.showMessageDialog(modelo.getVista(), "✅ Producto actualizado correctamente");
            listarProductoss();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al actualizar producto");
        }
    }

    private void eliminarProducto() {
        try {
            int codigo = Integer.parseInt(modelo.getVista().txtCodigo.getText());
            int opcion = JOptionPane.showConfirmDialog(modelo.getVista(), "¿Desea eliminar este producto?");
            if (opcion == JOptionPane.YES_OPTION) {
                if (implementacion.eliminar(codigo)) {
                    JOptionPane.showMessageDialog(modelo.getVista(), "✅ Producto eliminado correctamente");
                    listarProductoss();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(modelo.getVista(), "❌ Error al eliminar producto");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error: " + ex.getMessage());
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
                modelo.getVista().txtCantidad1.setText(String.valueOf(p.getCantidad()));
            } else {
                JOptionPane.showMessageDialog(modelo.getVista(), "Producto no encontrado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelo.getVista(), "Error: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        var vista = modelo.getVista();
        vista.txtCodigo.setText("");
        vista.txtNombre.setText("");
        vista.txtDescripcion.setText("");
        vista.txtStockMinimo.setText("");
        vista.txtStockActual.setText("");
        vista.txtCantidad1.setText("");
        vista.txtBuscar.setText("");
        if (vista.cmbCategoria.getItemCount() > 0) vista.cmbCategoria.setSelectedIndex(0);
        if (vista.cmbMarca.getItemCount() > 0) vista.cmbMarca.setSelectedIndex(0);
        if (vista.cmbMedida.getItemCount() > 0) vista.cmbMedida.setSelectedIndex(0);
    }

    // ==================== COMBOS ====================
    private void cargarCategorias() {
        try {
            List<ModeloCategoria> lista = implementacion.obtenerCategorias();
            JComboBox<String> cmb = modelo.getVista().cmbCategoria;
            cmb.removeAllItems();
            for (ModeloCategoria c : lista) {
                cmb.addItem(c.getCodigo() + " - " + c.getDescripcion());
            }
        } catch (Exception e) {
            System.out.println("Error cargarCategorias: " + e.getMessage());
        }
    }

    private void cargarMarcas() {
        try {
            List<ModeloMarcas> lista = implementacion.obtenerMarcas();
            JComboBox<String> cmb = modelo.getVista().cmbMarca;
            cmb.removeAllItems();
            for (ModeloMarcas m : lista) {
                cmb.addItem(m.getMarca() + " - " + m.getDescripcion());
            }
        } catch (Exception e) {
            System.out.println("Error cargarMarcas: " + e.getMessage());
        }
    }

    private void cargarMedidas() {
        try {
            List<ModeloMedidas> lista = implementacion.obtenerMedidas();
            JComboBox<String> cmb = modelo.getVista().cmbMedida;
            cmb.removeAllItems();
            for (ModeloMedidas mm : lista) {
                cmb.addItem(mm.getCodigo() + " - " + mm.getDescripcion());
            }
        } catch (Exception e) {
            System.out.println("Error cargarMedidas: " + e.getMessage());
        }
    }

    // ==================== ICONOS ====================
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
