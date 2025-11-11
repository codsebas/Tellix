package sql;

public class Sql {

    //
    //
    // ---------------------------------------------------------------------
    // --- CONSTANTES PARA TABLA MARCAS (CRUD) ---
    // ---------------------------------------------------------------------//En esta clase deben colocar todos los SQL que se utlizaran, ya sea para realizar sus cruds, o solamente las consultas.
    // MARCA
    private final String INSERTAR_MARCA = "INSERT INTO marca (marca, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_MARCAS = "SELECT marca, descripcion FROM marca";
    private final String CONSULTA_MARCA_POR_CODIGO = "SELECT marca, descripcion FROM marca WHERE marca = ?";
    private final String ACTUALIZAR_MARCA = "UPDATE marca SET descripcion = ? WHERE marca = ?";
    private final String ELIMINAR_MARCA = "DELETE FROM marca WHERE marca = ?";
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
    // --- CONSTANTES PARA TABLA CATEGORIA (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_DESCUENTOS = "INSERT INTO descuentos (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_DESCUENTOS = "SELECT codigo, descripcion FROM descuentos";
    private final String CONSULTA_DESCUENTOS_POR_CODIGO = "SELECT codigo, descripcion FROM descuentos WHERE codigo = ?";
    private final String ACTUALIZAR_DESCUENTOS = "UPDATE descuentos SET descripcion = ? WHERE codigo = ?";
    private final String ELIMINAR_DESCUENTOS = "DELETE FROM descuentos WHERE codigo = ?";
    // Para llenar ComboBox por código
    private final String CONSULTA_CODIGOS_DESCUENTOS = "SELECT codigo FROM descuentos ORDER BY codigo";

    // Para llenar ComboBox por descripción
    private final String CONSULTA_DESCRIPCIONES_DESCUENTOS = "SELECT descripcion FROM descuentos ORDER BY descripcion";

    // Para búsqueda insensible a mayúsculas/minúsculas
    private final String BUSCAR_DESCUENTOS = """
    SELECT codigo, descripcion 
    FROM categoria 
    WHERE UPPER(codigo) LIKE UPPER(?) OR UPPER(descripcion) LIKE UPPER(?) 
    ORDER BY codigo
""";
    // --- CONSTANTES PARA TABLA CATEGORIA (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_IMPUESTOS = "INSERT INTO impuestos (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_IMPUESTOS = "SELECT codigo, descripcion FROM impuestos";
    private final String CONSULTA_IMPUESTOS_POR_CODIGO = "SELECT codigo, descripcion FROM impuestos WHERE codigo = ?";
    private final String ACTUALIZAR_IMPUESTOS = "UPDATE impuestos SET descripcion = ? WHERE codigo = ?";
    private final String ELIMINAR_IMPUESTOS = "DELETE FROM impuestos WHERE codigo = ?";
    // Para llenar ComboBox por código
    private final String CONSULTA_CODIGOS_IMPUESTOS = "SELECT codigo FROM impuestos ORDER BY codigo";

    // Para llenar ComboBox por descripción
    private final String CONSULTA_DESCRIPCIONES_IMPUESTOS = "SELECT descripcion FROM impuestos ORDER BY descripcion";

    // Para búsqueda insensible a mayúsculas/minúsculas
    private final String BUSCAR_IMPUESTOS = """
    SELECT codigo, descripcion 
    FROM categoria 
    WHERE UPPER(codigo) LIKE UPPER(?) OR UPPER(descripcion) LIKE UPPER(?) 
    ORDER BY codigo
""";
    // --- CONSTANTES PARA TABLA METODOSDELIQUIDACION (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_METODOSDELIQUIDACION = "INSERT INTO metodosdeliquidacion (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_METODOSDELIQUIDACION = "SELECT codigo, descripcion FROM metodosdeliquidacion";
    private final String CONSULTA_METODOSDELIQUIDACION_POR_CODIGO = "SELECT codigo, descripcion FROM metodosdeliquidacion WHERE codigo = ?";
    private final String ACTUALIZAR_METODOSDELIQUIDACION = "UPDATE metodosdeliquidacion SET descripcion = ? WHERE codigo = ?";
    private final String ELIMINAR_METODOSDELIQUIDACION = "DELETE FROM metodosdeliquidacion WHERE codigo = ?";
    // Para llenar ComboBox por código
    private final String CONSULTA_CODIGOS_METODOSDELIQUIDACION = "SELECT codigo FROM metodosdeliquidacion ORDER BY codigo";

