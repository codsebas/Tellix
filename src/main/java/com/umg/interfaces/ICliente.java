package com.umg.interfaces;

import com.umg.modelo.ModeloCliente;

import java.util.List;

public interface ICliente {

    // ===========================
    // CRUD CLIENTE
    // ===========================
    boolean insertar(ModeloCliente c);
    boolean actualizar(ModeloCliente c);
    boolean eliminar(String nit);                 // soft-delete por NIT
    ModeloCliente obtenerPorNit(String nit);
    List<ModeloCliente> obtenerTodos();

    // ===========================
    // COMBOS / BÚSQUEDA
    // ===========================
    List<String> obtenerNits();                   // para ComboBox por NIT
    List<String> obtenerNombresCompletos();       // para autocomplete
    List<ModeloCliente> buscar(String texto);     // búsqueda por NIT y nombre/apellido
    String decirHola();                           // (si lo usas)

    // ===========================
    // CONTACTOS DE CLIENTE
    // ===========================
    int nextIdContactoCliente();                  // secuencia/identidad para contactos

    // <<--- CORREGIDO: lista de contactos, no de clientes
    List<ModeloCliente> obtenerContactosPorCliente(String nit);

    boolean insertarContactoCliente(              // inserta un contacto vinculado al NIT
                                                  int identificacion,
                                                  int correlativo,
                                                  Integer tipoContacto,
                                                  String infoContacto,
                                                  String telefono,
                                                  String nitCliente
    );

    boolean eliminarContactosPorCliente(String nit); // borra todos los contactos del NIT
}
