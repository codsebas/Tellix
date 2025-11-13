package com.umg.modelo;

import com.umg.vistas.VistaRepresentantes;

public class ModeloRepresentates {

    // ===== Vista asociada =====
    private VistaRepresentantes vista;

    public ModeloRepresentates(VistaRepresentantes vista) {
        this.vista = vista;
    }

    public VistaRepresentantes getVista() {
        return vista;
    }

    public void setVista(VistaRepresentantes vista) {
        this.vista = vista;
    }

    // ======================================================
    // PROVEEDOR (vinculado al combo cmbProveedores)
    // ======================================================

    private String nitProveedor;           // viene del combo (NIT seleccionado)
    private String nombreProveedor;        // txtNombreProveedor
    private String direccionFiscal;        // txtDireccionFiscal
    private String telefonoProveedor;      // txtTelefonoProveedor

    // ======================================================
    // REPRESENTANTE
    // ======================================================

    private String nitRepresentante;       // txtNitRepresentante
    private String primerNombre;           // txtPrimerNombre
    private String segundoNombre;          // txtSegundoNombre
    private String primerApellido;         // txtPrimerApellido
    private String segundoApellido;        // txtSegundoApellido
    private String apellidoCasada;         // txtApellidoCasada

    // ======================================================
    // CONTACTO DEL REPRESENTANTE
    // ======================================================

    private Integer tipoContacto;          // cmbTipoContacto (código)
    private String infoContacto;           // txtInfoContacto
    private String telContacto;// txtTelContacto
    private String cmbProveedores;

    public String getCmbProveedores() {
        return cmbProveedores;
    }

    public void setCmbProveedores(String cmbProveedores) {
        this.cmbProveedores = cmbProveedores;
    }
    // ===================== Getters / Setters =====================

    // --- Proveedor ---
    public String getNitProveedor() { return nitProveedor; }
    public void setNitProveedor(String nitProveedor) { this.nitProveedor = nitProveedor; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    public String getDireccionFiscal() { return direccionFiscal; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }

    public String getTelefonoProveedor() { return telefonoProveedor; }
    public void setTelefonoProveedor(String telefonoProveedor) { this.telefonoProveedor = telefonoProveedor; }

    // --- Representante ---
    public String getNitRepresentante() { return nitRepresentante; }
    public void setNitRepresentante(String nitRepresentante) { this.nitRepresentante = nitRepresentante; }

    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getApellidoCasada() { return apellidoCasada; }
    public void setApellidoCasada(String apellidoCasada) { this.apellidoCasada = apellidoCasada; }

    // --- Contacto ---
    public Integer getTipoContacto() { return tipoContacto; }
    public void setTipoContacto(Integer tipoContacto) { this.tipoContacto = tipoContacto; }

    public String getInfoContacto() { return infoContacto; }
    public void setInfoContacto(String infoContacto) { this.infoContacto = infoContacto; }

    public String getTelContacto() { return telContacto; }
    public void setTelContacto(String telContacto) { this.telContacto = telContacto; }
}
