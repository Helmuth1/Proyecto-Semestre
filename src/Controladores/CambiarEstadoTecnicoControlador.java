/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import sistema_tickets_servicio.ConexionBD;
import sistema_tickets_servicio.Tickets;

/**
 *
 * @author helmu
 */
public class CambiarEstadoTecnicoControlador {
        @FXML private TableView<Tickets> ticketTable;
    @FXML private TableColumn<Tickets, Integer> idColumn;
    @FXML private TableColumn<Tickets, String> titleColumn;
    @FXML private TableColumn<Tickets, String> statusColumn;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button backButton;

    private ObservableList<Tickets> ticketList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        titleColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));

        statusComboBox.setItems(FXCollections.observableArrayList("Finalizado"));

        cargarTickets();
    }

    private void cargarTickets() {
        ticketList.clear();
        try (Connection conn = ConexionBD.getPostgreSQLConnection()) {
            String sql = "SELECT id, titulo, estado FROM tickets WHERE estado <> 'Finalizado'";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ticketList.add(new Tickets(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("estado")
                    ));
                }
                ticketTable.setItems(ticketList);
            }
        } catch (SQLException e) {
            showAlert("Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void guardarCambios() {
        Tickets selected = ticketTable.getSelectionModel().getSelectedItem();
        String nuevoEstado = statusComboBox.getValue();

        if (selected == null || nuevoEstado == null) {
            showAlert("Campos requeridos", "Seleccione un ticket y un estado.");
            return;
        }

        try (Connection conn = ConexionBD.getPostgreSQLConnection()) {
            String updateSQL = "UPDATE tickets SET estado = ?, fecha_ultima_actualizacion = NOW() WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                stmt.setString(1, nuevoEstado);
                stmt.setInt(2, selected.getId());
                stmt.executeUpdate();
                showAlert("Éxito", "El estado fue actualizado.");
                cargarTickets();
            }
        } catch (SQLException e) {
            showAlert("Error al actualizar", e.getMessage());
        }
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/menuTecnico.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showAlert("Error al volver al menú", e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
