/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sistema_tickets_servicio.ConexionBD;
import java.sql.DriverManager;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sistema_tickets_servicio.Usuarios;

/**
 *
 * @author helmu
 */
public class LoginControlador {
    
    @FXML
    private TextField txtNombreUsuario;
    
    @FXML
    private PasswordField txtContrasena;
    
    @FXML
    private ImageView imgLogoEmpresa;

    @FXML
    private Label lblNombreEmpresa;
    
    @FXML
    private Button btnIniciarSesion;
    
    @FXML
    private Button crearCuentabt;

    @FXML
    private Button iniciarSesionbt;
    
    private ConexionBD conexionBD = new ConexionBD();


    @FXML
    public void initialize() {
        lblNombreEmpresa.setText("Servicio de Tickets");
        
        Image logo = new Image("/imagenes/tickets.png"); 
        imgLogoEmpresa.setImage(logo);
        
        
    }
   
    @FXML
    private void iniciarSesion(ActionEvent event) {
        String usuario = txtNombreUsuario.getText();
        String contrasena = txtContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, ingresa tu usuario y contraseña.");
            return;
        }

        String url = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";
        String dbUser = "neondb_owner";
        String dbPassword = "npg_M9JAQdy0Enhl";

        String consulta = "SELECT id, nombrecompleto, correoelectronico, nombreusuario, contraseña, rolasignado, departamento, activo FROM usuarios WHERE nombreusuario = ? AND contraseña = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(consulta)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                boolean activo = rs.getBoolean("activo");
                String rol = rs.getString("rolasignado"); 

                if (activo) {
                    mostrarAlertaInformacion("Inicio de sesión exitoso", "Bienvenido, " + usuario + "!");

                    Usuarios usuarioLogueado = new Usuarios(
                        rs.getInt("id"),
                        rs.getString("nombrecompleto"),
                        rs.getString("correoelectronico"),
                        rs.getString("nombreusuario"),
                        rs.getString("contraseña"), 
                        rs.getString("rolasignado"),
                        rs.getString("departamento"),
                        rs.getBoolean("activo")
                    );
                    
                switch (rol.toLowerCase()) {
                case "administrador":
                mostrarMenuAdmin(event);
                break;
                case "técnico":
                case "tecnico":
                mostrarMenuTecnico(event);
                break;
                case "usuario":
                mostrarMenuUsuario(event);
                break;
                default:
        mostrarAlerta("Rol desconocido", "Tu rol no está registrado correctamente. Contacta al administrador.");
}
                } else {
                    mostrarAlerta("Inicio de sesión fallido", "Tu cuenta está inactiva. Contacta al administrador.");
                }
            } else {
                mostrarAlerta("Inicio de sesión fallido", "Usuario o contraseña incorrectos.");
            }

        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error de base de datos durante el inicio de sesión", e);
            mostrarAlerta("Error de conexión", "No se pudo conectar a la base de datos o hubo un error al procesar la solicitud.");
        }
    }
    
    @FXML
    private void mostrarCrearCuenta(ActionEvent event) throws IOException {
        Parent crearCuentaParent = FXMLLoader.load(getClass().getResource("/vista/CrearCuenta.fxml"));
        Scene crearCuentaScene = new Scene(crearCuentaParent);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(crearCuentaScene);
        currentStage.show();
    }

    @FXML
    private void mostrarMenuTecnico(ActionEvent event){
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/menuTecnico.fxml")); 
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        } catch (IOException ex) {
        Logger.getLogger(LoginControlador.class.getName()).log(Level.SEVERE, "Error al cargar menuTecnico.fxml", ex);
        mostrarAlerta("Error al cargar la ventana", "No se pudo cargar el menú del técnico.");
        }
    }
    
    @FXML
    private void mostrarMenuUsuario(ActionEvent event){
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/menuUser.fxml")); 
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        } catch (IOException ex) {
        Logger.getLogger(LoginControlador.class.getName()).log(Level.SEVERE, "Error al cargar menuUsuario.fxml", ex);
        mostrarAlerta("Error al cargar la ventana", "No se pudo cargar el menú del usuario.");
        }
    }

    @FXML
    private void mostrarMenuAdmin(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml")); 
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginControlador.class.getName()).log(Level.SEVERE, "Error al cargar menuAdmin.fxml", ex);
            mostrarAlerta("Error al cargar la ventana", "No se pudo cargar la ventana del menú principal.");
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

        private void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION); 
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


