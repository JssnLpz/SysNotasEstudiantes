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

    private JTable tabla;
    private UsuarioDAL dao;
    private Connection conexion;

    public FrmUsuario() {
        setTitle("Lista de Usuarios");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        try {
            conexion = Conexion.getConnection();
            dao = new UsuarioDAL(conexion);
        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        cargarTabla();

        // Botones
        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarUsuarioSeleccionado());
    }

    private void cargarTabla() {
        try {
            List<Usuario> listaUsuarios = dao.obtenerTodos();

            String[] columnas = {"ID Usuario", "Nombre", "Teléfono", "Estado"};
            Object[][] datos = new Object[listaUsuarios.size()][4];

            for (int i = 0; i < listaUsuarios.size(); i++) {
                Usuario usuario = listaUsuarios.get(i);
                datos[i][0] = usuario.getIdUsuario();
                datos[i][1] = usuario.getNombre();
                datos[i][2] = usuario.getTelefono();
                datos[i][3] = usuario.getEstado() == 1 ? "Activo" : "Inactivo";
            }

            tabla = new JTable(datos, columnas);
            JScrollPane scroll = new JScrollPane(tabla);
            add(scroll, BorderLayout.CENTER);

        } catch (SQLException e) {
            mostrarError("Error al obtener usuarios: " + e.getMessage());
        }
    }

    private void recargarTabla() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        cargarTabla();

        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarUsuarioSeleccionado());

        revalidate();
        repaint();
    }

    private void mostrarFormularioAgregar() {
        JTextField txtNombre = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtClave = new JTextField();
        String[] estados = {"Activo", "Inactivo"};
        JComboBox<String> cmbEstado = new JComboBox<>(estados);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Clave:"));
        panel.add(txtClave);
        panel.add(new JLabel("Estado:"));
        panel.add(cmbEstado);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Usuario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Usuario nuevo = new Usuario();
                nuevo.setNombre(txtNombre.getText());
                nuevo.setTelefono(txtTelefono.getText());
                nuevo.setClave(txtClave.getText());
                nuevo.setEstado(cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0);

                if (dao.insertar(nuevo)) {
                    JOptionPane.showMessageDialog(this, "Usuario agregado correctamente");
                    recargarTabla();
                } else {
                    mostrarError("No se pudo agregar el usuario.");
                }

            } catch (SQLException e) {
                mostrarError("Error al insertar usuario: " + e.getMessage());
            }
        }
    }

    private void mostrarFormularioEditar() {
        int id = obtenerIdUsuarioSeleccionado();
        if (id == -1) {
            mostrarError("Debe seleccionar un usuario de la tabla.");
            return;
        }

        try {
            Usuario usuario = dao.obtenerPorId(id);
            if (usuario == null) {
                mostrarError("Usuario no encontrado.");
                return;
            }

            JTextField txtNombre = new JTextField(usuario.getNombre());
            JTextField txtTelefono = new JTextField(usuario.getTelefono());
            JTextField txtClave = new JTextField(usuario.getClave());
            String[] estados = {"Activo", "Inactivo"};
            JComboBox<String> cmbEstado = new JComboBox<>(estados);
            cmbEstado.setSelectedIndex(usuario.getEstado() == 1 ? 0 : 1);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Teléfono:"));
            panel.add(txtTelefono);
            panel.add(new JLabel("Clave:"));
            panel.add(txtClave);
            panel.add(new JLabel("Estado:"));
            panel.add(cmbEstado);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuario",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                usuario.setNombre(txtNombre.getText());
                usuario.setTelefono(txtTelefono.getText());
                usuario.setClave(txtClave.getText());
                usuario.setEstado(cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0);

                if (dao.actualizar(usuario)) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
                    recargarTabla();
                } else {
                    mostrarError("No se pudo actualizar el usuario.");
                }
            }

        } catch (SQLException ex) {
            mostrarError("Error al editar usuario: " + ex.getMessage());
        }
    }

    private void eliminarUsuarioSeleccionado() {
        int id = obtenerIdUsuarioSeleccionado();
        if (id == -1) {
            mostrarError("Debe seleccionar un usuario de la tabla.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro que deseas eliminar este usuario?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                    recargarTabla();
                } else {
                    mostrarError("No se pudo eliminar el usuario.");
                }
            } catch (SQLException ex) {
                mostrarError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private int obtenerIdUsuarioSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return -1;
        return Integer.parseInt(tabla.getValueAt(fila, 0).toString());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrmUsuario ventana = new FrmUsuario();
            ventana.setVisible(true);
        });
    }
}


