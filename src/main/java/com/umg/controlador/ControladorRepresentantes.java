package com.umg.controlador;

import com.umg.implementacion.ProveedorImp;
import com.umg.implementacion.TipoContactoImp;
import com.umg.interfaces.IProveedor;
import com.umg.interfaces.ITipoContacto;
import com.umg.modelo.ModeloProveedores;
import com.umg.modelo.ModeloRepresentates;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ControladorRepresentantes implements ActionListener, MouseListener {
    ModeloRepresentates modelo;

    // ===== Modelo y servicios =====
    private final IProveedor servicio = new ProveedorImp();
    private final ITipoContacto.Servicio svcTipoContacto = new TipoContactoImp();

    // ===== Botones (superior) =====
    private final JPanel btnNuevo, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private final JLabel lblNuevo, lblActualizar, lblEliminar, lblBuscar, lblLimpiar;

    // ===== Botones (contactos) =====
    private final JPanel btnNuevoC, btnActualizarC, btnEliminarC;
    private final JLabel  lblNuevoC, lblActualizarC, lblEliminarC;

    // Cache para iconos (hover)
    private final Map<JPanel,String> iconosBotones = new HashMap<>();

    // ===== Buffer de contactos (UI) =====
    private static class ContactoTmp {
        Integer correlativo;
        Integer tipoContacto;    // código en tipo_contacto
        String  info;
        String  telefono;
    }
    private final java.util.List<ContactoTmp> bufferContactos = new ArrayList<>();

    // ===== Cache tipo_contacto =====
    private final Map<Integer,String> tcod2desc = new HashMap<>();
    private final Map<String,Integer> tdesc2cod = new HashMap<>();
    public ControladorRepresentantes(ModeloRepresentates modelo) {
        this.modelo = modelo;
        var v = modelo.getVista();

        // Mapear botones superiores
        btnNuevo      = v.btnNuevo;
        btnActualizar = v.btnActualizar;
        btnEliminar   = v.btnEliminar;
        btnBuscar     = v.btnBuscar;
        btnLimpiar    = v.btnLimpiar;

        lblNuevo      = v.lblNuevo;
        lblActualizar = v.lblActualizar;
        lblEliminar   = v.lblEliminar;
        lblBuscar     = v.lblBuscar;
        lblLimpiar    = v.lblLimpiar;

        if (lblNuevo != null)      lblNuevo.setName("icono");
        if (lblActualizar != null) lblActualizar.setName("icono");
        if (lblEliminar != null)   lblEliminar.setName("icono");
        if (lblBuscar != null)     lblBuscar.setName("icono");
        if (lblLimpiar != null)    lblLimpiar.setName("icono");

        // Mapear botones de contactos (inferior)
        btnNuevoC      = v.btnNuevo1;
        btnActualizarC = v.btnActualizar1;
        btnEliminarC   = v.btnEliminar1;

        lblNuevoC      = v.lblNuevo1;
        lblActualizarC = v.lblActualizar1;
        lblEliminarC   = v.lblEliminar1;

        if (lblNuevoC != null)      lblNuevoC.setName("icono");
        if (lblActualizarC != null) lblActualizarC.setName("icono");
        if (lblEliminarC != null)   lblEliminarC.setName("icono");

        inicializarIconos();

        // Listeners de mouse (como en clientes)
        if (btnNuevo != null)      btnNuevo.addMouseListener(this);
        if (btnActualizar != null) btnActualizar.addMouseListener(this);
        if (btnEliminar != null)   btnEliminar.addMouseListener(this);
        if (btnBuscar != null)     btnBuscar.addMouseListener(this);
        if (btnLimpiar != null)    btnLimpiar.addMouseListener(this);

        if (btnNuevoC != null)      btnNuevoC.addMouseListener(this);
        if (btnActualizarC != null) btnActualizarC.addMouseListener(this);
        if (btnEliminarC != null)   btnEliminarC.addMouseListener(this);

        // ===== Combos =====
        cargarCacheTiposContacto();
        poblarComboTipoContacto(); // usa vista.getCmbTipoContacto()

        // ===== Datos iniciales =====
        refrescarTabla();

        // Selección en tabla superior -> llenar formulario + cargar contactos
        JTable tbl = modelo.getVista().getTablaClientes();
        if (tbl != null) {
            tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tbl.getSelectionModel().addListSelectionListener(ev -> {
                if (!ev.getValueIsAdjusting()) onSeleccionProveedor();
            });
        }

        // Click en tabla de contactos -> pasar a campos (manteniendo combo por código)
        JTable tc = modelo.getVista().getTablaContactos();
        if (tc != null) {
            tc.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    int row = tc.getSelectedRow();
                    if (row < 0 || row >= bufferContactos.size()) return;
                    ContactoTmp c = bufferContactos.get(row);
                    v.txtInfoContacto.setText(nvl(c.info));
                    v.txtTelContacto.setText(nvl(c.telefono));
                    setComboTipoContactoByCode(c.tipoContacto);
                }
            });
        }
    }

    private boolean esTipoTelefono(String desc) {
        if (desc == null) return false;
        String d = desc.toUpperCase(Locale.ROOT);
        return d.contains("TEL") || d.contains("FONO") || d.contains("CEL")
                || d.contains("WHATS") || d.contains("PHONE");
    }

    // ===================== Acciones de barra superior =====================
