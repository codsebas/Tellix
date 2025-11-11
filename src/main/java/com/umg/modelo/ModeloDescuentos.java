/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaDescuentos;
/**
 *
 * @author Lisi
 */
public class ModeloDescuentos {
    VistaDescuentos vista;

    public ModeloDescuentos(VistaDescuentos vista) {
        this.vista = vista;
    }

    public VistaDescuentos getVista() {
        return vista;
    }

    public void setVista(VistaDescuentos vista) {
        this.vista = vista;
    }
    
}
