package com.umg.interfaces;

import com.umg.modelo.ModeloCategoria;

import java.util.List;

public interface ICategoria {
    // CRUD básico
    boolean insertar(ModeloCategoria modeloCategoria);
    boolean actualizar(ModeloCategoria modeloCategoria);
    boolean eliminar(int codigo);
    ModeloCategoria obtenerPorCodigo(int codigo);
    List<ModeloCategoria> obtenerTodos();

    // NUEVOS MÉTODOS PARA COMBOBOX Y BÚSQUEDA
    List<Integer> obtenerCodigos();             // Para ComboBox por código
    List<String> obtenerDescripciones();       // Para ComboBox por descripción
    List<ModeloCategoria> buscar(String texto); // Para búsqueda insensible a mayúsculas/minúsculas
}