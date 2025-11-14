package com.umg.controlador;

import com.umg.implementacion.CuentaPorPagarImp;
import com.umg.interfaces.ICuentaPorPagar;
import com.umg.modelo.ModeloCuentasPorPagar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ControladorCuentasPorPagar implements ActionListener, MouseListener {

    private final ModeloCuentasPorPagar modelo;

    // Servicio
    private final ICuentaPorPagar.Servicio svcCxp = new CuentaPorPagarImp();

    // Vista
    private final JPanel btnBuscar;
    private final JLabel lblBuscar;
    private final JTable tblCuentasPorPagar;

    private final Map<JPanel, String> iconosBotones = new HashMap<>();

    public ControladorCuentasPorPagar(ModeloCuentasPorPagar modelo) {
        this.modelo = modelo;

        var v = modelo.getVista();
        this.btnBuscar         = v.btnBuscar1;          // <-- este es el botón azul "Buscar"
        this.lblBuscar         = v.lblBuscar;
        this.tblCuentasPorPagar = v.tblCuentasPorPagar;

        if (lblBuscar != null) lblBuscar.setName("icono");

        inicializarIconos();

        if (btnBuscar != null) {
            btnBuscar.addMouseListener(this);
        }
    }

    // =========================================================
    // Llenar tabla al presionar Buscar
    // =========================================================
    private void cargarCuentasPorPagar() {
        var lista = svcCxp.listarTodas();

        DefaultTableModel m = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        m.setColumnIdentifiers(new Object[]{
                "Correlativo",
                "No. Documento",
                "Método Pago",
                "Valor Total",
                "Valor Pagado",
                "Fecha Límite",
                "No. Cuenta",
                "Banco"
        });

        for (ICuentaPorPagar.RowCxp r : lista) {
            String metodo = (r.metodoPagoDesc != null && !r.metodoPagoDesc.isBlank())
                    ? r.metodoPagoDesc
                    : String.valueOf(r.metodoPago);

            m.addRow(new Object[]{
                    r.correlativo,
                    r.noDocumento,
                    metodo,
                    r.valorTotal,
                    r.valorPagado,
                    r.fechaLimite,
                    r.numeroCuenta,
                    r.banco
            });
        }

        tblCuentasPorPagar.setModel(m);
    }

    // =========================================================
    // ActionListener (por si luego usas botones con actionPerformed)
    // =========================================================
    @Override
    public void actionPerformed(ActionEvent e) { }

    // =========================================================
    // MouseListener
    // =========================================================
    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if (src == btnBuscar) {
            cargarCuentasPorPagar();
        }
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
        iconosBotones.put(btnBuscar, "/com/umg/iconos/IconoBoton1.png");
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
            if (comp instanceof JLabel lbl && nombre.equals(lbl.getName())) {
                return lbl;
            }
        }
        return null;
    }
}
