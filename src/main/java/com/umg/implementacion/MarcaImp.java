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
    private Conector con = Sesion.getConexion();
    private Sql sql = new Sql();

    //Método para obtener las categorias
    public List<ModeloMarcas> obtenerMarcas() {
        List<ModeloMarcas> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT marca, descripcion FROM marca ORDER BY marca"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloMarcas Marcas = new ModeloMarcas();
                Marcas.setMarcas(rs.getInt("marca"));
                Marcas.setDescripcion(rs.getString("descripcion"));
                lista.add(Marcas);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerMarcas: " + e.getMessage());
        }
        return lista;
    }
}

