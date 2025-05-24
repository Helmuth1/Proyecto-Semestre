/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import sistema_tickets_servicio.ControladorSesion;
import sistema_tickets_servicio.EstadoTicket;
import sistema_tickets_servicio.EstadoTickets1;

/**
 *
 * @author helmu
 */
public class DefinirEstadosControlador {
      
    private static final Logger LOGGER = Logger.getLogger(DefinirEstadosControlador.class.getName());
    private EstadoTickets1 estado1;
    private ObservableList<EstadoTicket> listaEstados;
    private EstadoTicket estadoSeleccionado; 

    @FXML
    private TableView<EstadoTicket> tblEstados;
    @FXML
    private TableColumn<EstadoTicket, Integer> colId;
    @FXML
    private TableColumn<EstadoTicket, String> colNombre;
    @FXML
    private TableColumn<EstadoTicket, Boolean> colEsFinal;

    @FXML
    private Label lblFormTitulo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private CheckBox chkEsFinal;
    @FXML
    private ListView<EstadoTicket> lstEstadosDisponibles;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRegresar;

    private ObservableList<EstadoTicket> todosLosEstadosParaListView; 

    private static final int ID_USUARIO_SISTEMA = 0; 
    
    @FXML
    public void initialize() {
        estado1 = new EstadoTickets1();
        listaEstados = FXCollections.observableArrayList();
        todosLosEstadosParaListView = FXCollections.observableArrayList();
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEsFinal.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isEsFinal()));
        colEsFinal.setCellFactory(column -> new TableCell<EstadoTicket, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Sí" : "No"); 
                }
            }
        });

        tblEstados.setItems(listaEstados);

        tblEstados.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                estadoSeleccionado = newValue;
                if (estadoSeleccionado != null) {
                    cargarDetallesEstado(estadoSeleccionado);
                    btnEliminar.setDisable(false); 
                } else {
                    limpiarCampos();
                    btnEliminar.setDisable(true);
                }
            });

        lstEstadosDisponibles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cargarEstados(); 
        deshabilitarFormulario(true); 
    }

    private void cargarEstados() {
        try {
            List<EstadoTicket> estados = estado1.getAllEstados();
            listaEstados.setAll(estados);
            todosLosEstadosParaListView.setAll(estados);
            lstEstadosDisponibles.setItems(todosLosEstadosParaListView);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar estados de ticket", e);
            mostrarAlertaError("Error de Carga", "No se pudieron cargar los estados desde la base de datos.");
        }
    }

    private void cargarDetallesEstado(EstadoTicket estado) {
        lblFormTitulo.setText("Editar Estado");
        txtNombre.setText(estado.getNombre());
        txtDescripcion.setText(estado.getDescripcion());
        chkEsFinal.setSelected(estado.isEsFinal());

        deshabilitarFormulario(false);
        
        lstEstadosDisponibles.getSelectionModel().clearSelection();

        ObservableList<EstadoTicket> tempLista = FXCollections.observableArrayList(todosLosEstadosParaListView);
        tempLista.removeIf(e -> e.getId() == estado.getId()); 

        lstEstadosDisponibles.setItems(tempLista);

        for (EstadoTicket siguiente : estado.getEstadosSiguientesPermitidos()) {
            tempLista.stream()
                     .filter(e -> e.getId() == siguiente.getId())
                     .findFirst()
                     .ifPresent(e -> lstEstadosDisponibles.getSelectionModel().select(e));
        }
    }

    private void limpiarCampos() {
        lblFormTitulo.setText("Detalles del Estado");
        txtNombre.clear();
        txtDescripcion.clear();
        chkEsFinal.setSelected(false);
        lstEstadosDisponibles.getSelectionModel().clearSelection();
        estadoSeleccionado = null; 
        btnEliminar.setDisable(true); 
        deshabilitarFormulario(true); 
        
        lstEstadosDisponibles.setItems(todosLosEstadosParaListView);
    }

    private void deshabilitarFormulario(boolean deshabilitar) {
        txtNombre.setDisable(deshabilitar);
        txtDescripcion.setDisable(deshabilitar);
        chkEsFinal.setDisable(deshabilitar);
        lstEstadosDisponibles.setDisable(deshabilitar);
        btnGuardar.setDisable(deshabilitar);
    }

    @FXML
    private void NuevoEstado() {
        limpiarCampos();
        lblFormTitulo.setText("Crear Nuevo Estado");
        deshabilitarFormulario(false);
        tblEstados.getSelectionModel().clearSelection(); 
        estadoSeleccionado = null; 
        btnEliminar.setDisable(true); 
        
        lstEstadosDisponibles.setItems(todosLosEstadosParaListView);
    }

    @FXML
    private void EditarEstado() {
        if (estadoSeleccionado != null) {
            cargarDetallesEstado(estadoSeleccionado);
            deshabilitarFormulario(false);
        } else {
            mostrarAlertaAdvertencia("Selección Requerida", "Por favor, selecciona un estado de la tabla para editar.");
        }
    }

    @FXML
    private void GuardarEstado(ActionEvent event) {
    String nombre = txtNombre.getText().trim();
    String descripcion = txtDescripcion.getText().trim();
    boolean esFinal = chkEsFinal.isSelected();
    List<EstadoTicket> estadosDestinoSeleccionados = new ArrayList<>(lstEstadosDisponibles.getSelectionModel().getSelectedItems());

    if (!validarCampos(nombre, esFinal, estadosDestinoSeleccionados)) return;

    int idUsuarioActual = obtenerIdUsuarioActual();
    boolean exito = false;
    int idEstado;

    if (estadoSeleccionado == null) {
        EstadoTicket nuevoEstado = new EstadoTicket(nombre, descripcion, esFinal) {};
        idEstado = estado1.insertEstado(nuevoEstado, idUsuarioActual);

        if (idEstado != -1) {
            nuevoEstado.setId(idEstado);
            exito = true;
        }
    } else {
        estadoSeleccionado.setNombre(nombre);
        estadoSeleccionado.setDescripcion(descripcion);
        estadoSeleccionado.setEsFinal(esFinal);
        estadoSeleccionado.setFechaUltimaActualizacion(LocalDateTime.now());
        exito = estado1.updateEstado(estadoSeleccionado, idUsuarioActual);
        idEstado = estadoSeleccionado.getId();
    }

    if (exito) {
        if (esFinal && !estadosDestinoSeleccionados.isEmpty()) {
            mostrarAlertaAdvertencia("Advertencia de Lógica", 
                "Un estado marcado como final no puede tener transiciones salientes. Las transiciones seleccionadas han sido descartadas.");
            estadosDestinoSeleccionados.clear();
        }

        boolean transicionesOk = estado1.setTransicionesEstado(idEstado, estadosDestinoSeleccionados, idUsuarioActual);

        if (transicionesOk) {
            mostrarAlertaInfo("Éxito", "Estado '" + nombre + "' " + (estadoSeleccionado == null ? "creado" : "actualizado") + " exitosamente.");
        } else {
            mostrarAlertaError("Error", "Estado '" + nombre + "' " + (estadoSeleccionado == null ? "creado" : "actualizado") + ", pero falló al establecer transiciones.");
        }

        cargarEstados();
        limpiarCampos();
    } else {
        mostrarAlertaError("Error", "No se pudo guardar el estado.");
    }
}

    @FXML
    private void EliminarEstado() {
        if (estadoSeleccionado == null) {
            mostrarAlertaAdvertencia("Selección Requerida", "Por favor, selecciona un estado de la tabla para eliminar.");
            return;
        }

        if (estado1.isEstadoInUse(estadoSeleccionado.getId())) {
            mostrarAlertaError("Estado en Uso", "No se puede eliminar el estado '" + estadoSeleccionado.getNombre() + "' porque está asignado a tickets activos.");
            return;
        }

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Eliminación");
        confirmAlert.setHeaderText("¿Estás seguro de que quieres eliminar el estado '" + estadoSeleccionado.getNombre() + "'?");
        confirmAlert.setContentText("Esta acción es irreversible y eliminará también todas las transiciones relacionadas.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int idUsuarioActual = ControladorSesion.getInstancia().isSesionActiva() && ControladorSesion.getInstancia().getUsuarioLogueado() != null
                                   ? ControladorSesion.getInstancia().getUsuarioLogueado().getId()
                                   : ID_USUARIO_SISTEMA;

            if (estado1.deleteEstado(estadoSeleccionado.getId(), idUsuarioActual)) {
                mostrarAlertaInfo("Éxito", "Estado '" + estadoSeleccionado.getNombre() + "' eliminado exitosamente.");
                cargarEstados();
                limpiarCampos();
            } else {
                mostrarAlertaError("Error", "No se pudo eliminar el estado '" + estadoSeleccionado.getNombre() + "'.");
            }
        }
    }

    @FXML
    private void Cancelar() {
        limpiarCampos();
        tblEstados.getSelectionModel().clearSelection();
        deshabilitarFormulario(true);
    }

    @FXML
    private void regresarMenu(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml")); 
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginControlador.class.getName()).log(Level.SEVERE, "Error al cargar menuAdmin.fxml", ex);
            mostrarAlertaError("Error al cargar la ventana", "No se pudo cargar la ventana del menú principal.");
        }
    }

    private void mostrarAlerta(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private boolean validarCampos(String nombre, boolean esFinal, List<EstadoTicket> estadosDestinoSeleccionados) {
    if (nombre.isEmpty()) {
        mostrarAlertaError("Error de Validación", "El nombre del estado no puede estar vacío.");
        return false;
    }

    if (nombre.length() < 3 || nombre.length() > 50) {
        mostrarAlertaError("Error de Validación", "El nombre del estado debe tener entre 3 y 50 caracteres.");
        return false;
    }

    int idActual = (estadoSeleccionado != null) ? estadoSeleccionado.getId() : -1;
    if (estado1.estadoExists(nombre, idActual)) {
        mostrarAlertaError("Error de Validación", "Ya existe un estado con este nombre.");
        return false;
    }

    if (!esFinal && estadosDestinoSeleccionados.isEmpty()) {
        mostrarAlertaError("Error de Validación", "Un estado no final debe tener al menos un estado siguiente permitido.");
        return false;
    }

    return true;
    }

    private int obtenerIdUsuarioActual() {
    var sesion = ControladorSesion.getInstancia();
    return (sesion.isSesionActiva() && sesion.getUsuarioLogueado() != null)
           ? sesion.getUsuarioLogueado().getId()
           : ID_USUARIO_SISTEMA;
    }

    private void mostrarAlertaError(String title, String content) {
        mostrarAlerta(AlertType.ERROR, title, null, content);
    }

    private void mostrarAlertaAdvertencia(String title, String content) {
        mostrarAlerta(AlertType.WARNING, title, null, content);
    }

    private void mostrarAlertaInfo(String title, String content) {
        mostrarAlerta(AlertType.INFORMATION, title, null, content);
    }
}
