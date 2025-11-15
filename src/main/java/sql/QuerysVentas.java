package sql;

public class QuerysVentas {

    // Todas las ventas (puedes quitar el ORDER BY si no lo quieres)
    private final String SELECT_ALL_VENTAS =
            "SELECT * FROM TELLIX.VENTA ORDER BY secuencia DESC";

    // Una venta por secuencia
    private final String SELECT_UNA_VENTA =
            "SELECT * FROM TELLIX.VENTA WHERE secuencia = ?";

    // Detalle de una venta (OJO: va contra DETALLE_VENTA)
    private final String SELECT_DET_VENTA =
            "SELECT * FROM TELLIX.DETALLE_VENTA " +
                    "WHERE secuencia = ? " +
                    "ORDER BY correlativo ASC";

    // Insert de la cabecera de venta (no incluimos secuencia porque es IDENTITY)
    private final String INSERT_VENTA =
            "INSERT INTO TELLIX.VENTA (" +
                    "cliente, " +
                    "fecha_operacion, " +
                    "hora_operacion, " +
                    "usuario_sistema, " +
                    "fk_metodo_pago, " +
                    "plazo_credito, " +
                    "tipo_plazo, " +
                    "estado, " +
                    "total" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // Insert del detalle de venta (no incluimos correlativo porque es IDENTITY)
    private final String INSERT_DETALLE_VENTA =
            "INSERT INTO TELLIX.DETALLE_VENTA (" +
                    "secuencia, " +
                    "codigo_producto, " +
                    "cantidad, " +
                    "precio_bruto, " +
                    "descuentos, " +
                    "impuestos" +
                    ") VALUES (?, ?, ?, ?, ?, ?)";

    private final String INSERTAR_INVENTARIO = "INSERT INTO TELLIX.INVENTARIO (" +
            "codigo_producto, cantidad_afectada, motivo, operacion, usuario) VALUES (?, ?, ?, ?, ?)";

    private final String ACTUALIZAR_STOCK_PRODUCTO = "UPDATE TELLIX.PRODUCTO SET stock_actual = ? " +
            "WHERE codigo = ?";

    private final String SELECT_ALL_METODOS_PAGO =
            "SELECT codigo, descripcion " +
                    "FROM TELLIX.METODO_LIQUIDACION " +
                    "ORDER BY descripcion";

    private final String INSERTAR_CUENTA_POR_COBRAR = "INSERT INTO TELLIX.CUENTA_POR_COBRAR (" +
            "secuencia, " +
            "estado, " +
            "metodo_pago, " +
            "valor_total, " +
            "valor_pagado, " +
            "fecha_limite, " +
            "numero_cuenta, " +
            "cliente_nit) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


    public String getSelectAllVentas() { return SELECT_ALL_VENTAS; }
    public String getSelectUnaVenta() { return SELECT_UNA_VENTA; }
    public String getSelectDetVenta() { return SELECT_DET_VENTA; }
    public String getInsertVenta() { return INSERT_VENTA; }
    public String getInsertDetalleVenta() { return INSERT_DETALLE_VENTA; }
    public String getINSERTAR_INVENTARIO() { return INSERTAR_INVENTARIO; }
    public String getACTUALIZAR_STOCK_PRODUCTO() { return ACTUALIZAR_STOCK_PRODUCTO; }
    public String getSELECT_ALL_METODOS_PAGO(){ return SELECT_ALL_METODOS_PAGO;}
    public String getINSERTAR_CUENTA_POR_COBRAR(){ return INSERTAR_CUENTA_POR_COBRAR; }
}
