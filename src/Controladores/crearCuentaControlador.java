/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author helmu
 */
public class crearCuentaControlador {
    
    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnRegresar;
    
    @FXML 
    private TextField txtNombre;
    
    @FXML 
    private TextField txtCorreo;
    
    @FXML 
    private TextField txtUsuario;
    
    @FXML 
    private TextField txtContrasena;
    
    

    @FXML
    public void initialize() {
 
    }
    
    @FXML
    private void crearCuenta(ActionEvent event) throws IOException {
    String nombre = txtNombre.getText().trim();
    String correo = txtCorreo.getText().trim();
    String usuario = txtUsuario.getText().trim();
    String contrasena = txtContrasena.getText();
    String rol = "Usuario";
    String departamento = null;

    if (nombre.length() < 3 || nombre.length() > 100) {
        mostrarAlerta("Nombre inválido", "El nombre debe tener entre 3 y 100 caracteres.");
        return;
    }

    if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
        mostrarAlerta("Correo inválido", "Introduce un correo electrónico válido.");
        return;
    }

    if (usuario.length() < 5 || usuario.length() > 30) {
        mostrarAlerta("Usuario inválido", "El nombre de usuario debe tener entre 5 y 30 caracteres.");
        return;
    }

    if (!contrasena.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
        mostrarAlerta("Contraseña insegura", "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial.");
        return;
    }

    String url = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";

    String verificarQuery = "SELECT COUNT(*) FROM usuarios WHERE correoelectronico = ? OR nombreusuario = ?";
    String insertarQuery = "INSERT INTO usuarios (nombrecompleto, correoelectronico, nombreusuario, contraseña, rolasignado, departamento) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(url);
     PreparedStatement verificarStmt = conn.prepareStatement(verificarQuery);
     PreparedStatement insertarStmt = conn.prepareStatement(insertarQuery)) {
     
    verificarStmt.setString(1, correo);
    verificarStmt.setString(2, usuario);

    ResultSet rs = verificarStmt.executeQuery();
    rs.next();
    if (rs.getInt(1) > 0) {
        mostrarAlerta("Duplicado", "El correo o nombre de usuario ya están registrados.");
        return;
    }

    insertarStmt.setString(1, nombre);
    insertarStmt.setString(2, correo);
    insertarStmt.setString(3, usuario);
    insertarStmt.setString(4, contrasena); 
    insertarStmt.setString(5, rol);
    insertarStmt.setString(6, rol.equalsIgnoreCase("Técnico") ? departamento : null);

    int filasAfectadas = insertarStmt.executeUpdate();

    if (filasAfectadas > 0) {
        mostrarAlertaInfo("Cuenta creada", "Usuario registrado exitosamente.");
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Inicio.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } else {
        mostrarAlerta("Error", "No se pudo crear el usuario.");
    }
    
    } catch (SQLException e) {
    e.printStackTrace(); 
    mostrarAlerta("Error de base de datos", "No se pudo registrar el usuario.");
    }
 }

    @FXML
    private void mostrarmenuAdmin(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }

    @FXML
    private void mostrarLogin(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/vista/inicio.fxml"));
        Scene loginScene = new Scene(loginParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(loginScene);
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
