package com.umg.implementacion;

import com.umg.interfaces.IRepresentantes;
import com.umg.modelo.ModeloRepresentates;
import com.umg.seguridad.Sesion;
import sql.Conector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepresentantesImp implements IRepresentantes {

    private final Conector con = Sesion.getConexion();

    // ======================================================
    // SQL REPRESENTANTE
    // ======================================================

    // ======================================================
// SQL REPRESENTANTE
// ======================================================

    private static final String INSERTAR_REPRESENTANTE =
            "INSERT INTO tellix.representante (" +
                    " nit_representante, fk_proveedor_nit, codigo," +
                    " nombre1, nombre2, apellido1, apellido2, apellido_casada" +
                    ") VALUES (?, ?, 1, ?, ?, ?, ?, ?)";

    private static final String ACTUALIZAR_REPRESENTANTE =
            "UPDATE tellix.representante SET " +
                    " nombre1 = ?, nombre2 = ?, apellido1 = ?, apellido2 = ?, apellido_casada = ? " +
                    "WHERE nit_representante = ? AND fk_proveedor_nit = ?";

    private static final String ELIMINAR_REPRESENTANTE =
            "DELETE FROM tellix.representante " +
                    "WHERE nit_representante = ? AND fk_proveedor_nit = ?";

    private static final String OBTENER_REPRESENTANTE =
            "SELECT nit_representante, fk_proveedor_nit, codigo," +
                    "       nombre1, nombre2, apellido1, apellido2, apellido_casada " +
                    "FROM tellix.representante " +
                    "WHERE nit_representante = ? AND fk_proveedor_nit = ?";


    // JOIN para tabla superior (Representantes con datos de proveedor)
    private static final String LISTAR_REP_ORDEN_NIT_PROV =
            "SELECT p.nit_proveedor, p.nombre AS nombre_proveedor, " +
                    "       r.nit_representante, " +
                    "       (r.nombre1 || ' ' || NVL(r.nombre2,'') || ' ' || r.apellido1 || ' ' || NVL(r.apellido2,'')) AS nombre_representante " +
                    "FROM tellix.proveedor p " +
                    "JOIN representante r ON r.fk_proveedor_nit = p.nit_proveedor " +
                    "ORDER BY p.nit_proveedor, r.nit_representante";

    private static final String LISTAR_REP_ORDEN_NOMBRE_PROV =
            "SELECT p.nit_proveedor, p.nombre AS nombre_proveedor, " +
                    "       r.nit_representante, " +
                    "       (r.nombre1 || ' ' || NVL(r.nombre2,'') || ' ' || r.apellido1 || ' ' || NVL(r.apellido2,'')) AS nombre_representante " +
                    "FROM tellix.proveedor p " +
                    "JOIN representante r ON r.fk_proveedor_nit = p.nit_proveedor " +
                    "ORDER BY p.nombre, r.nit_representante";

    private static final String BUSCAR_REPRESENTANTES =
            "SELECT p.nit_proveedor, p.nombre AS nombre_proveedor, " +
                    "       r.nit_representante, " +
                    "       (r.nombre1 || ' ' || NVL(r.nombre2,'') || ' ' || r.apellido1 || ' ' || NVL(r.apellido2,'')) AS nombre_representante " +
                    "FROM tellix.proveedor p " +
                    "JOIN representante r ON r.fk_proveedor_nit = p.nit_proveedor " +
                    "WHERE UPPER(p.nit_proveedor) LIKE ? " +
                    "   OR UPPER(p.nombre) LIKE ? " +
                    "   OR UPPER(r.nit_representante) LIKE ? " +
                    "   OR UPPER(r.nombre1 || ' ' || NVL(r.nombre2,'') || ' ' || r.apellido1 || ' ' || NVL(r.apellido2,'')) LIKE ? " +
                    "ORDER BY p.nit_proveedor, r.nit_representante";
    ;

    // ======================================================
    // SQL CONTACTOS REPRESENTANTE
    // ======================================================

    private static final String OBTENER_CONTACTOS_REPRESENTANTE =
            "SELECT c.correlativo_contacto, c.tipo_contacto, c.info_contacto, " +
                    "       t.descripcion AS desc_tipo " +
                    "FROM tellix.contacto_representante c " +
                    "LEFT JOIN tipo_contacto t ON t.codigo = c.tipo_contacto " +
                    "WHERE c.fk_representante = ? " +
                    "ORDER BY c.correlativo_contacto";

    private static final String INSERTAR_CONTACTO_REPRESENTANTE =
            // correlativo_contacto se asume identity/sequence, codigo lo fijo en 1
            "INSERT INTO tellix.contacto_representante (" +
                    " correlativo_contacto, codigo, info_contacto, tipo_contacto, fk_representante" +
                    ") VALUES (DEFAULT, 1, ?, ?, ?)";

    private static final String ACTUALIZAR_CONTACTO_REPRESENTANTE =
            "UPDATE tellix.contacto_representante SET " +
                    " info_contacto = ?, tipo_contacto = ? " +
                    "WHERE correlativo_contacto = ? AND fk_representante = ?";

    private static final String ELIMINAR_CONTACTO_REPRESENTANTE =
            "DELETE FROM tellix.contacto_representante " +
                    "WHERE correlativo_contacto = ? AND fk_representante = ?";

    private static final String ELIMINAR_CONTACTOS_REPRESENTANTE =
            "DELETE FROM tellix.contacto_representante " +
                    "WHERE fk_representante = ?";

    // ======================================================
    // IMPLEMENTACIÓN INTERFAZ
    // ======================================================

    // -------- REPRESENTANTE --------

    @Override
    public boolean insertarRepresentante(ModeloRepresentates rep) {
        try (PreparedStatement ps = con.preparar(INSERTAR_REPRESENTANTE)) {
            ps.setString(1, rep.getNitRepresentante());
            ps.setString(2, rep.getNitProveedor());
            ps.setString(3, rep.getPrimerNombre());
            ps.setString(4, rep.getSegundoNombre());
            ps.setString(5, rep.getPrimerApellido());
            ps.setString(6, rep.getSegundoApellido());
            ps.setString(7, rep.getApellidoCasada());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertarRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarRepresentante(ModeloRepresentates rep) {
        try (PreparedStatement ps = con.preparar(ACTUALIZAR_REPRESENTANTE)) {
            ps.setString(1, rep.getPrimerNombre());
            ps.setString(2, rep.getSegundoNombre());
            ps.setString(3, rep.getPrimerApellido());
            ps.setString(4, rep.getSegundoApellido());
            ps.setString(5, rep.getApellidoCasada());
            ps.setString(6, rep.getNitRepresentante());
            ps.setString(7, rep.getNitProveedor());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizarRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarRepresentante(String nitProveedor, String nitRepresentante) {
        try (PreparedStatement ps = con.preparar(ELIMINAR_REPRESENTANTE)) {
            ps.setString(1, nitRepresentante);
            ps.setString(2, nitProveedor);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminarRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ModeloRepresentates obtenerRepresentante(String nitProveedor, String nitRepresentante) {
        ModeloRepresentates rep = null;
        try (PreparedStatement ps = con.preparar(OBTENER_REPRESENTANTE)) {
            ps.setString(1, nitRepresentante);
            ps.setString(2, nitProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rep = mapRepresentante(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Error obtenerRepresentante: " + e.getMessage());
        }
        return rep;
    }

    @Override
    public List<RowRep> listarRepresentantesOrdenNitProveedor() {
        List<RowRep> lista = new ArrayList<>();
        try (PreparedStatement ps = con.preparar(LISTAR_REP_ORDEN_NIT_PROV);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRowRep(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listarRepresentantesOrdenNitProveedor: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<RowRep> listarRepresentantesOrdenNombreProveedor() {
        List<RowRep> lista = new ArrayList<>();
        try (PreparedStatement ps = con.preparar(LISTAR_REP_ORDEN_NOMBRE_PROV);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRowRep(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listarRepresentantesOrdenNombreProveedor: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<RowRep> buscarRepresentantes(String texto) {
        List<RowRep> lista = new ArrayList<>();
        String filtro = (texto == null ? "" : texto.trim()).toUpperCase();
        String like = "%" + filtro + "%";

        try (PreparedStatement ps = con.preparar(BUSCAR_REPRESENTANTES)) {
            ps.setString(1, like); // NIT proveedor
            ps.setString(2, like); // Nombre proveedor
            ps.setString(3, like); // NIT representante
            ps.setString(4, like); // Nombre representante
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRowRep(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Error buscarRepresentantes: " + e.getMessage());
        }
        return lista;
    }

    // -------- CONTACTOS REPRESENTANTE --------

    @Override
    public List<ContactoRep> obtenerContactosRepresentante(String nitRepresentante) {
        List<ContactoRep> lista = new ArrayList<>();
        try (PreparedStatement ps = con.preparar(OBTENER_CONTACTOS_REPRESENTANTE)) {
            ps.setString(1, nitRepresentante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContactoRep c = new ContactoRep();
                    c.correlativoContacto = rs.getInt("correlativo_contacto");
                    if (rs.wasNull()) c.correlativoContacto = null;
                    c.tipoContacto     = rs.getInt("tipo_contacto");
                    if (rs.wasNull()) c.tipoContacto = null;
                    c.infoContacto     = rs.getString("info_contacto");
                    c.descripcionTipo  = rs.getString("desc_tipo");
                    // aquí puedes decidir si es teléfono o info genérica
                    c.telefonoVisible  = c.infoContacto;
                    c.nitRepresentante = nitRepresentante;
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
        try (PreparedStatement ps = con.preparar(INSERTAR_CONTACTO_REPRESENTANTE)) {
            ps.setString(1, infoContacto);
            if (tipoContacto == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, tipoContacto);
            }
            ps.setString(3, nitRepresentante);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertarContactoRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarContactoRepresentante(Integer correlativoContacto,
                                                   String nitRepresentante,
                                                   Integer tipoContacto,
                                                   String infoContacto) {
        try (PreparedStatement ps = con.preparar(ACTUALIZAR_CONTACTO_REPRESENTANTE)) {
            ps.setString(1, infoContacto);
            if (tipoContacto == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, tipoContacto);
            }
            ps.setInt(3, correlativoContacto);
            ps.setString(4, nitRepresentante);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizarContactoRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarContactoRepresentante(Integer correlativoContacto, String nitRepresentante) {
        try (PreparedStatement ps = con.preparar(ELIMINAR_CONTACTO_REPRESENTANTE)) {
            ps.setInt(1, correlativoContacto);
            ps.setString(2, nitRepresentante);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminarContactoRepresentante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarContactosRepresentante(String nitRepresentante) {
        try (PreparedStatement ps = con.preparar(ELIMINAR_CONTACTOS_REPRESENTANTE)) {
            ps.setString(1, nitRepresentante);
            ps.executeUpdate(); // si borra 0 o más, está bien
            return true;
        } catch (Exception e) {
            System.out.println("Error eliminarContactosRepresentante: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // MAPPERS
    // ======================================================

    private ModeloRepresentates mapRepresentante(ResultSet rs) throws SQLException {
        ModeloRepresentates m = new ModeloRepresentates(null);
        m.setNitRepresentante(rs.getString("nit_representante"));
        m.setNitProveedor(rs.getString("fk_proveedor_nit"));  // <- este nombre exacto
        m.setPrimerNombre(rs.getString("nombre1"));
        m.setSegundoNombre(rs.getString("nombre2"));
        m.setPrimerApellido(rs.getString("apellido1"));
        m.setSegundoApellido(rs.getString("apellido2"));
        m.setApellidoCasada(rs.getString("apellido_casada"));
        return m;
    }


    private RowRep mapRowRep(ResultSet rs) throws SQLException {
        RowRep r = new RowRep();
        r.nitProveedor       = rs.getString("nit_proveedor");
        r.nombreProveedor    = rs.getString("nombre_proveedor");
        r.nitRepresentante   = rs.getString("nit_representante");
        r.nombreRepresentante= rs.getString("nombre_representante");
        return r;
    }
}
