package com.umg.implementacion;

import com.umg.interfaces.ITipoContacto;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoContactoImp implements ITipoContacto.Servicio {

    private final Conector con = Sesion.getConexion();
    private final Sql sql = new Sql();

    // ================== Servicio (público) ==================
    @Override
    public boolean crear(String descripcion) {
        try { return insertarDAO(descripcion) > 0; }
        catch (Exception e) { System.out.println("crear TipoContacto: " + e.getMessage()); return false; }
    }

    @Override
    public boolean actualizar(int codigo, String descripcion) {
        try { return actualizarDAO(codigo, descripcion) > 0; }
        catch (Exception e) { System.out.println("actualizar TipoContacto: " + e.getMessage()); return false; }
    }

    @Override
    public boolean eliminar(int codigo) {
        try { return eliminarDAO(codigo) > 0; }
        catch (Exception e) { System.out.println("eliminar TipoContacto: " + e.getMessage()); return false; }
    }

    @Override
    public ITipoContacto.Registro obtener(int codigo) {
        try { return obtenerPorCodigoDAO(codigo); }
        catch (Exception e) { System.out.println("obtener TipoContacto: " + e.getMessage()); return null; }
    }

    @Override
    public List<ITipoContacto.Registro> listarOrdenadoPor(String campo) {
        try { return listarOrdenadoPorDAO(campo); }
        catch (Exception e) { System.out.println("listar TiposContacto: " + e.getMessage()); return List.of(); }
    }

    @Override
    public List<ITipoContacto.Registro> buscar(String filtro) {
        try { return buscarPorDescripcionDAO(filtro); }
        catch (Exception e) { System.out.println("buscar TiposContacto: " + e.getMessage()); return List.of(); }
    }

    // ================== DAO (privado) ==================
    private int insertarDAO(String descripcion) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getINSERTAR_TIPO_CONTACTO());
        ps.setString(1, descripcion);
        return ps.executeUpdate();
    }

    private int actualizarDAO(int codigo, String descripcion) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getACTUALIZAR_TIPO_CONTACTO());
        ps.setString(1, descripcion);
        ps.setInt(2, codigo);
        return ps.executeUpdate();
    }

    private int eliminarDAO(int codigo) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getELIMINAR_TIPO_CONTACTO());
        ps.setInt(1, codigo);
        return ps.executeUpdate();
    }

    private ITipoContacto.Registro obtenerPorCodigoDAO(int codigo) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getOBTENER_TIPO_CONTACTO_POR_CODIGO());
        ps.setInt(1, codigo);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? map(rs) : null;
        }
    }

    private List<ITipoContacto.Registro> listarOrdenadoPorDAO(String campo) throws SQLException {
        String col = "descripcion".equalsIgnoreCase(campo) ? "descripcion" : "codigo";
        String q = String.format(sql.getLISTAR_TIPOS_CONTACTO_ORDENADO(), col);
        PreparedStatement ps = con.preparar(q);
        try (ResultSet rs = ps.executeQuery()) {
            List<ITipoContacto.Registro> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    private List<ITipoContacto.Registro> buscarPorDescripcionDAO(String filtro) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getBUSCAR_TIPO_CONTACTO());
        ps.setString(1, "%" + (filtro == null ? "" : filtro.trim()) + "%");
        try (ResultSet rs = ps.executeQuery()) {
            List<ITipoContacto.Registro> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    private ITipoContacto.Registro map(ResultSet rs) throws SQLException {
        // NUMBER -> BigDecimal -> int
        java.math.BigDecimal bd = rs.getBigDecimal("codigo");
        int cod = (bd == null) ? 0 : bd.intValue();
        String des = rs.getString("descripcion");
        return new ITipoContacto.Registro(cod, des);
    }
}
