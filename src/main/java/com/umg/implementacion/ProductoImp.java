package com.umg.implementacion;

import com.umg.interfaces.IProducto;
import com.umg.modelo.ModeloCategoria;
import com.umg.modelo.ModeloMarcas;
import com.umg.modelo.ModeloMedidas;
import com.umg.modelo.ModeloProducto;
import com.umg.seguridad.Sesion;
import sql.Conector;

import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoImp implements IProducto {

    private final Conector con = Sesion.getConexion();

    @Override
    public DefaultTableModel listarProductos() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 🔒 ninguna celda editable
            }
        };
        model.setColumnIdentifiers(new Object[]{
                "Código", "Nombre", "Descripción", "Stock Mínimo",
                "Stock Actual", "Categoría", "Marca", "Medida", "Cantidad", "Estado"
        });

        try {
            String sql = "SELECT p.codigo, p.nombre, p.descripcion, p.stock_minimo, p.stock_actual, " +
                    "c.descripcion AS categoria, m.descripcion AS marca, me.descripcion AS medida, " +
                    "p.cantidad_medida, p.estado " +
                    "FROM producto p " +
                    "LEFT JOIN categoria c ON p.categoria = c.codigo " +
                    "LEFT JOIN marca m ON p.marca = m.marca " +
                    "LEFT JOIN medida me ON p.medida = me.codigo " +
                    "WHERE p.estado = 'A' " +
                    "ORDER BY p.codigo";

            PreparedStatement ps = con.preparar(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("stock_minimo"),
                        rs.getInt("stock_actual"),
                        rs.getString("categoria"),
                        rs.getString("marca"),
                        rs.getString("medida"),
                        rs.getInt("cantidad_medida"),
                        rs.getString("estado")
                });
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar productos: " + e.getMessage());
        }

        return model;
    }

    @Override
    public boolean insertar(ModeloProducto p) {
        try {
            PreparedStatement ps = con.preparar(
                    "INSERT INTO producto(nombre, descripcion, stock_minimo, stock_actual, estado, categoria, marca, medida, cantidad_medida) " +
                            "VALUES(?, ?, ?, ?, 'A', ?, ?, ?, ?)"
            );

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStockMinimo());
            ps.setInt(4, p.getStockActual());
            ps.setInt(5, p.getCategoria());
            ps.setString(6, p.getMarca());
            ps.setString(7, p.getMedida());
            ps.setDouble(8, p.getCantidad());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("❌ Error insertar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(ModeloProducto p) {
        try {
            PreparedStatement ps = con.preparar(
                    "UPDATE producto SET nombre=?, descripcion=?, stock_minimo=?, stock_actual=?, estado=?, " +
                            "categoria=?, marca=?, medida=?, cantidad_medida=? " +
                            "WHERE codigo=?"
            );

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStockMinimo());
            ps.setInt(4, p.getStockActual());
            ps.setString(5, p.getEstado());
            ps.setInt(6, p.getCategoria());
            ps.setString(7, p.getMarca());
            ps.setString(8, p.getMedida());
            ps.setDouble(9, p.getCantidad());
            ps.setInt(10, p.getCodigo());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("❌ Error actualizar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int codigo) {
        try {
            PreparedStatement ps = con.preparar(
                    "UPDATE producto SET estado='I' WHERE codigo=?"
            );
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("❌ Error cambiar estado a inactivo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ModeloProducto obtenerPorCodigo(int codigo) {
        ModeloProducto p = null;
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT * FROM producto WHERE codigo=? AND estado='A'"
            );
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
                p.setCategoria(rs.getInt("categoria"));
                p.setMarca(rs.getString("marca"));
                p.setMedida(rs.getString("medida"));
                p.setCantidad(rs.getInt("cantidad_medida"));
            }
        } catch (Exception e) {
            System.out.println("❌ Error obtenerPorCodigo: " + e.getMessage());
        }
        return p;
    }

    @Override
    public List<ModeloProducto> obtenerTodos() {
        List<ModeloProducto> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT * FROM producto WHERE estado='A' ORDER BY codigo"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloProducto p = new ModeloProducto();
                p.setCodigo(rs.getInt("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setEstado(rs.getString("estado"));
                p.setCategoria(rs.getInt("categoria"));
                p.setMarca(rs.getString("marca"));
                p.setMedida(rs.getString("medida"));
                p.setCantidad(rs.getInt("cantidad_medida"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("❌ Error obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Integer> obtenerCodigos() {
        List<Integer> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT codigo FROM producto WHERE estado='A' ORDER BY codigo"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getInt("codigo"));
        } catch (Exception e) {
            System.out.println("❌ Error obtenerCodigos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<String> obtenerNombres() {
        List<String> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT nombre FROM producto WHERE estado='A' ORDER BY nombre"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString("nombre"));
        } catch (Exception e) {
            System.out.println("❌ Error obtenerNombres: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<ModeloProducto> buscar(String texto) {
        List<ModeloProducto> lista = new ArrayList<>();
        try {
            String sql = "SELECT p.codigo, p.nombre, p.stock_actual, p.categoria, c.descripcion AS categoria " +
                    "FROM producto p " +
                    "LEFT JOIN categoria c ON p.categoria = c.codigo " +
                    "WHERE p.estado='A' AND (UPPER(p.nombre) LIKE ? OR UPPER(p.descripcion) LIKE ? OR TO_CHAR(p.codigo) LIKE ?) " +
                    "ORDER BY p.codigo";

            PreparedStatement ps = con.preparar(sql);
            ps.setString(1, "%" + texto.toUpperCase() + "%");
            ps.setString(2, "%" + texto.toUpperCase() + "%");
            ps.setString(3, "%" + texto + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloProducto p = new ModeloProducto();
                p.setCodigo(rs.getInt("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setCategoria(rs.getInt("categoria"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("❌ Error buscar producto: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Cargar categorías
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
            System.out.println("❌ Error obtenerCategorias: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Cargar marcas
    public List<ModeloMarcas> obtenerMarcas() {
        List<ModeloMarcas> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT marca, descripcion FROM marca ORDER BY marca"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloMarcas marca = new ModeloMarcas();
                marca.setMarca(rs.getString("marca"));
                marca.setDescripcion(rs.getString("descripcion"));
                lista.add(marca);
            }
        } catch (Exception e) {
            System.out.println("❌ Error obtenerMarcas: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Cargar medidas
    public List<ModeloMedidas> obtenerMedidas() {
        List<ModeloMedidas> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.preparar(
                    "SELECT codigo, descripcion FROM medida ORDER BY codigo"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModeloMedidas medida = new ModeloMedidas();
                medida.setCodigo(rs.getString("codigo"));
                medida.setDescripcion(rs.getString("descripcion"));
                lista.add(medida);
            }
        } catch (Exception e) {
            System.out.println("❌ Error obtenerMedidas: " + e.getMessage());
        }
        return lista;
    }

    public Double obtenerPrecioActual(int codigoProducto) {
        Double precio = null;
        try {
            String sql = "SELECT precio_venta " +
                    "FROM precio p " +
                    "WHERE p.fk_producto_codigo = ? " +
                    "  AND p.estado = 'A' " +
                    "  AND ((p.inicio_vigencia IS NULL OR SYSDATE >= p.inicio_vigencia) " +
                    "       AND (p.fin_vigencia IS NULL OR SYSDATE <= p.fin_vigencia)) " +
                    "ORDER BY p.inicio_vigencia DESC NULLS LAST";

            PreparedStatement ps = con.preparar(sql);
            ps.setInt(1, codigoProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                precio = rs.getDouble("precio_venta");
            }
        } catch (Exception e) {
            System.out.println("❌ Error obtenerPrecioActual: " + e.getMessage());
        }
        return precio;
    }
}
