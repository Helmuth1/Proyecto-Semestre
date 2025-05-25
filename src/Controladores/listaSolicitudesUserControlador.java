/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistema_tickets_servicio.TicketTabla;

/**
 *
 * @author helmu
 */
public class listaSolicitudesUserControlador {
    
    @FXML private TableView<TicketTabla> tablaTickets;
    @FXML private TableColumn<TicketTabla, Integer> colNumeroTicket;
    @FXML private TableColumn<TicketTabla, String> colEstado;
    @FXML private TableColumn<TicketTabla, LocalDateTime> colFechaCreacion;
    @FXML private TableColumn<TicketTabla, String> colDepartamento;
    @FXML private TableColumn<TicketTabla, String> colPrioridad;
    @FXML private TableColumn<TicketTabla, String> colResumen;

    @FXML private TextField txtBuscarTicket;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private ComboBox<String> cmbPrioridad;
    @FXML private ComboBox<String> cmbDepartamento;
    @FXML private Label lblMensajeEstado;

    private ObservableList<TicketTabla> listaTickets = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNumeroTicket.setCellValueFactory(data -> data.getValue().numeroTicketProperty().asObject());
        colEstado.setCellValueFactory(data -> data.getValue().estadoProperty());
        colFechaCreacion.setCellValueFactory(data -> data.getValue().fechaCreacionProperty());
        colDepartamento.setCellValueFactory(data -> data.getValue().departamentoProperty());
        colPrioridad.setCellValueFactory(data -> data.getValue().prioridadProperty());
        colResumen.setCellValueFactory(data -> data.getValue().resumenProperty());

        cmbEstado.setItems(FXCollections.observableArrayList("Pendiente", "En Proceso", "Finalizado"));
        cmbPrioridad.setItems(FXCollections.observableArrayList("Baja", "Media", "Alta"));
        cmbDepartamento.setItems(FXCollections.observableArrayList("Soporte", "Redes", "Desarrollo", "Otros"));

        cargarTickets(null, null, null, null);
    }

    private void cargarTickets(String texto, String estado, String prioridad, String departamento) {
        listaTickets.clear();
        String sql = "SELECT * FROM tickets WHERE 1=1";

        List<Object> params = new ArrayList<>();
        if (texto != null && !texto.trim().isEmpty()) {
            sql += " AND (CAST(id AS TEXT) LIKE ? OR resumen ILIKE ?)";
            params.add("%" + texto + "%");
            params.add("%" + texto + "%");
        }
        if (estado != null) {
            sql += " AND estado = ?";
            params.add(estado);
        }
        if (prioridad != null) {
            sql += " AND prioridad = ?";
            params.add(prioridad);
        }
        if (departamento != null) {
            sql += " AND departamento = ?";
            params.add(departamento);
        }

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?sslmode=require", "neondb_owner", "npg_M9JAQdy0Enhl");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            TicketTabla t = new TicketTabla(
                rs.getInt("id"),
                rs.getString("estado"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                rs.getString("departamento_asignado_id"),
                rs.getString("prioridad"),
                rs.getString("descripcion")
            );
                listaTickets.add(t);
            }

            tablaTickets.setItems(listaTickets);
            lblMensajeEstado.setText(listaTickets.isEmpty() ? "No se encontraron tickets." : "");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMensajeEstado.setText("Error al cargar los tickets.");
        }
    }

    @FXML
    private void aplicarFiltros(ActionEvent event) {
        String texto = txtBuscarTicket.getText();
        String estado = cmbEstado.getValue();
        String prioridad = cmbPrioridad.getValue();
        String departamento = cmbDepartamento.getValue();

        cargarTickets(texto, estado, prioridad, departamento);
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscarTicket.clear();
        cmbEstado.getSelectionModel().clearSelection();
        cmbPrioridad.getSelectionModel().clearSelection();
        cmbDepartamento.getSelectionModel().clearSelection();
        cargarTickets(null, null, null, null);
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/menuUser.fxml"));
            Stage stage = (Stage) tablaTickets.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            lblMensajeEstado.setText("Error al regresar al menú.");
        }
    }
}
