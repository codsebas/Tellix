package sql;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conector {

    private static final String CLASE = "org.postgresql.Driver";
    private final String HOST = "localhost";
    private final String USER = "postgres";
    private final String PASS = "umg.2025";
    private final String DATABASE = "assenta_db";
    private final String URL;

    private Connection link;
    private PreparedStatement ps;

    public Conector() {
        this.URL = "jdbc:postgresql://" + this.HOST + "/" + this.DATABASE;
    }

    public void conectar() {
        try {
            Class.forName(CLASE);
            this.link = DriverManager.getConnection(this.URL, this.USER, this.PASS);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar: " + ex.getMessage());
        }
    }

    public void desconectar() {
        try {
            if (this.link != null) {
                this.link.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

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

    public void mensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipoMensaje);
    }
}

