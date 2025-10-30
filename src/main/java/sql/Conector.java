package sql;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conector {

    // Driver de Oracle
    private static final String CLASE = "oracle.jdbc.driver.OracleDriver";

    // Datos de conexión
    private final String HOST = "localhost";
    private final String PORT = "1521";
    private final String SERVICE = "XEPDB1"; // Service Name del PDB
    private final String USER = "telix";
    private final String PASS = "telix123";

    private final String URL;

    private Connection link;
    private PreparedStatement ps;

    // Constructor
    public Conector() {
        // URL con Service Name
        this.URL = "jdbc:oracle:thin:@" + HOST + ":" + PORT + "/" + SERVICE;
    }

    // Conectar
    public void conectar() {
        try {
            Class.forName(CLASE);
            this.link = DriverManager.getConnection(this.URL, this.USER, this.PASS);
            System.out.println("Conexión exitosa a Oracle!");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar: " + ex.getMessage());
        }
    }

    // Desconectar
    public void desconectar() {
        try {
            if (this.link != null) {
                this.link.close();
                System.out.println("Desconexión realizada.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Preparar sentencia SQL
    public PreparedStatement preparar(String sql) {
        if (this.link == null) {
            System.out.println("ERROR: La conexión no está activa. Llama a conectar().");
            return null;
        }
        try {
            ps = link.prepareStatement(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ps;
    }

    // Mostrar mensaje con JOptionPane
    public void mensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipoMensaje);
    }
}
