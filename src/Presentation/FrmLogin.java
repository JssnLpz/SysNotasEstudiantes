package Presentation;

import dataAccess.Conexion;
import entities.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FrmLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;

    public FrmLogin() {
        setTitle("Inicio de Sesión");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal con borde y fondo
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245)); // Gris claro

        JLabel lblTitulo = new JLabel("Login de Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(130, 10, 200, 30);
        panel.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 60, 80, 25);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(140, 60, 180, 25);
        panel.add(txtUsuario);

        JLabel lblClave = new JLabel("Contraseña:");
        lblClave.setBounds(50, 100, 80, 25);
        panel.add(lblClave);

        txtClave = new JPasswordField();
        txtClave.setBounds(140, 100, 180, 25);
        panel.add(txtClave);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(140, 145, 100, 30);
        btnIngresar.setBackground(new Color(255, 140, 0)); // Naranja
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        panel.add(btnIngresar);

        add(panel, BorderLayout.CENTER);

        btnIngresar.addActionListener(e -> validarLogin());
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña");
            return;
        }

        String sql = "SELECT * FROM dbo.Usuario WHERE Nombre = ? AND Clave = ? AND Estado = 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, clave);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("idUsuario"));
                    u.setNombre(rs.getString("Nombre"));
                    u.setClave(rs.getString("Clave"));
                    u.setTelefono(rs.getString("Telefono"));
                    u.setEstado(rs.getInt("Estado"));

                    JOptionPane.showMessageDialog(this, "¡Bienvenida, " + u.getNombre() + "!");

                    new FrmUsuario().setVisible(true);
                    this.dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "Usuario, contraseña o estado incorrecto");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en la conexión o consulta:\n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}

