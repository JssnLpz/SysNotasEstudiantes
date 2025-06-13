package Presentation;

import dataAccess.Conexion;
import dataAccess.UsuarioDAL;
import entities.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class FrmLogin extends JFrame {
    private JTextField txtTelefono;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private UsuarioDAL usuarioDAL;

    public FrmLogin() {
        setTitle("🔑 Inicio de Sesión");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245)); // Fondo gris claro

        JLabel lblTitulo = new JLabel("Login de Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(130, 10, 200, 30);
        panel.add(lblTitulo);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(50, 60, 80, 25);
        panel.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(140, 60, 180, 25);
        panel.add(txtTelefono);

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

        // Inicializar conexión y usuarioDAL
        try {
            Connection conexion = Conexion.getConnection();
            usuarioDAL = new UsuarioDAL(conexion);
        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        btnIngresar.addActionListener(e -> validarLogin());
    }

    private void validarLogin() {
        String telefono = txtTelefono.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (telefono.isEmpty() || clave.isEmpty()) {
            mostrarError("Por favor ingrese teléfono y contraseña.");
            return;
        }

        try {
            Usuario usuario = usuarioDAL.autenticar(telefono, clave);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuario.getNombre() + "!");
                new FrmUsuario().setVisible(true);
                this.dispose(); // Cierra la ventana de login
            } else {
                mostrarError("Teléfono, contraseña o estado incorrecto.");
            }
        } catch (SQLException e) {
            mostrarError("Error en la conexión o consulta: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}

