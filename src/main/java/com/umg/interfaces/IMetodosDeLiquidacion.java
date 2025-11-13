package com.umg.interfaces;

import com.umg.modelo.ModeloMetodosDeLiquidacion;
import java.util.List;

public interface IMetodosDeLiquidacion {

    class RowMetodo {
        public int codigo;
        public String descripcion;

        public RowMetodo(int codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }
    }

    boolean insertar(ModeloMetodosDeLiquidacion m);
    boolean actualizar(ModeloMetodosDeLiquidacion m);
    boolean eliminar(int codigo);

    List<RowMetodo> listarOrdenadoPor(String campoOrden); // "CODIGO" o "DESCRIPCION"
    List<RowMetodo> buscar(String filtro, String campoOrden);
}
