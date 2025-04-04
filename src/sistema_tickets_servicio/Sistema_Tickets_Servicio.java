/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package sistema_tickets_servicio;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 *
 * @author helmu
 */
public class Sistema_Tickets_Servicio extends Application {

    @Override
    public void start(Stage PantallaPrincipal) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/CrearCuenta.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            PantallaPrincipal.setScene(scene);
            PantallaPrincipal.setTitle("Inicio de Sesion");
            PantallaPrincipal.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
