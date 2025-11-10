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
    // Para llenar ComboBox por código
    private final String CONSULTA_CODIGOS_CATEGORIA = "SELECT codigo FROM categoria ORDER BY codigo";

    // Para llenar ComboBox por descripción
    private final String CONSULTA_DESCRIPCIONES_CATEGORIA = "SELECT descripcion FROM categoria ORDER BY descripcion";

    // Para búsqueda insensible a mayúsculas/minúsculas
    private final String BUSCAR_CATEGORIA = """
    SELECT codigo, descripcion 
    FROM categoria 
    WHERE UPPER(codigo) LIKE UPPER(?) OR UPPER(descripcion) LIKE UPPER(?) 
    ORDER BY codigo
""";
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

// ---------------------------------------------------------------------
// --- CONSULTAS DE REPORTES (prefijo tellix)
// ---------------------------------------------------------------------

// ===== VENTAS =====

    // Ventas del día
    private final String REPORTE_VENTAS_DIA = """
    SELECT v.secuencia, v.cliente, v.fecha_operacion, dv.codigo_producto, dv.cantidad,
           dv.precio_bruto, (dv.precio_bruto * dv.cantidad) AS importe
    FROM tellix.venta v
    JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
    WHERE v.fecha_operacion = TRUNC(SYSDATE)
    ORDER BY v.secuencia
""";

    // Ventas por rango de fechas
    private final String REPORTE_VENTAS_RANGO = """
    SELECT v.secuencia, v.cliente, v.fecha_operacion, dv.codigo_producto, dv.cantidad, dv.precio_bruto
    FROM tellix.venta v
    JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
    WHERE v.fecha_operacion BETWEEN ? AND ?
    ORDER BY v.fecha_operacion
""";

    // Total de ventas por rango
    private final String REPORTE_TOTAL_VENTAS_RANGO = """
    SELECT SUM(dv.precio_bruto * dv.cantidad) AS total_rango
    FROM tellix.venta v
    JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
    WHERE v.fecha_operacion BETWEEN ? AND ?
""";

    // Ventas mensuales (últimos 6 meses)
    private final String REPORTE_VENTAS_MENSUALES = """
    SELECT TO_CHAR(v.fecha_operacion, 'YYYY-MM') AS mes,
           SUM(dv.precio_bruto * dv.cantidad) AS total_mes
    FROM tellix.venta v
    JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
    WHERE v.fecha_operacion >= ADD_MONTHS(TRUNC(SYSDATE, 'MM'), -5)
    GROUP BY TO_CHAR(v.fecha_operacion, 'YYYY-MM')
    ORDER BY mes
""";

// ===== COMPRAS =====

    // Compras del día
    private final String REPORTE_COMPRAS_DIA = """
    SELECT c.no_documento, c.proveedor, c.fecha_operacion,
           dc.codigo_producto, dc.cantidad, dc.precio_bruto
    FROM tellix.compra c
    JOIN tellix.detalle_compra dc ON dc.no_documento = c.no_documento
    WHERE c.fecha_operacion = TRUNC(SYSDATE)
    ORDER BY c.no_documento
""";

    // Compras por rango
    private final String REPORTE_COMPRAS_RANGO = """
    SELECT c.no_documento, c.proveedor, c.fecha_operacion,
           SUM(dc.cantidad * dc.precio_bruto) AS total_doc
    FROM tellix.compra c
    JOIN tellix.detalle_compra dc ON dc.no_documento = c.no_documento
    WHERE c.fecha_operacion BETWEEN ? AND ?
    GROUP BY c.no_documento, c.proveedor, c.fecha_operacion
    ORDER BY c.fecha_operacion
""";

    // Compras por proveedor
    private final String REPORTE_COMPRAS_PROVEEDOR = """
    SELECT c.proveedor, COUNT(DISTINCT c.no_documento) AS documentos,
           SUM(dc.cantidad * dc.precio_bruto) AS total_compras
    FROM tellix.compra c
    JOIN tellix.detalle_compra dc ON dc.no_documento = c.no_documento
    GROUP BY c.proveedor
    ORDER BY total_compras DESC
""";

// ===== CLIENTES Y PRODUCTOS =====

    // Mejores clientes por monto y facturas
    private final String REPORTE_MEJORES_CLIENTES = """
    SELECT v.cliente,
           SUM(dv.precio_bruto * dv.cantidad) AS total_monto,
           COUNT(DISTINCT v.secuencia) AS facturas
    FROM tellix.venta v
    JOIN tellix.detalle_venta dv ON dv.secuencia = v.secuencia
    WHERE v.fecha_operacion BETWEEN ? AND ?
    GROUP BY v.cliente
    ORDER BY total_monto DESC
""";

    // Productos más vendidos por cantidad
    private final String REPORTE_PRODUCTOS_MAS_VENDIDOS_CANT = """
    SELECT dv.codigo_producto, SUM(dv.cantidad) AS total_cantidad
    FROM tellix.detalle_venta dv
    JOIN tellix.venta v ON v.secuencia = dv.secuencia
    WHERE v.fecha_operacion BETWEEN ? AND ?
    GROUP BY dv.codigo_producto
    ORDER BY total_cantidad DESC
""";

    // Productos más vendidos por monto
    private final String REPORTE_PRODUCTOS_MAS_VENDIDOS_MONTO = """
    SELECT dv.codigo_producto, SUM(dv.cantidad * dv.precio_bruto) AS total_monto
    FROM tellix.detalle_venta dv
    JOIN tellix.venta v ON v.secuencia = dv.secuencia
    WHERE v.fecha_operacion BETWEEN ? AND ?
    GROUP BY dv.codigo_producto
    ORDER BY total_monto DESC
""";

    // Productos con stock bajo
    private final String REPORTE_STOCK_BAJO = """
    SELECT codigo, nombre, stock_actual, stock_minimo
    FROM tellix.producto
    WHERE stock_actual <= stock_minimo
    ORDER BY stock_actual ASC
""";

