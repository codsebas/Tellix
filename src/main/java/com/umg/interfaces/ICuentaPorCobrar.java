package com.umg.interfaces;

import java.util.List;

public interface ICuentaPorCobrar {

    class RowCxc {
        public int correlativo;
        public int secuencia;
        public int metodoPago;        // código FK
        public String metodoPagoDesc; // descripción en letras
        public java.sql.Date fechaLimite;
        public double valorTotal;
        public double valorPagado;
        public String numeroCuenta;
        public String clienteNit;
    }

    // Servicio para usar en la implementación
    interface Servicio {
        List<RowCxc> listarTodo();   // <-- mismo nombre que en CuentaPorCobrarImp
    }
}
