package com.umg.implementacion;

import com.umg.modelo.ModeloCategoria;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarcaImp {
    private Conector con = Sesion.getConexion();
    private Sql sql = new Sql();

    //Método para obtener las categorias
    public List<ModeloCategoria> obtenerMarcas() {
        List<ModeloCategoria> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT codigo, descripcion FROM marca ORDER BY codigo"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloCategoria categoria = new ModeloCategoria();
                categoria.setCodigo(rs.getInt("codigo"));
                categoria.setDescripcion(rs.getString("descripcion"));
                lista.add(categoria);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerMarcas: " + e.getMessage());
        }
        return lista;
    }
}

