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
public class FlujosTrabajoControlador {
    
       
    @FXML
    private Button btnRegresar;
    
    @FXML
    public void initialize() {

    }

    @FXML
    private void mostrarMenu(ActionEvent event) throws IOException {
        Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml"));
        Scene menuAdminScene = new Scene(menuAdminParent);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.setScene(menuAdminScene);
        currentStage.show();
    }
}
