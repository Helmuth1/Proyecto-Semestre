/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistema_tickets_servicio.ConexionBD;

/**
 *
 * @author helmu
 */
public class AgregarNotaTecnicoControlador {
        @FXML private TextField txtIdTicket;
    @FXML private TextArea txtContenidoNota;
    @FXML private TextField txtAdjuntoRuta;
    @FXML private Label lblMensaje;
    @FXML private Button btnRegresar;

    private File archivoAdjunto;

    @FXML
    public void seleccionarAdjunto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo adjunto");
        archivoAdjunto = fileChooser.showOpenDialog(null);
        if (archivoAdjunto != null) {
            txtAdjuntoRuta.setText(archivoAdjunto.getAbsolutePath());
        }
    }

    @FXML
    public void guardarNota(ActionEvent event) {
        String idTicket = txtIdTicket.getText().trim();
        String contenido = txtContenidoNota.getText().trim();
        String rutaAdjunto = archivoAdjunto != null ? archivoAdjunto.getAbsolutePath() : null;

        if (idTicket.isEmpty() || contenido.isEmpty()) {
            lblMensaje.setText("El ID del ticket y el contenido de la nota son obligatorios.");
            return;
        }

        try (Connection conn = ConexionBD.getPostgreSQLConnection()) {
            String sql = "INSERT INTO notas_tickets (id_ticket, id_usuario_creador, contenido, adjunto_ruta) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(idTicket));
            stmt.setInt(2, 1);
            stmt.setString(3, contenido);
            stmt.setString(4, rutaAdjunto);

            stmt.executeUpdate();
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("Nota guardada con éxito.");

            // Limpia los campos 
            txtContenidoNota.clear();
            txtIdTicket.clear();
            txtAdjuntoRuta.clear();
            archivoAdjunto = null;

        } catch (SQLException e) {
            e.printStackTrace();
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error al guardar la nota.");
        }
    }
    
    @FXML
    private void regresarMenu(ActionEvent event) {
        try {
            Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/menuTecnico.fxml"));
            Scene menuAdminScene = new Scene(menuAdminParent);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(menuAdminScene);
            currentStage.show();
        } catch (IOException ex) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cargar menuAdmin.fxml", ex);
        }
    }
    
    @FXML
    public void limpiar() {
    txtIdTicket.clear();
    txtContenidoNota.clear();
    txtAdjuntoRuta.clear();
    }
}
