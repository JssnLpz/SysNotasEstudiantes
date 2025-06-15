
package Presentation;

import businessLogic.CursoServices;
import dataAccess.Conexion;
import dataAccess.CursoDAL;
import entities.Curso;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// Agregar formularios al menú
import Presentation.FrmUsuario;
import Presentation.FrmEstudiante;
import Presentation.FrmInscripcion;

public class FrmCurso extends JFrame {

    private JTable tabla;
    private CursoServices services;
    private Connection conexion;

    public FrmCurso() {
        setTitle("📚 Lista de Cursos");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        crearMenu(); // Menú superior

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
            CursoDAL dao = new CursoDAL(conexion);
            services = new CursoServices(dao);
        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        cargarTabla(contenedor);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarCursoSeleccionado());
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFormularios = new JMenu("Formularios");

        JMenuItem itemUsuarios = new JMenuItem("Usuarios");
        itemUsuarios.addActionListener(e -> new FrmUsuario().setVisible(true));

        JMenuItem itemEstudiantes = new JMenuItem("Estudiantes");
        itemEstudiantes.addActionListener(e -> new FrmEstudiante().setVisible(true));

        JMenuItem itemInscripciones = new JMenuItem("Inscripciones");
        itemInscripciones.addActionListener(e -> new FrmInscripcion().setVisible(true));

        menuFormularios.add(itemUsuarios);
        menuFormularios.add(itemEstudiantes);
        menuFormularios.add(itemInscripciones);

        menuBar.add(menuFormularios);
        setJMenuBar(menuBar);
    }

    private void cargarTabla(JPanel contenedor) {
        try {
            List<Curso> listaCursos = services.obtenerTodos();
            String[] columnas = {"ID Curso", "Nombre", "Descripción", "Estado"};
            Object[][] datos = new Object[listaCursos.size()][4];

            for (int i = 0; i < listaCursos.size(); i++) {
                Curso curso = listaCursos.get(i);
                datos[i][0] = curso.getIdCurso();
                datos[i][1] = curso.getNombre();
                datos[i][2] = curso.getDescripcion();
                datos[i][3] = curso.getEstado() == 1 ? "Activo" : "Inactivo";
            }

            tabla = new JTable(datos, columnas);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tabla.setRowHeight(24);
            tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            tabla.getTableHeader().setBackground(new Color(200, 221, 242));

            JScrollPane scroll = new JScrollPane(tabla);
            contenedor.add(scroll, BorderLayout.CENTER);

        } catch (SQLException e) {
            mostrarError("Error al obtener cursos: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarFormularioAgregar() {
        JTextField txtNombre = new JTextField();
        JTextArea txtDescripcion = new JTextArea(4, 20);
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Descripción:"));
        panel.add(new JScrollPane(txtDescripcion));
        panel.add(new JLabel("Estado:"));
        panel.add(cmbEstado);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Curso",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Curso nuevo = new Curso();
                nuevo.setNombre(txtNombre.getText().trim());
                nuevo.setDescripcion(txtDescripcion.getText().trim());
                nuevo.setEstado(cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0);

                services.insertar(nuevo);
                JOptionPane.showMessageDialog(this, "Curso agregado correctamente");
                recargarTabla();
            } catch (Exception ex) {
                mostrarError("Error al agregar curso: " + ex.getMessage());
            }
        }
    }

    private void mostrarFormularioEditar() {
        int id = obtenerIdCursoSeleccionado();
        if (id == -1) {
            mostrarError("Debe seleccionar un curso.");
            return;
        }

        try {
            Curso curso = services.obtenerPorId(id);
            if (curso == null) {
                mostrarError("Curso no encontrado.");
                return;
            }

            JTextField txtNombre = new JTextField(curso.getNombre());
            JTextArea txtDescripcion = new JTextArea(curso.getDescripcion(), 4, 20);
            JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
            cmbEstado.setSelectedIndex(curso.getEstado() == 1 ? 0 : 1);

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Descripción:"));
            panel.add(new JScrollPane(txtDescripcion));
            panel.add(new JLabel("Estado:"));
            panel.add(cmbEstado);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Curso",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                curso.setNombre(txtNombre.getText().trim());
                curso.setDescripcion(txtDescripcion.getText().trim());
                curso.setEstado(cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0);

                services.actualizar(curso);
                JOptionPane.showMessageDialog(this, "Curso actualizado.");
                recargarTabla();
            }

        } catch (Exception ex) {
            mostrarError("Error al editar curso: " + ex.getMessage());
        }
    }

    private void eliminarCursoSeleccionado() {
        int id = obtenerIdCursoSeleccionado();
        if (id == -1) {
            mostrarError("Debe seleccionar un curso.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este curso?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (services.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Curso eliminado.");
                    recargarTabla();
                } else {
                    mostrarError("No se pudo eliminar.");
                }
            } catch (SQLException ex) {
                mostrarError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void recargarTabla() {
        getContentPane().removeAll();
        setLayout(new BorderLayout(10, 10));
        crearMenu();
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
        btnEliminar.addActionListener(e -> eliminarCursoSeleccionado());

        revalidate();
        repaint();
    }

    private int obtenerIdCursoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return -1;
        return Integer.parseInt(tabla.getValueAt(fila, 0).toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrmCurso ventana = new FrmCurso();
            ventana.setVisible(true);
        });
    }
}

