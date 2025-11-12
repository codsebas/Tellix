package com.umg.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ITipoContacto {

    // DTO simple
    class Registro {
        public final Integer codigo;
        public final String  descripcion;
        public Registro(Integer codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }
    }

    // Acceso a datos
    interface DAO {
        int insertar(String descripcion) throws SQLException;
        int actualizar(int codigo, String descripcion) throws SQLException;
        int eliminar(int codigo) throws SQLException;

        Registro obtenerPorCodigo(int codigo) throws SQLException;
        List<Registro> listarOrdenadoPor(String campo) throws SQLException;     // "codigo" | "descripcion"
        List<Registro> buscarPorDescripcion(String filtro) throws SQLException; // LIKE '%filtro%'
    }

    // Servicio (envoltura)
    interface Servicio {
        boolean crear(String descripcion);
        boolean actualizar(int codigo, String descripcion);
        boolean eliminar(int codigo);

        Registro obtener(int codigo);
        List<Registro> listarOrdenadoPor(String campo);
        List<Registro> buscar(String filtro);
    }
}