//    private void onNuevo() {
//        try {
//            var v = modelo.getVista();
//            String nitProv = safe(v.txtNIT.getText());
//            String nomProv = safe(v.txtNombreProveedor.getText());
//            if (nitProv == null || nomProv == null) {
//                mensaje("NIT Proveedor y Nombre son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            String dir = nvl(safe(v.txtDireccionFiscal.getText()));
//            String tel = nvl(safe(v.txtTelefonoProveedor.getText()));
//
//            if (!servicio.insertarProveedor(nitProv, nomProv, dir, tel)) {
//                mensaje("No se pudo guardar el proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // Representante (opcional)
//            String nitRep = safe(v.txtNitRepresentante.getText());
//            if (nitRep != null) {
//                int codigo = servicio.nextCodigoRepresentante(nitProv);
//                servicio.insertarRepresentante(
//                        nitRep, nitProv, codigo,
//                        nvl(safe(v.txtPrimerNombre.getText())),
//                        nvl(safe(v.txtSegundoNombre.getText())),
//                        nvl(safe(v.txtPrimerApellido.getText())),
//                        nvl(safe(v.txtSegundoApellido.getText())),
//                        nvl(safe(v.txtApellidoCasada.getText()))
//                );
//                // Contactos del buffer
//                for (ContactoTmp c : bufferContactos) {
//                    String info = decideInfoParaBD(c);
//                    servicio.insertarContactoRepresentante(nitRep, c.tipoContacto, info);
//                }
//            }
//
//            mensaje("Proveedor guardado.", "OK", JOptionPane.INFORMATION_MESSAGE);
//            limpiarFormulario();
//            refrescarTabla();
//        } catch (Exception ex) {
//            mensaje("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }

