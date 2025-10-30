package com.umg.interfaces;
import com.umg.modelo.ModeloProducto;
import java.util.List;
public interface IProducto {
    boolean insertar(ModeloProducto p);
    boolean actualizar(ModeloProducto p);
    boolean eliminar(int codigo);
    ModeloProducto obtenerPorCodigo(int codigo);
    List<ModeloProducto> obtenerTodos();
}
