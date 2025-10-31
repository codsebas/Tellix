package sql;

public class Sql {

    //En esta clase deben colocar todos los SQL que se utlizaran, ya sea para realizar sus cruds, o solamente las consultas.
    // ---------------------------------------------------------------------
    // --- CONSTANTES PARA TABLA CATEGORIA (CRUD) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_CATEGORIA = "INSERT INTO categoria (codigo, descripcion) VALUES (?, ?)";
    private final String CONSULTA_TODAS_CATEGORIAS = "SELECT codigo, descripcion FROM categoria ORDER BY codigo";
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

    // ---------------------------------------------------------------------
    // --- COMPRAS (HEADER) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_COMPRA = """
        INSERT INTO compra (
            no_documento, proveedor, representante, fecha_operacion, hora_operacion,
            usuario_sistema, fk_metodo_pago, plazo_credito, tipo_plazo, estado
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    private final String ACTUALIZAR_COMPRA = """
        UPDATE compra SET
            proveedor = ?, representante = ?, fecha_operacion = ?, hora_operacion = ?,
            usuario_sistema = ?, fk_metodo_pago = ?, plazo_credito = ?, tipo_plazo = ?, estado = ?
        WHERE no_documento = ?
        """;

    private final String ELIMINAR_COMPRA = """
        DELETE FROM compra WHERE no_documento = ?
        """;

    private final String CONSULTAR_COMPRA_POR_DOC = """
        SELECT
            c.no_documento, c.proveedor, p.nombre AS nombre_proveedor,
            c.representante, r.nombre1 || ' ' || r.apellido1 AS nombre_representante,
            c.fecha_operacion, c.hora_operacion, c.usuario_sistema,
            c.fk_metodo_pago, ml.descripcion AS metodo_pago,
            c.plazo_credito, c.tipo_plazo, c.estado
        FROM compra c
        LEFT JOIN proveedor p       ON p.nit_proveedor   = c.proveedor
        LEFT JOIN representante r   ON r.nit_representante = c.representante
        LEFT JOIN metodo_liquidacion ml ON ml.codigo      = c.fk_metodo_pago
        WHERE c.no_documento = ?
        """;

    private final String LISTAR_COMPRAS = """
        SELECT
            c.no_documento, c.fecha_operacion, c.hora_operacion, c.estado,
            c.proveedor, p.nombre AS nombre_proveedor,
            c.fk_metodo_pago, ml.descripcion AS metodo_pago,
            NVL( (SELECT SUM(dc.cantidad * dc.precio_bruto) - NVL(SUM(dc.descuentos),0) + NVL(SUM(dc.impuestos),0)
                  FROM detalle_compra dc
                  WHERE dc.no_documento = c.no_documento), 0) AS total_compra
        FROM compra c
        LEFT JOIN proveedor p ON p.nit_proveedor = c.proveedor
        LEFT JOIN metodo_liquidacion ml ON ml.codigo = c.fk_metodo_pago
        ORDER BY c.fecha_operacion DESC, c.no_documento DESC
        """;

    // ---------------------------------------------------------------------
    // --- DETALLE DE COMPRA ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_DETALLE_COMPRA = """
        INSERT INTO detalle_compra (
            no_documento, correlativo, codigo_producto, cantidad,
            precio_bruto, descuentos, impuestos
        ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    private final String ELIMINAR_DETALLE_COMPRA_POR_DOC = """
        DELETE FROM detalle_compra WHERE no_documento = ?
        """;

    private final String CONSULTAR_DETALLE_POR_DOC = """
        SELECT
            d.no_documento, d.correlativo,
            d.codigo_producto, p.nombre AS nombre_producto,
            d.cantidad, d.precio_bruto, d.descuentos, d.impuestos
        FROM detalle_compra d
        JOIN producto p ON p.codigo = d.codigo_producto
        WHERE d.no_documento = ?
        ORDER BY d.correlativo
        """;

    // ---------------------------------------------------------------------
    // --- INVENTARIO / STOCK (ingreso por compra) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_MOV_INV_INGRESO = """
        INSERT INTO inventario (
            codigo_producto, correlativo, cantidad_afectada, motivo,
            operacion, usuario, fecha_operacion, hora_operacion
        ) VALUES (?, ?, ?, ?, 'INGRESO', ?, ?, ?)
        """;

    private final String SUMAR_STOCK_PRODUCTO = """
        UPDATE producto
        SET stock_actual = NVL(stock_actual,0) + ?
        WHERE codigo = ?
        """;

    // ---------------------------------------------------------------------
    // --- CUENTAS POR PAGAR (para compras a CRÉDITO) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_CXP = """
        INSERT INTO cuenta_por_pagar (
            no_documento, estado, metodo_pago, valor, fecha_limite,
            numero_cuenta, banco
        ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    private final String ACTUALIZAR_CXP = """
        UPDATE cuenta_por_pagar SET
            estado = ?, metodo_pago = ?, valor = ?, fecha_limite = ?,
            numero_cuenta = ?, banco = ?
        WHERE no_documento = ?
        """;

    private final String CONSULTAR_CXP_POR_DOC = """
        SELECT
            cxp.no_documento, cxp.estado, cxp.metodo_pago, ml.descripcion AS metodo_pago_desc,
            cxp.valor, cxp.fecha_limite, cxp.numero_cuenta, cxp.banco
        FROM cuenta_por_pagar cxp
        LEFT JOIN metodo_liquidacion ml ON ml.codigo = cxp.metodo_pago
        WHERE cxp.no_documento = ?
        """;

    private final String LISTAR_CXP_PENDIENTES = """
        SELECT
            cxp.no_documento,
            NVL(cxp.valor,0) AS valor,
            cxp.estado, cxp.fecha_limite,
            c.proveedor, p.nombre AS nombre_proveedor
        FROM cuenta_por_pagar cxp
        JOIN compra c        ON c.no_documento = cxp.no_documento
        LEFT JOIN proveedor p ON p.nit_proveedor = c.proveedor
        WHERE cxp.estado = 'P'
        ORDER BY cxp.fecha_limite NULLS LAST, cxp.no_documento
        """;

    private final String ELIMINAR_CXP = """
        DELETE FROM cuenta_por_pagar WHERE no_documento = ?
        """;

    // ---------------------------------------------------------------------
    // --- PAGO A PROVEEDORES (registro de movimiento en cuenta bancaria) ---
    // ---------------------------------------------------------------------
    private final String INSERTAR_MOVIMIENTO_CUENTA = """
        INSERT INTO movimiento_cuenta (
            id_movimiento, cuenta_numero, tipo_documento, fecha_operacion,
            monto, descripcion
        ) VALUES (?, ?, ?, ?, ?, ?)
        """;

    private final String CONSULTAR_CUENTAS_BANCARIAS = """
        SELECT
            c.numero, c.titular, c.estado, c.descripcion,
            c.tipo_cuenta, tc.descripcion AS tipo_desc,
            c.banco_number, b.nombre AS banco_nombre
        FROM cuenta c
        LEFT JOIN tipo_cuenta tc ON tc.codigo = c.tipo_cuenta
        LEFT JOIN banco b ON b.codigo_number = c.banco_number
        ORDER BY b.nombre, c.numero
        """;

    // ---------------------------------------------------------------------
    // --- APOYO / CATÁLOGOS ---
    // ---------------------------------------------------------------------
    private final String LISTAR_PROVEEDORES = """
        SELECT nit_proveedor, nombre, direccion_fiscal, telefono
        FROM proveedor
        ORDER BY nombre
        """;

    private final String LISTAR_REPRESENTANTES_POR_PROVEEDOR = """
        SELECT
            nit_representante,
            nombre1 || ' ' || NVL(nombre2,'') || ' ' || apellido1 || ' ' || NVL(apellido2,'') AS nombre_completo
        FROM representante
        WHERE fk_proveedor_nit = ?
        ORDER BY apellido1, nombre1
        """;

    private final String LISTAR_METODOS_LIQUIDACION = """
        SELECT codigo, descripcion FROM metodo_liquidacion ORDER BY codigo
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
    public String getINSERTAR_COMPRA() { return INSERTAR_COMPRA; }
    public String getACTUALIZAR_COMPRA() { return ACTUALIZAR_COMPRA; }
    public String getELIMINAR_COMPRA() { return ELIMINAR_COMPRA; }
    public String getCONSULTAR_COMPRA_POR_DOC() { return CONSULTAR_COMPRA_POR_DOC; }
    public String getLISTAR_COMPRAS() { return LISTAR_COMPRAS; }

    public String getINSERTAR_DETALLE_COMPRA() { return INSERTAR_DETALLE_COMPRA; }
    public String getELIMINAR_DETALLE_COMPRA_POR_DOC() { return ELIMINAR_DETALLE_COMPRA_POR_DOC; }
    public String getCONSULTAR_DETALLE_POR_DOC() { return CONSULTAR_DETALLE_POR_DOC; }

    public String getINSERTAR_MOV_INV_INGRESO() { return INSERTAR_MOV_INV_INGRESO; }
    public String getSUMAR_STOCK_PRODUCTO() { return SUMAR_STOCK_PRODUCTO; }

    public String getINSERTAR_CXP() { return INSERTAR_CXP; }
    public String getACTUALIZAR_CXP() { return ACTUALIZAR_CXP; }
    public String getCONSULTAR_CXP_POR_DOC() { return CONSULTAR_CXP_POR_DOC; }
    public String getLISTAR_CXP_PENDIENTES() { return LISTAR_CXP_PENDIENTES; }
    public String getELIMINAR_CXP() { return ELIMINAR_CXP; }

    public String getINSERTAR_MOVIMIENTO_CUENTA() { return INSERTAR_MOVIMIENTO_CUENTA; }
    public String getCONSULTAR_CUENTAS_BANCARIAS() { return CONSULTAR_CUENTAS_BANCARIAS; }

    public String getLISTAR_PROVEEDORES() { return LISTAR_PROVEEDORES; }
    public String getLISTAR_REPRESENTANTES_POR_PROVEEDOR() { return LISTAR_REPRESENTANTES_POR_PROVEEDOR; }
    public String getLISTAR_METODOS_LIQUIDACION() { return LISTAR_METODOS_LIQUIDACION; }
}

