package com.umg.interfaces;

import com.umg.modelo.ModeloCategoria;
import com.umg.modelo.ModeloMarcas;

import java.util.List;

public interface IMarca {
    // CRUD básico
    boolean insertar(ModeloMarcas modeloMarcas);
    boolean actualizar(ModeloMarcas modeloMarcas);
    boolean eliminar(int codigo);
    ModeloCategoria obtenerPorCodigo(int codigo);
    List<ModeloMarcas> obtenerTodos();

    // NUEVOS MÉTODOS PARA COMBOBOX Y BÚSQUEDA
    List<Integer> obtenerCodigos();             // Para ComboBox por código
    List<String> obtenerDescripciones();       // Para ComboBox por descripción
    List<ModeloMarcas> buscar(String texto); // Para búsqueda insensible a mayúscul
}
