/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import sistema_tickets_servicio.ConexionBD;

/**
 *
 * @author helmu
 */
public class RegistrodepControlador {
       
    @FXML
    private Button btnRegresar;
    
    @FXML 
    private TextField txtNombreDepartamento;
    
    @FXML 
    private TextArea txtDescripcion;
    
    @FXML 
    private ListView<String> listTecnicos;
    
    private ConexionBD conexionBD = new ConexionBD();
    
    @FXML
    public void initialize() {
    listTecnicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    cargarTecnicosDesdeBD();
    }

    private void cargarTecnicosDisponibles() {
    String url = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";
    String query = "SELECT nombreusuario FROM usuarios WHERE rolasignado = 'Tecnico'";
    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            listTecnicos.getItems().add(rs.getString("nombreusuario"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void cargarTecnicosDesdeBD() {
    String url = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";
    String sql = "SELECT nombreusuario FROM usuarios WHERE rolasignado = 'Tecnico'";
    
    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String nombreTecnico = rs.getString("nombreusuario");
            listTecnicos.getItems().add(nombreTecnico);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        mostrarAlerta("Error de carga", "No se pudo obtener la lista de técnicos.");
    }
}
    
    
    @FXML
    private void guardarDepartamento(ActionEvent event) {
    String nombre = txtNombreDepartamento.getText().trim();
    String descripcion = txtDescripcion.getText().trim();
    List<String> tecnicosSeleccionados = listTecnicos.getSelectionModel().getSelectedItems();

    if (nombre.length() < 3 || nombre.length() > 50) {
        mostrarAlerta("Nombre inválido", "El nombre debe tener entre 3 y 50 caracteres.");
        return;
    }
    
    String url = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";
    String insertDepartamento = "INSERT INTO departamentos (nombre, descripcion) VALUES (?, ?) RETURNING id";
    String insertRelacion = "INSERT INTO departamento_tecnico (id_departamento, nombreusuario) VALUES (?, ?)";

    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement insertDeptStmt = conn.prepareStatement(insertDepartamento);
    ) {
        insertDeptStmt.setString(1, nombre);
        insertDeptStmt.setString(2, descripcion.isEmpty() ? null : descripcion);

        ResultSet rs = insertDeptStmt.executeQuery();
        if (rs.next()) {
            int idDepartamento = rs.getInt("id");

            try (PreparedStatement insertRelacionStmt = conn.prepareStatement(insertRelacion)) {
                for (String tecnico : tecnicosSeleccionados) {
                    insertRelacionStmt.setInt(1, idDepartamento);
                    insertRelacionStmt.setString(2, tecnico);
                    insertRelacionStmt.addBatch();
                }
                insertRelacionStmt.executeBatch();
            }

            mostrarAlertaInfo("Departamento creado", "El departamento fue creado y los técnicos asignados.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudo crear el departamento.");
    }
}
    
    @FXML
    private void mostrarMenu(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
        
    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
