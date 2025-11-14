package com.umg.modelo;

public class ModeloRepresentanteDB {
    private String nit;
    private String nit_proveedor;
    private String nombre;

    public ModeloRepresentanteDB() {
    }

    public ModeloRepresentanteDB(String nit, String nit_proveedor, String nombre) {
        this.nit = nit;
        this.nit_proveedor = nit_proveedor;
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNit_proveedor() {
        return nit_proveedor;
    }

    public void setNit_proveedor(String nit_proveedor) {
        this.nit_proveedor = nit_proveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
