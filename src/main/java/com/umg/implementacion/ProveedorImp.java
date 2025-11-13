package com.umg.implementacion;

import com.umg.interfaces.IProveedor;
import com.umg.modelo.ModeloProveedores;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorImp implements IProveedor {

    private final Conector con = Sesion.getConexion();
    private final Sql sql = new Sql();

    // ======================================================
    // CRUD PROVEEDOR
    // ======================================================

    @Override
    public boolean insertarProveedor(String nitProveedor, String nombre, String direccionFiscal, String telefono) {
        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_PROVEEDOR());
            ps.setString(1, nitProveedor);
            ps.setString(2, nombre);
            ps.setString(3, direccionFiscal);
            ps.setString(4, telefono);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertarProveedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarProveedor(String nitProveedor, String nombre, String direccionFiscal, String telefono) {
        try {
            PreparedStatement ps = con.preparar(sql.getACTUALIZAR_PROVEEDOR());
            ps.setString(1, nombre);
            ps.setString(2, direccionFiscal);
            ps.setString(3, telefono);
            ps.setString(4, nitProveedor);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizarProveedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarProveedor(String nitProveedor) {
        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_PROVEEDOR());
            ps.setString(1, nitProveedor);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminarProveedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ModeloProveedores obtenerProveedorPorNit(String nitProveedor) {
        ModeloProveedores p = null;
        try {
            PreparedStatement ps = con.preparar(sql.getCONSULTAR_PROV_DETALLE_POR_NIT());
            ps.setString(1, nitProveedor); // subquery
            ps.setString(2, nitProveedor); // where principal
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) p = mapProveedorDetalle(rs);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerProveedorPorNit: " + e.getMessage());
        }
        return p;
    }
    // Nuevo mapper con columnas del JOIN
    private ModeloProveedores mapProveedorDetalle(ResultSet rs) throws SQLException {
        ModeloProveedores p = new ModeloProveedores();
        p.setNitProveedor(rs.getString("nit_proveedor"));
        p.setNombreProveedor(rs.getString("nombre_proveedor"));
        p.setDireccionFiscal(rs.getString("direccion_fiscal"));
        p.setTelefonoProveedor(rs.getString("telefono"));

        // ---- representante (puede venir null si no existe) ----
        p.setNitRepresentante(rs.getString("nit_representante"));
        p.setNombre1(rs.getString("nombre1"));
        p.setNombre2(rs.getString("nombre2"));
        p.setApellido1(rs.getString("apellido1"));
        p.setApellido2(rs.getString("apellido2"));
        p.setApellidoCasada(rs.getString("apellido_casada"));
        return p;
    }

    // ======================================================
    // LISTADOS / BÚSQUEDA
    // ======================================================

    @Override
    public List<RowProv> listarProveedoresConRepresentante() {
        List<RowProv> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getLISTAR_PROV_CON_REP());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRowProv(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listarProveedoresConRepresentante: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<RowProv> buscarProveedores(String texto) {
        List<RowProv> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getBUSCAR_PROV_CON_REP());
            String like = "%" + (texto == null ? "" : texto.trim().toUpperCase()) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRowProv(rs));
            }
        } catch (Exception e) {
            System.out.println("Error buscarProveedores: " + e.getMessage());
        }
        return lista;
    }

    // ======================================================
    // COMBOS / RÁPIDOS
    // ======================================================

    @Override
    public List<String> obtenerNitsProveedor() {
        List<String> out = new ArrayList<>();
        final String Q = "SELECT nit_proveedor FROM proveedor ORDER BY nit_proveedor";
        try {
            PreparedStatement ps = con.preparar(Q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerNitsProveedor: " + e.getMessage());
        }
        return out;
    }

    @Override
    public List<String> obtenerNombresProveedor() {
        List<String> out = new ArrayList<>();
        final String Q = "SELECT nombre FROM proveedor ORDER BY nombre";
        try {
            PreparedStatement ps = con.preparar(Q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerNombresProveedor: " + e.getMessage());
        }
        return out;
    }

    @Override
    public String decirHola() { return ""; }

    // ======================================================
    // REPRESENTANTE
    // ======================================================

    @Override
    public int nextCodigoRepresentante(String nitProveedor) {
        try {
            PreparedStatement ps = con.preparar(sql.getNEXT_CODIGO_REPRESENTANTE());
            ps.setString(1, nitProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 1;
            }
        } catch (Exception e) {
            System.out.println("Error nextCodigoRepresentante: " + e.getMessage());
            return 1;
        }
    }

    @Override
    public boolean insertarRepresentante(String nitRepresentante,
                                         String fkProveedorNit,
                                         int codigo,
                                         String nombre1,
                                         String nombre2,
                                         String apellido1,
                                         String apellido2,
                                         String apellidoCasada) {
        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_REPRESENTANTE());
            int i = 1;
            ps.setString(i++, nitRepresentante);
            ps.setString(i++, fkProveedorNit);
            ps.setInt(i++,    codigo);
            ps.setString(i++, nombre1);
            ps.setString(i++, nombre2);
            ps.setString(i++, apellido1);
            ps.setString(i++, apellido2);
            ps.setString(i++, apellidoCasada);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertarRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarRepresentante(String nitRepresentante,
                                           String fkProveedorNit,
                                           int codigo,
                                           String nombre1,
                                           String nombre2,
                                           String apellido1,
                                           String apellido2,
                                           String apellidoCasada) {
        try {
            PreparedStatement ps = con.preparar(sql.getACTUALIZAR_REPRESENTANTE());
            int i = 1;
            ps.setString(i++, nombre1);
            ps.setString(i++, nombre2);
            ps.setString(i++, apellido1);
            ps.setString(i++, apellido2);
            ps.setString(i++, apellidoCasada);
            ps.setString(i++, nitRepresentante);
            ps.setString(i++, fkProveedorNit);
            ps.setInt(i++,    codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizarRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarRepresentantesDeProveedor(String nitProveedor) {
        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_REPRESENTANTES_DE_PROV());
            ps.setString(1, nitProveedor);
            ps.executeUpdate(); // aunque borre 0, OK
            return true;
        } catch (Exception e) {
            System.out.println("Error eliminarRepresentantesDeProveedor: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // CONTACTOS DE REPRESENTANTE
    // ======================================================

    @Override
    public List<ContactoRep> obtenerContactosRepresentante(String nitRepresentante) {
        List<ContactoRep> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getOBTENER_CONTACTOS_REPRESENTANTE());
            ps.setString(1, nitRepresentante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContactoRep c = new ContactoRep();
                    c.correlativoContacto = getIntOrNull(rs, "correlativo_contacto");
                    c.tipoContacto        = getIntOrNull(rs, "tipo_contacto");
                    c.infoContacto        = rs.getString("info_contacto");
                    c.fkRepresentante     = nitRepresentante;
                    lista.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println("Error obtenerContactosRepresentante: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean insertarContactoRepresentante(String nitRepresentante,
                                                 Integer tipoContacto,
                                                 String infoContacto) {
        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_CONTACTO_REPRESENTANTE());
            int i = 1;
            // INSERT ... (correlativo_contacto, codigo, info_contacto, tipo_contacto, fk_representante)
            // VALUES (DEFAULT, 1, ?, ?, ?)
            ps.setString(i++, infoContacto);
            if (tipoContacto == null) ps.setNull(i++, Types.INTEGER);
            else                      ps.setInt(i++,  tipoContacto);
            ps.setString(i++, nitRepresentante);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertarContactoRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarContactosRepresentante(String nitRepresentante) {
        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_CONTACTOS_REPRESENTANTE());
            ps.setString(1, nitRepresentante);
            ps.executeUpdate(); // aunque 0 filas
            return true;
        } catch (Exception e) {
            System.out.println("Error eliminarContactosRepresentante: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // MAPPERS
    // ======================================================

    private IProveedor.RowProv mapRowProv(ResultSet rs) throws SQLException {
        IProveedor.RowProv r = new IProveedor.RowProv();
        r.nitProveedor       = rs.getString("nit_proveedor");
        r.nombreProveedor    = rs.getString("nombre_proveedor");
        r.nitRepresentante   = rs.getString("nit_representante");
        r.nombreRepresentante= rs.getString("nombre_representante");
        r.direccionFiscal    = rs.getString("direccion_fiscal");
        r.telefono           = rs.getString("telefono");
        return r;
    }

    private ModeloProveedores mapProveedor(ResultSet rs) throws SQLException {
        ModeloProveedores p = new ModeloProveedores();
        p.setNitProveedor(rs.getString("nit_proveedor"));
        p.setNombreProveedor(rs.getString("nombre"));
        p.setDireccionFiscal(rs.getString("direccion_fiscal"));
        p.setTelefonoProveedor(rs.getString("telefono"));
        // estado opcional si existe en tu tabla
        return p;
    }

    // ======================================================
    // Helpers
    // ======================================================

    private Integer getIntOrNull(ResultSet rs, String col) throws SQLException {
        java.math.BigDecimal bd = rs.getBigDecimal(col);
        return (bd == null) ? null : bd.intValue();
    }
}
