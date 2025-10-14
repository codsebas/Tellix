package com.umg.implementacion;

import com.umg.interfaces.ICliente;

public class ClienteImp implements ICliente {
    @Override
    public String decirHola() {
        String hola = "Pequeño Femboy Chileno";
        return hola;
    }
}
