package com.umg.implementacion;

import com.umg.interfaces.IProducto;
import com.umg.modelo.ModeloCategoria;
import com.umg.modelo.ModeloProducto;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoImp implements IProducto {

    private Conector con = Sesion.getConexion();
    private Sql sql = new Sql();

    @Override
    public boolean insertar(ModeloProducto p) {
        try {
            PreparedStatement ps = con.preparar(
                    "INSERT INTO producto(codigo,nombre,descripcion,stock_minimo,stock_actual,estado,fk_categoria,fk_marca,fk_medida,cantidad_medida) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?)"
            );
            ps.setInt(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getStockMinimo());
            ps.setInt(5, p.getStockActual());
            ps.setString(6, p.getEstado());
            ps.setInt(7, p.getFkCategoria());
            ps.setString(8, p.getFkMarca());
            ps.setString(9, p.getFkMedida());
            ps.setDouble(10, p.getCantidadMedida());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(ModeloProducto p) {
        try {
            PreparedStatement ps = con.preparar(
                    "UPDATE producto SET nombre=?, descripcion=?, stock_minimo=?, stock_actual=?, estado=?, fk_categoria=?, fk_marca=?, fk_medida=?, cantidad_medida=? " +
                            "WHERE codigo=?"
            );
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStockMinimo());
            ps.setInt(4, p.getStockActual());
            ps.setString(5, p.getEstado());
            ps.setInt(6, p.getFkCategoria());
            ps.setString(7, p.getFkMarca());
            ps.setString(8, p.getFkMedida());
            ps.setDouble(9, p.getCantidadMedida());
            ps.setInt(10, p.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int codigo) {
        try {
            PreparedStatement ps = con.preparar("DELETE FROM producto WHERE codigo=?");
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ModeloProducto obtenerPorCodigo(int codigo) {
        ModeloProducto p = null;
        try {
            PreparedStatement ps = con.preparar("SELECT * FROM producto WHERE codigo=?");
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new ModeloProducto();
                p.setCodigo(rs.getInt("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setEstado(rs.getString("estado"));
                p.setFkCategoria(rs.getInt("fk_categoria"));
                p.setFkMarca(rs.getString("fk_marca"));
                p.setFkMedida(rs.getString("fk_medida"));
                p.setCantidadMedida(rs.getDouble("cantidad_medida"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorCodigo: " + e.getMessage());
        }
        return p;
    }

    @Override
    public List<ModeloProducto> obtenerTodos() {
        List<ModeloProducto> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar("SELECT * FROM producto");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloProducto p = new ModeloProducto();
                p.setCodigo(rs.getInt("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setEstado(rs.getString("estado"));
                p.setFkCategoria(rs.getInt("fk_categoria"));
                p.setFkMarca(rs.getString("fk_marca"));
                p.setFkMedida(rs.getString("fk_medida"));
                p.setCantidadMedida(rs.getDouble("cantidad_medida"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Integer> obtenerCodigos() {
        List<Integer> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar("SELECT codigo FROM producto ORDER BY codigo");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getInt("codigo"));
        } catch (Exception e) {
            System.out.println("Error obtenerCodigos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<String> obtenerNombres() {
        List<String> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar("SELECT nombre FROM producto ORDER BY nombre");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString("nombre"));
        } catch (Exception e) {
            System.out.println("Error obtenerNombres: " + e.getMessage());
        }
        return lista;
    }


    @Override
    public List<ModeloProducto> buscar(String texto) {
        List<ModeloProducto> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT * FROM producto WHERE UPPER(nombre) LIKE ? OR UPPER(descripcion) LIKE ? OR TO_CHAR(codigo) LIKE ?"
            );
            ps.setString(1, "%" + texto.toUpperCase() + "%");
            ps.setString(2, "%" + texto.toUpperCase() + "%");
            ps.setString(3, "%" + texto + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloProducto p = new ModeloProducto();
                p.setCodigo(rs.getInt("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setEstado(rs.getString("estado"));
                p.setFkCategoria(rs.getInt("fk_categoria"));
                p.setFkMarca(rs.getString("fk_marca"));
                p.setFkMedida(rs.getString("fk_medida"));
                p.setCantidadMedida(rs.getDouble("cantidad_medida"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error buscar producto: " + e.getMessage());
        }
        return lista;
    }

    // Método para obtener todas las categorías
    public List<ModeloCategoria> obtenerCategorias() {
        List<ModeloCategoria> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT codigo, descripcion FROM categoria ORDER BY codigo"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloCategoria categoria = new ModeloCategoria();
                categoria.setCodigo(rs.getInt("codigo"));
                categoria.setDescripcion(rs.getString("descripcion"));
                lista.add(categoria);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerCategorias: " + e.getMessage());
        }
        return lista;
    }
}

