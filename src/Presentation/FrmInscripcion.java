package Presentation;

import businessLogic.InscripcionServices;
import dataAccess.Conexion;
import dataAccess.InscripcionDAL;
import entities.Inscripcion;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FrmInscripcion extends JFrame {

    private JTable tabla;
    private InscripcionServices services;
    private Connection conexion;

    public FrmInscripcion() {
        setTitle("📋 Lista de Inscripciones");
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
        // No agregamos botón eliminar según lo solicitado

        JButton[] botones = {btnAgregar, btnEditar};
        for (JButton btn : botones) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(59, 89, 182));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        contenedor.add(panelBotones, BorderLayout.SOUTH);

        try {
            conexion = Conexion.getConnection();
            InscripcionDAL dao = new InscripcionDAL(conexion);
            services = new InscripcionServices(dao);
        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        cargarTabla(contenedor);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
    }

    private void cargarTabla(JPanel contenedor) {
        try {
            List<Inscripcion> lista = services.obtenerTodos();
            String[] columnas = {"ID", "ID Usuario", "ID Estudiante", "ID Curso", "Nota", "Estado"};
            Object[][] datos = new Object[lista.size()][6];

            for (int i = 0; i < lista.size(); i++) {
                Inscripcion insc = lista.get(i);
                datos[i][0] = insc.getIdInscripcion();
                datos[i][1] = insc.getIdUsuario();
                datos[i][2] = insc.getIdEstudiante();
                datos[i][3] = insc.getIdCurso();
                datos[i][4] = insc.getNota();
                datos[i][5] = insc.getEstado() == 1 ? "Activo" : "Inactivo";
            }

            tabla = new JTable(datos, columnas);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tabla.setRowHeight(24);
            tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            tabla.getTableHeader().setBackground(new Color(200, 221, 242));

            JScrollPane scroll = new JScrollPane(tabla);
            contenedor.add(scroll, BorderLayout.CENTER);

        } catch (SQLException e) {
            mostrarError("Error al obtener inscripciones: " + e.getMessage());
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

        JButton[] botones = {btnAgregar, btnEditar};
        for (JButton btn : botones) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(59, 89, 182));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        contenedor.add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());

        revalidate();
        repaint();
    }

    private void mostrarFormularioAgregar() {
        JTextField txtIdUsuario = new JTextField();
        JTextField txtIdEstudiante = new JTextField();
        JTextField txtIdCurso = new JTextField();
        JTextField txtNota = new JTextField();
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("ID Usuario:"));
        panel.add(txtIdUsuario);
        panel.add(new JLabel("ID Estudiante:"));
        panel.add(txtIdEstudiante);
        panel.add(new JLabel("ID Curso:"));
        panel.add(txtIdCurso);
        panel.add(new JLabel("Nota:"));
        panel.add(txtNota);
        panel.add(new JLabel("Estado:"));
        panel.add(cmbEstado);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Inscripción",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int idUsuario = Integer.parseInt(txtIdUsuario.getText());
                int idEstudiante = Integer.parseInt(txtIdEstudiante.getText());
                int idCurso = Integer.parseInt(txtIdCurso.getText());
                int nota = Integer.parseInt(txtNota.getText());
                int estado = cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0;

                Inscripcion nueva = new Inscripcion(0, idUsuario, idEstudiante, idCurso, nota, estado);
                services.agregar(nueva);
                JOptionPane.showMessageDialog(this, "Inscripción agregada correctamente");
                recargarTabla();
            } catch (NumberFormatException ex) {
                mostrarError("Debe ingresar números válidos en los campos de ID y nota.");
            } catch (Exception e) {
                mostrarError("Error al agregar inscripción: " + e.getMessage());
            }
        }
    }

    private void mostrarFormularioEditar() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            mostrarError("Debe seleccionar una inscripción.");
            return;
        }

        try {
            Inscripcion insc = services.obtenerPorId(id);
            if (insc == null) {
                mostrarError("Inscripción no encontrada.");
                return;
            }

            JTextField txtIdUsuario = new JTextField(String.valueOf(insc.getIdUsuario()));
            JTextField txtIdEstudiante = new JTextField(String.valueOf(insc.getIdEstudiante()));
            JTextField txtIdCurso = new JTextField(String.valueOf(insc.getIdCurso()));
            JTextField txtNota = new JTextField(String.valueOf(insc.getNota()));
            JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
            cmbEstado.setSelectedIndex(insc.getEstado() == 1 ? 0 : 1);

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(new JLabel("ID Usuario:"));
            panel.add(txtIdUsuario);
            panel.add(new JLabel("ID Estudiante:"));
            panel.add(txtIdEstudiante);
            panel.add(new JLabel("ID Curso:"));
            panel.add(txtIdCurso);
            panel.add(new JLabel("Nota:"));
            panel.add(txtNota);
            panel.add(new JLabel("Estado:"));
            panel.add(cmbEstado);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Inscripción",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int idUsuario = Integer.parseInt(txtIdUsuario.getText());
                    int idEstudiante = Integer.parseInt(txtIdEstudiante.getText());
                    int idCurso = Integer.parseInt(txtIdCurso.getText());
                    int nota = Integer.parseInt(txtNota.getText());
                    int estado = cmbEstado.getSelectedItem().equals("Activo") ? 1 : 0;

                    insc.setIdUsuario(idUsuario);
                    insc.setIdEstudiante(idEstudiante);
                    insc.setIdCurso(idCurso);
                    insc.setNota(nota);
                    insc.setEstado(estado);

                    services.actualizar(insc);
                    JOptionPane.showMessageDialog(this, "Inscripción actualizada.");
                    recargarTabla();

                } catch (NumberFormatException ex) {
                    mostrarError("Debe ingresar números válidos en los campos de ID y nota.");
                } catch (Exception e) {
                    mostrarError("Error al actualizar inscripción: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            mostrarError("Error al obtener inscripción: " + e.getMessage());
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
            FrmInscripcion ventana = new FrmInscripcion();
            ventana.setVisible(true);
        });
    }
}


