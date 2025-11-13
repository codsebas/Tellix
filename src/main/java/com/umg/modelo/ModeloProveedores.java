package com.umg.modelo;

import com.umg.vistas.VistaProveedores;

public class ModeloProveedores {
    // ===== Vista asociada (igual que en ModeloCliente) =====
    VistaProveedores vista;

    public ModeloProveedores() { }
    public ModeloProveedores(VistaProveedores vista) { this.vista = vista; }

    public VistaProveedores getVista() { return vista; }
    public void setVista(VistaProveedores vista) { this.vista = vista; }

    // =======================================================
    // CONTACTO (para la tabla inferior, similar a ModeloCliente)
    // =======================================================
    private Integer identificacion;
    private Integer correlativo;
    private Integer tipoContacto;
    private String  infoContacto;
    private String  telefono;          // teléfono del contacto
    private String  nitProveedor;      // FK para contacto (análogo a nitCliente)

    // Campos “genéricos” que usabas también en ModeloCliente
    String info;
    String nit;

    // =======================================================
    // PROVEEDOR (datos del proveedor)
    // =======================================================
    private String  nombreProveedor;      // nombre de la empresa
    private String  direccionFiscal;
    private String  telefonoProveedor;
    private String  estado;               // si lo manejas (A/I), opcional

    // =======================================================
    // REPRESENTANTE (persona asociada al proveedor)
    // =======================================================
    private Integer codigo;               // nullable (código de representante si lo usas)
    private String  nitRepresentante;
    private String  nombre1;
    private String  nombre2;
    private String  nombre3;              // por si lo necesitas, igual que en clientes
    private String  apellido1;
    private String  apellido2;
    private String  apellidoCasada;

    // ===================== Getters/Setters =====================
    // --- contacto ---
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

    public String getNitProveedor() { return nitProveedor; }
    public void setNitProveedor(String nitProveedor) { this.nitProveedor = nitProveedor; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    // --- proveedor ---
    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    public String getDireccionFiscal() { return direccionFiscal; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }

    public String getTelefonoProveedor() { return telefonoProveedor; }
    public void setTelefonoProveedor(String telefonoProveedor) { this.telefonoProveedor = telefonoProveedor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // --- representante ---
    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }

    public String getNitRepresentante() { return nitRepresentante; }
    public void setNitRepresentante(String nitRepresentante) { this.nitRepresentante = nitRepresentante; }

    public String getNombre1() { return nombre1; }
    public void setNombre1(String nombre1) { this.nombre1 = nombre1; }

    public String getNombre2() { return nombre2; }
    public void setNombre2(String nombre2) { this.nombre2 = nombre2; }

    public String getNombre3() { return nombre3; }
    public void setNombre3(String nombre3) { this.nombre3 = nombre3; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public String getApellidoCasada() { return apellidoCasada; }
    public void setApellidoCasada(String apellidoCasada) { this.apellidoCasada = apellidoCasada; }
}
