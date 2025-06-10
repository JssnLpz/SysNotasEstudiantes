package Presentation;

import dataAccess.UsuarioDAL;
import dataAccess.Conexion;
import entities.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FrmUsuario extends JFrame {

    public FrmUsuario() {
        setTitle("Lista de Usuarios");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Obtener la conexión desde la clase Conexion
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection(); // Usamos el método correcto de la clase Conexion
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // Salir si no hay conexión
        }

        // Obtener los datos
        UsuarioDAL dao = new UsuarioDAL(conexion);
        List<Usuario> listaUsuarios;
        try {
            listaUsuarios = dao.obtenerTodos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Definir columnas
        String[] columnas = {"ID Usuario", "Nombre", "Teléfono", "Estado"};
        Object[][] datos = new Object[listaUsuarios.size()][4];

        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuario = listaUsuarios.get(i);
            datos[i][0] = usuario.getIdUsuario();
            datos[i][1] = usuario.getNombre();
            datos[i][2] = usuario.getTelefono();
            datos[i][3] = usuario.getEstado() == 1 ? "Activo" : "Inactivo";
        }

        // Crear tabla
        JTable tabla = new JTable(datos, columnas);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrmUsuario ventana = new FrmUsuario();
            ventana.setVisible(true);
        });
    }
}
