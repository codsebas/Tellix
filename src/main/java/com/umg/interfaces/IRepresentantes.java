package com.umg.interfaces;

import com.umg.modelo.ModeloRepresentates;

import java.util.List;

public interface IRepresentantes {

    // ======================================================
    // REPRESENTANTE (persona asociada a un proveedor)
    // ======================================================

    /**
     * Inserta un representante nuevo para un proveedor.
     * Se espera que en el ModeloRepresentates vengan llenos:
     *  - nitProveedor
     *  - nitRepresentante
     *  - primerNombre, segundoNombre
     *  - primerApellido, segundoApellido, apellidoCasada
     */
    boolean insertarRepresentante(ModeloRepresentates representante);

    /**
     * Actualiza los datos del representante (mismo NIT proveedor + NIT representante).
     */
    boolean actualizarRepresentante(ModeloRepresentates representante);

    /**
     * Elimina un representante específico (por NIT proveedor + NIT representante).
     */
    boolean eliminarRepresentante(String nitProveedor, String nitRepresentante);

    /**
     * Obtiene un representante por NIT proveedor + NIT representante.
     */
    ModeloRepresentates obtenerRepresentante(String nitProveedor, String nitRepresentante);

    /**
     * Lista de representantes con datos del proveedor (para llenar la tabla superior).
     * Ordenado por NIT de proveedor.
     */
    List<RowRep> listarRepresentantesOrdenNitProveedor();

    /**
     * Lista de representantes con datos del proveedor, ordenados por Nombre de proveedor.
     */
    List<RowRep> listarRepresentantesOrdenNombreProveedor();

    /**
     * Búsqueda por NIT/Nombres (proveedor o representante) para la tabla superior.
     */
    List<RowRep> buscarRepresentantes(String texto);


    // ======================================================
    // CONTACTOS DEL REPRESENTANTE
    // ======================================================

    /**
     * Obtiene los contactos de un representante (para la tabla inferior).
     */
    List<ContactoRep> obtenerContactosRepresentante(String nitRepresentante);

    /**
     * Inserta un nuevo contacto para el representante.
     *
     * @param nitRepresentante NIT del representante (FK)
     * @param tipoContacto     código de tipo_contacto
     * @param infoContacto     dato que se guardará en info_contacto (tel, correo, etc.)
     */
    boolean insertarContactoRepresentante(
            String  nitRepresentante,
            Integer tipoContacto,
            String  infoContacto
    );

    /**
     * Actualiza un contacto existente (si decides manejar edición directa).
     */
    boolean actualizarContactoRepresentante(
            Integer correlativoContacto,
            String  nitRepresentante,
            Integer tipoContacto,
            String  infoContacto
    );

    /**
     * Elimina un contacto específico del representante.
     */
    boolean eliminarContactoRepresentante(Integer correlativoContacto, String nitRepresentante);

    /**
     * Elimina todos los contactos de un representante (útil al borrar/actualizar en bloque).
     */
    boolean eliminarContactosRepresentante(String nitRepresentante);


    // ======================================================
    // DTOs para resultados de consulta
    // ======================================================

    /**
     * Fila para la tabla superior:
     *  NIT Proveedor | Nombre Proveedor | NIT Representante | Nombre Representante
     */
    class RowRep {
        public String nitProveedor;
        public String nombreProveedor;
        public String nitRepresentante;
        public String nombreRepresentante;

        public RowRep() { }

        public RowRep(String nitProveedor,
                      String nombreProveedor,
                      String nitRepresentante,
                      String nombreRepresentante) {
            this.nitProveedor      = nitProveedor;
            this.nombreProveedor   = nombreProveedor;
            this.nitRepresentante  = nitRepresentante;
            this.nombreRepresentante = nombreRepresentante;
        }
    }

    /**
     * Contacto del representante (para la tabla inferior).
     * Coincide con las columnas:
     *  No. | Tipo | Información | Teléfono
     *
     * En BD normalmente se guarda un solo campo info_contacto, pero aquí
     * puedes separar "info" y "telefono" según el tipo de contacto.
     */
    class ContactoRep {
        public Integer correlativoContacto;   // PK / identificador del registro
        public Integer tipoContacto;          // código en tipo_contacto
        public String  infoContacto;          // lo que va a la columna info_contacto en BD
        public String  descripcionTipo;       // texto para mostrar en la tabla
        public String  telefonoVisible;       // si decides separar tel en la UI
        public String  nitRepresentante;      // FK

        public ContactoRep() { }

        public ContactoRep(Integer correlativoContacto,
                           Integer tipoContacto,
                           String  infoContacto,
                           String  descripcionTipo,
                           String  telefonoVisible,
                           String  nitRepresentante) {
            this.correlativoContacto = correlativoContacto;
            this.tipoContacto        = tipoContacto;
            this.infoContacto        = infoContacto;
            this.descripcionTipo     = descripcionTipo;
            this.telefonoVisible     = telefonoVisible;
            this.nitRepresentante    = nitRepresentante;
        }
    }
}
