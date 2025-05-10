/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author helmu
 */
public class LoginControlador {
    
    @FXML
    public void iniciarSesion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Menú Administrador");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            ((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void crearCuenta(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/CrearCuenta.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Crear Cuenta");
            stage.setScene(new Scene(root));
            stage.show();

            // Cierra la ventana actual
            ((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
