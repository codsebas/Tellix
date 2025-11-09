package com.umg.modelo;

import com.umg.vistas.VistaCompras;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ModeloCompras {
    VistaCompras vista;

    String noDocumento;
    String nitProveedor;
    String nitRepresentante;   // puede ser null
    LocalDate fecha;
    long usuarioSistema;
    long metodoPago;
    Integer plazoCredito;      // null si contado
    String tipoPlazo;         // "DIAS" o null
    boolean esCredito;
    String numeroCuenta;       // opcional para CxP
    String banco       ;
    long codigoProducto;
    BigDecimal cantidad;
    BigDecimal precioBruto;
    BigDecimal descuentos;  // puede ser null
    BigDecimal impuestos;
    String cuentaNumero;
    BigDecimal monto;

    public ModeloCompras() {
    }

    public ModeloCompras(VistaCompras vista) {
        this.vista = vista;
    }

    public VistaCompras getVista() {
        return vista;
    }

    public void setVista(VistaCompras vista) {
        this.vista = vista;
    }
}
