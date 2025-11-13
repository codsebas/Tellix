package com.umg.implementacion;

import com.umg.interfaces.IProveedor;
import com.umg.modelo.ModeloProveedores;
import com.umg.seguridad.Sesion;
import sql.Conector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorImp implements IProveedor {

    private final Conector con = Sesion.getConexion();

    // ======================================================
    //  SQL PROPIO (sin clase Sql)
    // ======================================================

    private static final String INSERTAR_PROVEEDOR =
            "INSERT INTO tellix.proveedor (nit_proveedor, nombre, direccion_fiscal, telefono) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String ACTUALIZAR_PROVEEDOR =
            "UPDATE tellix.proveedor " +
                    "SET nombre = ?, direccion_fiscal = ?, telefono = ? " +
                    "WHERE nit_proveedor = ?";

    private static final String ELIMINAR_PROVEEDOR =
            "DELETE FROM tellix.proveedor " +
                    "WHERE nit_proveedor = ?";

    private static final String OBTENER_PROVEEDOR_POR_NIT =
            "SELECT nit_proveedor, nombre, direccion_fiscal, telefono " +
                    "FROM tellix.proveedor " +
                    "WHERE nit_proveedor = ?";

    // Listar ordenado por NIT
    private static final String LISTAR_PROV_ORDEN_NIT =
            "SELECT nit_proveedor, nombre, direccion_fiscal, telefono " +
                    "FROM tellix.proveedor " +
                    "ORDER BY nit_proveedor";

    // Listar ordenado por Nombre
    private static final String LISTAR_PROV_ORDEN_NOMBRE =
            "SELECT nit_proveedor, nombre, direccion_fiscal, telefono " +
                    "FROM tellix.proveedor " +
                    "ORDER BY nombre";

    // Buscar por NIT / Nombre
    private static final String BUSCAR_PROV =
            "SELECT nit_proveedor, nombre, direccion_fiscal, telefono " +
                    "FROM tellix.proveedor " +
                    "WHERE UPPER(nit_proveedor) LIKE ? " +
                    "   OR UPPER(nombre)       LIKE ? " +
                    "ORDER BY nit_proveedor";

    // ======================================================
    // CRUD PROVEEDOR
    // ======================================================

    @Override
    public boolean insertarProveedor(ModeloProveedores proveedor) {
        try (PreparedStatement ps = con.preparar(INSERTAR_PROVEEDOR)) {
            ps.setString(1, proveedor.getNitProveedor());
            ps.setString(2, proveedor.getNombreProveedor());
            ps.setString(3, proveedor.getDireccionFiscal());
            ps.setString(4, proveedor.getTelefonoProveedor());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertarProveedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarProveedor(ModeloProveedores proveedor) {
        try (PreparedStatement ps = con.preparar(ACTUALIZAR_PROVEEDOR)) {
            ps.setString(1, proveedor.getNombreProveedor());
            ps.setString(2, proveedor.getDireccionFiscal());
            ps.setString(3, proveedor.getTelefonoProveedor());
            ps.setString(4, proveedor.getNitProveedor());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizarProveedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarProveedor(String nitProveedor) {
        try (PreparedStatement ps = con.preparar(ELIMINAR_PROVEEDOR)) {
            ps.setString(1, nitProveedor);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminarProveedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ModeloProveedores obtenerProveedorPorNit(String nitProveedor) {
        ModeloProveedores p = null;
        try (PreparedStatement ps = con.preparar(OBTENER_PROVEEDOR_POR_NIT)) {
            ps.setString(1, nitProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapProveedor(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Error obtenerProveedorPorNit: " + e.getMessage());
        }
        return p;
    }

    // ======================================================
    // LISTADOS / BÚSQUEDA
    // ======================================================

    @Override
    public List<RowProv> listarProveedoresOrdenNit() {
        List<RowProv> lista = new ArrayList<>();
        try (PreparedStatement ps = con.preparar(LISTAR_PROV_ORDEN_NIT);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRowProv(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listarProveedoresOrdenNit: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<RowProv> listarProveedoresOrdenNombre() {
        List<RowProv> lista = new ArrayList<>();
        try (PreparedStatement ps = con.preparar(LISTAR_PROV_ORDEN_NOMBRE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRowProv(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listarProveedoresOrdenNombre: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<RowProv> buscarProveedores(String texto) {
        List<RowProv> lista = new ArrayList<>();
        String filtro = (texto == null ? "" : texto.trim()).toUpperCase();
        String like = "%" + filtro + "%";

        try (PreparedStatement ps = con.preparar(BUSCAR_PROV)) {
            ps.setString(1, like); // NIT
            ps.setString(2, like); // Nombre
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRowProv(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Error buscarProveedores: " + e.getMessage());
        }
        return lista;
    }

    // ======================================================
    // COMBOS / AYUDAS
    // ======================================================

    @Override
    public List<String> obtenerNitsProveedor() {
        List<String> out = new ArrayList<>();
        final String Q = "SELECT nit_proveedor FROM proveedor ORDER BY nit_proveedor";
        try (PreparedStatement ps = con.preparar(Q);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerNitsProveedor: " + e.getMessage());
        }
        return out;
    }

    @Override
    public List<String> obtenerNombresProveedor() {
        List<String> out = new ArrayList<>();
        final String Q = "SELECT nombre FROM proveedor ORDER BY nombre";
        try (PreparedStatement ps = con.preparar(Q);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerNombresProveedor: " + e.getMessage());
        }
        return out;
    }

    @Override
    public String decirHola() {
        return "";
    }

    // ======================================================
    // MAPPERS
    // ======================================================

    private IProveedor.RowProv mapRowProv(ResultSet rs) throws SQLException {
        IProveedor.RowProv r = new IProveedor.RowProv();
        r.nitProveedor    = rs.getString("nit_proveedor");
        r.nombreProveedor = rs.getString("nombre");
        r.direccionFiscal = rs.getString("direccion_fiscal");
        r.telefono        = rs.getString("telefono");
        return r;
    }

    private ModeloProveedores mapProveedor(ResultSet rs) throws SQLException {
        ModeloProveedores p = new ModeloProveedores();
        p.setNitProveedor(rs.getString("nit_proveedor"));
        p.setNombreProveedor(rs.getString("nombre"));
        p.setDireccionFiscal(rs.getString("direccion_fiscal"));
        p.setTelefonoProveedor(rs.getString("telefono"));
        return p;
    }
}
