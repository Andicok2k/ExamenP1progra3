

package ec.edu.taller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Ventana {


    private JPanel Ventana;
    private JTabbedPane tabbedPane1;

    
    private JLabel lblNuevoIncidente;
    private JLabel lblCodigo;
    private JLabel lblTipo;
    private JLabel lblDescripcion;
    private JLabel lblPrioridad;
    private JLabel lblEstado;

    private JTextField txtCodigo;
    private JComboBox cmbTipo;
    private JTextArea txtDescripcion;
    private JSpinner spPrioridad;
    private JButton btnRegistrar;


    private JLabel lblPendientes;
    private JLabel lblDisponibles;
    private JLabel lblCapacidad;
    private JLabel lblCodigoGestion;
    private JLabel lblEstadoGestion;
    private JLabel lblTipoGestion;
    private JLabel lblPrioridadGestion;
    private JLabel lblDescripcionGestion;

    private JTextArea txtDescripcionGestion;
    private JTextArea txtListar;

    private JButton btnSiguiente;
    private JButton btnAtender;
    private JButton btnListar;


    private GestorIncidentes gestor;

    public Ventana() {

        gestor = new GestorIncidentes(10);


        gestor.predefinir();


        cmbTipo.removeAllItems();
        cmbTipo.addItem("MALWARE");
        cmbTipo.addItem("PHISHING");
        cmbTipo.addItem("ACCESO_NO_AUTORIZADO");
        cmbTipo.addItem("FUGA_DATOS");
        cmbTipo.addItem("OTRO");

        spPrioridad.setModel(
                new SpinnerNumberModel(1, 1, 5, 1)
        );


        lblEstado.setText("Estado Inicial: PENDIENTE");

        // CAMPOS SOLO LECTURA
        txtDescripcionGestion.setEditable(false);
        txtListar.setEditable(false);

        actualizarContadores();
        mostrarSiguiente();


        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    registrar();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage()
                    );
                }
            }
        });


        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    mostrarSiguiente();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage()
                    );
                }
            }
        });


        btnAtender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    atender();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage()
                    );
                }
            }
        });


        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    listar();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage()
                    );
                }
            }
        });
    }


    public void registrar() throws Exception {

        String codigo = txtCodigo.getText().trim();
        String tipo = cmbTipo.getSelectedItem().toString();
        String descripcion = txtDescripcion.getText().trim();
        int prioridad = (Integer) spPrioridad.getValue();

        if (codigo.isEmpty()) {
            throw new Exception("Ingrese el codigo");
        }

        if (descripcion.isEmpty()) {
            throw new Exception("Ingrese la descripcion");
        }

        if (gestor.existeIncidente(codigo)) {
            throw new Exception("El codigo ya existe");
        }

        if (gestor.consultarEspaciosDisponibles() == 0) {
            throw new Exception("La cola esta llena");
        }

        IncidenteSeguridad incidente =
                new IncidenteSeguridad(
                        codigo,
                        tipo,
                        descripcion,
                        prioridad
                );

        boolean registrado =
                gestor.registrarIncidente(incidente);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    null,
                    "Incidente registrado correctamente"
            );

            txtCodigo.setText("");
            txtDescripcion.setText("");
            cmbTipo.setSelectedIndex(0);
            spPrioridad.setValue(1);

            actualizarContadores();
            mostrarSiguiente();

        } else {
            throw new Exception("No se pudo registrar");
        }
    }


    public void mostrarSiguiente() {

        IncidenteSeguridad incidente =
                gestor.consultarSiguienteIncidente();

        if (incidente == null) {

            lblCodigoGestion.setText("Codigo: -");
            lblTipoGestion.setText("Tipo: -");
            lblPrioridadGestion.setText("Prioridad: -");
            lblEstadoGestion.setText("Estado: -");
            txtDescripcionGestion.setText("");

        } else {

            lblCodigoGestion.setText(
                    "Codigo: " + incidente.getCodigo()
            );

            lblTipoGestion.setText(
                    "Tipo: " + incidente.getTipo()
            );

            lblPrioridadGestion.setText(
                    "Prioridad: " + incidente.getPrioridad()
            );

            lblEstadoGestion.setText(
                    "Estado: " + incidente.getEstado()
            );

            txtDescripcionGestion.setText(
                    incidente.getDescripcion()
            );
        }

        actualizarContadores();
    }

    // ATENDER INCIDENTE
    public void atender() throws Exception {

        IncidenteSeguridad incidente =
                gestor.atenderSiguienteIncidente();

        if (incidente == null) {
            throw new Exception(
                    "No hay incidentes pendientes"
            );
        }

        JOptionPane.showMessageDialog(
                null,
                "Incidente atendido: " + incidente.getCodigo()
                        + "\nEstado: " + incidente.getEstado()
        );

        actualizarContadores();
        mostrarSiguiente();
        listar();
    }


    public void listar() {

        List<IncidenteSeguridad> lista =
                gestor.listarIncidentes();

        StringBuilder sb = new StringBuilder();

        if (lista.isEmpty()) {
            txtListar.setText("No hay incidentes pendientes");
            return;
        }

        for (IncidenteSeguridad incidente : lista) {

            sb.append("Codigo: ")
                    .append(incidente.getCodigo());

            sb.append(" | Tipo: ")
                    .append(incidente.getTipo());

            sb.append(" | Prioridad: ")
                    .append(incidente.getPrioridad());

            sb.append(" | Estado: ")
                    .append(incidente.getEstado());

            sb.append("\n");
        }

        txtListar.setText(sb.toString());
    }


    public void actualizarContadores() {

        int pendientes =
                gestor.contarIncidentesPendientes();

        int disponibles =
                gestor.consultarEspaciosDisponibles();

        int capacidad = pendientes + disponibles;

        lblPendientes.setText(
                "Pendientes: " + pendientes
        );

        lblDisponibles.setText(
                "Disponibles: " + disponibles
        );

        lblCapacidad.setText(
                "Capacidad: " + capacidad
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().Ventana);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}


