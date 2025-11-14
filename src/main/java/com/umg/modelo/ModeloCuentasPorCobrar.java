package com.umg.modelo;

import com.umg.vistas.VistaCuentasPorCobrar;

public class ModeloCuentasPorCobrar {

    private final VistaCuentasPorCobrar vista;
    public ModeloCuentasPorCobrar( VistaCuentasPorCobrar vista) {
        this.vista = vista;
    }
    public VistaCuentasPorCobrar getVista() {
        return vista;
    }
}
