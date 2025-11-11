/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaBancos;
/**
 *
 * @author Lisi
 */
public class ModeloBancos {
    VistaBancos vista;

    public ModeloBancos(VistaBancos vista) {
        this.vista = vista;
    }

    public VistaBancos getVista() {
        return vista;
    }

    public void setVista(VistaBancos vista) {
        this.vista = vista;
    }
}
