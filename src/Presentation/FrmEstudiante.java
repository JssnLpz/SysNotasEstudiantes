package Presentation;

import businessLogic.EstudianteServices;
import dataAccess.Conexion;
import dataAccess.EstudianteDAL;
import entities.Estudiante;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FrmEstudiante extends JFrame {

    private JTable tabla;
    private EstudianteServices services;
    private Connection conexion;

    public FrmEstudiante() {
        setTitle("🎓 Lista de Estudiantes");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contenedor);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar = new JButton("➕ Agregar");
        JButton btnEditar = new JButton("✏️ Editar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");

        JButton[] botones = {btnAgregar, btnEditar, btnEliminar};
        for (JButton btn : botones) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(59, 89, 182));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        contenedor.add(panelBotones, BorderLayout.SOUTH);

        try {
            conexion = Conexion.getConnection();
            EstudianteDAL dao = new EstudianteDAL(conexion);
            services = new EstudianteServices(dao);
        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        cargarTabla(contenedor);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
    }

    private void cargarTabla(JPanel contenedor) {
        try {
            List<Estudiante> lista = services.obtenerTodos();
            String[] columnas = {"ID", "Nombre", "Teléfono", "Estado"};
            Object[][] datos = new Object[lista.size()][4];

            for (int i = 0; i < lista.size(); i++) {
                Estudiante e = lista.get(i);
                datos[i][0] = e.getIdEstudiante();
                datos[i][1] = e.getNombre();
                datos[i][2] = e.getTelefono();
                datos[i][3] = e.getEstado() == 1 ? "Activo" : "Inactivo";
            }

            tabla = new JTable(datos, columnas);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tabla.setRowHeight(24);
            tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            tabla.getTableHeader().setBackground(new Color(200, 221, 242));

            JScrollPane scroll = new JScrollPane(tabla);
            contenedor.add(scroll, BorderLayout.CENTER);

        } catch (SQLException e) {
            mostrarError("Error al obtener estudiantes: " + e.getMessage());
        }
    }

    private void recargarTabla() {
        getContentPane().removeAll();
        setLayout(new BorderLayout(10, 10));
        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contenedor);
        cargarTabla(contenedor);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar = new JButton("➕ Agregar");
        JButton btnEditar = new JButton("✏️ Editar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");

        JButton[] botones = {btnAgregar, btnEditar, btnEliminar};
        for (JButton btn : botones) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(59, 89, 182));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        contenedor.add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        revalidate();
        repaint();
    }

    private void mostrarFormularioAgregar() {
        JTextField txtNombre = new JTextField();
        JTextField txtTelefono = new JTextField();
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Estado:"));
        panel.add(cmbEstado);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Estudiante",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Estudiante nuevo = new Estudiante(0, txtNombre.getText(), txtTelefono.getText(),
                        cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0);
                services.insertar(nuevo);
                JOptionPane.showMessageDialog(this, "Estudiante agregado correctamente");
                recargarTabla();
            } catch (Exception e) {
                mostrarError("Error al agregar estudiante: " + e.getMessage());
            }
        }
    }

    private void mostrarFormularioEditar() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            mostrarError("Debe seleccionar un estudiante.");
            return;
        }

        try {
            Estudiante est = services.obtenerPorId(id);
            if (est == null) {
                mostrarError("Estudiante no encontrado.");
                return;
            }

            JTextField txtNombre = new JTextField(est.getNombre());
            JTextField txtTelefono = new JTextField(est.getTelefono());
            JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
            cmbEstado.setSelectedIndex(est.getEstado() == 1 ? 0 : 1);

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Teléfono:"));
            panel.add(txtTelefono);
            panel.add(new JLabel("Estado:"));
            panel.add(cmbEstado);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Estudiante",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                est.setNombre(txtNombre.getText());
                est.setTelefono(txtTelefono.getText());
                est.setEstado(cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0);

                services.actualizar(est);
                JOptionPane.showMessageDialog(this, "Estudiante actualizado.");
                recargarTabla();
            }

        } catch (Exception ex) {
            mostrarError("Error al editar estudiante: " + ex.getMessage());
        }
    }

    private void eliminarSeleccionado() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            mostrarError("Debe seleccionar un estudiante.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este estudiante?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (services.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Estudiante eliminado.");
                    recargarTabla();
                } else {
                    mostrarError("No se pudo eliminar.");
                }
            } catch (SQLException ex) {
                mostrarError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private int obtenerIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return -1;
        return Integer.parseInt(tabla.getValueAt(fila, 0).toString());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrmEstudiante ventana = new FrmEstudiante();
            ventana.setVisible(true);
        });
    }
}
