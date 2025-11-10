package sql;

public class Sql {

    //En esta clase deben colocar todos los SQL que se utlizaran, ya sea para realizar sus cruds, o solamente las consultas.
    // ---------------------------------------------------------------------
    // --- CONSTANTES PARA TABLA CATEGORIA (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_CATEGORIA = "INSERT INTO categoria (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_CATEGORIAS = "SELECT codigo, descripcion FROM categoria";
    private final String CONSULTA_CATEGORIA_POR_CODIGO = "SELECT codigo, descripcion FROM categoria WHERE codigo = ?";
    private final String ACTUALIZAR_CATEGORIA = "UPDATE categoria SET descripcion = ? WHERE codigo = ?";
    private final String ELIMINAR_CATEGORIA = "DELETE FROM categoria WHERE codigo = ?";

    // ---------------------------------------------------------------------
    // --- CONSTANTES PARA TABLA PRODUCTO (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_PRODUCTO = """
            INSERT INTO Producto (
                codigo, nombre, descripcion, stock_minimo, stock_actual, 
                estado, fk_categoria, fk_marca, fk_medida, cantidad_medida
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private final String ACTUALIZAR_PRODUCTO = """
            UPDATE Producto SET
                nombre = ?, descripcion = ?, stock_minimo = ?, stock_actual = ?, 
                estado = ?, fk_categoria = ?, fk_marca = ?, fk_medida = ?, cantidad_medida = ?
            WHERE codigo = ?
            """;

    private final String ELIMINAR_PRODUCTO = "DELETE FROM Producto WHERE codigo = ?";

    // Consulta con JOINs para obtener los nombres descriptivos de las FKs (categoría, marca, medida)
    private final String CONSULTA_TODOS_PRODUCTOS = """
            SELECT 
                p.codigo, p.nombre, p.descripcion, p.stock_minimo, p.stock_actual, p.estado, 
                c.descripcion AS nombre_categoria, m.marca AS nombre_marca, md.descripcion AS nombre_medida, 
                p.cantidad_medida, p.fk_categoria, p.fk_marca, p.fk_medida
            FROM Producto p 
            JOIN categoria c ON p.fk_categoria = c.codigo
            JOIN marca m ON p.fk_marca = m.marca
            JOIN medida md ON p.fk_medida = md.codigo
            ORDER BY p.codigo
            """;

    private final String CONSULTA_PRODUCTO_POR_CODIGO = """
            SELECT 
                p.codigo, p.nombre, p.descripcion, p.stock_minimo, p.stock_actual, p.estado, 
                c.descripcion AS nombre_categoria, m.marca AS nombre_marca, md.descripcion AS nombre_medida, 
                p.cantidad_medida, p.fk_categoria, p.fk_marca, p.fk_medida
            FROM Producto p 
            JOIN categoria c ON p.fk_categoria = c.codigo
            JOIN marca m ON p.fk_marca = m.marca
            JOIN medida md ON p.fk_medida = md.codigo
            WHERE p.codigo = ?
            """;



    // ===== COMPRAS =====
    public static final String INSERT_COMPRA = """
    INSERT INTO compra (no_documento, proveedor, representante, fecha_operacion, hora_operacion,
                        usuario_sistema, fk_metodo_pago, plazo_credito, tipo_plazo, estado)
    VALUES (?, ?, ?, ?, SYSTIMESTAMP, ?, ?, ?, ?, ?)
  """;

    public static final String INSERT_DETALLE_COMPRA = """
    INSERT INTO detalle_compra (no_documento, correlativo, codigo_producto, cantidad, precio_bruto, descuentos, impuestos)
    VALUES (?, ?, ?, ?, ?, ?, ?)
  """;

    // correlativo de detalle (siguiente por compra)
    public static final String NEXT_CORR_DETALLE = """
    SELECT NVL(MAX(correlativo),0)+1 AS next_corr
    FROM detalle_compra
    WHERE no_documento = ?
  """;

    // ===== INVENTARIO =====
    // bitácora (correlativo por producto)
    public static final String NEXT_CORR_INVENTARIO = """
    SELECT NVL(MAX(correlativo),0)+1 AS next_corr
    FROM inventario
    WHERE codigo_producto = ?
  """;

    public static final String INSERT_MOV_INVENTARIO = """
    INSERT INTO inventario (codigo_producto, correlativo, cantidad_afectada, motivo, operacion,
                            usuario, fecha_operacion, hora_operacion)
    VALUES (?, ?, ?, ?, ?, ?, SYSDATE, SYSTIMESTAMP)
  """;

    // actualizar stock_actual en PRODUCTO (entrada por compra)
    public static final String UPDATE_STOCK_PRODUCTO = """
    UPDATE producto
    SET stock_actual = stock_actual + ?
    WHERE codigo = ?
  """;

    // ===== CUENTAS POR PAGAR =====
    public static final String INSERT_CXP = """
    INSERT INTO cuenta_por_pagar (no_documento, estado, metodo_pago, valor, fecha_limite, numero_cuenta, banco)
    VALUES (?, ?, ?, ?, ?, ?, ?)
  """;

    public static final String UPDATE_CXP_ESTADO = """
    UPDATE cuenta_por_pagar SET estado = ? WHERE no_documento = ?
  """;

    // ===== MOVIMIENTOS BANCARIOS (usados como pagos a proveedor) =====
    public static final String INSERT_MOVIMIENTO_CUENTA = """
    INSERT INTO movimiento_cuenta (id_movimiento, cuenta_numero, tipo_documento, fecha_operacion, monto, descripcion)
    VALUES (SEQ_MOV_CTA.NEXTVAL, ?, ?, SYSDATE, ?, ?)
  """;

    // total pagado a una compra, usando movimientos etiquetados
    public static final String SUM_PAGOS_COMPRA = """
    SELECT NVL(SUM(monto),0) AS total_pagado
    FROM movimiento_cuenta
    WHERE tipo_documento = 'CXP' AND descripcion = ?
  """;

    // total de la compra (para validar contra CxP.valor)
    public static final String TOTAL_COMPRA = """
    SELECT NVL(SUM( (precio_bruto - NVL(descuentos,0)) + NVL(impuestos,0) ),0) AS total
    FROM detalle_compra
    WHERE no_documento = ?
  """;





    // --- CONSTRUCTOR ---
    public Sql() {
    }
    // ---------------------------------------------------------------------
    // --- MÉTODOS GETTERS PARA CATEGORIA ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_CATEGORIA() {
        return INSERTAR_CATEGORIA;
    }

