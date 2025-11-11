package com.umg.modelo;

import com.umg.vistas.VistaClientes;

public class ModeloCliente {
    VistaClientes vista;

    public ModeloCliente() {
    }

    public ModeloCliente(VistaClientes vista) {
        this.vista = vista;
    }

    public VistaClientes getVista() {
        return vista;
    }

    public void setVista(VistaClientes vista) {
        this.vista = vista;
    }

    private Integer identificacion;
    private Integer correlativo;
    private Integer tipoContacto;
    private String infoContacto;
    private String telefono;
    private String nitCliente;
    // código de tipo_contacto
    String info;
    String nit;

    private Integer codigo;         // nullable
    private String  nombre1;
    private String  nombre2;
    private String  nombre3;
    private String  apellido1;
    private String  apellido2;
    private String  apellidoCasada;
    private Integer tipoCliente;    // FK a tipo_cliente(codigo), nullable
    private Double  limiteCredito;  // nullable
    private String  direccion;
    private String  estado;

    public Integer getIdentificacion() { return identificacion; }
    public void setIdentificacion(Integer identificacion) { this.identificacion = identificacion; }
    public Integer getCorrelativo() { return correlativo; }
    public void setCorrelativo(Integer correlativo) { this.correlativo = correlativo; }
    public Integer getTipoContacto() { return tipoContacto; }
    public void setTipoContacto(Integer tipoContacto) { this.tipoContacto = tipoContacto; }
    public String getInfoContacto() { return infoContacto; }
    public void setInfoContacto(String infoContacto) { this.infoContacto = infoContacto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getNitCliente() { return nitCliente; }
    public void setNitCliente(String nitCliente) { this.nitCliente = nitCliente; }
    public String getInfo() { return info; }

    public String getNit() {
        return nit;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getNombre3() {
        return nombre3;
    }

    public void setNombre3(String nombre3) {
        this.nombre3 = nombre3;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getApellidoCasada() {
        return apellidoCasada;
    }

    public void setApellidoCasada(String apellidoCasada) {
        this.apellidoCasada = apellidoCasada;
    }

    public Integer getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(Integer tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
