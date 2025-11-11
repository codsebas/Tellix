/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaMetodosDeLiquidacion;
/**
 *
 * @author Lisi
 */
public class ModeloMetodosDeLiquidacion {
    VistaMetodosDeLiquidacion vista;

    public ModeloMetodosDeLiquidacion(VistaMetodosDeLiquidacion vista) {
        this.vista = vista;
    }

    public VistaMetodosDeLiquidacion getVista() {
        return vista;
    }

    public void setVista(VistaMetodosDeLiquidacion vista) {
        this.vista = vista;
    }
}
