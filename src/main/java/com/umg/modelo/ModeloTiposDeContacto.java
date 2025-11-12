/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaTiposDeContacto;
/**
 *
 * @author Lisi
 */
public class ModeloTiposDeContacto {
    VistaTiposDeContacto vista;

    public ModeloTiposDeContacto(VistaTiposDeContacto vista) {
        this.vista = vista;
    }

    public VistaTiposDeContacto getVista() {
        return vista;
    }

    public void setVista(VistaTiposDeContacto vista) {
        this.vista = vista;
    }
    // Campos
    private Integer codigo;   // identity (nullable aquí)
    private String  descripcion;

    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
