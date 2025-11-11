package com.umg.implementacion;

import com.umg.modelo.ModeloMedidas;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedidaImp {

    private Conector con = Sesion.getConexion();
    private Sql sql = new Sql();

    public boolean insertar(ModeloMedidas m) {
        try {
            PreparedStatement ps = con.preparar("INSERT INTO medida (codigo, descripcion) VALUES (?, ?)");
            ps.setString(1, m.getCodigo());
            ps.setString(2, m.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar medida: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(ModeloMedidas m) {
        try {
            PreparedStatement ps = con.preparar("UPDATE medida SET descripcion=? WHERE codigo=?");
            ps.setString(1, m.getDescripcion());
            ps.setString(2, m.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar medida: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigo) {
        try {
            PreparedStatement ps = con.preparar("DELETE FROM medida WHERE codigo=?");
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar medida: " + e.getMessage());
            return false;
        }
    }

    public ModeloMedidas obtenerPorCodigo(String codigo) {
        ModeloMedidas m = null;
        try {
            PreparedStatement ps = con.preparar("SELECT codigo, descripcion FROM medida WHERE codigo=?");
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m = new ModeloMedidas();
                m.setCodigo(rs.getString("codigo"));
                m.setDescripcion(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorCodigo medida: " + e.getMessage());
        }
        return m;
    }

    public List<ModeloMedidas> obtenerTodos() {
        List<ModeloMedidas> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar("SELECT codigo, descripcion FROM medida ORDER BY codigo");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloMedidas m = new ModeloMedidas();
                m.setCodigo(rs.getString("codigo"));
                m.setDescripcion(rs.getString("descripcion"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerTodos medidas: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerCodigos() {
        List<String> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar("SELECT codigo FROM medida ORDER BY codigo");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("codigo"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerCodigos medidas: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerDescripciones() {
        List<String> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar("SELECT descripcion FROM medida ORDER BY descripcion");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerDescripciones medidas: " + e.getMessage());
        }
        return lista;
    }

    public List<ModeloMedidas> buscar(String texto) {
        List<ModeloMedidas> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar("SELECT codigo, descripcion FROM medida WHERE UPPER(descripcion) LIKE ? OR codigo LIKE ?");
            ps.setString(1, "%" + texto.toUpperCase() + "%");
            ps.setString(2, "%" + texto + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloMedidas m = new ModeloMedidas();
                m.setCodigo(rs.getString("codigo"));
                m.setDescripcion(rs.getString("descripcion"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error buscar medidas: " + e.getMessage());
        }
        return lista;
    }
}
