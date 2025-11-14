package com.umg.interfaces;

import java.util.List;

public interface ICuentaPorPagar {

    class RowCxp {
        public int correlativo;
        public int noDocumento;
        public int metodoPago;        // código FK
        public String metodoPagoDesc; // descripción del método
        public double valorTotal;
        public double valorPagado;
        public java.sql.Date fechaLimite;
        public String numeroCuenta;
        public String banco;
    }

    interface Servicio {
        List<RowCxp> listarTodas();
    }
}
