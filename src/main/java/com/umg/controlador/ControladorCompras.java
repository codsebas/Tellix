package com.umg.controlador;

import com.umg.modelo.ModeloCompras;
import com.umg.implementacion.MetodosDeLiquidacionImp;
import com.umg.interfaces.IMetodosDeLiquidacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ControladorCompras implements ActionListener, MouseListener {

    private ModeloCompras modelo;

    // ===== Servicio para métodos de liquidación =====
// ASÍ (correcto)
    private final IMetodosDeLiquidacion svcMetodos = new MetodosDeLiquidacionImp();


    // ===== Componentes de la vista =====
    private final JPanel btnAgregar;
    private final JPanel btnRegistrarCompra;
    private final JPanel btnEliminar;
    private final JPanel btnBuscarProducto;
    private final JPanel btnBuscarProveedor;

    private final JLabel lblAgregar;
    private final JLabel lblRegistrarCompra;
    private final JLabel lblEliminar;
    private final JLabel lblBuscarProducto;
    private final JLabel lblBuscarCliente;

    // Combo de método de pago
    private final JComboBox<String> cmbMetodoDePago;

    private final Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorCompras(ModeloCompras modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        // Inicializar botones y labels
        btnAgregar          = vista.btnAgregar;
        btnRegistrarCompra  = vista.btnRegistrarCompra;
        btnEliminar         = vista.btnEliminar;
        btnBuscarProducto   = vista.btnBuscarProducto;
        btnBuscarProveedor  = vista.btnBuscarProveedor;

        lblAgregar          = vista.lblAgregar;
        lblRegistrarCompra  = vista.lblRegistrarCompra;
        lblEliminar         = vista.lblEliminar;
        lblBuscarProducto   = vista.lblBuscarProducto;
        lblBuscarCliente    = vista.lblBuscarCliente;

        // Dar nombre a los labels para manejar iconos
        if (lblAgregar         != null) lblAgregar.setName("icono");
        if (lblRegistrarCompra != null) lblRegistrarCompra.setName("icono");
        if (lblEliminar        != null) lblEliminar.setName("icono");
        if (lblBuscarProducto  != null) lblBuscarProducto.setName("icono");
        if (lblBuscarCliente   != null) lblBuscarCliente.setName("icono");

        inicializarIconos();

        // Listeners de mouse (igual que en otros controladores)
        if (btnAgregar         != null) btnAgregar.addMouseListener(this);
        if (btnRegistrarCompra != null) btnRegistrarCompra.addMouseListener(this);
        if (btnEliminar        != null) btnEliminar.addMouseListener(this);
        if (btnBuscarProducto  != null) btnBuscarProducto.addMouseListener(this);
        if (btnBuscarProveedor != null) btnBuscarProveedor.addMouseListener(this);

        // ===== Combo de Métodos de Pago =====
        cmbMetodoDePago = vista.getCmbMetodoDePago();
        cargarMetodosDePagoEnCombo();
        cargarTiposPlazoEnCombo();
    }

    // =========================================================
    // Cargar Métodos de Pago en el combo
    // =========================================================
    private void cargarMetodosDePagoEnCombo() {
        if (cmbMetodoDePago == null) return;

        cmbMetodoDePago.removeAllItems();
        cmbMetodoDePago.addItem("-- Seleccione --");

        try {
            var lista = svcMetodos.listarOrdenadoPor("DESCRIPCION");
            for (IMetodosDeLiquidacion.RowMetodo r : lista) {
                // Solo mostramos el nombre del método como pediste
                cmbMetodoDePago.addItem(r.descripcion);
            }
        } catch (Exception e) {
            System.out.println("cargarMetodosDePagoEnCombo: " + e.getMessage());
        }
    }

    // Si más adelante querés el código del método seleccionado, aquí puedes
    // hacer un mapa desc→código igual que en Representantes, pero por ahora
    // solo mostramos los nombres en el combo.

    // =========================================================
    // ActionListener
    // =========================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        // Por ahora vacío; puedes enrutar acciones aquí si luego usas ActionListener
    }

    // =========================================================
    // MouseListener
    // =========================================================
    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();

        if (src == btnRegistrarCompra) {
            onRegistrarCompra();
        }

        // Aquí luego puedes poner la lógica:
        // if (src == btnAgregar) { ... }
        // if (src == btnRegistrarCompra) { ... }
        // etc.
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        cambiarIconoBoton((JPanel) e.getSource(), false);
    }

    // =========================================================
    // Iconos
    // =========================================================
    private void inicializarIconos() {
        iconosBotones.put(btnAgregar,          "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnRegistrarCompra,  "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnEliminar,         "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnBuscarProducto,   "/com/umg/iconos/IconoBoton1.png");
        iconosBotones.put(btnBuscarProveedor,  "/com/umg/iconos/IconoBoton1.png");
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
    // =========================================================
// Cargar Tipo de Plazo en el combo
// =========================================================
    private void cargarTiposPlazoEnCombo() {
        var vista = modelo.getVista();
        JComboBox<String> cb = vista.cmbTipoPlazo;   // es public, lo usas directo

        if (cb == null) return;

        cb.removeAllItems();
        cb.addItem("-- Seleccione --");
        cb.addItem("Día");
        cb.addItem("Mes");
        cb.addItem("Año");
    }
    // Devuelve D / M / A según lo que se haya elegido en el combo
    private String obtenerTipoPlazoSeleccionado() {
        var vista = modelo.getVista();
        JComboBox<String> cb = vista.cmbTipoPlazo;
        if (cb == null) return null;

        Object sel = cb.getSelectedItem();
        if (sel == null) return null;

        String txt = sel.toString().trim();

        if (txt.equalsIgnoreCase("Día") || txt.equalsIgnoreCase("Dia")) {
            return "D";
        } else if (txt.equalsIgnoreCase("Mes")) {
            return "M";
        } else if (txt.equalsIgnoreCase("Año") || txt.equalsIgnoreCase("Ano")) {
            return "A";
        } else {
            return null;  // "-- Seleccione --" u otra cosa
        }
    }
    private void onRegistrarCompra() {
        var vista = modelo.getVista();

        // 1) Obtener tipo de plazo en formato D / M / A
        String tipoPlazo = obtenerCodigoTipoPlazoSeleccionado();
        if (tipoPlazo == null) {
            JOptionPane.showMessageDialog(vista,
                    "Seleccione un Tipo de Plazo (Día, Mes o Año).",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2) Obtener el plazo numérico
        String plazoTxt = vista.txtPlazoCredito.getText().trim();
        Integer plazoCredito = null;
        if (!plazoTxt.isEmpty()) {
            try {
                plazoCredito = Integer.parseInt(plazoTxt);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista,
                        "El Plazo de Crédito debe ser numérico.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 3) Aquí armas tu ModeloCompras y lo mandas al servicio/DAO
        ModeloCompras compra = new ModeloCompras(vista);
        compra.setTipoPlazo(tipoPlazo);      // <-- AQUI SE GUARDA D / M / A
        compra.setPlazoCredito(plazoCredito);
        // ... setear demás campos: proveedor, método de pago, total, etc.

        // 4) Llamar a tu implementación para insertar en BD
        // if (svcCompras.insertarCompra(compra)) { ... }
    }

    // Devuelve "D", "M" o "A" según lo seleccionado en cmbTipoPlazo
    private String obtenerCodigoTipoPlazoSeleccionado() {
        var vista = modelo.getVista();

        // Usa el combo directo, porque en tu vista es public
        JComboBox<String> cb = vista.cmbTipoPlazo;
        // Si tuvieras un getter sería:
        // JComboBox<String> cb = vista.getCmbTipoPlazo();

        if (cb == null) return null;

        Object sel = cb.getSelectedItem();
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
