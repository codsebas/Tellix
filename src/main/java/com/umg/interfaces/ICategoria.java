package com.umg.interfaces;

import com.umg.modelo.ModeloCategoria;

import java.util.List;

public interface ICategoria {
    boolean insertar(ModeloCategoria modeloCategoria);
    boolean actualizar(ModeloCategoria modeloCategoria);
    boolean eliminar(int codigo);
    ModeloCategoria obtenerPorCodigo(int codigo);
    List<ModeloCategoria> obtenerTodos();

}
