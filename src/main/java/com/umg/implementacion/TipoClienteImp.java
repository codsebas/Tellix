package com.umg.implementacion;

import com.umg.interfaces.ITipoCliente;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.Sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementación JDBC del DAO de tipo_cliente. */
public class TipoClienteImp implements ITipoCliente.DAO {

    private final Conector con = Sesion.getConexion(); // credenciales centralizadas
    private final Sql sql = new Sql();                  // constantes SQL (getINSERTAR_TIPO_CLIENTE, etc.)

    @Override
    public int insertar(String descripcion) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getINSERTAR_TIPO_CLIENTE());
        ps.setString(1, descripcion);
        return ps.executeUpdate();
    }

    @Override
    public int actualizar(int codigo, String descripcion) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getACTUALIZAR_TIPO_CLIENTE());
        ps.setString(1, descripcion);
        ps.setInt(2, codigo);
        return ps.executeUpdate();
    }

    @Override
    public int eliminar(int codigo) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getELIMINAR_TIPO_CLIENTE());
        ps.setInt(1, codigo);
        return ps.executeUpdate();
    }

    @Override
    public ITipoCliente.Registro obtenerPorCodigo(int codigo) throws SQLException {
        PreparedStatement ps = con.preparar(sql.getCONSULTAR_TIPO_CLIENTE_POR_CODIGO());
        ps.setInt(1, codigo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ITipoCliente.Registro(
                        rs.getInt("codigo"),
                        rs.getString("descripcion")
                );
            }
        }
        return null;
    }

    @Override
    public List<ITipoCliente.Registro> listarOrdenadoPor(String campo) throws SQLException {
        String q = "descripcion".equalsIgnoreCase(campo)
                ? sql.getCONSULTAR_TODOS_TIPO_CLIENTE_ORD_DESCRIPCION()
                : sql.getCONSULTAR_TODOS_TIPO_CLIENTE_ORD_CODIGO();

        List<ITipoCliente.Registro> out = new ArrayList<>();
        PreparedStatement ps = con.preparar(q);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new ITipoCliente.Registro(
                        rs.getInt("codigo"),
                        rs.getString("descripcion")
                ));
            }
        }
        return out;
    }

    @Override
    public List<ITipoCliente.Registro> buscarPorDescripcion(String filtro) throws SQLException {
        List<ITipoCliente.Registro> out = new ArrayList<>();
        PreparedStatement ps = con.preparar(sql.getBUSCAR_TIPO_CLIENTE_POR_DESCRIPCION());
        ps.setString(1, "%" + (filtro == null ? "" : filtro.trim()) + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new ITipoCliente.Registro(
                        rs.getInt("codigo"),
                        rs.getString("descripcion")
                ));
            }
        }
        return out;
    }
}
