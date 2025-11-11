package com.umg.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ITipoCliente {
    /** DTO simple */
    public static class Registro {
        public final Integer codigo;
        public final String descripcion;
        public Registro(Integer codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }
    }

    /** Operaciones de acceso a datos */
    public interface DAO {
        int insertar(String descripcion) throws SQLException;
        int actualizar(int codigo, String descripcion) throws SQLException;
        int eliminar(int codigo) throws SQLException;

        Registro obtenerPorCodigo(int codigo) throws SQLException;
        List<Registro> listarOrdenadoPor(String campo) throws SQLException;       // "codigo" | "descripcion"
        List<Registro> buscarPorDescripcion(String filtro) throws SQLException;    // LIKE '%filtro%'
    }

    /** Capa de servicio (envoltorio del DAO) */
    public interface Servicio {
        boolean crear(String descripcion);
        boolean actualizar(int codigo, String descripcion);
        boolean eliminar(int codigo);

        Registro obtener(int codigo);
        List<Registro> listarOrdenadoPor(String campo);
        List<Registro> buscar(String filtro);
    }
}

