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
}
