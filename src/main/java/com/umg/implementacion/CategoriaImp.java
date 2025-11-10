package com.umg.implementacion;

import com.umg.interfaces.ICategoria;
import com.umg.modelo.ModeloCategoria;
import sql.Conector;
import sql.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaImp implements ICategoria {

    private Conector con = new Conector("tellix", "tellix123"); // usuario y contraseña
    private Sql sql = new Sql();

    @Override
    public boolean insertar(ModeloCategoria c) {
        if (!con.conectar()) return false;
        try {
            PreparedStatement ps = con.preparar(sql.getINSERTAR_CATEGORIA());
            ps.setInt(1, c.getCodigo());
            ps.setString(2, c.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar: " + e.getMessage());
            return false;
        } finally {
            con.desconectar();
        }
    }

    @Override
    public boolean actualizar(ModeloCategoria c) {
        if (!con.conectar()) return false;
        try {
            PreparedStatement ps = con.preparar(sql.getACTUALIZAR_CATEGORIA());
            ps.setString(1, c.getDescripcion());
            ps.setInt(2, c.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar: " + e.getMessage());
            return false;
        } finally {
            con.desconectar();
        }
    }

    @Override
    public boolean eliminar(int codigo) {
        if (!con.conectar()) return false;
        try {
            PreparedStatement ps = con.preparar(sql.getELIMINAR_CATEGORIA());
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar: " + e.getMessage());
            return false;
        } finally {
            con.desconectar();
        }
    }

    @Override
    public ModeloCategoria obtenerPorCodigo(int codigo) {
        ModeloCategoria c = null;
        if (!con.conectar()) return null;
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
            con.desconectar();
        }
        return c;
    }

    @Override
    public List<ModeloCategoria> obtenerTodos() {
        List<ModeloCategoria> lista = new ArrayList<>();
        if (!con.conectar()) return lista;
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
            con.desconectar();
        }
        return lista;
    }
}
