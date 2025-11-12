/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.modelo;
import com.umg.vistas.VistaMovimentoStock;
import com.umg.vistas.VistaMovimientoCuentas;
/**
 *
 * @author Lisi
 */
public class ModeloMovimientoCuentas {
    VistaMovimientoCuentas vista;

    public ModeloMovimientoCuentas(VistaMovimientoCuentas vista) {
        this.vista = vista;
    }

    public VistaMovimientoCuentas getVista() {
        return vista;
    }

    public void setVista(VistaMovimientoCuentas vista) {
        this.vista = vista;
    }
}
