package com.umg.modelo;

import com.umg.vistas.VistaCategorias;

public class ModeloCategoria {
    // Manteniendo tu estilo: el modelo contiene referencia a la vista
    private VistaCategorias vista;

    private int codigo;
    private String descripcion;

    public ModeloCategoria(VistaCategorias vista) {
        this.vista = vista;
    }
    public ModeloCategoria() {
        // Constructor vacío, útil para instanciar objetos sin vista
    }

    public VistaCategorias getVista() {
        return vista;
    }

    public void setVista(VistaCategorias vista) {
        this.vista = vista;
    }

    // getters / setters para campos de la entidad
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
