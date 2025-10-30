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
}
}
