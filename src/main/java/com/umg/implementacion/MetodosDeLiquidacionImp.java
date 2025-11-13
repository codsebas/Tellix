package com.umg.implementacion;

import com.umg.interfaces.IMetodosDeLiquidacion;
import com.umg.modelo.ModeloMetodosDeLiquidacion;
import com.umg.seguridad.Sesion;
import sql.Conector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodosDeLiquidacionImp implements IMetodosDeLiquidacion {

    private final Conector con = Sesion.getConexion();

    // ===========================
    // SQL
    // ===========================
    private static final String INSERTAR =
            "INSERT INTO tellix.metodo_liquidacion (descripcion) " +
                    "VALUES (?)";

    private static final String ACTUALIZAR =
            "UPDATE tellix.metodo_liquidacion " +
                    "SET descripcion = ? " +
                    "WHERE codigo = ?";

    private static final String ELIMINAR =
            "DELETE FROM tellix.metodo_liquidacion " +
                    "WHERE codigo = ?";

    private static final String LISTAR_BASE =
            "SELECT codigo, descripcion " +
                    "FROM tellix.metodo_liquidacion ";

    // ===========================
    // Helpers
    // ===========================
    private String normalizarOrden(String campo) {
        if (campo == null) return "codigo";
        campo = campo.trim().toUpperCase();
        if (campo.startsWith("DESC")) return "descripcion";
        return "codigo";
    }

    private RowMetodo mapRow(ResultSet rs) throws SQLException {
        return new RowMetodo(
                rs.getInt("codigo"),
                rs.getString("descripcion")
        );
    }

    // ===========================
    // CRUD
    // ===========================

    @Override
    public boolean insertar(ModeloMetodosDeLiquidacion m) {
        try (PreparedStatement ps = con.preparar(INSERTAR)) {
            ps.setString(1, m.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar metodo_liquidacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(ModeloMetodosDeLiquidacion m) {
        try (PreparedStatement ps = con.preparar(ACTUALIZAR)) {
            ps.setString(1, m.getDescripcion());
            ps.setInt(2, m.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar metodo_liquidacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int codigo) {
        try (PreparedStatement ps = con.preparar(ELIMINAR)) {
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar metodo_liquidacion: " + e.getMessage());
            return false;
        }
    }

    // ===========================
    // LISTAR / BUSCAR
    // ===========================

    @Override
    public List<RowMetodo> listarOrdenadoPor(String campoOrden) {
        List<RowMetodo> lista = new ArrayList<>();
        String orden = normalizarOrden(campoOrden);

        String sql = LISTAR_BASE + "ORDER BY " + orden;

        try (PreparedStatement ps = con.preparar(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listar metodo_liquidacion: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<RowMetodo> buscar(String filtro, String campoOrden) {
        List<RowMetodo> lista = new ArrayList<>();
        String orden = normalizarOrden(campoOrden);

        if (filtro == null) filtro = "";
        String like = "%" + filtro.trim().toUpperCase() + "%";

        String sql =
                LISTAR_BASE +
                        "WHERE TO_CHAR(codigo) LIKE ? " +
                        "   OR UPPER(descripcion) LIKE ? " +
                        "ORDER BY " + orden;

        try (PreparedStatement ps = con.preparar(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Error buscar metodo_liquidacion: " + e.getMessage());
        }

        return lista;
    }
}
