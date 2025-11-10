package com.umg.implementacion;

import com.umg.interfaces.ICategoria;
import com.umg.modelo.ModeloCategoria;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaImp implements ICategoria {

    private Conector con = Sesion.getConexion(); // usuario y contraseña
    private Sql sql = new Sql();

    @Override
    public boolean insertar(ModeloCategoria c) {

        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_CATEGORIA());
            ps.setInt(1, c.getCodigo());
            ps.setString(2, c.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar: " + e.getMessage());
            return false;
        } finally {

        }
    }

    @Override
    public boolean actualizar(ModeloCategoria c) {

        try {
            PreparedStatement ps = con.preparar(sql.getACTUALIZAR_CATEGORIA());
            ps.setString(1, c.getDescripcion());
            ps.setInt(2, c.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar: " + e.getMessage());
            return false;
        } finally {

        }
    }

    @Override
    public boolean eliminar(int codigo) {

        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_CATEGORIA());
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar: " + e.getMessage());
            return false;
        } finally {

        }
    }

    @Override
    public ModeloCategoria obtenerPorCodigo(int codigo) {
        ModeloCategoria c = null;

        try {
            PreparedStatement ps = con.preparar(sql.getCONSULTA_CATEGORIA_POR_CODIGO());
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new ModeloCategoria();
                c.setCodigo(rs.getInt("codigo"));
                c.setDescripcion(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorCodigo: " + e.getMessage());
        } finally {

        }
        return c;
    }

    @Override
    public List<ModeloCategoria> obtenerTodos() {
        List<ModeloCategoria> lista = new ArrayList<>();

        try {
            PreparedStatement ps = con.preparar(sql.getCONSULTA_TODAS_CATEGORIAS());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloCategoria c = new ModeloCategoria();
                c.setCodigo(rs.getInt("codigo"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerTodos: " + e.getMessage());
        } finally {

        }
        return lista;
    }

    @Override
    public List<Integer> obtenerCodigos() {
        List<Integer> lista = new ArrayList<>();

        try {
            String sqlQuery = "SELECT codigo FROM categoria ORDER BY codigo";
            PreparedStatement ps = con.preparar(sqlQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("codigo"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerCodigos: " + e.getMessage());
        } finally {

        }
        return lista;
    }

    @Override
    public List<String> obtenerDescripciones() {
        List<String> lista = new ArrayList<>();

        try {
            String sqlQuery = "SELECT descripcion FROM categoria ORDER BY descripcion";
            PreparedStatement ps = con.preparar(sqlQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerDescripciones: " + e.getMessage());
        } finally {

        }
        return lista;
    }

    @Override
    public List<ModeloCategoria> buscar(String texto) {
        List<ModeloCategoria> lista = new ArrayList<>();

        try {
            String sqlBuscar = "SELECT codigo, descripcion FROM categoria " +
                    "WHERE UPPER(descripcion) LIKE ? OR TO_CHAR(codigo) LIKE ?";
            PreparedStatement ps = con.preparar(sqlBuscar);
            ps.setString(1, "%" + texto.toUpperCase() + "%");
            ps.setString(2, "%" + texto + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloCategoria c = new ModeloCategoria(null);
                c.setCodigo(rs.getInt("codigo"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error buscar: " + e.getMessage());
        } finally {

        }
        return lista;
    }


}

