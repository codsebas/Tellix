package com.umg.modelo;

import com.umg.vistas.VistaMetodosDeLiquidacion;

public class ModeloMetodosDeLiquidacion {

    private VistaMetodosDeLiquidacion vista;

    private Integer codigo;
    private String descripcion;

    public ModeloMetodosDeLiquidacion(VistaMetodosDeLiquidacion vista) {
        this.vista = vista;
    }

    public VistaMetodosDeLiquidacion getVista() {
        return vista;
    }

    public void setVista(VistaMetodosDeLiquidacion vista) {
        this.vista = vista;
    }

    // ===== Campos de la tabla metodo_liquidacion =====
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
