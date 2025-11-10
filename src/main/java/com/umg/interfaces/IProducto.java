package com.umg.interfaces;
import com.umg.modelo.ModeloProducto;

import javax.swing.table.DefaultTableModel;
import java.util.List;
public interface IProducto {
    DefaultTableModel listarProductos();

    // CRUD básico
    boolean insertar(ModeloProducto producto);
    boolean actualizar(ModeloProducto producto);
    boolean eliminar(int codigo);
    ModeloProducto obtenerPorCodigo(int codigo);
    List<ModeloProducto> obtenerTodos();

    // Métodos auxiliares
    List<Integer> obtenerCodigos();                 // Para ComboBox por código
    List<String> obtenerNombres();                 // Para ComboBox por nombre
    List<ModeloProducto> buscar(String texto);     // Para búsqueda insensible a mayúsculas/minúsculas
}