package com.umg.controlador;

import com.umg.implementacion.ClienteImp;
import com.umg.interfaces.ICliente;
import com.umg.modelo.ModeloCliente;

/* ===== NUEVO: para cargar Tipos de Cliente en el combo ===== */
import com.umg.interfaces.ITipoCliente;
import com.umg.implementacion.TipoClienteImp;
import javax.swing.DefaultComboBoxModel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorClientes implements ActionListener, MouseListener {

    // ====== Modelo y servicio ======
    ModeloCliente modelo;
    private final ICliente servicio = new ClienteImp();

    /* ===== NUEVO: DAO para tipos de cliente (para el combo) ===== */
    private final ITipoCliente.DAO tiposDao = new TipoClienteImp();

    // ====== Botones (bloque superior) ======
    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    // ====== Botones (bloque Contacto) ======
    private JPanel btnNuevoC, btnActualizarC, btnEliminarC;
    private JLabel lblNuevoC, lblActualizarC, lblEliminarC;

    private final Map<JPanel, String> iconosBotones = new HashMap<>();

    // ====== Buffer de contactos (solo tabla) ======
    private static class ContactoTmp {
        Integer identificacion;     // null o negativo si aún no existe en BD
        Integer correlativo;        // 1..n para la tabla
        Integer tipoContacto;       // código de tipo_contacto
        String info;
        String telefono;
        String nit;                 // NIT dueño
    }
    private final List<ContactoTmp> bufferContactos = new ArrayList<>();
    private int tempIdSeed = -1;    // IDs negativos temporales

    /* ===== NUEVO: item para el combo (muestra descripción, guarda código) ===== */
    private static class ComboItem {
        final Integer codigo; final String descripcion;
        ComboItem(Integer codigo, String descripcion){ this.codigo=codigo; this.descripcion=descripcion; }
        @Override public String toString(){ return descripcion; } // solo se verá la descripción
    }

    public ControladorClientes(ModeloCliente modelo) {
        this.modelo = modelo;

        var vista = modelo.getVista();

        // ====== mapear botones superiores ======
        btnNuevo      = vista.btnNuevo;
        btnActualizar = vista.btnActualizar;
        btnEliminar   = vista.btnEliminar;
        btnBuscar     = vista.btnBuscar;
        btnLimpiar    = vista.btnLimpiar;

        lblNuevo      = vista.lblNuevo;
        lblActualizar = vista.lblActualizar;
        lblEliminar   = vista.lblEliminar;
        lblBuscar     = vista.lblBuscar;
        lblLimpiar    = vista.lblLimpiar;

        lblNuevo.setName("icono");
        lblActualizar.setName("icono");
        lblEliminar.setName("icono");
        lblBuscar.setName("icono");
        lblLimpiar.setName("icono");

        // ====== mapear botones de Contacto ======
        btnNuevoC      = vista.btnNuevo1;
        btnActualizarC = vista.btnActualizar1;
        btnEliminarC   = vista.btnEliminar1;

        lblNuevoC      = vista.lblNuevo1;
        lblActualizarC = vista.lblActualizar1;
        lblEliminarC   = vista.lblEliminar1;

        lblNuevoC.setName("icono");
        lblActualizarC.setName("icono");
        lblEliminarC.setName("icono");

        inicializarIconos();

        // Los panel-botón usan MouseListener
        btnNuevo.addMouseListener(this);
        btnActualizar.addMouseListener(this);
        btnEliminar.addMouseListener(this);
        btnBuscar.addMouseListener(this);
        btnLimpiar.addMouseListener(this);

        btnNuevoC.addMouseListener(this);
        btnActualizarC.addMouseListener(this);
        btnEliminarC.addMouseListener(this);

        /* ===== NUEVO: cargar combo de tipos de cliente desde BD ===== */
        cargarComboTiposCliente();

        // 👉 Cargar datos al iniciar
        refrescarTabla();

        // 👉 Registrar selección de la tabla de clientes UNA sola vez
        JTable tablaClientes = tablaClientes();
        if (tablaClientes != null) {
            tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionListener ls = ev -> { if (!ev.getValueIsAdjusting()) onSeleccionCliente(); };
            tablaClientes.getSelectionModel().addListSelectionListener(ls);
        }

        // 👉 Click en tabla de contactos para pasar a los campos
        JTable tablaContactos = tablaContactos();
        if (tablaContactos != null) {
            tablaContactos.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    int row = tablaContactos.getSelectedRow();
                    if (row >= 0 && row < bufferContactos.size()) {
                        ContactoTmp t = bufferContactos.get(row);
                        vista.txtInfoContacto.setText(nvl(t.info));
                        vista.txtTelContacto.setText(nvl(t.telefono));
                        setComboByCode(localizarComboTipoContacto(), t.tipoContacto);
                    }
                }
            });
        }
    }

    // ===========================================================
    // MouseListener (para panel-botón)
    // ===========================================================
    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();

        if (src == btnNuevo)          onNuevo();
        else if (src == btnActualizar) onActualizar();
        else if (src == btnEliminar)   onEliminar();
        else if (src == btnBuscar)     onBuscar();
        else if (src == btnLimpiar)    onLimpiar();

        else if (src == btnNuevoC)      onNuevoContacto();
        else if (src == btnActualizarC) onActualizarContacto();
        else if (src == btnEliminarC)   onEliminarContacto();
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), true); }

    @Override
    public void mouseExited(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), false); }

    // ===========================================================
    // Iconos
    // ===========================================================
    private void inicializarIconos() {
        String ruta = "/com/umg/iconos/IconoBoton1.png";
        iconosBotones.put(btnNuevo, ruta);
        iconosBotones.put(btnActualizar, ruta);
        iconosBotones.put(btnEliminar, ruta);
        iconosBotones.put(btnBuscar, ruta);
        iconosBotones.put(btnLimpiar, ruta);

        iconosBotones.put(btnNuevoC, ruta);
        iconosBotones.put(btnActualizarC, ruta);
        iconosBotones.put(btnEliminarC, ruta);
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

    // ===========================================================
    // Acciones CRUD – CLIENTE
    // ===========================================================
    private void onSeleccionCliente() {
        JTable tbl = tablaClientes();
        int row = (tbl == null) ? -1 : tbl.getSelectedRow();
        if (row < 0) return;

        String nit = String.valueOf(tbl.getValueAt(row, 0)).trim();
        if (nit.isEmpty()) return;

        var c = servicio.obtenerPorNit(nit);
        if (c == null) return;

        // --- Rellenar campos del formulario ---
        var v = modelo.getVista();
        v.txtNIT.setText(c.getNit());
        v.txtPrimerNombre.setText(nvl(c.getNombre1()));
        v.txtSegundoNombre.setText(nvl(c.getNombre2()));
        v.txtTercerNombre.setText(nvl(c.getNombre3()));
        v.txtPrimerApellido.setText(nvl(c.getApellido1()));
        v.txtSegundoApellido.setText(nvl(c.getApellido2()));
        v.txtApellidoDeCasada.setText(nvl(c.getApellidoCasada()));
        v.txtDirección.setText(nvl(c.getDireccion()));
        v.txtLimiteDeCredito.setText(c.getLimiteCredito()==null? "" : String.valueOf(c.getLimiteCredito()));
        setComboByCode(localizarComboTipoCliente(), c.getTipoCliente());

        // --- Cargar contactos del NIT seleccionado ---
        cargarContactosDe(nit);
    }

    private void onNuevo() {
        try {
            ModeloCliente c = leerFormulario();
            if (!validarMinimo(c)) return;

            if (servicio.insertar(c)) {
                // Persistir contactos del buffer (nuevo cliente)
                int corr = 1;
                for (ContactoTmp t : bufferContactos) {
                    int id = servicio.nextIdContactoCliente();
                    servicio.insertarContactoCliente(
                            id, corr++,
                            nvlInt(t.tipoContacto),
                            nvlStr(t.info),
                            nvlStr(t.telefono),
                            c.getNit()
                    );
                }

                mensaje("Cliente guardado.", "OK", JOptionPane.INFORMATION_MESSAGE);
                refrescarTabla();
                limpiarFormulario();
            } else {
                mensaje("No se pudo guardar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            mensaje("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onActualizar() {
        try {
            ModeloCliente c = leerFormulario();
            if (c.getNit() == null || c.getNit().isBlank()) {
                mensaje("Indica el NIT para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (servicio.actualizar(c)) {
                // Reemplazo: borro e inserto contactos vigentes del buffer
                servicio.eliminarContactosPorCliente(c.getNit());
                int corr = 1;
                for (ContactoTmp t : bufferContactos) {
                    int id = servicio.nextIdContactoCliente();
                    servicio.insertarContactoCliente(
                            id, corr++,
                            nvlInt(t.tipoContacto),
                            nvlStr(t.info),
                            nvlStr(t.telefono),
                            c.getNit()
                    );
                }

                mensaje("Cliente actualizado.", "OK", JOptionPane.INFORMATION_MESSAGE);
                refrescarTabla();
            } else {
                mensaje("No se pudo actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            mensaje("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() { // Soft-delete
        var v = modelo.getVista();
        String nit = safe(v.txtNIT.getText());
        if (nit == null || nit.isBlank()) {
            mensaje("Indica el NIT para desactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(v, "¿Desactivar cliente " + nit + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        if (servicio.eliminar(nit)) {
            mensaje("Cliente desactivado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
            limpiarFormulario();
        } else {
            mensaje("No se pudo desactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onBuscar() {
        var v = modelo.getVista();
        String texto = safe(v.txtBuscar.getText());
        cargarTabla(servicio.buscar(texto == null ? "" : texto));
    }

    private void onLimpiar() {
        limpiarFormulario();
        refrescarTabla();
    }

    // ===========================================================
    // Acciones – CONTACTOS (solo tabla/buffer)
    // ===========================================================
    private void onNuevoContacto() {
        var v = modelo.getVista();
        String nit = safe(v.txtNIT.getText());
        if (nit == null) {
            mensaje("Primero selecciona/indica un NIT.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer tipo = leerTipoContactoDesdeUI();
        String info  = safe(v.txtInfoContacto.getText());
        String tel   = safe(v.txtTelContacto.getText());

        ContactoTmp t = new ContactoTmp();
        t.identificacion = tempIdSeed--; // temporal
        t.correlativo    = bufferContactos.size() + 1;
        t.tipoContacto   = tipo;
        t.info           = info;
        t.telefono       = tel;
        t.nit            = nit;

        bufferContactos.add(t);
        refrescarTablaContactos();
    }

    private void onActualizarContacto() {
        JTable tabla = tablaContactos();
        int row = (tabla == null) ? -1 : tabla.getSelectedRow();
        if (row < 0) { mensaje("Selecciona un contacto en la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }

        var v = modelo.getVista();
        ContactoTmp t = bufferContactos.get(row);
        t.tipoContacto = leerTipoContactoDesdeUI();
        t.info         = safe(v.txtInfoContacto.getText());
        t.telefono     = safe(v.txtTelContacto.getText());

        refrescarTablaContactos();
    }

    private void onEliminarContacto() {
        JTable tabla = tablaContactos();
        int row = (tabla == null) ? -1 : tabla.getSelectedRow();
        if (row < 0) { mensaje("Selecciona un contacto en la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }

        bufferContactos.remove(row);
        int i = 1;
        for (ContactoTmp t : bufferContactos) t.correlativo = i++;
        refrescarTablaContactos();
    }

    // ===========================================================
    // Helpers de UI / Tablas
    // ===========================================================
    private void refrescarTabla() {
        cargarTabla(servicio.obtenerTodos());
    }
    // Cache: codigo -> descripcion de tipo_cliente
    private final Map<Integer, String> cacheTipoCliente = new HashMap<>();
    private void cargarCacheTiposCliente() {
        if (!cacheTipoCliente.isEmpty()) return;  // ya cargado
        try {
            var con = com.umg.seguridad.Sesion.getConexion();
            var ps  = con.preparar("SELECT codigo, descripcion FROM tipo_cliente");
            var rs  = ps.executeQuery();
            while (rs.next()) {
                java.math.BigDecimal bd = rs.getBigDecimal(1);  // NUMBER -> BigDecimal
                int codigo = (bd == null) ? 0 : bd.intValue();
                cacheTipoCliente.put(codigo, rs.getString(2));
            }
        } catch (Exception e) {
            System.out.println("WARN tipos_cliente cache: " + e.getMessage());
        }
    }
    private void actualizarTablaUI(JTable t) {
        if (t == null) return;
        SwingUtilities.invokeLater(() -> {
            if (t.getModel() instanceof javax.swing.table.DefaultTableModel m) {
                m.fireTableDataChanged();
            }
            t.revalidate();
            t.repaint();
        });
    }


    private void cargarTabla(List<ModeloCliente> data) {
        JTable tabla = tablaClientes();
        if (tabla == null) return;

        // asegúrate de tener el cache cargado
        cargarCacheTiposCliente();

        DefaultTableModel model = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new Object[]{"NIT", "Nombre", "Apellido", "Tipo de Cliente"});

        for (var c : data) {
            String nombres   = join(c.getNombre1(), c.getNombre2(), c.getNombre3());
            String apellidos = join(c.getApellido1(), c.getApellido2(), c.getApellidoCasada());
            String tipoDesc  = (c.getTipoCliente() == null)
                    ? ""
                    : cacheTipoCliente.getOrDefault(c.getTipoCliente(), String.valueOf(c.getTipoCliente()));
            model.addRow(new Object[]{ c.getNit(), nombres, apellidos, tipoDesc });
        }
        tabla.setModel(model);
        actualizarTablaUI(tabla); // si ya agregaste este helper; opcional pero recomendado
    }


    private void refrescarTablaContactos() {
        JTable tabla = tablaContactos();
        if (tabla == null) return;

        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        m.setColumnIdentifiers(new Object[]{"No.", "Tipo", "Información", "Teléfono"});

        for (ContactoTmp t : bufferContactos) {
            m.addRow(new Object[]{ t.correlativo, t.tipoContacto, t.info, t.telefono });
        }
        tabla.setModel(m);
    }

    // Carga contactos desde BD al buffer y refresca la tabla
    private void cargarContactosDe(String nit) {
        bufferContactos.clear();
        try {
            var lista = servicio.obtenerContactosPorCliente(nit); // Debe filtrar por fk_cliente_nit
            int corr = 1;
            for (var x : lista) {
                ContactoTmp t = new ContactoTmp();
                t.identificacion = x.getIdentificacion();
                t.correlativo    = (x.getCorrelativo() != null) ? x.getCorrelativo() : corr;
                t.tipoContacto   = x.getTipoContacto();
                t.info           = x.getInfoContacto();
                t.telefono       = x.getTelefono();
                t.nit            = nit;
                bufferContactos.add(t);
                corr++;
            }
        } catch (Exception ex) {
            mensaje("Error cargando contactos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refrescarTablaContactos();
    }

    // Obtiene las dos JTables de la vista: [0]=clientes, [1]=contactos
    private List<JTable> tablasEnVista() {
        List<JTable> out = new ArrayList<>();
        acumular(modelo.getVista(), JTable.class, out);
        return out;
    }
    private JTable tablaClientes() {
        try {
            JTable t = modelo.getVista().getTablaClientes();
            if (t != null) return t;
        } catch (Throwable ignore) { }
        List<JTable> ts = tablasEnVista();
        return ts.isEmpty() ? null : ts.get(0);
    }
    private JTable tablaContactos() {
        List<JTable> ts = tablasEnVista();
        return ts.size() >= 2 ? ts.get(1) : null;
    }

    private <T extends Component> void acumular(Container root, Class<T> tipo, List<T> out) {
        for (Component c : root.getComponents()) {
            if (tipo.isInstance(c)) out.add(tipo.cast(c));
            if (c instanceof Container cont) acumular(cont, tipo, out);
        }
    }

    // ===========================================================
    // Lectura de formulario
    // ===========================================================
    private ModeloCliente leerFormulario() {
        var v = modelo.getVista();
        ModeloCliente c = new ModeloCliente();

        c.setNit(safe(v.txtNIT.getText()));
        c.setCodigo(null);
        c.setNombre1(safe(v.txtPrimerNombre.getText()));
        c.setNombre2(safe(v.txtSegundoNombre.getText()));
        c.setNombre3(safe(v.txtTercerNombre.getText()));
        c.setApellido1(safe(v.txtPrimerApellido.getText()));
        c.setApellido2(safe(v.txtSegundoApellido.getText()));
        c.setApellidoCasada(safe(v.txtApellidoDeCasada.getText()));

        c.setTipoCliente(leerTipoClienteDesdeUI());

        Double lim = null;
        String txtLim = safe(v.txtLimiteDeCredito.getText());
        if (txtLim != null) try { lim = Double.parseDouble(txtLim); } catch (Exception ignored) {}
        c.setLimiteCredito(lim);

        c.setDireccion(safe(v.txtDirección.getText()));
        c.setEstado("A");
        return c;
    }

    // Lee el código de "Tipo de Cliente" del combo de la vista (usando ComboItem)
    private Integer leerTipoClienteDesdeUI() {
        JComboBox<?> cmb = localizarComboTipoCliente();
        if (cmb == null) return null;
        Object sel = cmb.getSelectedItem();
        if (sel instanceof ComboItem ci) return ci.codigo;
        return null;
    }

    private boolean validarMinimo(ModeloCliente c) {
        if (c == null) { mensaje("Datos de cliente inválidos.", "Aviso", JOptionPane.WARNING_MESSAGE); return false; }
        if (c.getNit()==null || c.getNit().isBlank()) { mensaje("NIT es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE); return false; }
        if (c.getNombre1()==null || c.getNombre1().isBlank()) { mensaje("El Primer Nombre es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE); return false; }
        if (c.getApellido1()==null || c.getApellido1().isBlank()) { mensaje("El Primer Apellido es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE); return false; }
        return true;
    }

    // ===========================================================
    // Combos y utilitarios
    // ===========================================================
    private Integer leerTipoContactoDesdeUI() {
        JComboBox<?> cmb = localizarComboTipoContacto();
        if (cmb == null) return null;
        Object sel = cmb.getSelectedItem();
        if (sel == null) return null;
        String s = sel.toString();
        try {
            if (s.contains("-")) s = s.split("-")[0].trim();
            return Integer.parseInt(s);
        } catch (Exception ignored) { return null; }
    }

    // Localiza el combo "Tipo de Contacto"
    private JComboBox<?> localizarComboTipoContacto() {
        JLabel lbl = buscarLabelConTexto(modelo.getVista(), "Tipo de Contacto");
        if (lbl != null && lbl.getParent() != null) {
            JComboBox<?> cb = buscarComboEn(lbl.getParent());
            if (cb != null) return cb;
        }
        // Fallback: segundo combo de la vista
        List<JComboBox<?>> combos = new ArrayList<>();
        recolectarCombos(modelo.getVista(), combos);
        return combos.size() >= 2 ? combos.get(1) : null;
    }

    // Localiza el combo "Tipo de Cliente"
    private JComboBox<?> localizarComboTipoCliente() {
        JLabel lbl = buscarLabelConTexto(modelo.getVista(), "Tipo de Cliente");
        if (lbl != null && lbl.getParent() != null) {
            JComboBox<?> cb = buscarComboEn(lbl.getParent());
            if (cb != null) return cb;
        }
        // Fallback: primer combo de la vista
        List<JComboBox<?>> combos = new ArrayList<>();
        recolectarCombos(modelo.getVista(), combos);
        return combos.isEmpty() ? null : combos.get(0);
    }

    // Busca recursivamente el primer JComboBox en un contenedor
    private JComboBox<?> buscarComboEn(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JComboBox<?> cb) return cb;
            if (c instanceof Container cont) {
                JComboBox<?> r = buscarComboEn(cont);
                if (r != null) return r;
            }
        }
        return null;
    }

    // Recolector de todos los JComboBox (para fallbacks)
    private void recolectarCombos(Container root, List<JComboBox<?>> out) {
        for (Component c : root.getComponents()) {
            if (c instanceof JComboBox<?> cb) out.add(cb);
            if (c instanceof Container cont) recolectarCombos(cont, out);
        }
    }

    // Selecciona en combo por código (funciona con ComboItem)
    private void setComboByCode(JComboBox<?> combo, Integer code) {
        if (combo == null || code == null) return;
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object it = combo.getItemAt(i);
            if (it instanceof ComboItem ci && code.equals(ci.codigo)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private JLabel buscarLabelConTexto(Container root, String texto) {
        for (Component c : root.getComponents()) {
            if (c instanceof JLabel lab) {
                String t = trimSafe(lab.getText());
                if (t != null && t.equalsIgnoreCase(texto)) return lab;
            }
            if (c instanceof Container cont) {
                JLabel r = buscarLabelConTexto(cont, texto);
                if (r != null) return r;
            }
        }
        return null;
    }

    private String join(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p != null && !p.isBlank()) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(p.trim());
            }
        }
        return sb.toString();
    }

    private void limpiarFormulario() {
        var v = modelo.getVista();
        v.txtNIT.setText("");
        v.txtPrimerNombre.setText("");
        v.txtSegundoNombre.setText("");
        v.txtTercerNombre.setText("");
        v.txtPrimerApellido.setText("");
        v.txtSegundoApellido.setText("");
        v.txtApellidoDeCasada.setText("");
        v.txtLimiteDeCredito.setText("");
        v.txtDirección.setText("");
        v.txtBuscar.setText("");

        JComboBox<?> cmbTCli = localizarComboTipoCliente();
        if (cmbTCli != null && cmbTCli.getItemCount() > 0) cmbTCli.setSelectedIndex(0);

        // limpiar buffer y tabla de contactos
        bufferContactos.clear();
        refrescarTablaContactos();
        // limpiar campos de contacto
        v.txtInfoContacto.setText("");
        v.txtTelContacto.setText("");
        JComboBox<?> cmbTCont = localizarComboTipoContacto();
        if (cmbTCont != null && cmbTCont.getItemCount() > 0) cmbTCont.setSelectedIndex(0);
    }

    private void mensaje(String m, String t, int tipo) {
        JOptionPane.showMessageDialog(modelo.getVista(), m, t, tipo);
    }

    private String safe(String s) { if (s == null) return null; String t = s.trim(); return t.isEmpty()? null : t; }
    private String trimSafe(String s) { return (s == null) ? null : s.trim(); }
    private String nvl(String s) { return (s == null) ? "" : s; }
    private int nvlInt(Integer i) { return (i == null) ? 0 : i; }
    private String nvlStr(String s) { return (s == null) ? "" : s; }

    // ActionListener (no lo usas con paneles, pero lo dejamos)
    @Override public void actionPerformed(ActionEvent e) { }

    /* ========= NUEVO: Cargar combo desde la BD ========= */
    private void cargarComboTiposCliente() {
        JComboBox<?> cb = localizarComboTipoCliente();
        if (cb == null) return;

        DefaultComboBoxModel<ComboItem> model = new DefaultComboBoxModel<>();
        model.addElement(new ComboItem(null, "-- Seleccione --"));
        try {
            for (var r : tiposDao.listarOrdenadoPor("descripcion")) {
                model.addElement(new ComboItem(r.codigo, r.descripcion));
            }
        } catch (Exception ex) {
            mensaje("Error cargando tipos de cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        @SuppressWarnings("unchecked")
        JComboBox<ComboItem> real = (JComboBox<ComboItem>) cb;
        real.setModel(model);
    }
}
