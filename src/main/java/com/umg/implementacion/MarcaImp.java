package com.umg.implementacion;

import com.umg.modelo.ModeloMarcas;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarcaImp {

    private Conector con = Sesion.getConexion(); // conexión activa
    private Sql sql = new Sql();

    // Insertar nueva marca
    public boolean insertar(ModeloMarcas m) {
        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_MARCA());
            ps.setString(1, m.getMarca());
            ps.setString(2, m.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar marca: " + e.getMessage());
            return false;
        }
    }

    // Actualizar marca existente
    public boolean actualizar(ModeloMarcas m) {
        try {
            PreparedStatement ps = con.preparar(sql.getACTUALIZAR_MARCA());
            ps.setString(1, m.getDescripcion());
            ps.setString(2, m.getMarca());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar marca: " + e.getMessage());
            return false;
        }
    }

    // Eliminar marca por código
    public boolean eliminar(String marca) {
        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_MARCA());
            ps.setString(1, marca);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar marca: " + e.getMessage());
            return false;
        }
    }

    // Obtener marca por código
    public ModeloMarcas obtenerPorCodigo(String marca) {
        ModeloMarcas m = null;
        try {
            PreparedStatement ps = con.preparar(sql.getCONSULTA_MARCA_POR_CODIGO());
            ps.setString(1, marca);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m = new ModeloMarcas();
                m.setMarca(rs.getString("marca"));
                m.setDescripcion(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorCodigo marca: " + e.getMessage());
        }
        return m;
    }

    // Obtener todas las marcas
    public List<ModeloMarcas> obtenerTodos() {
        List<ModeloMarcas> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(sql.getCONSULTA_TODAS_MARCAS());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloMarcas m = new ModeloMarcas();
                m.setMarca(rs.getString("marca"));
                m.setDescripcion(rs.getString("descripcion"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerTodos marcas: " + e.getMessage());
        }
        return lista;
    }

    // Obtener solo códigos de marcas
    public List<Integer> obtenerCodigos() {
        List<Integer> lista = new ArrayList<>();
        try {
            String sqlQuery = "SELECT marca FROM marca ORDER BY marca";
            PreparedStatement ps = con.preparar(sqlQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("marca"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerCodigos marcas: " + e.getMessage());
        }
        return lista;
    }

    // Obtener solo descripciones de marcas
    public List<String> obtenerDescripciones() {
        List<String> lista = new ArrayList<>();
        try {
            String sqlQuery = "SELECT descripcion FROM marca ORDER BY descripcion";
            PreparedStatement ps = con.preparar(sqlQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerDescripciones marcas: " + e.getMessage());
        }
        return lista;
    }

    // Buscar marcas por texto
    public List<ModeloMarcas> buscar(String texto) {
        List<ModeloMarcas> lista = new ArrayList<>();
        try {
            String sqlBuscar = "SELECT marca, descripcion FROM marca " +
                    "WHERE UPPER(descripcion) LIKE ? OR marca LIKE ?";
            PreparedStatement ps = con.preparar(sqlBuscar);
            ps.setString(1, "%" + texto.toUpperCase() + "%");
            ps.setString(2, "%" + texto + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloMarcas m = new ModeloMarcas();
                m.setMarca(rs.getString("marca"));
                m.setDescripcion(rs.getString("descripcion"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error buscar marcas: " + e.getMessage());
        }
        return lista;
    }
}