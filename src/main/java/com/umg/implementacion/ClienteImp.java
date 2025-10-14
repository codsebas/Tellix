package com.umg.implementacion;

import com.umg.interfaces.ICliente;

public class ClienteImp implements ICliente {
    @Override
    public String decirHola() {
        String hola = "Hola, desde la implementación";
        return hola;
    }
}
