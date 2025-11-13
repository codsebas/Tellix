package com.umg.interfaces;

import com.umg.modelo.ModeloProveedores;

import java.util.List;

public interface IProveedor {

    // ======================================================
    // CRUD PROVEEDOR
    // ======================================================
    boolean insertarProveedor(String nitProveedor, String nombre, String direccionFiscal, String telefono);
    boolean actualizarProveedor(String nitProveedor, String nombre, String direccionFiscal, String telefono);
    boolean eliminarProveedor(String nitProveedor);                 // delete por NIT
    ModeloProveedores obtenerProveedorPorNit(String nitProveedor);

    // Listado para la tabla (p + su primer representante si existe)
    List<RowProv> listarProveedoresConRepresentante();

    // Búsqueda por NIT/Nombre (mismo shape que el listado)
    List<RowProv> buscarProveedores(String texto);

    // ======================================================
    // COMBOS / BÚSQUEDA RÁPIDA
    // ======================================================
    List<String> obtenerNitsProveedor();              // para combos
    List<String> obtenerNombresProveedor();           // autocomplete / ayudas
    String decirHola();                                // opcional (compat)

    // ======================================================
    // REPRESENTANTE
    // ======================================================
    int nextCodigoRepresentante(String nitProveedor);

    boolean insertarRepresentante(
            String nitRepresentante,
            String fkProveedorNit,
            int    codigo,
            String nombre1,
            String nombre2,
            String apellido1,
            String apellido2,
            String apellidoCasada
    );

    boolean actualizarRepresentante(
            String nitRepresentante,
            String fkProveedorNit,
            int    codigo,
            String nombre1,
            String nombre2,
            String apellido1,
            String apellido2,
            String apellidoCasada
    );

    boolean eliminarRepresentantesDeProveedor(String nitProveedor);

    // ======================================================
    // CONTACTOS DE REPRESENTANTE
    // ======================================================
    List<ContactoRep> obtenerContactosRepresentante(String nitRepresentante);

    boolean insertarContactoRepresentante(
            String  nitRepresentante,
            Integer tipoContacto,     // FK a tipo_contacto(codigo)
            String  infoContacto      // teléfono/email/etc. (tu SQL lo guarda en info_contacto)
    );

    boolean eliminarContactosRepresentante(String nitRepresentante);

    // ======================================================
    // DTOs para resultados de consulta
    // ======================================================

    /**
     * Fila para la tabla principal (proveedores con su primer representante).
     * Coincide con las columnas que cargas en el JTable:
     * NIT Proveedor | Nombre Proveedor | NIT Representante | Nombre Representante | Direccion Fiscal | Telefono
     */
    class RowProv {
        public String nitProveedor;
        public String nombreProveedor;
        public String nitRepresentante;
        public String nombreRepresentante;
        public String direccionFiscal;
        public String telefono;

        public RowProv() { }

        public RowProv(String nitProveedor, String nombreProveedor,
                       String nitRepresentante, String nombreRepresentante,
                       String direccionFiscal, String telefono) {
            this.nitProveedor = nitProveedor;
            this.nombreProveedor = nombreProveedor;
            this.nitRepresentante = nitRepresentante;
            this.nombreRepresentante = nombreRepresentante;
            this.direccionFiscal = direccionFiscal;
            this.telefono = telefono;
        }
    }

    /**
     * Contacto del representante (según tu tabla contacto_representante).
     * Nota: tu SQL maneja "info_contacto" (puede contener teléfono/email).
     */
    class ContactoRep {
        public Integer correlativoContacto;   // si lo traes del SELECT
        public Integer tipoContacto;          // código tipo_contacto
        public String  infoContacto;          // dato (tel/email/etc.)
        public String  fkRepresentante;       // NIT representante (opcional)

        public ContactoRep() { }

        public ContactoRep(Integer correlativoContacto, Integer tipoContacto, String infoContacto, String fkRepresentante) {
            this.correlativoContacto = correlativoContacto;
            this.tipoContacto = tipoContacto;
            this.infoContacto = infoContacto;
            this.fkRepresentante = fkRepresentante;
        }
    }
}
