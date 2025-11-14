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
         // "DIAS" o null
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

    private String tipoPlazo;
    public void setTipoPlazo(String tipoPlazo) { this.tipoPlazo = tipoPlazo; }
    public String getTipoPlazo() { return tipoPlazo; }

    public ModeloCompras() {
    }

    public Integer getPlazoCredito() {
        return plazoCredito;
    }

    public void setPlazoCredito(Integer plazoCredito) {
        this.plazoCredito = plazoCredito;
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
