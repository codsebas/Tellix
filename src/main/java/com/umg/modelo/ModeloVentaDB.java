package com.umg.modelo;

import oracle.sql.DATE;
import oracle.sql.TIMESTAMP;

public class ModeloVentaDB {
    private String nit;
    private DATE fecha_operacion;
    private TIMESTAMP hora_operacion;
    private String usuario_sistema;
    private int metodo_pago;
    private int plazo_credito;
    private char tipo_plazo;
    private char estado;
    private float total;

    public ModeloVentaDB() {
    }

    public ModeloVentaDB(String nit, DATE fecha_operacion, TIMESTAMP hora_operacion, String usuario_sistema, int metodo_pago, int plazo_credito, char tipo_plazo, char estado, float total) {
        this.nit = nit;
        this.fecha_operacion = fecha_operacion;
        this.hora_operacion = hora_operacion;
        this.usuario_sistema = usuario_sistema;
        this.metodo_pago = metodo_pago;
        this.plazo_credito = plazo_credito;
        this.tipo_plazo = tipo_plazo;
        this.estado = estado;
        this.total = total;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public DATE getFecha_operacion() {
        return fecha_operacion;
    }

    public void setFecha_operacion(DATE fecha_operacion) {
        this.fecha_operacion = fecha_operacion;
    }

    public TIMESTAMP getHora_operacion() {
        return hora_operacion;
    }

    public void setHora_operacion(TIMESTAMP hora_operacion) {
        this.hora_operacion = hora_operacion;
    }

    public String getUsuario_sistema() {
        return usuario_sistema;
    }

    public void setUsuario_sistema(String usuario_sistema) {
        this.usuario_sistema = usuario_sistema;
    }

    public int getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(int metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public int getPlazo_credito() {
        return plazo_credito;
    }

    public void setPlazo_credito(int plazo_credito) {
        this.plazo_credito = plazo_credito;
    }

    public char getTipo_plazo() {
        return tipo_plazo;
    }

    public void setTipo_plazo(char tipo_plazo) {
        this.tipo_plazo = tipo_plazo;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
