package com.umg.modelo;

import com.umg.vistas.VistaProveedores;

public class ModeloProveedores {

    // ===== Vista asociada =====
    private VistaProveedores vista;

    public ModeloProveedores() { }

    public ModeloProveedores(VistaProveedores vista) {
        this.vista = vista;
    }

    public VistaProveedores getVista() {
        return vista;
    }

    public void setVista(VistaProveedores vista) {
        this.vista = vista;
    }

    // =======================================================
    //  CAMPOS PROVEEDOR (coinciden con la tabla proveedor)
    // =======================================================

    // nit_proveedor (PK)
    private String nitProveedor;

    // nombre
    private String nombreProveedor;

    // direccion_fiscal
    private String direccionFiscal;

    // telefono
    private String telefonoProveedor;

    // ===================== Getters / Setters =====================

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }
}
