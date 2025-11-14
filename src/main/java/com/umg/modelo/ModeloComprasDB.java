package com.umg.modelo;


import java.sql.Date;
import java.sql.Timestamp;

public class ModeloComprasDB {
    private String proveedor;
    private String representante;
    private Date fecha_operacion;
    private Timestamp hora_operacion;
    private String usuario_sistema;
    private int metodo_pago;
    private int plazo_credito;
    private String tipo_plazo;
    private String estado;

    public ModeloComprasDB() {
    }

    public ModeloComprasDB(String proveedor, String representante, Date fecha_operacion, Timestamp hora_operacion, String usuario_sistema, int metodo_pago, int plazo_credito, String tipo_plazo, String estado) {
        this.proveedor = proveedor;
        this.representante = representante;
        this.fecha_operacion = fecha_operacion;
        this.hora_operacion = hora_operacion;
        this.usuario_sistema = usuario_sistema;
        this.metodo_pago = metodo_pago;
        this.plazo_credito = plazo_credito;
        this.tipo_plazo = tipo_plazo;
        this.estado = estado;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public Date getFecha_operacion() {
        return fecha_operacion;
    }

    public void setFecha_operacion(Date fecha_operacion) {
        this.fecha_operacion = fecha_operacion;
    }

    public Timestamp getHora_operacion() {
        return hora_operacion;
    }

    public void setHora_operacion(Timestamp hora_operacion) {
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

    public String getTipo_plazo() {
        return tipo_plazo;
    }

    public void setTipo_plazo(String tipo_plazo) {
        this.tipo_plazo = tipo_plazo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
