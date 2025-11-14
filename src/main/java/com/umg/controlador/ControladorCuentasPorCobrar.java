package com.umg.controlador;

import com.umg.implementacion.CuentaPorCobrarImp;
import com.umg.interfaces.ICuentaPorCobrar;
import com.umg.modelo.ModeloCuentasPorCobrar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class ControladorCuentasPorCobrar implements ActionListener, MouseListener {

    private final ModeloCuentasPorCobrar modelo;

    // Servicio
    private final ICuentaPorCobrar.Servicio svcCxc = new CuentaPorCobrarImp();

    // Vista
    private final JPanel btnBuscar;
    private final JLabel lblBuscar;
    private final JTable tblCuentasPorCobrar;

    private final Map<JPanel,String> iconosBotones = new HashMap<>();

    public ControladorCuentasPorCobrar(ModeloCuentasPorCobrar modelo) {
        this.modelo = modelo;

        var v = modelo.getVista();
        this.btnBuscar           = v.btnBuscar;
        this.lblBuscar           = v.lblBuscar;
        this.tblCuentasPorCobrar = v.tblCuentasPorCobrar;

        if (lblBuscar != null) lblBuscar.setName("icono");

        inicializarIconos();

        if (btnBuscar != null) {
            btnBuscar.addMouseListener(this);
        }

        // Si quieres que al abrir ya cargue todo:
        // cargarCuentasPorCobrar();
    }

    // =========================================================
    // Acción principal: Buscar -> llenar tabla
    // =========================================================
    private void cargarCuentasPorCobrar() {
        var v = modelo.getVista();

        var lista = svcCxc.listarTodo();

        DefaultTableModel m = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        m.setColumnIdentifiers(new Object[]{
                "Correlativo",
                "Secuencia",
                "Método Pago",   // ahora mostrará la descripción
                "Valor Total",
                "Valor Pagado",
                "Fecha Límite",
                "Número Cuenta",
                "NIT"
        });

        for (ICuentaPorCobrar.RowCxc r : lista) {
            m.addRow(new Object[]{
                    r.correlativo,
                    r.secuencia,
                    r.metodoPagoDesc,  // <- aquí usamos la DESCRIPCIÓN, no el código
                    r.valorTotal,
                    r.valorPagado,
                    r.fechaLimite,
                    r.numeroCuenta,
                    r.clienteNit
            });
        }

        v.tblCuentasPorCobrar.setModel(m);
    }

    // =========================================================
    // ActionListener
    // =========================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        // por si luego usas ActionListener
    }

    // =========================================================
    // MouseListener
    // =========================================================
    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if (src == btnBuscar) {
            cargarCuentasPorCobrar();
        }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        cambiarIconoBoton((JPanel)e.getSource(), true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        cambiarIconoBoton((JPanel)e.getSource(), false);
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
