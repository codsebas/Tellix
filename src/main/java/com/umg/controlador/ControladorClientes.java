package com.umg.controlador;

import com.umg.implementacion.ClienteImp;
import com.umg.implementacion.TipoClienteImp;
import com.umg.implementacion.TipoContactoImp;
import com.umg.interfaces.ICliente;
import com.umg.interfaces.ITipoCliente;
import com.umg.interfaces.ITipoContacto;
import com.umg.modelo.ModeloCliente;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ControladorClientes implements ActionListener, MouseListener {

    // ====== Modelo y servicio ======
    ModeloCliente modelo;
    private final ICliente servicio = new ClienteImp();

    // ===== DAO/Servicio para combos =====
    private final ITipoCliente.DAO tiposDao = new TipoClienteImp();
    private final ITipoContacto.Servicio svcTipoContacto = new TipoContactoImp();

    // ====== Botones (bloque superior) ======
    private JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    // ====== Botones (bloque Contacto) ======
    private JPanel btnNuevoC, btnActualizarC, btnEliminarC;
    private JLabel lblNuevoC, lblActualizarC, lblEliminarC;

    private final Map<JPanel, String> iconosBotones = new HashMap<>();

    // ====== Buffer de contactos (solo tabla) ======
    private static class ContactoTmp {
        Integer identificacion;
        Integer correlativo;
        Integer tipoContacto;   // FK
        String info;
        String telefono;
        String nit;
    }
    private final List<ContactoTmp> bufferContactos = new ArrayList<>();
    private int tempIdSeed = -1;

    // Item para combo de Tipos de Cliente (mostrar desc, guardar cod)
    private static class ComboItem {
        final Integer codigo; final String descripcion;
        ComboItem(Integer codigo, String descripcion){ this.codigo=codigo; this.descripcion=descripcion; }
        @Override public String toString(){ return descripcion; }
    }

    // Caches
    private final Map<Integer, String> tcod2desc = new HashMap<>();
    private final Map<String, Integer> tdesc2cod = new HashMap<>();
    private final Map<Integer, String> cacheTipoCliente = new HashMap<>();

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

        // listeners
        btnNuevo.addMouseListener(this);
        btnActualizar.addMouseListener(this);
        btnEliminar.addMouseListener(this);
        btnBuscar.addMouseListener(this);
        btnLimpiar.addMouseListener(this);

        btnNuevoC.addMouseListener(this);
        btnActualizarC.addMouseListener(this);
        btnEliminarC.addMouseListener(this);

        // Combos
        cargarCacheTiposContacto();
        poblarComboTipoContacto();   // solo descripciones

        cargarComboTiposCliente();   // ComboItem (desc visible)

        // Datos iniciales
        refrescarTabla();

        // === selección de cliente -> llenar formulario y contactos ===
        JTable tblCli = modelo.getVista().getTablaClientes();
        if (tblCli != null) {
            tblCli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tblCli.getSelectionModel().addListSelectionListener(ev -> {
                if (!ev.getValueIsAdjusting()) onSeleccionCliente(); // ← carga formulario + contactos
            });
        }


        // selección en tabla clientes -> llenar form
        JTable tablaClientes = tablaClientes();
        if (tablaClientes != null) {
            tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionListener ls = ev -> { if (!ev.getValueIsAdjusting()) onSeleccionCliente(); };
            tablaClientes.getSelectionModel().addListSelectionListener(ls);
        }

        // clic en tabla contactos -> pasar a campos y setear combo
        JTable tblContactos = tablaContactos();
        if (tblContactos != null) {
            tblContactos.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    int row = tblContactos.getSelectedRow();
                    if (row < 0 || row >= bufferContactos.size()) return;

                    ContactoTmp t = bufferContactos.get(row);
                    vista.txtInfoContacto.setText(nvl(t.info));
                    vista.txtTelContacto.setText(nvl(t.telefono));
                    setComboTipoContactoByCode(t.tipoContacto);
                }
            });
        }
    }

    // =============== MouseListener =================
    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();

        if (src == btnNuevo)           onNuevo();
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
    @Override public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), true); }
    @Override public void mouseExited (MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), false); }

    // =============== Iconos =================
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
        for (Component comp : boton.getComponents())
            if (comp instanceof JLabel lbl && nombre.equals(lbl.getName())) return lbl;
        return null;
    }

    private void onSeleccionCliente() {
        JTable tbl = modelo.getVista().getTablaClientes();
        int row = (tbl == null) ? -1 : tbl.getSelectedRow();
        if (row < 0) return;

        String nit = String.valueOf(tbl.getValueAt(row, 0)).trim();
        if (nit.isEmpty()) return;

        var c = servicio.obtenerPorNit(nit);
        if (c == null) return;

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

        // posicionar combo de tipo cliente (usa ComboItem en el modelo)
        @SuppressWarnings("unchecked")
        JComboBox<?> cmbTCli = localizarComboTipoCliente();
        setComboByCode(cmbTCli, c.getTipoCliente());


        // === CARGAR CONTACTOS DEL NIT EN LA TABLA ===
        cargarContactosDe(nit);
    }


    private void onNuevo() {
        try {
            ModeloCliente c = leerFormulario();
            if (!validarMinimo(c)) return;

            if (servicio.insertar(c)) {
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

    private void onEliminar() {
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

    // =============== Contactos (buffer) =================
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
        t.identificacion = tempIdSeed--;
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

    // =============== Helpers UI / Tablas =================
    private void refrescarTabla() {
        cargarCacheTiposCliente();
        cargarTabla(servicio.obtenerTodos());
    }

    private void cargarTabla(List<ModeloCliente> data) {
        JTable tabla = tablaClientes();
        if (tabla == null) return;

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
        actualizarTablaUI(tabla);
    }
    private void refrescarTablaContactos() {
        JTable tabla = modelo.getVista().getTablaContactos(); // este es tu tblClientes1
        if (tabla == null) return;

        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        m.setColumnIdentifiers(new Object[]{"No.", "Tipo", "Información", "Teléfono"});

        for (ContactoTmp t : bufferContactos) {
            String tipoDesc = (t.tipoContacto == null)
                    ? ""
                    : tcod2desc.getOrDefault(t.tipoContacto, String.valueOf(t.tipoContacto));
            m.addRow(new Object[]{ t.correlativo, tipoDesc, t.info, t.telefono });
        }
        tabla.setModel(m);
    }


    // Lee contactos desde la BD al buffer y refresca tabla
    private void cargarContactosDe(String nit) {
        bufferContactos.clear();
        try {
            var lista = servicio.obtenerContactosPorCliente(nit); // filtra por fk_cliente_nit
            int corr = 1;
            for (var x : lista) {
                ContactoTmp t = new ContactoTmp();
                t.identificacion = x.getIdentificacion();
                t.correlativo    = (x.getCorrelativo() != null) ? x.getCorrelativo() : corr;
                t.tipoContacto   = x.getTipoContacto();    // <-- código
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



    private List<JTable> tablasEnVista() {
        List<JTable> out = new ArrayList<>();
        acumular(modelo.getVista(), JTable.class, out);
        return out;
    }
    private JTable tablaClientes() {
        try { JTable t = modelo.getVista().getTablaClientes(); if (t != null) return t; } catch (Throwable ignore) { }
        List<JTable> ts = tablasEnVista();
        return ts.isEmpty() ? null : ts.get(0);
    }
    private JTable tablaContactos() {
        try { return modelo.getVista().getTablaContactos(); }
        catch (Throwable ignore) { return null; }
    }

    private <T extends Component> void acumular(Container root, Class<T> tipo, List<T> out) {
        for (Component c : root.getComponents()) {
            if (tipo.isInstance(c)) out.add(tipo.cast(c));
            if (c instanceof Container cont) acumular(cont, tipo, out);
        }
    }

    // =============== Lectura de formulario =================
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

    // Lee el código de "Tipo de Cliente" del combo (usando localizador, NO getters)
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

    private Integer leerTipoContactoDesdeUI() {
        JComboBox<String> cb = modelo.getVista().getCmbTipoContacto();
        if (cb == null) return null;
        Object sel = cb.getSelectedItem();
        if (sel == null) return null;
        String desc = sel.toString().trim();
        if (desc.equalsIgnoreCase("-- Seleccione --")) return null;
        return tdesc2cod.get(desc); // de la cache codigo<-descripcion
    }

    private JComboBox<?> localizarComboTipoContacto() {
        JLabel lbl = buscarLabelConTexto(modelo.getVista(), "Tipo de Contacto");
        if (lbl != null && lbl.getParent() != null) {
            JComboBox<?> cb = buscarComboEn(lbl.getParent());
            if (cb != null) return cb;
        }
        List<JComboBox<?>> combos = new ArrayList<>();
        recolectarCombos(modelo.getVista(), combos);
        return combos.size() >= 2 ? combos.get(1) : null;
    }

    private JComboBox<?> localizarComboTipoCliente() {
        JLabel lbl = buscarLabelConTexto(modelo.getVista(), "Tipo de Cliente");
        if (lbl != null && lbl.getParent() != null) {
            JComboBox<?> cb = buscarComboEn(lbl.getParent());
            if (cb != null) return cb;
        }
        List<JComboBox<?>> combos = new ArrayList<>();
        recolectarCombos(modelo.getVista(), combos);
        return combos.isEmpty() ? null : combos.get(0);
    }

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

    private void recolectarCombos(Container root, List<JComboBox<?>> out) {
        for (Component c : root.getComponents()) {
            if (c instanceof JComboBox<?> cb) out.add(cb);
            if (c instanceof Container cont) recolectarCombos(cont, out);
        }
    }

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

    private void setComboTipoContactoByCode(Integer code) {
        if (code == null) return;
        JComboBox<String> cb = modelo.getVista().getCmbTipoContacto();
        if (cb == null) return;

        String desc = tcod2desc.get(code); // de la cache descripcion<-codigo
        if (desc == null) return;

        for (int i = 0; i < cb.getItemCount(); i++) {
            if (desc.equals(cb.getItemAt(i))) {
                cb.setSelectedIndex(i);
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

        bufferContactos.clear();
        refrescarTablaContactos();

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

    @Override public void actionPerformed(ActionEvent e) { }

    // =============== Caches / combos =================
    private void cargarCacheTiposCliente() {
        if (!cacheTipoCliente.isEmpty()) return;
        try {
            var con = com.umg.seguridad.Sesion.getConexion();
            var ps  = con.preparar("SELECT codigo, descripcion FROM tipo_cliente");
            var rs  = ps.executeQuery();
            while (rs.next()) {
                java.math.BigDecimal bd = rs.getBigDecimal(1);
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
            if (t.getModel() instanceof DefaultTableModel m) m.fireTableDataChanged();
            t.revalidate();
            t.repaint();
        });
    }

    private void cargarCacheTiposContacto() {
        tcod2desc.clear();
        tdesc2cod.clear();
        try {
            var lista = svcTipoContacto.listarOrdenadoPor("descripcion");
            for (var r : lista) {
                tcod2desc.put(r.codigo, r.descripcion);
                tdesc2cod.put(r.descripcion, r.codigo);
            }
        } catch (Exception e) {
            System.out.println("cargarCacheTiposContacto: " + e.getMessage());
        }
    }

    private void poblarComboTipoContacto() {
        JComboBox<String> cb = modelo.getVista().getCmbTipoContacto(); // usa el combo real
        if (cb == null) return;

        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        m.addElement("-- Seleccione --");

        // ordenar alfabéticamente por descripción
        java.util.List<String> descs = new java.util.ArrayList<>(tdesc2cod.keySet());
        descs.sort(String::compareToIgnoreCase);
        for (String d : descs) m.addElement(d);

        cb.setModel(m);
    }


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