    // Para llenar ComboBox por descripción
    private final String CONSULTA_DESCRIPCIONES_METODOSDELIQUIDACION = "SELECT descripcion FROM categoria ORDER BY descripcion";

    // Para búsqueda insensible a mayúsculas/minúsculas
    private final String BUSCAR_METODOSDELIQUIDACION = """
    SELECT codigo, descripcion 
    FROM metodosdeliquidacion 
    WHERE UPPER(codigo) LIKE UPPER(?) OR UPPER(descripcion) LIKE UPPER(?) 
    ORDER BY codigo
""";
    // --- CONSTANTES PARA TABLA BANCOS (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_BANCOS = "INSERT INTO bancos (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_BANCOS = "SELECT codigo, descripcion FROM bancos";
    private final String CONSULTA_BANCOS_POR_CODIGO = "SELECT codigo, descripcion FROM bancos WHERE codigo = ?";
    private final String ACTUALIZAR_BANCOS = "UPDATE bancos SET descripcion = ? WHERE codigo = ?";
    private final String ELIMINAR_BANCOS = "DELETE FROM bancos WHERE codigo = ?";
    // Para llenar ComboBox por código
    private final String CONSULTA_CODIGOS_BANCOS = "SELECT codigo FROM bancos ORDER BY codigo";

    // Para llenar ComboBox por descripción
    private final String CONSULTA_DESCRIPCIONES_BANCOS = "SELECT descripcion FROM bancos ORDER BY descripcion";

    // Para búsqueda insensible a mayúsculas/minúsculas
    private final String BUSCAR_BANCOS = """
    SELECT codigo, descripcion 
    FROM bancos 
    WHERE UPPER(codigo) LIKE UPPER(?) OR UPPER(descripcion) LIKE UPPER(?) 
    ORDER BY codigo
""";
    // --- CONSTANTES PARA TABLA TIPOSDECUENTA (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_TIPOSDECUENTA = "INSERT INTO tiposdecuenta (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_TIPOSDECUENTA = "SELECT codigo, descripcion FROM tiposdecuenta";
    private final String CONSULTA_TIPOSDECUENTA_POR_CODIGO = "SELECT codigo, descripcion FROM tiposdecuenta WHERE codigo = ?";
    private final String ACTUALIZAR_TIPOSDECUENTA = "UPDATE tiposdecuenta SET descripcion = ? WHERE codigo = ?";
    private final String ELIMINAR_TIPOSDECUENTA = "DELETE FROM tiposdecuenta WHERE codigo = ?";
    // Para llenar ComboBox por código
    private final String CONSULTA_CODIGOS_TIPOSDECUENTA = "SELECT codigo FROM tiposdecuenta ORDER BY codigo";

    // Para llenar ComboBox por descripción
    private final String CONSULTA_DESCRIPCIONES_TIPOSDECUENTA = "SELECT descripcion FROM tiposdecuenta ORDER BY descripcion";

    // Para búsqueda insensible a mayúsculas/minúsculas
    private final String BUSCAR_TIPOSDECUENTA = """
    SELECT codigo, descripcion 
    FROM tiposdecuenta 
    WHERE UPPER(codigo) LIKE UPPER(?) OR UPPER(descripcion) LIKE UPPER(?) 
    ORDER BY codigo
""";
    // --- CONSTANTES PARA TABLA TIPOSDECONTACTO (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_TIPOSDECONTACTO = "INSERT INTO tiposdecontacto (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_TIPOSDECONTACTO = "SELECT codigo, descripcion FROM tiposdecontacto";
    private final String CONSULTA_TIPOSDECONTACTO_POR_CODIGO = "SELECT codigo, descripcion FROM tiposdecontacto WHERE codigo = ?";
    private final String ACTUALIZAR_TIPOSDECONTACTO = "UPDATE tiposdeecontacto SET descripcion = ? WHERE codigo = ?";
    private final String ELIMINAR_TIPOSDECONTACTO = "DELETE FROM c<tiposdeontacto WHERE codigo = ?";
    // Para llenar ComboBox por código
    private final String CONSULTA_CODIGOS_TIPOSDECONTACTO = "SELECT codigo FROM contacto ORDER BY codigo";

