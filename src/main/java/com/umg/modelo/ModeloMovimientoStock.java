/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaMovimentoStock;
/**
 *
 * @author Lisi
 */
public class ModeloMovimientoStock {
    VistaMovimentoStock vista;

    public ModeloMovimientoStock(VistaMovimentoStock vista) {
        this.vista = vista;
    }

    public VistaMovimentoStock getVista() {
        return vista;
    }

    public void setVista(VistaMovimentoStock vista) {
        this.vista = vista;
    }
}
