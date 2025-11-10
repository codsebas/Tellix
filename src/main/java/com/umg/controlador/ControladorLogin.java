package com.umg.controlador;

import com.umg.modelo.ModeloLogin;
import com.umg.modelo.ModeloMenu;
import com.umg.vistas.VistaLogin;
import com.umg.vistas.VistaMenu;
import com.umg.vistas.VistaPrincipal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.umg.seguridad.Sesion;
import sql.Conector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControladorLogin implements MouseListener, KeyListener {
    ModeloLogin modelo;
    VistaLogin vista;
    VistaPrincipal vistaPrincipal;

    public ControladorLogin(ModeloLogin modelo, VistaLogin vista, VistaPrincipal vistaPrincipal) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;

        vista.getBtnIniciarSesion().addMouseListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getComponent().equals(this.vista.getBtnIniciarSesion())){
            String user = this.vista.getTxtUsuario().getText();
            String pass = String.valueOf(this.vista.getTxtPassword().getPassword());
            if (autenticar(user,pass)){
                mostrarMenu();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void mostrarMenu(){
        ModeloMenu modelo = new ModeloMenu();
        VistaMenu vista = new VistaMenu();
        new ControladorMenu(modelo, vista, vistaPrincipal);
        vistaPrincipal.cambiarPanel(vista);
    }

    public boolean autenticar(String user, String pass) {
        Conector con = new Conector(user, pass);

        if (con.conectar()) {
            try {
                // Consulta para obtener el rol del usuario según la tabla "usuario"
                String sql = """
    SELECT rol_usuario
    FROM telix.usuario
    WHERE user_name = ?
    AND contrasena = STANDARD_HASH(?, 'SHA256')
""";
                System.out.println("Usuario: " + user + "Contraseña: " + pass);
                PreparedStatement ps = con.preparar(sql);
                ps.setString(1, user);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String rol = rs.getString("rol_usuario");

                    // Iniciar la sesión con el rol y la conexión activa
                    Sesion.iniciarSesion(con, user, rol);
                    System.out.println("Sesión iniciada: " + user + " (" + rol + ")");
                    return true;
                } else {
                    System.out.println("Usuario no encontrado en tabla de roles.");
                    con.desconectar();
                    return false;
                }

            } catch (Exception e) {
                System.out.println("Error al obtener rol: " + e.getMessage());
                con.desconectar();
                return false;
            }
        } else {
            System.out.println("Error de conexión: credenciales incorrectas.");
            return false;
        }
    }
}
