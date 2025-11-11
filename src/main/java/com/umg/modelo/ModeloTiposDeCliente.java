package com.umg.modelo;

import com.umg.vistas.VistaTiposDeCliente;

public class ModeloTiposDeCliente {
    VistaTiposDeCliente vista;

    public ModeloTiposDeCliente(VistaTiposDeCliente vista) {
        this.vista = vista;
    }

    public VistaTiposDeCliente getVista() {
        return vista;
    }

    public void setVista(VistaTiposDeCliente vista) {
        this.vista = vista;
    }

    /* ===== Campos del tipo_cliente ===== */
    private Integer codigo;       // GENERATED ALWAYS AS IDENTITY (nullable en el modelo)
    private String  descripcion;

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