// ===== CUENTAS POR COBRAR / PAGAR =====

    // Cuentas por cobrar vencidas
    private final String REPORTE_CXC_VENCIDAS = """
    SELECT *
    FROM tellix.cuenta_por_cobrar
    WHERE estado = 'P' AND fecha_limite < TRUNC(SYSDATE)
    ORDER BY fecha_limite
""";

    // Cuentas por cobrar próximas (7 días)
    private final String REPORTE_CXC_PROXIMAS = """
    SELECT *
    FROM tellix.cuenta_por_cobrar
    WHERE estado = 'P' AND fecha_limite BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE) + 7
    ORDER BY fecha_limite
""";

    // Cuentas por pagar vencidas
    private final String REPORTE_CXP_VENCIDAS = """
    SELECT *
    FROM tellix.cuenta_por_pagar
    WHERE estado = 'P' AND fecha_limite < TRUNC(SYSDATE)
    ORDER BY fecha_limite
""";

    // Cuentas por pagar próximas (7 días)
    private final String REPORTE_CXP_PROXIMAS = """
    SELECT *
    FROM tellix.cuenta_por_pagar
    WHERE estado = 'P' AND fecha_limite BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE) + 7
    ORDER BY fecha_limite
""";

// ===== INVENTARIO =====

    // Movimientos de inventario por rango de fechas
    private final String REPORTE_INVENTARIO_RANGO = """
    SELECT i.codigo_producto, i.operacion, i.cantidad_afectada, i.motivo,
           i.fecha_operacion, i.usuario
    FROM tellix.inventario i
    WHERE i.fecha_operacion BETWEEN ? AND ?
    ORDER BY i.codigo_producto, i.fecha_operacion
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
    public String getCONSULTA_CODIGOS_CATEGORIA() { return CONSULTA_CODIGOS_CATEGORIA; }
    public String getCONSULTA_DESCRIPCIONES_CATEGORIA() { return CONSULTA_DESCRIPCIONES_CATEGORIA; }
    public String getBUSCAR_CATEGORIA() { return BUSCAR_CATEGORIA; }

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

    public String getREPORTE_VENTAS_DIA() {
        return REPORTE_VENTAS_DIA;
    }

    public String getREPORTE_VENTAS_RANGO() {
        return REPORTE_VENTAS_RANGO;
    }

    public String getREPORTE_TOTAL_VENTAS_RANGO() {
        return REPORTE_TOTAL_VENTAS_RANGO;
    }

    public String getREPORTE_VENTAS_MENSUALES() {
        return REPORTE_VENTAS_MENSUALES;
    }

    public String getREPORTE_COMPRAS_DIA() {
        return REPORTE_COMPRAS_DIA;
    }

    public String getREPORTE_COMPRAS_RANGO() {
        return REPORTE_COMPRAS_RANGO;
    }

    public String getREPORTE_COMPRAS_PROVEEDOR() {
        return REPORTE_COMPRAS_PROVEEDOR;
    }

    public String getREPORTE_MEJORES_CLIENTES() {
        return REPORTE_MEJORES_CLIENTES;
    }

    public String getREPORTE_PRODUCTOS_MAS_VENDIDOS_CANT() {
        return REPORTE_PRODUCTOS_MAS_VENDIDOS_CANT;
    }

    public String getREPORTE_PRODUCTOS_MAS_VENDIDOS_MONTO() {
        return REPORTE_PRODUCTOS_MAS_VENDIDOS_MONTO;
    }

    public String getREPORTE_STOCK_BAJO() {
        return REPORTE_STOCK_BAJO;
    }

    public String getREPORTE_CXC_VENCIDAS() {
        return REPORTE_CXC_VENCIDAS;
    }

    public String getREPORTE_CXC_PROXIMAS() {
        return REPORTE_CXC_PROXIMAS;
    }

    public String getREPORTE_CXP_VENCIDAS() {
        return REPORTE_CXP_VENCIDAS;
    }

    public String getREPORTE_CXP_PROXIMAS() {
        return REPORTE_CXP_PROXIMAS;
    }

    public String getREPORTE_INVENTARIO_RANGO() {
        return REPORTE_INVENTARIO_RANGO;
    }
}

