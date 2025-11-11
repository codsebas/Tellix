package com.umg.interfaces;

import com.umg.modelo.ModeloMedidas;
import java.util.List;

public interface IMedida {
    boolean insertar(ModeloMedidas medida);
    boolean actualizar(ModeloMedidas medida);
    boolean eliminar(String codigo);
    ModeloMedidas obtenerPorCodigo(String codigo);
    List<ModeloMedidas> obtenerTodos();

    // Para ComboBox
    List<String> obtenerCodigos();
    List<String> obtenerDescripciones();

    // Para búsqueda
    List<ModeloMedidas> buscar(String texto);
}
