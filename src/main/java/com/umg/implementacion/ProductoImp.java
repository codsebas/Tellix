package com.umg.implementacion;

import com.umg.interfaces.IProducto;
import com.umg.modelo.ModeloProducto;
import sql.Conector;
import sql.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoImp implements IProducto {
    private Conector con = new Conector();
    private Sql sql = new Sql(); // usamos tu clase Sql

    @Override
    public boolean insertar(ModeloProducto p) {
        try {
            con.conectar();
            PreparedStatement ps = con.preparar(sql.getINSERTAR_PRODUCTO());

            ps.setInt(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getStockMinimo());
            ps.setInt(5, p.getStockActual());
            ps.setInt(6, 1); // estado activo
            ps.setString(7, p.getCategoria());
            ps.setString(8, p.getMarca());
            ps.setString(9, p.getMedida());
            ps.setDouble(10, p.getCantidad());

            int res = ps.executeUpdate();
            return res > 0;

        } catch (Exception e) {
            System.out.println("Error insertar: " + e.getMessage());
            return false;
        } finally {
            con.desconectar();
        }
    }

    @Override
    public boolean actualizar(ModeloProducto p) {
        try {
            con.conectar();
            PreparedStatement ps = con.preparar(sql.getACTUALIZAR_PRODUCTO());

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStockMinimo());
            ps.setInt(4, p.getStockActual());
            ps.setInt(5, 1); // estado activo
            ps.setString(6, p.getCategoria());
            ps.setString(7, p.getMarca());
            ps.setString(8, p.getMedida());
            ps.setDouble(9, p.getCantidad());
            ps.setInt(10, p.getCodigo());

            int res = ps.executeUpdate();
            return res > 0;

        } catch (Exception e) {
            System.out.println("Error actualizar: " + e.getMessage());
            return false;
        } finally {
            con.desconectar();
        }
    }

    @Override
    public boolean eliminar(int codigo) {
        try {
            con.conectar();
            PreparedStatement ps = con.preparar(sql.getELIMINAR_PRODUCTO());
            ps.setInt(1, codigo);
            int res = ps.executeUpdate();
            return res > 0;

        } catch (Exception e) {
            System.out.println("Error eliminar: " + e.getMessage());
            return false;
        } finally {
            con.desconectar();
        }
    }

    @Override
    public ModeloProducto obtenerPorCodigo(int codigo) {
        ModeloProducto p = null;
        try {
            con.conectar();
            PreparedStatement ps = con.preparar(sql.getCONSULTA_PRODUCTO_POR_CODIGO());
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new ModeloProducto();
                p.setCodigo(rs.getInt("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setPrecio(rs.getDouble("cantidad_medida")); // si tu precio está en cantidad_medida
                p.setCategoria(rs.getString("fk_categoria"));
                p.setMarca(rs.getString("fk_marca"));
                p.setMedida(rs.getString("fk_medida"));
                p.setCantidad(rs.getDouble("cantidad_medida"));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorCodigo: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return p;
    }

    @Override
    public List<ModeloProducto> obtenerTodos() {
        List<ModeloProducto> lista = new ArrayList<>();
        try {
            con.conectar();
            PreparedStatement ps = con.preparar(sql.getCONSULTA_TODOS_PRODUCTOS());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ModeloProducto p = new ModeloProducto();
                p.setCodigo(rs.getInt("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setPrecio(rs.getDouble("cantidad_medida"));
                p.setCategoria(rs.getString("fk_categoria"));
                p.setMarca(rs.getString("fk_marca"));
                p.setMedida(rs.getString("fk_medida"));
                p.setCantidad(rs.getDouble("cantidad_medida"));
                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error obtenerTodos: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return lista;
    }
}
