package com.umg.implementacion;

import com.umg.interfaces.ICliente;
import com.umg.modelo.ModeloCliente;

import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.SystemColor.info;

public class ClienteImp implements ICliente {

    private final Conector con = Sesion.getConexion(); // credenciales gestionadas en Sesion
    private final Sql sql = new Sql();

    // ======================================================
    // CRUD CLIENTE
    // ======================================================

    @Override
    public boolean insertar(ModeloCliente c) {
        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_CLIENTE());
            ps.setString(1,  c.getNit());
            ps.setString(2,  c.getNombre1());
            ps.setString(3,  c.getNombre2());
            ps.setString(4,  c.getNombre3());
            ps.setString(5,  c.getApellido1());
            ps.setString(6,  c.getApellido2());
            ps.setString(7,  c.getApellidoCasada());
            if (c.getTipoCliente()==null) ps.setNull(8,  Types.INTEGER); else ps.setInt(8,  c.getTipoCliente());
            if (c.getLimiteCredito()==null) ps.setNull(9, Types.NUMERIC); else ps.setDouble(9, c.getLimiteCredito());
            ps.setString(10, c.getDireccion());
            ps.setString(11, c.getEstado() == null ? "A" : c.getEstado()); // por si viene null
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(ModeloCliente c) {
        try {
            PreparedStatement ps = con.preparar(sql.getACTUALIZAR_CLIENTE());
            ps.setString(1,  c.getNombre1());
            ps.setString(2,  c.getNombre2());
            ps.setString(3,  c.getNombre3());
            ps.setString(4,  c.getApellido1());
            ps.setString(5,  c.getApellido2());
            ps.setString(6,  c.getApellidoCasada());
            if (c.getTipoCliente()==null) ps.setNull(7,  Types.INTEGER); else ps.setInt(7,  c.getTipoCliente());
            if (c.getLimiteCredito()==null) ps.setNull(8, Types.NUMERIC); else ps.setDouble(8, c.getLimiteCredito());
            ps.setString(9,  c.getDireccion());
            ps.setString(10, c.getEstado() == null ? "A" : c.getEstado());
            ps.setString(11, c.getNit());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String nit) { // soft-delete
        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_CLIENTE_SOFT());
            ps.setString(1, nit);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error desactivar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ModeloCliente obtenerPorNit(String nit) {
        ModeloCliente c = null;
        try {
            PreparedStatement ps = con.preparar(sql.getCONSULTA_CLIENTE_POR_NIT());
            ps.setString(1, nit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = mapRowCliente(rs);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorNit: " + e.getMessage());
        }
        return c;
    }

    @Override
    public List<ModeloCliente> obtenerTodos() {
        List<ModeloCliente> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getCONSULTA_TODOS_CLIENTES_ACTIVOS());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRowCliente(rs));
        } catch (Exception e) {
            System.out.println("Error obtenerTodos clientes: " + e.getMessage());
        }
        return lista;
    }

    private ModeloCliente mapRowCliente(ResultSet rs) throws SQLException {
        ModeloCliente c = new ModeloCliente();
        c.setNit(rs.getString("nit"));
        c.setNombre1(rs.getString("nombre_1"));
        c.setNombre2(rs.getString("nombre_2"));
        c.setNombre3(rs.getString("nombre_3"));
        c.setApellido1(rs.getString("apellido_1"));
        c.setApellido2(rs.getString("apellido_2"));
        c.setApellidoCasada(rs.getString("apellido_casada"));

        c.setTipoCliente(getIntOrNull(rs, "tipo_cliente"));   // <--- CAMBIO

        Object lim = rs.getObject("limite_credito");          // NUMBER -> BigDecimal
        c.setLimiteCredito(lim == null ? null : ((Number) lim).doubleValue());

        c.setDireccion(rs.getString("direccion"));
        c.setEstado(rs.getString("estado"));
        return c;
    }

    // ======================================================
    // COMBOS / BÚSQUEDA
    // ======================================================

    @Override
    public List<String> obtenerNits() {
        List<String> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getOBTENER_NITS_CLIENTE());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString(1));
        } catch (Exception e) {
            System.out.println("Error obtenerNits: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<String> obtenerNombresCompletos() {
        List<String> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getOBTENER_NOMBRES_COMPLETOS_CLIENTE());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString(1));
        } catch (Exception e) {
            System.out.println("Error obtenerNombresCompletos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<ModeloCliente> buscar(String texto) {
        List<ModeloCliente> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getBUSCAR_CLIENTE());
            String like = "%" + (texto == null ? "" : texto.trim().toUpperCase()) + "%";
            ps.setString(1, like);
            ps.setString(2, like);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloCliente c = new ModeloCliente();
                c.setNit(rs.getString("nit"));
                c.setTipoCliente(getIntOrNull(rs, "tipo_cliente"));   // <--- CAMBIO
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error buscar clientes: " + e.getMessage());
        }
        return lista;
    }


    @Override
    public String decirHola() {
        return "";
    }

    // ======================================================
    // LISTADO PARA TABLA (si tu vista usa SELECT específico)
    // ======================================================
    public ResultSet listarClientesRS() {
        try {
            return con.preparar(sql.getLISTAR_CLIENTES()).executeQuery();
        } catch (Exception e) {
            System.out.println("Error listarClientesRS: " + e.getMessage());
            return null;
        }
    }

    // ======================================================
    // CONTACTOS DE CLIENTE
    // ======================================================

    /** Trae los contactos existentes de un NIT. */
    @Override
    public int nextIdContactoCliente() {
        try {
            PreparedStatement ps = con.preparar(sql.getNEXT_ID_CONTACTO_CLIENTE());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 1;
            }
        } catch (Exception e) {
            System.out.println("Error nextIdContactoCliente: " + e.getMessage());
            return 1;
        }
    }

    @Override
    public List<ModeloCliente> obtenerContactosPorCliente(String nit) {
        List<ModeloCliente> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getOBTENER_CONTACTOS_POR_CLIENTE());
            ps.setString(1, nit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ModeloCliente m = new ModeloCliente();
                    m.setIdentificacion(rs.getInt("identificacion"));
                    m.setCorrelativo(rs.getInt("correlativo_contacto"));
                    m.setTipoContacto((Integer) rs.getObject("tipo_contacto"));
                    m.setInfo(rs.getString("info_contacto"));
                    m.setTelefono(rs.getString("telefono"));
                    m.setNit(rs.getString("fk_cliente_nit"));
                    lista.add(m);
                }
            }
        } catch (Exception e) {
            System.out.println("Error obtenerContactosPorCliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean insertarContactoCliente(int identificacion,
                                           int correlativo,                // <--- se ignora (identity en BD)
                                           Integer tipoContacto,
                                           String infoContacto,
                                           String telefono,
                                           String nitCliente) {
        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_CONTACTO_CLIENTE());
            int i = 1;
            ps.setInt(i++, identificacion);                           // identificacion
            if (tipoContacto == null) ps.setNull(i++, Types.INTEGER); // tipo_contacto
            else                     ps.setInt(i++, tipoContacto);
            ps.setString(i++, infoContacto);                          // info_contacto
            ps.setString(i++, telefono);                              // telefono
            ps.setString(i++, nitCliente);                            // fk_cliente_nit
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertarContactoCliente: " + e.getMessage());
            return false;
        }
    }
    private Integer getIntOrNull(ResultSet rs, String col) throws SQLException {
        java.math.BigDecimal bd = rs.getBigDecimal(col);
        return (bd == null) ? null : bd.intValue();
    }



    /** Borra todos los contactos de un NIT (útil para actualizar por reemplazo). */
    @Override
    public boolean eliminarContactosPorCliente(String nit) {
        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_CONTACTOS_POR_CLIENTE());
            ps.setString(1, nit);          // ✅ SOLO 1 parámetro: fk_cliente_nit
            ps.executeUpdate();
            return true;                   // aunque afecte 0 filas, la ejecución fue correcta
        } catch (Exception e) {
            System.out.println("Error eliminarContactosPorCliente: " + e.getMessage());
            return false;
        }
    }

}
