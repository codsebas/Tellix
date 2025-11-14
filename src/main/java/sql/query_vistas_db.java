package sql;

public class query_vistas_db {
    private final String SELECT_PRODUCTO = "SELECT * FROM TELLIX.V_PRODUCTO_RESUMEN  WHERE CODIGO = ?";
    private final String SELECT_APLICACION = "SELECT * FROM TELLIX.V_PRODUCTO_RESUMEN WHERE CODIGO =? AND APLICACION = ?";
    private final String SELECT_CLIENTE_NIT = "SELECT * FROM TELLIX.V_CLIENTE_NOMBRE WHERE NIT = ?";
    private final String SELECT_CLIENTE_NOMBRE = "SELECT * FROM TELLIX.V_CLIENTE_NOMBRE WHERE NOMBRE LIKE %?%";
    private final String SELECT_REPRESENTANTE_PROVEEDOR = "SELECT * FROM TELLIX.VW_REPRESENTANTES_PROVEEDOR WHERE NIT_PROVEEDOR = ?";
    public String getSELECT_PRODUCTO() {
        return SELECT_PRODUCTO;
    }

    public String getSELECT_APLICACION() {
        return SELECT_APLICACION;
    }

    public String getSELECT_CLIENTE_NIT() {
        return SELECT_CLIENTE_NIT;
    }

    public String getSELECT_CLIENTE_NOMBRE() {
        return SELECT_CLIENTE_NOMBRE;
    }

    public String getSELECT_REPRESENTANTE_PROVEEDOR() {
        return SELECT_REPRESENTANTE_PROVEEDOR;
    }
}