//    private void onActualizar() {
//        try {
//            var v = modelo.getVista();
//            String nitProv = safe(v.txtNIT.getText());
//            if (nitProv == null) { mensaje("Indica el NIT del proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
//
//            if (!servicio.actualizarProveedor(
//                    nitProv,
//                    nvl(safe(v.txtNombreProveedor.getText())),
//                    nvl(safe(v.txtDireccionFiscal.getText())),
//                    nvl(safe(v.txtTelefonoProveedor.getText()))
//            )) {
//                mensaje("No se pudo actualizar el proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // upsert del representante + reemplazo de contactos
//            String nitRep = safe(v.txtNitRepresentante.getText());
//            if (nitRep != null) {
//                int codigo = 1; // si manejas varios reps por proveedor, el que corresponda
//                boolean okRep = servicio.actualizarRepresentante(
//                        nitRep, nitProv, codigo,
//                        nvl(safe(v.txtPrimerNombre.getText())),
//                        nvl(safe(v.txtSegundoNombre.getText())),
//                        nvl(safe(v.txtPrimerApellido.getText())),
//                        nvl(safe(v.txtSegundoApellido.getText())),
//                        nvl(safe(v.txtApellidoCasada.getText()))
//                );
//                if (!okRep) {
//                    servicio.insertarRepresentante(
//                            nitRep, nitProv, codigo,
//                            nvl(safe(v.txtPrimerNombre.getText())),
//                            nvl(safe(v.txtSegundoNombre.getText())),
//                            nvl(safe(v.txtPrimerApellido.getText())),
//                            nvl(safe(v.txtSegundoApellido.getText())),
//                            nvl(safe(v.txtApellidoCasada.getText()))
//                    );
//                }
//
//                servicio.eliminarContactosRepresentante(nitRep);
//                for (ContactoTmp c : bufferContactos) {
//                    servicio.insertarContactoRepresentante(nitRep, c.tipoContacto, decideInfoParaBD(c));
//                }
//            }
//
//            mensaje("Proveedor actualizado.", "OK", JOptionPane.INFORMATION_MESSAGE);
//            refrescarTabla();
//        } catch (Exception ex) {
//            mensaje("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onEliminar() {
//        var v = modelo.getVista();
//        String nitProv = safe(v.txtNIT.getText());
//        if (nitProv == null) { mensaje("Indica el NIT del proveedor.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
//
//        int r = JOptionPane.showConfirmDialog(v, "¿Eliminar proveedor " + nitProv + "?", "Confirmar",
//                JOptionPane.YES_NO_OPTION);
//        if (r != JOptionPane.YES_OPTION) return;
//
//        servicio.eliminarRepresentantesDeProveedor(nitProv);
//        if (servicio.eliminarProveedor(nitProv)) {
//            mensaje("Proveedor eliminado.", "OK", JOptionPane.INFORMATION_MESSAGE);
//            limpiarFormulario();
//            refrescarTabla();
//        } else {
//            mensaje("No se pudo eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
//        }
//    }

    private void onBuscar() {
        String txt = safe(modelo.getVista().txtBuscar.getText());
        cargarTabla(servicio.buscarProveedores(txt));
    }

    private void onLimpiar() { limpiarFormulario(); }

    // ===================== Contactos (buffer) =====================
    private void onNuevoContacto() {
        var v = modelo.getVista();
        ContactoTmp c = new ContactoTmp();
        c.tipoContacto = leerTipoContactoDesdeUI();
        c.info         = safe(v.txtInfoContacto.getText());
        c.telefono     = safe(v.txtTelContacto.getText());
        c.correlativo  = bufferContactos.size() + 1;
        bufferContactos.add(c);
        refrescarTablaContactos();
    }

    private void onActualizarContacto() {
        JTable t = modelo.getVista().getTablaContactos();
        int row = (t == null) ? -1 : t.getSelectedRow();
        if (row < 0) { mensaje("Selecciona un contacto.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }

        var v = modelo.getVista();
        ContactoTmp c = bufferContactos.get(row);
        c.tipoContacto = leerTipoContactoDesdeUI();
        c.info         = safe(v.txtInfoContacto.getText());
        c.telefono     = safe(v.txtTelContacto.getText());
        refrescarTablaContactos();
    }

    private void onEliminarContacto() {
        JTable t = modelo.getVista().getTablaContactos();
        int row = (t == null) ? -1 : t.getSelectedRow();
        if (row < 0) { mensaje("Selecciona un contacto.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        bufferContactos.remove(row);
        int i = 1; for (ContactoTmp c : bufferContactos) c.correlativo = i++;
        refrescarTablaContactos();
    }

    private void onSeleccionProveedor() {
        JTable tbl = modelo.getVista().getTablaClientes();
        int row = (tbl == null) ? -1 : tbl.getSelectedRow();
        if (row < 0) return;

        String nitProv = valStr(tbl.getValueAt(row, 0));
        if (nitProv == null) return;

        var p = servicio.obtenerProveedorPorNit(nitProv);
        if (p == null) return;

        var v = modelo.getVista();

        // Proveedor (incluye su teléfono propio)
//        v.txtNIT.setText(nvl(p.getNitProveedor()));
        v.txtNombreProveedor.setText(nvl(p.getNombreProveedor()));
        v.txtDireccionFiscal.setText(nvl(p.getDireccionFiscal()));
        v.txtTelefonoProveedor.setText(nvl(p.getTelefonoProveedor()));  // <- teléfono del PROVEEDOR

        // Representante (si lo tienes en el modelo)
        v.txtNitRepresentante.setText(nvl(p.getNitRepresentante()));
        v.txtPrimerNombre.setText(nvl(p.getNombre1()));
        v.txtSegundoNombre.setText(nvl(p.getNombre2()));
        v.txtPrimerApellido.setText(nvl(p.getApellido1()));
        v.txtSegundoApellido.setText(nvl(p.getApellido2()));
        v.txtApellidoCasada.setText(nvl(p.getApellidoCasada()));

        // Cargar contactos del representante → esto llenará la tabla y
        // dejará listo el cuadro "Teléfono" del contacto cuando selecciones una fila abajo
        cargarContactosDeRepresentante(valStr(p.getNitRepresentante()));
    }

    private void cargarContactosDeRepresentante(String nitRep) {
        bufferContactos.clear();
        if (nitRep == null || nitRep.isBlank()) { refrescarTablaContactos(); return; }

        try {
            var lista = servicio.obtenerContactosRepresentante(nitRep);
            int i = 1;
            for (var x : lista) {
                ContactoTmp c = new ContactoTmp();
                c.correlativo  = i++;
                c.tipoContacto = x.tipoContacto;

                String desc = tcod2desc.getOrDefault(c.tipoContacto, "");
                if (esTipoTelefono(desc)) {
                    c.telefono = x.infoContacto;  // viene en info_contacto
                    c.info     = "";
                } else {
                    c.info     = x.infoContacto;
                    c.telefono = "";
                }
                bufferContactos.add(c);
            }
        } catch (Exception e) {
            mensaje("Error cargando contactos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refrescarTablaContactos();
    }



    // ===================== Tablas UI =====================
    private void refrescarTabla() {
        cargarTabla(servicio.listarProveedoresConRepresentante());
    }

    private void cargarTabla(java.util.List<IProveedor.RowProv> data) {
        JTable t = modelo.getVista().getTablaClientes();
        if (t == null) return;

        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        m.setColumnIdentifiers(new Object[]{"NIT Proveedor","Nombre Proveedor","NIT Representante","Nombre Representante"});
        for (var r : data) {
            m.addRow(new Object[]{ r.nitProveedor, r.nombreProveedor, r.nitRepresentante, r.nombreRepresentante });
        }
        t.setModel(m);
        SwingUtilities.invokeLater(() -> { t.revalidate(); t.repaint(); });
    }

    private void refrescarTablaContactos() {
        JTable t = modelo.getVista().getTablaContactos();
        if (t == null) return;

        DefaultTableModel m = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        m.setColumnIdentifiers(new Object[]{"No.","Tipo","Información","Teléfono"});
        for (ContactoTmp c : bufferContactos) {
            String tipo = (c.tipoContacto==null)? "" : tcod2desc.getOrDefault(c.tipoContacto, String.valueOf(c.tipoContacto));
            m.addRow(new Object[]{ c.correlativo, tipo, nvl(c.info), nvl(c.telefono) });
        }
        t.setModel(m);
    }

    // ===================== Combos / caches =====================
    private void cargarCacheTiposContacto() {
        tcod2desc.clear(); tdesc2cod.clear();
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
        JComboBox<String> cb = modelo.getVista().getCmbTipoContacto();
        if (cb == null) return;

        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        m.addElement("-- Seleccione --");
        java.util.List<String> ds = new ArrayList<>(tdesc2cod.keySet());
        ds.sort(String::compareToIgnoreCase);
        for (String d : ds) m.addElement(d);
        cb.setModel(m);
    }

    private Integer leerTipoContactoDesdeUI() {
        JComboBox<String> cb = modelo.getVista().getCmbTipoContacto();
        if (cb == null) return null;
        Object sel = cb.getSelectedItem();
        if (sel == null) return null;
        String d = sel.toString().trim();
        if (d.equalsIgnoreCase("-- Seleccione --")) return null;
        return tdesc2cod.getOrDefault(d, null);
    }

    private void setComboTipoContactoByCode(Integer code) {
        if (code == null) return;
        JComboBox<String> cb = modelo.getVista().getCmbTipoContacto();
        if (cb == null) return;
        String desc = tcod2desc.get(code);
        if (desc == null) return;
        for (int i=0; i<cb.getItemCount(); i++) {
            if (desc.equals(cb.getItemAt(i))) { cb.setSelectedIndex(i); break; }
        }
    }

    // ===================== Util / UI =====================
    private void limpiarFormulario() {
        var v = modelo.getVista();

        // Proveedor
//        v.txtNIT.setText("");
        v.txtNombreProveedor.setText("");
        v.txtDireccionFiscal.setText("");
        v.txtTelefonoProveedor.setText("");
        v.txtBuscar.setText("");

        // Representante
        v.txtNitRepresentante.setText("");
        v.txtPrimerNombre.setText("");
        v.txtSegundoNombre.setText("");
        v.txtPrimerApellido.setText("");
        v.txtSegundoApellido.setText("");
        v.txtApellidoCasada.setText("");

        // Contactos
        v.txtInfoContacto.setText("");
        v.txtTelContacto.setText("");
        JComboBox<String> cb = modelo.getVista().getCmbTipoContacto();
        if (cb != null && cb.getItemCount() > 0) cb.setSelectedIndex(0);

        bufferContactos.clear();
        refrescarTablaContactos();
    }

    private String decideInfoParaBD(ContactoTmp c) {
        String d = (c.tipoContacto==null)? null : tcod2desc.get(c.tipoContacto);
        if (d != null && d.toUpperCase().contains("TEL")) return nvl(c.telefono);
        return nvl(c.info);
    }

    private void mensaje(String m, String t, int tipo) {
        JOptionPane.showMessageDialog(modelo.getVista(), m, t, tipo);
    }
    private static String nvl(String s){ return (s==null)? "" : s; }
    private static String safe(String s){ if (s==null) return null; String t=s.trim(); return t.isEmpty()? null : t; }
    private static String valStr(Object o){ return (o==null)? null : String.valueOf(o).trim(); }

    // ===================== MouseListener / iconos =====================
    @Override public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
//        if (src == btnNuevo) onNuevo();
//        else if (src == btnActualizar) onActualizar();
//        else if (src == btnEliminar) onEliminar();
//        else if (src == btnBuscar) onBuscar();
//        else if (src == btnLimpiar) onLimpiar();
//        else if (src == btnNuevoC) onNuevoContacto();
//        else if (src == btnActualizarC) onActualizarContacto();
//        else if (src == btnEliminarC) onEliminarContacto();
    }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), true); }
    @Override public void mouseExited (MouseEvent e) { cambiarIconoBoton((JPanel)e.getSource(), false); }
    @Override public void actionPerformed(ActionEvent e) { }

    private void inicializarIconos() {
        String ruta = "/com/umg/iconos/IconoBoton1.png";
        if (btnNuevo!=null)      iconosBotones.put(btnNuevo, ruta);
        if (btnActualizar!=null) iconosBotones.put(btnActualizar, ruta);
        if (btnEliminar!=null)   iconosBotones.put(btnEliminar, ruta);
        if (btnBuscar!=null)     iconosBotones.put(btnBuscar, ruta);
        if (btnLimpiar!=null)    iconosBotones.put(btnLimpiar, ruta);
        if (btnNuevoC!=null)      iconosBotones.put(btnNuevoC, ruta);
        if (btnActualizarC!=null) iconosBotones.put(btnActualizarC, ruta);
        if (btnEliminarC!=null)   iconosBotones.put(btnEliminarC, ruta);
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
        for (Component comp : boton.getComponents())
            if (comp instanceof JLabel lbl && nombre.equals(lbl.getName())) return lbl;
        return null;
    }
}
