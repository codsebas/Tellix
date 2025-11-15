package sql;

public class QuerysCompras {

    // Todas las compras
    private final String SELECT_ALL_COMPRAS =
            "SELECT * FROM TELLIX.COMPRA ORDER BY no_documento DESC";

    // Una compra por no_documento
    private final String SELECT_UNA_COMPRA =
            "SELECT * FROM TELLIX.COMPRA WHERE no_documento = ?";

    // Detalle de una compra
    private final String SELECT_DET_COMPRA =
            "SELECT * FROM TELLIX.DETALLE_COMPRA " +
                    "WHERE no_documento = ? " +
                    "ORDER BY correlativo ASC";

    // Insert de la cabecera de compra
    private final String INSERT_COMPRA =
            "INSERT INTO TELLIX.COMPRA (" +
                    "proveedor, " +
                    "representante, " +
                    "fecha_operacion, " +
                    "hora_operacion, " +
                    "usuario_sistema, " +
                    "fk_metodo_pago, " +
                    "plazo_credito, " +
                    "tipo_plazo, " +
                    "estado" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // Insert del detalle de compra
    private final String INSERT_DETALLE_COMPRA =
            "INSERT INTO TELLIX.DETALLE_COMPRA (" +
                    "no_documento, " +
                    "codigo_producto, " +
                    "cantidad, " +
                    "precio_bruto, " +
                    "descuentos, " +
                    "impuestos" +
                    ") VALUES (?, ?, ?, ?, ?, ?)";

    // ==== Misma lógica que en ventas para inventario y producto ====

    private final String INSERTAR_INVENTARIO =
            "INSERT INTO TELLIX.INVENTARIO (" +
                    "codigo_producto, cantidad_afectada, motivo, operacion, usuario" +
                    ") VALUES (?, ?, ?, ?, ?)";

    private final String ACTUALIZAR_STOCK_PRODUCTO =
            "UPDATE TELLIX.PRODUCTO SET stock_actual = ? " +
                    "WHERE codigo = ?";

    // ===== NUEVOS SELECTS PARA CATÁLOGOS =====

    // Proveedores
    private final String SELECT_ALL_PROVEEDORES =
            "SELECT nit_proveedor, nombre, direccion_fiscal, telefono " +
                    "FROM TELLIX.PROVEEDOR " +
                    "ORDER BY nombre";

    // Representantes (usando la vista)
    private final String SELECT_ALL_REPRESENTANTES =
            "SELECT nit_proveedor, nit_representante, nombre_completo " +
                    "FROM TELLIX.VW_REPRESENTANTES_PROVEEDOR " +
                    "WHERE NIT_PROVEEDOR = ? " +
                    "ORDER BY nombre_completo";

    // Métodos de pago
    private final String SELECT_ALL_METODOS_PAGO =
            "SELECT codigo, descripcion " +
                    "FROM TELLIX.METODO_LIQUIDACION " +
                    "ORDER BY descripcion";

    private final String INSERTAR_CUENTA_POR_PAGAR = "INSERT INTO TELLIX.CUENTA_POR_PAGAR (" +
            "no_documento, " +
            "estado, " +
            "metodo_pago, " +
            "valor_total, " +
            "valor_pagado, " +
            "fecha_limite, " +
            "numero_cuenta, " +
            "banco) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


    // Getters existentes
    public String getSelectAllCompras()          { return SELECT_ALL_COMPRAS; }
    public String getSelectUnaCompra()           { return SELECT_UNA_COMPRA; }
    public String getSelectDetCompra()           { return SELECT_DET_COMPRA; }
    public String getInsertCompra()              { return INSERT_COMPRA; }
    public String getInsertDetalleCompra()       { return INSERT_DETALLE_COMPRA; }
    public String getINSERTAR_INVENTARIO()       { return INSERTAR_INVENTARIO; }
    public String getACTUALIZAR_STOCK_PRODUCTO() { return ACTUALIZAR_STOCK_PRODUCTO; }

    // Nuevos getters
    public String getSelectAllProveedores()      { return SELECT_ALL_PROVEEDORES; }
    public String getSelectAllRepresentantes()   { return SELECT_ALL_REPRESENTANTES; }
    public String getSelectAllMetodosPago()      { return SELECT_ALL_METODOS_PAGO; }

    public String getINSERTAR_CUENTA_POR_PAGAR() { return INSERTAR_CUENTA_POR_PAGAR; }
}
