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
import javafx.stage.Stage;

/**
 *
 * @author helmu
 */
public class menuAdminControlador {
    
    @FXML
    private Button btnRegresar;
    
    @FXML
    private Button btnRoles;
    
    @FXML
    private Button btnRegistrar;
    
    @FXML
    private Button btnLista;
    
    @FXML
    private Button btnDefinir;
    
    @FXML
    private Button btnAgregar;
    
    @FXML
    private Button btnConfigurar;
    
    @FXML
    private Button btnCrear;
    
    @FXML
    public void initialize() {
        
    }

    @FXML
    private void mostrarInicio(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/inicio.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
    @FXML
    private void crearTicket(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/creacionTicket.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
    @FXML
    private void registrarUsuarios(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/Gestion_roles.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
    @FXML
    private void mostrarRegistrar(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/RegistroDepartamentos.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
    @FXML
    private void mostrarLista(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/listaSolicitudes.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
        @FXML
    private void mostrarEstado(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/CambiarEstado.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
      
    @FXML
    private void mostrarNota(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/AgregarNota.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
    
        @FXML
    private void mostrarFlujos(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/listaSolicitudes.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }

}
