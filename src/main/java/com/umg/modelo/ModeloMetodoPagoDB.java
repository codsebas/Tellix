package com.umg.modelo;

public class ModeloMetodoPagoDB {
    private int codigo;
    private String metodo_pago;

    public ModeloMetodoPagoDB() {
    }

    public ModeloMetodoPagoDB(int codigo, String metodo_pago) {
        this.codigo = codigo;
        this.metodo_pago = metodo_pago;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }
}