    public String getCONSULTA_TODAS_CATEGORIAS() {
        return CONSULTA_TODAS_CATEGORIAS;
    }

    public String getCONSULTA_CATEGORIA_POR_CODIGO() {
        return CONSULTA_CATEGORIA_POR_CODIGO;
    }

    public String getACTUALIZAR_CATEGORIA() {
        return ACTUALIZAR_CATEGORIA;
    }

    public String getELIMINAR_CATEGORIA() {
        return ELIMINAR_CATEGORIA;
    }
    // ---------------------------------------------------------------------
    // --- MÉTODOS GETTERS PARA PRODUCTO ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_PRODUCTO() {
        return INSERTAR_PRODUCTO;
    }

    public String getCONSULTA_TODOS_PRODUCTOS() {
        return CONSULTA_TODOS_PRODUCTOS;
    }

    public String getCONSULTA_PRODUCTO_POR_CODIGO() {
        return CONSULTA_PRODUCTO_POR_CODIGO;
    }

    public String getACTUALIZAR_PRODUCTO() {
        return ACTUALIZAR_PRODUCTO;
    }

    public String getELIMINAR_PRODUCTO() {
        return ELIMINAR_PRODUCTO;
    }

    // ====== Getters públicos ======
    public static String getInsertCompra() { return INSERT_COMPRA; }
    public static String getInsertDetalleCompra() { return INSERT_DETALLE_COMPRA; }
    public static String getNextCorrDetalle() { return NEXT_CORR_DETALLE; }
    public static String getNextCorrInventario() { return NEXT_CORR_INVENTARIO; }
    public static String getInsertMovInventario() { return INSERT_MOV_INVENTARIO; }
    public static String getUpdateStockProducto() { return UPDATE_STOCK_PRODUCTO; }
    public static String getInsertCxp() { return INSERT_CXP; }
    public static String getUpdateCxpEstado() { return UPDATE_CXP_ESTADO; }
    public static String getInsertMovimientoCuenta() { return INSERT_MOVIMIENTO_CUENTA; }
    public static String getSumPagosCompra() { return SUM_PAGOS_COMPRA; }
    public static String getTotalCompra() { return TOTAL_COMPRA; }
}

