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

    public String getSelectAllVentas() { return SELECT_ALL_VENTAS; }
    public String getSelectUnaVenta() { return SELECT_UNA_VENTA; }
    public String getSelectDetVenta() { return SELECT_DET_VENTA; }
    public String getInsertVenta() { return INSERT_VENTA; }
    public String getInsertDetalleVenta() { return INSERT_DETALLE_VENTA; }
}
