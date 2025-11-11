/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaImpuestos;
/**
 *
 * @author Lisi
 */
public class ModeloImpuestos {
    VistaImpuestos vista;

    public ModeloImpuestos(VistaImpuestos vista) {
        this.vista = vista;
    }

    public VistaImpuestos getVista() {
        return vista;
    }

    public void setVista(VistaImpuestos vista) {
        this.vista = vista;
    } 
}
