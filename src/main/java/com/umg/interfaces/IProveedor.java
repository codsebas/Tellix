package com.umg.interfaces;

import com.umg.modelo.ModeloProveedores;

import java.util.List;

public interface IProveedor {

    // ======================================================
    // CRUD PROVEEDOR  (tabla: proveedor)
    // ======================================================

    /**
     * Inserta un nuevo proveedor.
     * Usa los campos:
     *  - nitProveedor
     *  - nombreProveedor
     *  - direccionFiscal
     *  - telefonoProveedor
     */
    boolean insertarProveedor(ModeloProveedores proveedor);

    /**
     * Actualiza un proveedor existente (clave: nitProveedor).
     */
    boolean actualizarProveedor(ModeloProveedores proveedor);

    /**
     * Elimina un proveedor por NIT.
     */
    boolean eliminarProveedor(String nitProveedor);

    /**
     * Obtiene un proveedor por NIT (para botón Buscar / cargar en los txt).
     */
    ModeloProveedores obtenerProveedorPorNit(String nitProveedor);

    // ======================================================
    // LISTADOS / BÚSQUEDA
    // ======================================================

    /**
     * Lista de proveedores ordenados por NIT (para llenar la tabla).
     */
    List<RowProv> listarProveedoresOrdenNit();

    /**
     * Lista de proveedores ordenados por Nombre (para el combo "Ordenar por").
     */
    List<RowProv> listarProveedoresOrdenNombre();

    /**
     * Búsqueda por NIT o Nombre (para txtBuscar).
     */
    List<RowProv> buscarProveedores(String texto);

    // ======================================================
    // COMBOS / AYUDAS
    // ======================================================

    List<String> obtenerNitsProveedor();     // por si usas combos
    List<String> obtenerNombresProveedor();  // autocomplete, etc.

    String decirHola();  // opcional, por si ya lo usas en otro lado


    // ======================================================
    // DTO para resultados de consulta (tabla principal)
    // ======================================================

    /**
     * Fila para la JTable de proveedores.
     * Columnas típicas:
     *  NIT Proveedor | Nombre Proveedor
     * (puedes usar también direccionFiscal y telefono si luego amplías la tabla)
     */
    class RowProv {
        public String nitProveedor;
        public String nombreProveedor;
        public String direccionFiscal;
        public String telefono;

        public RowProv() { }

        public RowProv(String nitProveedor,
                       String nombreProveedor,
                       String direccionFiscal,
                       String telefono) {
            this.nitProveedor = nitProveedor;
            this.nombreProveedor = nombreProveedor;
            this.direccionFiscal = direccionFiscal;
            this.telefono = telefono;
        }
    }
}
