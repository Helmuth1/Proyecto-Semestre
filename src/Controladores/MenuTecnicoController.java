/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class MenuTecnicoController implements Initializable {
    
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnlista;
    @FXML
    private Button btncambiar;
    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAgregar;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }    
    
        @FXML
    private void crearTicket(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/creacionTicketTecnico.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
        @FXML
    private void listaSolicitudes(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/listaSolicitudesTecnico.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
        @FXML
    private void cambiarEstado(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/CambiarEstadoTecnico.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
        @FXML
    private void agregarNota(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/AgregarNotaTecnico.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
        @FXML
    private void regresarMenu(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/inicio.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
    
}
