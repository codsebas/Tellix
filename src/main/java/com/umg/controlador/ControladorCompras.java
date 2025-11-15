package com.umg.controlador;

import com.umg.implementacion.CompraImp;
import com.umg.modelo.*;
import com.umg.implementacion.MetodosDeLiquidacionImp;
import com.umg.interfaces.IMetodosDeLiquidacion;
import com.umg.seguridad.Sesion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorCompras implements ActionListener, MouseListener {

    private ModeloCompras modelo;

    private final JPanel btnAgregar;
    private final JPanel btnRegistrarCompra;
    private final JPanel btnEliminar;
    private final JPanel btnBuscarProducto;

    private final JLabel lblAgregar;
    private final JLabel lblRegistrarCompra;
    private final JLabel lblEliminar;
    private final JLabel lblBuscarProducto;

    private final JComboBox<String> cmbMetodoDePago;

    private  ModeloResumProd resumProd = new ModeloResumProd();
    private  ModeloComprasDB comprasDB = new ModeloComprasDB();
    private ModeloCuentasXPagarDB cuentasXPagarDB = new ModeloCuentasXPagarDB();
    private  List<ModeloDetalleCompraDB> detallleCompraDB = new ArrayList<>();

    private List<ModeloProveedoresDB> listaProveedores = new ArrayList<>();
    private List<ModeloMetodoPagoDB> listaMetodosPago = new ArrayList<>();
    private List<ModeloRepresentanteDB> listaRepresentantes = new ArrayList<>();

    // Arreglos paralelos para mapear índices del combo -> código/NIT
    private String[] arrNitProveedores;        // para cmbProveedor
    private int[]    arrCodMetodosPago;        // para cmbMetodoDePago
    private String[] arrNitRepresentantes;     // para cmRepresentante

    private CompraImp compra = new CompraImp();

    private final Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorCompras(ModeloCompras modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        btnAgregar          = vista.btnAgregar;
        btnRegistrarCompra  = vista.btnRegistrarCompra;
        btnEliminar         = vista.btnEliminar;
        btnBuscarProducto   = vista.btnBuscarProducto;

        lblAgregar          = vista.lblAgregar;
        lblRegistrarCompra  = vista.lblRegistrarCompra;
        lblEliminar         = vista.lblEliminar;
        lblBuscarProducto   = vista.lblBuscarProducto;

        if (lblAgregar         != null) lblAgregar.setName("icono");
        if (lblRegistrarCompra != null) lblRegistrarCompra.setName("icono");
        if (lblEliminar        != null) lblEliminar.setName("icono");
        if (lblBuscarProducto  != null) lblBuscarProducto.setName("icono");

        inicializarIconos();

        if (btnAgregar         != null) btnAgregar.addMouseListener(this);
        if (btnRegistrarCompra != null) btnRegistrarCompra.addMouseListener(this);
        if (btnEliminar        != null) btnEliminar.addMouseListener(this);
        if (btnBuscarProducto  != null) btnBuscarProducto.addMouseListener(this);

        cmbMetodoDePago = vista.getCmbMetodoDePago();

        // ===== INICIALIZACIONES RELACIONADAS A TABLA Y COMBOS =====
        configurarTabla();
        cargarProveedoresEnCombo();
        cargarMetodosPagoEnCombo();
        cargarTiposPlazoEnCombo();

        // Cuando cambie el proveedor, actualizamos NIT/nombre y representantes
        if (vista.cmbProveedor != null) {
            vista.cmbProveedor.addActionListener(e -> onProveedorSeleccionado());
        }

        // Cuando cambie el representante, actualizamos el textbox de info
        if (vista.cmRepresentante != null) {
            vista.cmRepresentante.addActionListener(e -> onRepresentanteSeleccionado());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private void inicializarIconos() {
        iconosBotones.put(btnAgregar,          "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnRegistrarCompra,  "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnEliminar,         "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnBuscarProducto,   "/com/umg/iconos/IconoBoton1.png");
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
            if (comp instanceof JLabel lbl && nombre.equals(lbl.getName())) {
                return lbl;
            }
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getComponent().equals(modelo.getVista().btnBuscarProducto)) {
            traerProducto();
        } else if(e.getComponent().equals(modelo.getVista().btnAgregar)) {
            agregarProducto();
        } else if(e.getComponent().equals(modelo.getVista().btnRegistrarCompra)) {
            agregarCompra();
        } else if(e.getComponent().equals(modelo.getVista().btnEliminar)) {
            // aquí luego puedes implementar eliminar fila de la tabla, si quieres
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
        if (e.getSource() instanceof JPanel panel) {
            cambiarIconoBoton(panel, true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JPanel panel) {
            cambiarIconoBoton(panel, false);
        }
    }

    public void traerProducto() {
        if (!modelo.getVista().txtBuscarProducto.getText().equals("")) {
            int valor = Integer.parseInt(modelo.getVista().txtBuscarProducto.getText());
            resumProd = compra.seleccionarProducto(valor);
            modelo.getVista().txtPrecioProducto.setText(String.valueOf(resumProd.getPrecioFinal()));
            modelo.getVista().txtNombreProducto.setText(resumProd.getNombre());
            modelo.getVista().txtStockDisponible.setText(String.valueOf(resumProd.getStockDisponible()));
        }
    }

    public void configurarTabla() {
        DefaultTableModel modeloTabla = new DefaultTableModel(
                new Object[]{
                        "Producto",
                        "Precio base",
                        "Impuestos",
                        "Descuentos",
                        "Precio final",
                        "Cantidad",
                        "Subtotal"
                }, 0
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

        ModeloDetalleCompraDB detalle = new ModeloDetalleCompraDB();
        detalle.setCod_producto(resumProd.getCodigo());
        detalle.setDescuentos(resumProd.getTotalDescuentos());
        detalle.setImpuestos(resumProd.getTotalImpuestos());
        detalle.setPrecio_bruto(resumProd.getPrecioBase());
        detalle.setCantidad(cantidad);
        agregarDetalle(detalle);

        modelo.getVista().txtTotalCompra.setText(String.valueOf(totalVenta));

        modelo.getVista().txtCantidadProducto.setText("");
        modelo.getVista().txtBuscarProducto.setText("");
        modelo.getVista().txtNombreProducto.setText("");
        modelo.getVista().txtStockDisponible.setText("");
        modelo.getVista().txtPrecioProducto.setText("");
        resumProd = null;
    }

    public void agregarDetalle(ModeloDetalleCompraDB modeloDetalle){
        detallleCompraDB.add(modeloDetalle);
    }

    public void agregarCompra(){
        int idxProv = modelo.getVista().cmbProveedor.getSelectedIndex();
        String nitProveedor = (idxProv > 0) ? arrNitProveedores[idxProv - 1] : null;

        int idxRep = modelo.getVista().cmRepresentante.getSelectedIndex();
        String nitRepresentante = (idxRep > 0) ? arrNitRepresentantes[idxRep - 1] : null;

        int idxMet = modelo.getVista().cmbMetodoDePago.getSelectedIndex();
        Integer codMetodo = (idxMet > 0) ? arrCodMetodosPago[idxMet - 1] : null;

        comprasDB.setProveedor(nitProveedor);
        comprasDB.setRepresentante(nitRepresentante);
        comprasDB.setFecha_operacion(Date.valueOf(LocalDate.now()));
        comprasDB.setHora_operacion(Timestamp.valueOf(LocalDateTime.now()));
        comprasDB.setUsuario_sistema(Sesion.getUsuario());
        comprasDB.setMetodo_pago(codMetodo);
        comprasDB.setPlazo_credito(Integer.parseInt(modelo.getVista().txtPlazoCredito.getText()));
        comprasDB.setTipo_plazo(obtenerCodigoTipoPlazoSeleccionado());
        comprasDB.setEstado("E");

        cuentasXPagarDB.setEstado("E");
        cuentasXPagarDB.setMetodo_pago(codMetodo);
        cuentasXPagarDB.setValor_total(Float.parseFloat(modelo.getVista().txtTotalCompra.getText()));

        Date fechaActual = comprasDB.getFecha_operacion();
        LocalDate fechaLimite = fechaActual.toLocalDate().plusDays(Integer.parseInt(modelo.getVista().txtPlazoCredito.getText()));
        cuentasXPagarDB.setFecha_limite(Date.valueOf(fechaLimite));
        cuentasXPagarDB.setValor_pagado(0);
        cuentasXPagarDB.setBanco(Integer.parseInt(modelo.getVista().txtBanco.getText()));
        cuentasXPagarDB.setNumero_cuenta(modelo.getVista().txtNumeroCuenta.getText());

        boolean resultado = compra.insertarCompra(comprasDB, detallleCompraDB, cuentasXPagarDB);
        if(resultado){
            limpiarTodo();
        } else {
            System.out.println("Errores");
        }
    }

    public void limpiarTodo(){
        modelo.getVista().txtCantidadProducto.setText("");
        modelo.getVista().txtBuscarProducto.setText("");
        modelo.getVista().txtNombreProducto.setText("");
        modelo.getVista().txtStockDisponible.setText("");
        modelo.getVista().txtPrecioProducto.setText("");
        modelo.getVista().tblProductos.setModel(new DefaultTableModel());
        configurarTabla();
        modelo.getVista().txtNITProveedor.setText("");
        modelo.getVista().txtNombreProveedor.setText("");
        modelo.getVista().txtInfoRepresentante.setText("");
        modelo.getVista().txtPlazoCredito.setText("");
        modelo.getVista().txtTotalCompra.setText("");
        modelo.getVista().txtNumeroCuenta.setText("");
        modelo.getVista().txtBanco.setText("");

        if (modelo.getVista().cmbMetodoDePago.getItemCount() > 0) {
            modelo.getVista().cmbMetodoDePago.setSelectedIndex(0);
        }
        if (modelo.getVista().cmbTipoPlazo.getItemCount() > 0) {
            modelo.getVista().cmbTipoPlazo.setSelectedIndex(0);
        }
        if (modelo.getVista().cmbProveedor.getItemCount() > 0) {
            modelo.getVista().cmbProveedor.setSelectedIndex(0);
        }
        if (modelo.getVista().cmRepresentante.getItemCount() > 0) {
            modelo.getVista().cmRepresentante.setSelectedIndex(0);
        }

        resumProd = null;
        comprasDB = null;
        detallleCompraDB = null;
    }

    private void cargarTiposPlazoEnCombo() {
        if (modelo.getVista().cmbTipoPlazo == null) return;

        modelo.getVista().cmbTipoPlazo.removeAllItems();
        modelo.getVista().cmbTipoPlazo.addItem("-- Seleccione --");
        modelo.getVista().cmbTipoPlazo.addItem("Día");
        modelo.getVista().cmbTipoPlazo.addItem("Mes");
        modelo.getVista().cmbTipoPlazo.addItem("Año");
    }

    private void cargarProveedoresEnCombo() {
        var vista = modelo.getVista();
        JComboBox<String> cmbProveedor = vista.cmbProveedor;

        cmbProveedor.removeAllItems();
        cmbProveedor.addItem("-- Seleccione --");

        listaProveedores = compra.seleccionarProveedores();
        arrNitProveedores = new String[listaProveedores.size()];

        int i = 0;
        for (ModeloProveedoresDB prov : listaProveedores) {
            cmbProveedor.addItem(prov.getNombre());   // solo descripción
            arrNitProveedores[i] = prov.getNit();     // para uso interno
            i++;
        }
    }

    private void cargarMetodosPagoEnCombo() {
        var vista = modelo.getVista();
        JComboBox<String> cmbMetodo = vista.cmbMetodoDePago;

        cmbMetodo.removeAllItems();
        cmbMetodo.addItem("-- Seleccione --");

        listaMetodosPago = compra.seleccionarMetodosPago();
        arrCodMetodosPago = new int[listaMetodosPago.size()];

        int i = 0;
        for (ModeloMetodoPagoDB mp : listaMetodosPago) {
            cmbMetodo.addItem(mp.getMetodo_pago());   // descripción
            arrCodMetodosPago[i] = mp.getCodigo();    // código real
            i++;
        }
    }

    private void cargarRepresentantesPorProveedor(String nitProveedor) {
        var vista = modelo.getVista();
        JComboBox<String> cmbRep = vista.cmRepresentante;

        cmbRep.removeAllItems();
        cmbRep.addItem("-- Seleccione --");

        // Pedimos al DAO solo los representantes de ese proveedor
        listaRepresentantes = compra.seleccionarRepresentantes(nitProveedor);
        arrNitRepresentantes = new String[listaRepresentantes.size()];

        int i = 0;
        for (ModeloRepresentanteDB rep : listaRepresentantes) {
            cmbRep.addItem(rep.getNombre());            // nombre_completo de la vista
            arrNitRepresentantes[i] = rep.getNit();     // NIT del representante
            i++;
        }
    }

    private void onProveedorSeleccionado() {
        var vista = modelo.getVista();
        int idx = vista.cmbProveedor.getSelectedIndex();

        // idx 0 = "-- Seleccione --"
        if (idx <= 0) {
            vista.txtNITProveedor.setText("");
            vista.txtNombreProveedor.setText("");
            // Limpio representantes también
            vista.cmRepresentante.removeAllItems();
            vista.cmRepresentante.addItem("-- Seleccione --");
            return;
        }

        // Ajustamos porque arrNitProveedores empieza en 0 y el combo en 1
        String nitProveedor = arrNitProveedores[idx - 1];
        ModeloProveedoresDB prov = listaProveedores.get(idx - 1);

        vista.txtNITProveedor.setText(prov.getNit());
        vista.txtNombreProveedor.setText(prov.getNombre());

        // Ahora cargamos representantes para este proveedor
        cargarRepresentantesPorProveedor(nitProveedor);
    }

    private void onRepresentanteSeleccionado() {
        var vista = modelo.getVista();
        int idx = vista.cmRepresentante.getSelectedIndex();

        if (idx <= 0 || listaRepresentantes == null || listaRepresentantes.isEmpty()) {
            vista.txtInfoRepresentante.setText("");
            return;
        }

        ModeloRepresentanteDB rep = listaRepresentantes.get(idx - 1);
        vista.txtInfoRepresentante.setText(rep.getNombre());
    }

    private String obtenerCodigoTipoPlazoSeleccionado() {
        if (modelo.getVista().cmbTipoPlazo == null) return null;

        Object sel = modelo.getVista().cmbTipoPlazo.getSelectedItem();
        if (sel == null) return null;

        String texto = sel.toString().trim();

        if (texto.equalsIgnoreCase("Día") || texto.equalsIgnoreCase("Dia")) {
            return "D";
        } else if (texto.equalsIgnoreCase("Mes")) {
            return "M";
        } else if (texto.equalsIgnoreCase("Año") || texto.equalsIgnoreCase("Ano")) {
            return "A";
        }

        // "-- Seleccione --" u otra cosa
        return null;
    }
}
