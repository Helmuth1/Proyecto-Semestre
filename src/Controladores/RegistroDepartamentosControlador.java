package Controladores;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistema_tickets_servicio.Departamento;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class RegistroDepartamentosControlador {

    @FXML
    private TextField txtNombreDepartamento;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private ListView<String> listaTecnicosDisponibles; 
    @FXML
    private Button btnAsignarTecnico; 
    @FXML
    private Button btnDesasignarTecnico; 

    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnRegresar;

    @FXML
    private TableView<Departamento> tablaDepartamentos;
    @FXML
    private TableColumn<Departamento, Integer> colId;
    @FXML
    private TableColumn<Departamento, String> colNombreDepartamento;
    @FXML
    private TableColumn<Departamento, String> colDescripcion;
    @FXML
    private TableColumn<Departamento, String> colTecnicosAsignados;

    @FXML
    private ListView<String> listaTecnicosDelDepartamento;


    private final String DB_URL = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreDepartamento.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTecnicosAsignados.setCellValueFactory(new PropertyValueFactory<>("tecnicosAsignadosString"));
        listaTecnicosDisponibles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cargarTecnicosDisponibles();
        cargarDepartamentos();

        
        tablaDepartamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombreDepartamento.setText(newSelection.getNombre());
                txtDescripcion.setText(newSelection.getDescripcion());
                listaTecnicosDelDepartamento.setItems(newSelection.getTecnicosAsignados());
                listaTecnicosDisponibles.getSelectionModel().clearSelection();
            } else {
                limpiarCampos();
            }
        });

        listaTecnicosDisponibles.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnAsignarTecnico.setDisable(newVal == null);
        });
        listaTecnicosDelDepartamento.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnDesasignarTecnico.setDisable(newVal == null);
        });
    }

    //Métodos de Conexión y Carga de Datos

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void cargarTecnicosDisponibles() {
        listaTecnicosDisponibles.getItems().clear();
        String query = "SELECT nombreusuario FROM usuarios WHERE rolasignado = 'Técnico' ORDER BY nombreusuario";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listaTecnicosDisponibles.getItems().add(rs.getString("nombreusuario"));
            }
        } catch (SQLException e) {
            Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al cargar técnicos disponibles", e);
            mostrarAlerta("Error de Carga", "No se pudo obtener la lista de técnicos disponibles.");
        }
    }

    private void cargarDepartamentos() {
        ObservableList<Departamento> listaDepartamentos = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre, descripcion FROM departamentos ORDER BY nombre"; // Ya no seleccionamos 'nombreusuario_tecnico_asignado'

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idDep = rs.getInt("id");
                String nombreDep = rs.getString("nombre");
                String descDep = rs.getString("descripcion");
                ObservableList<String> tecnicosDelDep = getTecnicosPorDepartamento(idDep); // Obtener técnicos para este departamento

                listaDepartamentos.add(new Departamento(idDep, nombreDep, descDep, tecnicosDelDep));
            }
            tablaDepartamentos.setItems(listaDepartamentos);

        } catch (SQLException e) {
            Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al cargar departamentos", e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar los departamentos.");
        }
    }

    private ObservableList<String> getTecnicosPorDepartamento(int idDepartamento) {
        ObservableList<String> tecnicos = FXCollections.observableArrayList();
        String sql = "SELECT dt.nombreusuario_tecnico FROM departamento_tecnico dt WHERE dt.id_departamento = ? ORDER BY dt.nombreusuario_tecnico";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDepartamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tecnicos.add(rs.getString("nombreusuario_tecnico"));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al cargar técnicos para departamento " + idDepartamento, e);
        }
        return tecnicos;
    }

    //Metodos Botones
    @FXML
    private void crearDepartamento(ActionEvent event) {
        String nombre = txtNombreDepartamento.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (!validarCampos(nombre)) {
            return;
        }

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); 

            if (existeNombreDepartamento(conn, nombre, null)) {
                mostrarAlerta("Error de Validación", "Ya existe un departamento con ese nombre.");
                conn.rollback();
                return;
            }

            String insertDepartamentoSQL = "INSERT INTO departamentos (nombre, descripcion) VALUES (?, ?) RETURNING id";
            int idDepartamento = -1;
            try (PreparedStatement ps = conn.prepareStatement(insertDepartamentoSQL)) {
                ps.setString(1, nombre);
                ps.setString(2, descripcion.isEmpty() ? null : descripcion);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idDepartamento = rs.getInt("id");
                } else {
                    throw new SQLException("No se pudo obtener el ID del departamento insertado.");
                }
            }

            ObservableList<String> tecnicosSeleccionados = listaTecnicosDelDepartamento.getItems();
            if (!tecnicosSeleccionados.isEmpty()) {
                String insertTecnicoSQL = "INSERT INTO departamento_tecnico (id_departamento, nombreusuario_tecnico) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertTecnicoSQL)) {
                    for (String tecnico : tecnicosSeleccionados) {
                        ps.setInt(1, idDepartamento);
                        ps.setString(2, tecnico);
                        ps.addBatch(); 
                    }
                    ps.executeBatch(); 
                }
            }

            String insertColaSQL = "INSERT INTO colas_atencion (id_departamento, nombre_cola) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertColaSQL)) {
                ps.setInt(1, idDepartamento);
                ps.setString(2, "Cola de " + nombre);
                ps.executeUpdate();
            }

            conn.commit(); 
            mostrarAlertaInfo("Creación Exitosa", "Departamento, técnicos asignados y cola de atención creados exitosamente.");
            cargarDepartamentos(); 
            limpiarCampos(); 

        } catch (SQLException e) {
            Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al crear departamento", e);
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al hacer rollback", ex);
            }
            mostrarAlerta("Error de Base de Datos", "No se pudo crear el departamento: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); 
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al cerrar conexión", e);
            }
        }
    }

    @FXML
    private void editarDepartamento(ActionEvent event) {
        Departamento departamentoSeleccionado = tablaDepartamentos.getSelectionModel().getSelectedItem();
        if (departamentoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor, selecciona un departamento de la tabla para editar.");
            return;
        }

        String nuevoNombre = txtNombreDepartamento.getText().trim();
        String nuevaDescripcion = txtDescripcion.getText().trim();

        if (!validarCampos(nuevoNombre)) {
            return;
        }

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); 

            if (existeNombreDepartamento(conn, nuevoNombre, departamentoSeleccionado.getId())) {
                mostrarAlerta("Error de Validación", "Ya existe otro departamento con ese nombre.");
                conn.rollback();
                return;
            }

            String updateDepartamentoSQL = "UPDATE departamentos SET nombre = ?, descripcion = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateDepartamentoSQL)) {
                ps.setString(1, nuevoNombre);
                ps.setString(2, nuevaDescripcion.isEmpty() ? null : nuevaDescripcion);
                ps.setInt(3, departamentoSeleccionado.getId());
                ps.executeUpdate();
            }

            String deleteAsignacionesSQL = "DELETE FROM departamento_tecnico WHERE id_departamento = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteAsignacionesSQL)) {
                ps.setInt(1, departamentoSeleccionado.getId());
                ps.executeUpdate();
            }

            ObservableList<String> tecnicosSeleccionados = listaTecnicosDelDepartamento.getItems();
            if (!tecnicosSeleccionados.isEmpty()) {
                String insertTecnicoSQL = "INSERT INTO departamento_tecnico (id_departamento, nombreusuario_tecnico) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertTecnicoSQL)) {
                    for (String tecnico : tecnicosSeleccionados) {
                        ps.setInt(1, departamentoSeleccionado.getId());
                        ps.setString(2, tecnico);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }


            conn.commit(); 
            mostrarAlertaInfo("Edición Exitosa", "Departamento actualizado.");
            cargarDepartamentos(); 
            limpiarCampos(); 

        } catch (SQLException e) {
            Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al editar departamento", e);
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al hacer rollback", ex);
            }
            mostrarAlerta("Error de Base de Datos", "No se pudo actualizar el departamento: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); 
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al cerrar conexión", e);
            }
        }
    }

    @FXML
    private void eliminarDepartamento(ActionEvent event) {
        Departamento departamentoSeleccionado = tablaDepartamentos.getSelectionModel().getSelectedItem();
        if (departamentoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor, selecciona un departamento de la tabla para eliminar.");
            return;
        }

        if (tieneTicketsActivos(departamentoSeleccionado.getId())) {
            mostrarAlerta("No se puede eliminar", "El departamento tiene tickets activos asociados y no puede ser eliminado.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Estás seguro de eliminar el departamento '" + departamentoSeleccionado.getNombre() + "'?");
        confirmacion.setContentText("Esta acción eliminará también la cola de atención y las asignaciones de técnicos. No se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Connection conn = null;
            try {
                conn = getConnection();
                conn.setAutoCommit(false); 

                String deleteDepartamentoSQL = "DELETE FROM departamentos WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(deleteDepartamentoSQL)) {
                    ps.setInt(1, departamentoSeleccionado.getId());
                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected > 0) {
                        conn.commit(); 
                        mostrarAlertaInfo("Eliminación Exitosa", "Departamento eliminado exitosamente.");
                        cargarDepartamentos();
                        limpiarCampos(); 
                    } else {
                        conn.rollback(); 
                        mostrarAlerta("Error", "No se pudo eliminar el departamento.");
                    }
                }
            } catch (SQLException e) {
                Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al eliminar departamento", e);
                try {
                    if (conn != null) conn.rollback(); 
                } catch (SQLException ex) {
                    Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al hacer rollback", ex);
                }
                mostrarAlerta("Error de Base de Datos", "No se pudo eliminar el departamento: " + e.getMessage());
            } finally {
                try {
                    if (conn != null) conn.setAutoCommit(true); 
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al cerrar conexión", e);
                }
            }
        }
    }

    //Métodos para Asignar/Desasignar tecnicos
    @FXML
    private void asignarTecnico(ActionEvent event) {
        ObservableList<String> selectedTecnicos = listaTecnicosDisponibles.getSelectionModel().getSelectedItems();
        if (selectedTecnicos.isEmpty()) {
            mostrarAlerta("Advertencia", "Por favor, selecciona uno o más técnicos de la lista de disponibles.");
            return;
        }

        Departamento departamentoSeleccionado = tablaDepartamentos.getSelectionModel().getSelectedItem();
        Integer idDepartamentoActual = (departamentoSeleccionado != null) ? departamentoSeleccionado.getId() : null;

        for (String tecnico : selectedTecnicos) {
            try {
                if (tecnicoYaAsignadoAOtroDepartamento(tecnico, idDepartamentoActual)) {
                    mostrarAlerta("Error de Asignación", "El técnico '" + tecnico + "' ya está asignado a otro departamento. Un técnico solo puede pertenecer a un departamento a la vez.");
                    continue; 
                }

                if (!listaTecnicosDelDepartamento.getItems().contains(tecnico)) {
                    listaTecnicosDelDepartamento.getItems().add(tecnico);
                }
            } catch (SQLException e) {
                Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al verificar asignación de técnico", e);
                mostrarAlerta("Error de Base de Datos", "No se pudo verificar la asignación del técnico.");
                return;
            }
        }
        listaTecnicosDisponibles.getSelectionModel().clearSelection();
    }

    @FXML
    private void desasignarTecnico(ActionEvent event) {
        String selectedTecnico = listaTecnicosDelDepartamento.getSelectionModel().getSelectedItem();
        if (selectedTecnico == null) {
            mostrarAlerta("Advertencia", "Por favor, selecciona un técnico de la lista del departamento para desasignar.");
            return;
        }
        listaTecnicosDelDepartamento.getItems().remove(selectedTecnico);
        listaTecnicosDelDepartamento.getSelectionModel().clearSelection();
    }

    private boolean tecnicoYaAsignadoAOtroDepartamento(String nombreTecnico, Integer idDepartamentoActual) throws SQLException {
        String sql = "SELECT COUNT(*) FROM departamento_tecnico WHERE nombreusuario_tecnico = ?";
        if (idDepartamentoActual != null) {
            sql += " AND id_departamento != ?"; 
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreTecnico);
            if (idDepartamentoActual != null) {
                ps.setInt(2, idDepartamentoActual);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        }
        return false;
    }

    private boolean tieneTecnicosAsignadosEnOtroDepartamento(ObservableList<String> tecnicosParaAsignar, Integer idDepartamentoExcluir) {
        for (String tecnico : tecnicosParaAsignar) {
            try {
                if (tecnicoYaAsignadoAOtroDepartamento(tecnico, idDepartamentoExcluir)) {
                    mostrarAlerta("Error de Asignación", "El técnico '" + tecnico + "' ya está asignado a otro departamento. Un técnico solo puede pertenecer a un departamento a la vez.");
                    return true; 
                }
            } catch (SQLException e) {
                Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al verificar asignación de técnico durante el guardado", e);
                mostrarAlerta("Error de Base de Datos", "No se pudo verificar la asignación de los técnicos.");
                return true; 
            }
        }
        return false; 
    }


    @FXML
    private void limpiarCampos() {
        txtNombreDepartamento.clear();
        txtDescripcion.clear();
        listaTecnicosDisponibles.getSelectionModel().clearSelection();
        listaTecnicosDelDepartamento.getItems().clear(); 
        tablaDepartamentos.getSelectionModel().clearSelection(); 
    }

    @FXML
    private void mostrarMenu(ActionEvent event) {
        try {
            Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml"));
            Scene menuAdminScene = new Scene(menuAdminParent);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(menuAdminScene);
            currentStage.show();
        } catch (IOException ex) {
            Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al cargar menuAdmin.fxml", ex);
            mostrarAlerta("Error de Carga", "No se pudo cargar la ventana del menú principal.");
        }
    }

    private boolean validarCampos(String nombre) {
        if (nombre.isEmpty()) {
            mostrarAlerta("Error de Validación", "El nombre del departamento no puede estar vacío.");
            return false;
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            mostrarAlerta("Error de Validación", "El nombre del departamento debe contener entre 3 y 50 caracteres.");
            return false;
        }
        return true;
    }

    private boolean existeNombreDepartamento(Connection conn, String nombre, Integer idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM departamentos WHERE nombre = ?";
        if (idExcluir != null) {
            sql += " AND id != ?";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            if (idExcluir != null) {
                ps.setInt(2, idExcluir);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean tieneTicketsActivos(int idDepartamento) {
        String sql = "SELECT COUNT(*) FROM tickets WHERE id_departamento = ? AND estado NOT IN ('Cerrado', 'Resuelto')";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDepartamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(RegistroDepartamentosControlador.class.getName()).log(Level.SEVERE, "Error al verificar tickets activos", e);
            mostrarAlerta("Error de BD", "No se pudo verificar si el departamento tiene tickets activos.");
        }
        return false;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
    
}
