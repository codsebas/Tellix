package sql;

import javax.swing.*;
import java.sql.*;

public class Conector {

    // Driver de Oracle
    private static final String CLASE = "oracle.jdbc.driver.OracleDriver";

    // Datos de conexión
    private final String HOST = "localhost";
    private final String PORT = "1521";
    private final String SERVICE = "freepdb1"; // Service Name del PDB
    private String usuario;
    private String contrasena;

    private final String URL;

    private Connection link;
    private PreparedStatement ps;

    // Constructor
    public Conector(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.URL = "jdbc:oracle:thin:@" + HOST + ":" + PORT + "/" + SERVICE;
    }

    // Conectar
    public boolean conectar() {
        try {
            Class.forName(CLASE);
            this.link = DriverManager.getConnection(this.URL, usuario, contrasena);
            System.out.println("Conexión exitosa como " + usuario);

            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error de conexión: " + ex.getMessage());
            return false;
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
