package com.umg.modelo;

import com.umg.vistas.VistaCuentasPorPagar;

public class ModeloCuentasPorPagar {

    // ===== Referencia a la vista =====
    private VistaCuentasPorPagar vista;

    // ===== Constructor =====
    public ModeloCuentasPorPagar(VistaCuentasPorPagar vista) {
        this.vista = vista;
    }



    // ===== Getter / Setter de la vista =====
    public VistaCuentasPorPagar getVista() {
        return vista;
    }

    public void setVista(VistaCuentasPorPagar vista) {
        this.vista = vista;
    }

    // (Si más adelante quisieras guardar campos como
    //  correlativo, noDocumento, etc., los puedes agregar aquí
    //  con sus getters/setters, pero para solo listar en la tabla
    //  con el controlador actual no hace falta nada más.)
}

