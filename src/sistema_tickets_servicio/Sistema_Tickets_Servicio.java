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
    public void start(Stage primaryStage) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("Prueba.fxml"));

        
        Scene scene = new Scene(root);
         primaryStage.setScene(scene);
         primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
