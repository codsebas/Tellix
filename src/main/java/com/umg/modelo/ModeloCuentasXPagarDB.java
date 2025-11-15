package com.umg.modelo;

import java.sql.Date;

public class ModeloCuentasXPagarDB {
    private int no_documento;
    private String estado;
    private int metodo_pago;
    private float valor_total;
    private float valor_pagado;
    private Date fecha_limite;
    private String numero_cuenta;
    private int banco;

    public ModeloCuentasXPagarDB() {
    }

    public ModeloCuentasXPagarDB(int no_documento, String estado, int metodo_pago, float valor_total, float valor_pagado, Date fecha_limite, String numero_cuenta, int banco) {
        this.no_documento = no_documento;
        this.estado = estado;
        this.metodo_pago = metodo_pago;
        this.valor_total = valor_total;
        this.valor_pagado = valor_pagado;
        this.fecha_limite = fecha_limite;
        this.numero_cuenta = numero_cuenta;
        this.banco = banco;
    }

    public int getNo_documento() {
        return no_documento;
    }

    public void setNo_documento(int no_documento) {
        this.no_documento = no_documento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(int metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public float getValor_total() {
        return valor_total;
    }

    public void setValor_total(float valor_total) {
        this.valor_total = valor_total;
    }

    public float getValor_pagado() {
        return valor_pagado;
    }

    public void setValor_pagado(float valor_pagado) {
        this.valor_pagado = valor_pagado;
    }

    public Date getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(Date fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public int getBanco() {
        return banco;
    }

    public void setBanco(int banco) {
        this.banco = banco;
    }
}
