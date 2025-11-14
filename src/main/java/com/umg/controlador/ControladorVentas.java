package com.umg.controlador;

import com.umg.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.umg.implementacion.VentaImp;
import com.umg.seguridad.Sesion;

public class ControladorVentas implements ActionListener, MouseListener {
    ModeloVentas modelo;

    // Componentes de la vista
    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;
    private VentaImp venta = new VentaImp();

    private ModeloResumProd resumProd = new ModeloResumProd();
    private ModeloVentaDB ventaDB = new ModeloVentaDB();
    private List<ModeloDetalleVentaDB> detalleVentaDB = new ArrayList();
    private ModeloClienteVistaRes clienteRes = new ModeloClienteVistaRes();

    private Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorVentas(ModeloVentas modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();
        // Inicializar botones y labels
        btnNuevo = vista.btnNuevo;
        btnActualizar = vista.btnInsertar;
        btnEliminar = vista.btnEliminar;
        btnBuscar = vista.btnBuscarProducto;
        btnLimpiar = vista.btnBuscarCliente;

        lblNuevo = vista.lblNuevo;
        lblActualizar = vista.lblActualizar;
        lblEliminar = vista.lblEliminar;
        lblBuscar = vista.lblBuscarProducto;
        lblLimpiar = vista.lblBuscarCliente;

        // Dar nombre a los labels para manejar iconos
        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        inicializarIconos();
        configurarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getComponent().equals(modelo.getVista().btnBuscarCliente)){
            traerCliente();
        } else if (e.getComponent().equals(modelo.getVista().btnNuevo)){
            agregarProducto();
        } else if (e.getComponent().equals(modelo.getVista().btnBuscarProducto)){
            traerProducto();
        } else if (e.getComponent().equals(modelo.getVista().btnEliminar)){
        } else if(e.getComponent().equals(modelo.getVista().btnInsertar)){
            agregarVenta();
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

    public void traerProducto(){
        if(!modelo.getVista().txtBuscarProducto.getText().equals("")){
            int valor = Integer.parseInt(modelo.getVista().txtBuscarProducto.getText());
            resumProd = venta.seleccionarProducto(valor);
            modelo.getVista().txtPrecioProducto.setText(String.valueOf(resumProd.getPrecioFinal()));
            modelo.getVista().txtNombreProducto.setText(resumProd.getNombre());
            modelo.getVista().txtStockDisponible.setText(String.valueOf(resumProd.getStockDisponible()));
        }
    }

    public void configurarTabla(){
        DefaultTableModel modeloTabla = new DefaultTableModel(
                new Object[]{
                        "Producto",
                        "Precio base",
                        "Impuestos",
                        "Descuentos",
                        "Precio final",
                        "Cantidad",
                        "Subtotal"
                }, 0 // 0 filas iniciales
        );
        modelo.getVista().tblProductos.setModel(modeloTabla);
    }

    public void agregarProducto() {
        String txtCant = modelo.getVista().txtCantidadProducto.getText().trim();

        if (txtCant.isEmpty()) {
            JOptionPane.showMessageDialog(
                    modelo.getVista(),
                    "Debe ingresar una cantidad.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCant);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    modelo.getVista(),
                    "La cantidad debe ser numérica.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (resumProd == null) {
            JOptionPane.showMessageDialog(
                    modelo.getVista(),
                    "No hay producto seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        float precioFinal = resumProd.getPrecioFinal();
        float subtotal = precioFinal * cantidad;

        DefaultTableModel dtm = (DefaultTableModel) modelo.getVista().tblProductos.getModel();

        Object[] fila = new Object[]{
                resumProd.getNombre(),          // Producto
                resumProd.getPrecioBase(),      // Precio base
                resumProd.getTotalImpuestos(),  // Impuestos
                resumProd.getTotalDescuentos(), // Descuentos
                resumProd.getPrecioFinal(),     // Precio final
                cantidad,                       // Cantidad
                subtotal                        // Subtotal
        };

        dtm.addRow(fila);

        float totalVenta = 0f;
        for (int i = 0; i < dtm.getRowCount(); i++) {
            Object valor = dtm.getValueAt(i, 6); // columna "Subtotal"
            if (valor instanceof Number) {
                totalVenta += ((Number) valor).floatValue();
            } else if (valor != null) {
                try {
                    totalVenta += Float.parseFloat(valor.toString());
                } catch (NumberFormatException ex) {
                }
            }
        }

        ModeloDetalleVentaDB detalle = new ModeloDetalleVentaDB();
        detalle.setCodigo_producto(resumProd.getCodigo());
        detalle.setDescuentos(resumProd.getTotalDescuentos());
        detalle.setImpuestos(resumProd.getTotalImpuestos());
        detalle.setPrecio_bruto(resumProd.getPrecioBase());
        detalle.setCantidad(cantidad);
        agregarDetalle(detalle);

        modelo.getVista().txtTotalVenta.setText(String.valueOf(totalVenta));

        modelo.getVista().txtCantidadProducto.setText("");
        modelo.getVista().txtBuscarProducto.setText("");
        modelo.getVista().txtNombreProducto.setText("");
        modelo.getVista().txtStockDisponible.setText("");
        modelo.getVista().txtPrecioProducto.setText("");
        resumProd = null;
    }

    public void traerCliente() {
        if (!modelo.getVista().txtBuscarCliente.getText().equals("")) {
            String nit = modelo.getVista().txtBuscarCliente.getText().trim();
            clienteRes = venta.seleccionarCliente(nit);

            if (clienteRes != null) {
                modelo.getVista().txtNITCliente.setText(clienteRes.getNit());
                modelo.getVista().txtNombreCliente.setText(clienteRes.getNombre());
            } else {
                JOptionPane.showMessageDialog(
                        modelo.getVista(),
                        "No se encontró ningún cliente con el NIT: " + nit,
                        "Cliente no encontrado",
                        JOptionPane.WARNING_MESSAGE
                );

                modelo.getVista().txtNITCliente.setText("");
                modelo.getVista().txtNombreCliente.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(
                    modelo.getVista(),
                    "Debe ingresar un NIT para buscar.",
                    "Validación",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void agregarDetalle(ModeloDetalleVentaDB modeloDetalle){
        detalleVentaDB.add(modeloDetalle);
    }

    public void agregarVenta(){
        ventaDB.setNit(modelo.getVista().txtNITCliente.getText());
        ventaDB.setFechaOperacion(Date.valueOf(LocalDate.now()));
        ventaDB.setHoraOperacion(Timestamp.valueOf(LocalDateTime.now()));
        ventaDB.setUsuarioSistema(Sesion.getUsuario());
        ventaDB.setMetodoPago(3);
        ventaDB.setPlazoCredito(Integer.parseInt(modelo.getVista().txtPlazoCredito.getText()));
        ventaDB.setTipoPlazo("");
        ventaDB.setEstado("E");

        boolean resultado = venta.insertarVenta(ventaDB, detalleVentaDB);
        if(resultado){
            limpiarTodo();
        } else {
            System.out.println("Errores");
        }
    }

    public boolean validarTodo(){
        return false;
    }

    public void limpiarTodo(){
        modelo.getVista().txtCantidadProducto.setText("");
        modelo.getVista().txtBuscarProducto.setText("");
        modelo.getVista().txtNombreProducto.setText("");
        modelo.getVista().txtStockDisponible.setText("");
        modelo.getVista().txtPrecioProducto.setText("");
        modelo.getVista().tblProductos.setModel(new DefaultTableModel());
        configurarTabla();
        modelo.getVista().txtBuscarCliente.setText("");
        modelo.getVista().txtNITCliente.setText("");
        modelo.getVista().txtNombreCliente.setText("");
        modelo.getVista().txtPlazoCredito.setText("");
        modelo.getVista().cmbMetodoDePago.setSelectedItem(0);
        modelo.getVista().cmbTipoPlazo.setSelectedItem(0);
        resumProd = null;
        ventaDB = null;
        detalleVentaDB = null;
        clienteRes = null;
    }
}