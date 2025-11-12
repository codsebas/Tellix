package sql;

public class query_vistas_db {
    private final String select_producto = "SELECT * FROM V_PRODUCTO_RESUMEN WHERE CODIGO = ?";
    private final String select_aplicacion = "SELECT * FROM V_PRODUCTO_RESUMEM WHERE CODIGO =? AND APLICACION = ?";

    public String getSelect_producto() {
        return select_producto;
    }

    public String getSelect_aplicacion() {
        return select_aplicacion;
    }
}