    // Para llenar ComboBox por descripción
    private final String CONSULTA_DESCRIPCIONES_TIPOSDECONTACTO = "SELECT descripcion FROM contacto ORDER BY descripcion";

    // Para búsqueda insensible a mayúsculas/minúsculas
    private final String BUSCAR_TIPOSDECONTACTO = """
    SELECT codigo, descripcion 
    FROM tiposdeontacto 
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
    // --- MÉTODOS GETTERS PARA DESCUENTOS ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_DESCUENTOS() {
        return INSERTAR_DESCUENTOS;
    }

    public String getCONSULTA_TODAS_DESCUENTOS() {
        return CONSULTA_TODAS_CATEGORIAS;
    }

    public String getCONSULTA_DESCUENTOS_POR_CODIGO() {
        return CONSULTA_CATEGORIA_POR_CODIGO;
    }

    public String getACTUALIZAR_DESCUENTOS() {
        return ACTUALIZAR_CATEGORIA;
    }

    public String getELIMINAR_DESCUENTOS() {
        return ELIMINAR_DESCUENTOS;
    }
    // --- MÉTODOS GETTERS PARA IMPUESTOS ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_IMPUESTOS() {
        return INSERTAR_CATEGORIA;
    }

    public String getCONSULTA_TODAS_IMPUESTOS() {
        return CONSULTA_TODAS_IMPUESTOS;
    }

    public String getCONSULTA_IMPUESTOS_POR_CODIGO() {
        return CONSULTA_CATEGORIA_POR_CODIGO;
    }

    public String getACTUALIZAR_IMPUESTOS() {
        return ACTUALIZAR_CATEGORIA;
    }

    public String getELIMINAR_IMPUESTOS() {
        return ELIMINAR_CATEGORIA;
    }
    // --- MÉTODOS GETTERS PARA METODOSDELIQUIDACION ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_METODOSDELIQUIDACION() {
        return INSERTAR_METODOSDELIQUIDACION;
    }

    public String getCONSULTA_TODAS_METODOSDELIQUIDACION() {
        return CONSULTA_TODAS_METODOSDELIQUIDACION;
    }

    public String getCONSULTA_METODOSDELIQUIDACION_POR_CODIGO() {
        return CONSULTA_METODOSDELIQUIDACION_POR_CODIGO;
    }

    public String getACTUALIZAR_METODOSDELIQUIDACION() {
        return ACTUALIZAR_METODOSDELIQUIDACION;
    }

    public String getELIMINAR_METODOSDELIQUIDACION() {
        return ELIMINAR_METODOSDELIQUIDACION;
    }
    // --- MÉTODOS GETTERS PARA TIPOSDECUENTA ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_TIPOSDECUENTA() {
        return INSERTAR_TIPOSDECUENTA;
    }

    public String getCONSULTA_TODAS_TIPOSDECUENTA() {
        return CONSULTA_TODAS_TIPOSDECUENTA;
    }

    public String getCONSULTA_TIPOSDECUENTA_POR_CODIGO() {
        return CONSULTA_TIPOSDECUENTA_POR_CODIGO;
    }

    public String getACTUALIZAR_TIPOSDECUENTA() {
        return ACTUALIZAR_TIPOSDECUENTA;
    }

    public String getELIMINAR_TIPOSDECUENTA() {
        return ELIMINAR_TIPOSDECUENTA;
    }
    // --- MÉTODOS GETTERS PARA BANCOS ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_BANCOS() {
        return INSERTAR_BANCOS;
    }

    public String getCONSULTA_TODAS_BANCOS() {
        return CONSULTA_TODAS_BANCOS;
    }

    public String getCONSULTA_BANCOS_POR_CODIGO() {
        return CONSULTA_BANCOS_POR_CODIGO;
    }

    public String getACTUALIZAR_BANCOS() {
        return ACTUALIZAR_BANCOS;
    }

    public String getELIMINAR_BANCOS() {
        return ELIMINAR_BANCOS;
    }
    // --- MÉTODOS GETTERS PARA TIPOSDECONTACTO ---
    // ---------------------------------------------------------------------
    public String getINSERTAR_TIPOSDECONTACTO() {
        return INSERTAR_TIPOSDECONTACTO;
    }

    public String getCONSULTA_TODAS_MEDIDA() {
        return CONSULTA_TODAS_TIPOSDECONTACTO;
    }

    public String getCONSULTA_TIPOSDECONTACTO_POR_CODIGO() {
        return CONSULTA_TIPOSDECONTACTO_POR_CODIGO;
    }

    public String getACTUALIZAR_MEDIDA() {
        return ACTUALIZAR_TIPOSDECONTACTO;
    }

    public String getELIMINAR_TIPOSDECONTACTO() {
        return ELIMINAR_TIPOSDECONTACTO;
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
    
    public String getCONSULTA_CODIGOS_TIPOSDECONTACTO() { return CONSULTA_CODIGOS_TIPOSDECONTACTO; }
    public String getCONSULTA_DESCRIPCIONES_TIPOSDECONTACTO() { return CONSULTA_DESCRIPCIONES_TIPOSDECONTACTO; }
    public String getBUSCAR_TIPOSDECONTACTO() { return BUSCAR_TIPOSDECONTACTO; }
    
    public String getCONSULTA_CODIGOS_BANCOS() { return CONSULTA_CODIGOS_BANCOS; }
    public String getCONSULTA_DESCRIPCIONES_BANCOS() { return CONSULTA_DESCRIPCIONES_BANCOS; }
    public String getBUSCAR_BANCOS() { return BUSCAR_BANCOS; }

    public String getCONSULTA_CODIGOS_TIPOSDECUENTA() { return CONSULTA_CODIGOS_TIPOSDECUENTA; }
    public String getCONSULTA_DESCRIPCIONES_TIPOSDECUENTA() { return CONSULTA_DESCRIPCIONES_TIPOSDECUENTA; }
    public String getBUSCAR_TIPOSDECUENTA() { return BUSCAR_TIPOSDECUENTA; }
    
    public String getCONSULTA_CODIGOS_METODOSDELIQUIDACION() { return CONSULTA_CODIGOS_METODOSDELIQUIDACION; }
    public String getCONSULTA_DESCRIPCIONES_METODOSDELIQUIDACION() { return CONSULTA_DESCRIPCIONES_METODOSDELIQUIDACION; }
    public String getBUSCAR_METODOSDELIQUIDACION() { return BUSCAR_METODOSDELIQUIDACION; }
    
    public String getCONSULTA_CODIGOS_IMPUESTOS() { return CONSULTA_CODIGOS_IMPUESTOS; }
    public String getCONSULTA_DESCRIPCIONES_IMPUESTOS() { return CONSULTA_DESCRIPCIONES_IMPUESTOS; }
    public String getBUSCAR_IMPUESTOS() { return BUSCAR_IMPUESTOS; }
    
    public String getCONSULTA_CODIGOS_DESCUENTOS() { return CONSULTA_CODIGOS_DESCUENTOS; }
    public String getCONSULTA_DESCRIPCIONES_DESCUENTOS() { return CONSULTA_DESCRIPCIONES_DESCUENTOS; }
    public String getBUSCAR_DESCUENTOS() { return BUSCAR_DESCUENTOS; }
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
    // Luego agrega los getters
    public String getINSERTAR_MARCA() { return INSERTAR_MARCA; }
    public String getCONSULTA_TODAS_MARCAS() { return CONSULTA_TODAS_MARCAS; }
    public String getCONSULTA_MARCA_POR_CODIGO() { return CONSULTA_MARCA_POR_CODIGO; }
    public String getACTUALIZAR_MARCA() { return ACTUALIZAR_MARCA; }
    public String getELIMINAR_MARCA() { return ELIMINAR_MARCA; }
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

