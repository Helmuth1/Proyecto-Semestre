/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sistema_tickets_servicio.ControladorSesion;
import sistema_tickets_servicio.Departamento;
import sistema_tickets_servicio.Ticket;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class CreacionTicketControlador {
    
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<Departamento> cmbDepartamento; 
    @FXML private ComboBox<String> cmbPrioridad;
    @FXML private Label lblNombreAdjunto;
    @FXML private Label lblMensajeEstado;

    private File selectedAdjuntoFile; 

    private final String url = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";
    private final String user = "neondb_owner";
    private final String password = "npg_M9JAQdy0Enhl";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @FXML
    public void initialize() {
        cargarDepartamentos();
        cargarPrioridades();
        limpiarCampos();
    }

    private void cargarDepartamentos() {
    ObservableList<Departamento> departamentos = FXCollections.observableArrayList();

    String sql = "SELECT id, nombre, descripcion FROM departamentos ORDER BY nombre"; 
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            departamentos.add(new Departamento(
                rs.getInt("id"),                     
                rs.getString("nombre"),              
                rs.getString("descripcion"),         
                FXCollections.observableArrayList()  
            ));
        }
        cmbDepartamento.setItems(departamentos);
    } catch (SQLException e) {
        Logger.getLogger(CreacionTicketControlador.class.getName()).log(Level.SEVERE, "Error al cargar departamentos", e);
        mostrarAlertaError("Error de BD", "No se pudieron cargar los departamentos.");
    }
}

    private void cargarPrioridades() {

        ObservableList<String> prioridades = FXCollections.observableArrayList("Alta", "Media", "Baja");
        cmbPrioridad.setItems(prioridades);
        if (!prioridades.isEmpty()) {
            cmbPrioridad.getSelectionModel().selectFirst(); 
        }
    }

    @FXML
    private void seleccionarAdjunto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo Adjunto");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de Documento", "*.pdf", "*.doc", "*.docx", "*.xls", "*.xlsx"),
            new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) lblNombreAdjunto.getScene().getWindow(); 
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (!validarAdjunto(file)) {
                return; 
            }
            selectedAdjuntoFile = file;
            lblNombreAdjunto.setText(file.getName());
        }
    }

    private boolean validarAdjunto(File file) {
        String fileName = file.getName();
        String fileExtension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fileExtension = fileName.substring(i+1).toLowerCase();
        }

        List<String> allowedExtensions = Arrays.asList("pdf", "doc", "docx", "xls", "xlsx", "png", "jpg", "jpeg");
        if (!allowedExtensions.contains(fileExtension)) {
            mostrarAlertaAdvertencia("Validación de Adjunto", "Formato de archivo no compatible. Formatos permitidos: PDF, JPG, PNG, DOCX, XLSX.");
            return false;
        }

        long maxSizeInBytes = 5 * 1024 * 1024; // 5 MB
        if (file.length() > maxSizeInBytes) {
            mostrarAlertaAdvertencia("Validación de Adjunto", "El tamaño del archivo no puede exceder los 5MB.");
            return false;
        }
        return true;
    }

    @FXML
    private void crearTicket(ActionEvent event) {
        //Obtener datos del formulario
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        Departamento departamentoSeleccionado = cmbDepartamento.getSelectionModel().getSelectedItem();
        String prioridad = cmbPrioridad.getSelectionModel().getSelectedItem();

        //Validar datos
        if (!validarDatosTicket(titulo, descripcion, departamentoSeleccionado, prioridad)) {
            return; 
        }

        int idDepartamento = departamentoSeleccionado.getId();

        int creadoPorUsuarioId = -1; 
        Integer creadoPorUsuarioIdParaDB = null; 

        if (ControladorSesion.getInstancia().isSesionActiva() && ControladorSesion.getInstancia().getUsuarioLogueado() != null) {
        creadoPorUsuarioId = ControladorSesion.getInstancia().getUsuarioLogueado().getId();
        creadoPorUsuarioIdParaDB = creadoPorUsuarioId;
        }

        //Guardar Adjunto 
        String adjuntoPathToSave = null;
        if (selectedAdjuntoFile != null) {
            try {
                Path adjuntosDir = Paths.get("recursos", "adjuntos_tickets");
                Files.createDirectories(adjuntosDir); 

                String fileName = "ticket_" + System.currentTimeMillis() + "_" + selectedAdjuntoFile.getName();
                Path destinationPath = adjuntosDir.resolve(fileName);
                Files.copy(selectedAdjuntoFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                adjuntoPathToSave = destinationPath.toAbsolutePath().toString();
            } catch (IOException e) {
                Logger.getLogger(CrearTicketControlador.class.getName()).log(Level.SEVERE, "Error al copiar el archivo adjunto", e);
                mostrarAlertaError("Error de Archivo", "No se pudo guardar el archivo adjunto. Intenta de nuevo.");
                return;
            }
        }

        //Crear el objeto Ticket
        Ticket nuevoTicket = new Ticket(
            titulo,
            descripcion,
            idDepartamento,
            prioridad,
            adjuntoPathToSave,
            creadoPorUsuarioId
        ) {};

        //Insertar en la Base de Datos
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        int nuevoTicketId = -1;
        
        try {
            conn = getConnection();
            String sql = "INSERT INTO tickets (titulo, descripcion, departamento_asignado_id, prioridad, adjuntos_ruta, estado, creado_por_usuario_id, fecha_creacion, fecha_ultima_actualizacion) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            System.out.println("DEBUG: Intentando establecer parámetro 1.");
            System.out.println("DEBUG: Titulo del ticket: " + (nuevoTicket != null ? nuevoTicket.getTitulo() : "nuevoTicket es NULL"));

                pstmt.setString(1, nuevoTicket.getTitulo());
                pstmt.setString(2, nuevoTicket.getDescripcion());
                pstmt.setInt(3, nuevoTicket.getDepartamentoAsignadoId());
                pstmt.setString(4, nuevoTicket.getPrioridad());
                pstmt.setString(5, nuevoTicket.getAdjuntosRuta());
                pstmt.setString(6, nuevoTicket.getEstado()); 

                if (ControladorSesion.getInstancia().isSesionActiva() && ControladorSesion.getInstancia().getUsuarioLogueado() != null) {
                creadoPorUsuarioIdParaDB = ControladorSesion.getInstancia().getUsuarioLogueado().getId();
                }

                if (creadoPorUsuarioIdParaDB != null) {
                pstmt.setInt(7, creadoPorUsuarioIdParaDB);
                } else {
                pstmt.setNull(7, java.sql.Types.INTEGER); 
                }
    
    pstmt.setTimestamp(8, java.sql.Timestamp.valueOf(LocalDateTime.now()));
    pstmt.setTimestamp(9, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                
                if (rs.next()) {
                    nuevoTicketId = rs.getInt(1); 
                    nuevoTicket.setId(nuevoTicketId); 

                    
                    mostrarAlertaInfo("Éxito", "Ticket #" + nuevoTicketId + " creado exitosamente y asignado a " + departamentoSeleccionado.getNombre() + ".");
                    lblMensajeEstado.setText("Ticket creado con ID: " + nuevoTicketId);

                    String nombreUsuarioLog = "Sistema/Invitado";
                    if (ControladorSesion.getInstancia().isSesionActiva() && ControladorSesion.getInstancia().getUsuarioLogueado() != null) {
                        nombreUsuarioLog = ControladorSesion.getInstancia().getUsuarioLogueado().getNombreusuario();
                    }

                    registrarLogTicket(nuevoTicketId, creadoPorUsuarioIdParaDB, "CREATE", "Ticket creado por " + nombreUsuarioLog); 
                    limpiarCampos();

                } else {
                    Logger.getLogger(CreacionTicketControlador.class.getName()).log(Level.WARNING, "Ticket creado pero no se pudo recuperar el ID generado.");
                    mostrarAlertaError("Advertencia", "Ticket creado, pero no se pudo obtener su ID. Revisa los logs de la aplicación y la base de datos.");
                    lblMensajeEstado.setText("Ticket creado (ID desconocido).");
                }
            } else {
                mostrarAlertaError("Error", "No se pudo crear el ticket.");
                lblMensajeEstado.setText("Error al crear ticket.");
            }

        } catch (SQLException e) {
            Logger.getLogger(CreacionTicketControlador.class.getName()).log(Level.SEVERE, "Error de BD al crear ticket", e);
            mostrarAlertaError("Error de BD", "Hubo un error al crear el ticket: " + e.getMessage());
            lblMensajeEstado.setText("Error de conexión o base de datos.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(CrearTicketControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de BD", e);
            }
        }
    }

    private boolean validarDatosTicket(String titulo, String descripcion, Departamento departamento, String prioridad) {

        if (titulo.isEmpty()) {
            mostrarAlertaAdvertencia("Validación", "El título del ticket no puede estar vacío.");
            return false;
        }
        if (titulo.length() < 3 || titulo.length() > 100) { // Longitud del título [cite: 50]
            mostrarAlertaAdvertencia("Validación", "El título debe contener entre 3 y 100 caracteres.");
            return false;
        }

        if (descripcion.isEmpty()) {
            mostrarAlertaAdvertencia("Validación", "La descripción del problema no puede estar vacía.");
            return false;
        }

        if (departamento == null) {
            mostrarAlertaAdvertencia("Validación", "Debes seleccionar un departamento para asignar el ticket.");
            return false;
        }

        if (prioridad == null || prioridad.isEmpty()) {
            mostrarAlertaAdvertencia("Validación", "Debes seleccionar un nivel de prioridad.");
            return false;
        }
        return true;
    }


    @FXML
    private void cancelarCreacion(ActionEvent event) {
        limpiarCampos();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mostrarAlertaInfo("Cancelado", "La creación del ticket ha sido cancelada.");
    }

    @FXML
    private void regresarMenu(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
    private void limpiarCampos() {
        txtTitulo.clear();
        txtDescripcion.clear();
        cmbDepartamento.getSelectionModel().clearSelection();
        cmbPrioridad.getSelectionModel().clearSelection();
        selectedAdjuntoFile = null;
        lblNombreAdjunto.setText("Ningún archivo seleccionado");
        lblMensajeEstado.setText("");
    }

    // Métodos para mostrar alertas 
    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void registrarLogTicket(int idTicket, Integer idUsuarioQueCambio, String tipoCambio, String descripcionCambio) { 
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
        conn = getConnection();
        String sql = "INSERT INTO auditoria_tickets (id_ticket, id_usuario_que_cambio, tipo_cambio, descripcion_cambio, fecha_cambio) VALUES (?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idTicket);
        
        // ¡CAMBIO CLAVE AQUÍ! Manejar el parámetro 2 (idUsuarioQueCambio) para que acepte NULL
        if (idUsuarioQueCambio != null) {
            pstmt.setInt(2, idUsuarioQueCambio);
        } else {
            pstmt.setNull(2, java.sql.Types.INTEGER); // Esto establece el parámetro a SQL NULL
        }
        
        pstmt.setString(3, tipoCambio);
        pstmt.setString(4, descripcionCambio);
        pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));
        pstmt.executeUpdate();
    } catch (SQLException e) {
        Logger.getLogger(CreacionTicketControlador.class.getName()).log(Level.SEVERE, "Error al registrar log de ticket", e);
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            Logger.getLogger(CreacionTicketControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de log de ticket", e);
        }
    }
  }   
}
