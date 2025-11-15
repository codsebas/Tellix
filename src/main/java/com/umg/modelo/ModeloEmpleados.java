package com.umg.modelo;

import com.umg.vistas.VistaEmpleados;

public class ModeloEmpleados {
    VistaEmpleados vista;

    public ModeloEmpleados(VistaEmpleados vista) {
        this.vista = vista;
    }

    public VistaEmpleados getVista() {
        return vista;
    }

    public void setVista(VistaEmpleados vista) {
        this.vista = vista;
    }
    
    private Integer codigoEmpleado;
    private String documentoIdentificacion;
    private String nombre1;
    private String nombre2;
    private String apellido1;
    private String apellido2;
    private String apellidoCasada;
    private String estado;        // 'A' o 'I'
    private Integer codigoJefe;   // FK a empleado.codigo_empleado

    // --- Getters & Setters ---

    public Integer getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(Integer codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getDocumentoIdentificacion() {
        return documentoIdentificacion;
    }

    public void setDocumentoIdentificacion(String documentoIdentificacion) {
        this.documentoIdentificacion = documentoIdentificacion;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCodigoJefe() {
        return codigoJefe;
    }

    public void setCodigoJefe(Integer codigoJefe) {
        this.codigoJefe = codigoJefe;
    }

    // Para mostrar nombre completo donde sea necesario
    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        if (nombre1 != null) sb.append(nombre1).append(" ");
        if (nombre2 != null) sb.append(nombre2).append(" ");
        if (apellido1 != null) sb.append(apellido1).append(" ");
        if (apellido2 != null) sb.append(apellido2).append(" ");
        return sb.toString().trim();
    }
}

