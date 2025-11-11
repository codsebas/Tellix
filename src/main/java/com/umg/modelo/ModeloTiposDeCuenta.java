/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaTiposDeCuenta;
/**
 *
 * @author Lisi
 */
public class ModeloTiposDeCuenta {
    VistaTiposDeCuenta vista;

    public ModeloTiposDeCuenta(VistaTiposDeCuenta vista) {
        this.vista = vista;
    }

    public VistaTiposDeCuenta getVista() {
        return vista;
    }

    public void setVista(VistaTiposDeCuenta vista) {
        this.vista = vista;
    }
    
}
