/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sistema_tickets_servicio.Usuarios;

/**
 *
 * @author helmu
 */
public class gestionRolesControlador {
     
    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnCrear; 
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;

    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private TextField txtCorreoElectronico;
    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtContrasena;
    @FXML
    private ComboBox<String> comboDepartamento;
    @FXML
    private ComboBox<String> comboRol;
    @FXML
    private ComboBox<String> comboEstado;

    @FXML
    private TableView<Usuarios> tablaUsuarios;
    @FXML
    private TableColumn<Usuarios, Integer> colId;
    @FXML
    private TableColumn<Usuarios, String> colNombreCompleto;
    @FXML
    private TableColumn<Usuarios, String> colCorreoElectronico;
    @FXML
    private TableColumn<Usuarios, String> colNombreUsuario;
    @FXML
    private TableColumn<Usuarios, String> colContrasena;
    @FXML
    private TableColumn<Usuarios, String> colRolAsignado; 
    @FXML
    private TableColumn<Usuarios, String> colDepartamento;
    @FXML
    private TableColumn<Usuarios, Boolean> colActivo;
    
    
    


    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreCompleto.setCellValueFactory(new PropertyValueFactory<>("nombrecompleto"));
        colCorreoElectronico.setCellValueFactory(new PropertyValueFactory<>("correoelectronico"));
        colNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreusuario"));
        colContrasena.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
        colRolAsignado.setCellValueFactory(new PropertyValueFactory<>("rolasignado")); 
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        ObservableList<String> roles = FXCollections.observableArrayList("Administrador", "Técnico", "Usuario");
        ObservableList<String> estados = FXCollections.observableArrayList("Activo", "Inactivo");
        comboEstado.setItems(estados);
        comboRol.setItems(roles);
        comboEstado.setValue("Activo");
        
        comboDepartamento.setDisable(true);
        comboRol.valueProperty().addListener((obs, oldVal, newVal) -> {
        comboDepartamento.setDisable(!"Técnico".equals(newVal));
        });

        cargarDepartamentosDesdeBD();
        cargarUsuarios();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombreCompleto.setText(newSelection.getNombrecompleto());
                txtCorreoElectronico.setText(newSelection.getCorreoelectronico());
                txtNombreUsuario.setText(newSelection.getNombreusuario());
                txtContrasena.setText(newSelection.getContrasena()); 
                comboRol.setValue(newSelection.getRolasignado());
                comboDepartamento.setValue(newSelection.getDepartamento());
                comboEstado.setValue(newSelection.isActivo() ? "Activo" : "Inactivo");
            } else {
                limpiarCampos();
            }
        });
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://ep-blue-truth-a47f4n7n-pooler.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_M9JAQdy0Enhl&sslmode=require";
        String user = "neondb_owner";
        String password = "npg_M9JAQdy0Enhl";
        return DriverManager.getConnection(url, user, password);
    }

    private void cargarDepartamentosDesdeBD() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            List<String> departamentos = new ArrayList<>();
            String sql = "SELECT nombre FROM departamentos";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                departamentos.add(rs.getString("nombre"));
            }
            comboDepartamento.setItems(FXCollections.observableArrayList(departamentos));
        } catch (SQLException e) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cargar departamentos", e);
            mostrarAlerta("Error de BD", "No se pudieron cargar los departamentos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de BD", e);
            }
        }
    }

    private void cargarUsuarios() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ObservableList<Usuarios> listaUsuarios = FXCollections.observableArrayList(); 
            String sql = "SELECT id, nombrecompleto, correoelectronico, nombreusuario, contraseña, rolasignado, departamento, activo FROM usuarios";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listaUsuarios.add(new Usuarios( 
                    rs.getInt("id"),
                    rs.getString("nombrecompleto"),
                    rs.getString("correoelectronico"),
                    rs.getString("nombreusuario"),
                    rs.getString("contraseña"), 
                    rs.getString("rolasignado"),
                    rs.getString("departamento"),
                    rs.getBoolean("activo")
                ));
            }
            tablaUsuarios.setItems(listaUsuarios);
        } catch (SQLException e) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cargar usuarios", e);
            mostrarAlerta("Error de BD", "No se pudieron cargar los usuarios: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de BD", e);
            }
        }
    }

    private void limpiarCampos() {
        txtNombreCompleto.clear();
        txtCorreoElectronico.clear();
        txtNombreUsuario.clear();
        txtContrasena.clear();
        comboRol.getSelectionModel().clearSelection();
        comboDepartamento.getSelectionModel().clearSelection();
        comboEstado.setValue("Activo");
        tablaUsuarios.getSelectionModel().clearSelection(); 
        
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cancelarRegistro(ActionEvent event) {
    limpiarCampos();
    }

    private void enviarCredencialesPorCorreo(String correo, String nombreUsuario, String contrasena) {
    System.out.println("SIMULANDO ENVÍO DE CORREO A: " + correo);
    System.out.println("Usuario: " + nombreUsuario + ", Contraseña: " + contrasena);
} 
    
    @FXML
    private void crearUsuario(ActionEvent event) {
        String nombreCompleto = txtNombreCompleto.getText().trim();
        String correoElectronico = txtCorreoElectronico.getText().trim();
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String contrasena = txtContrasena.getText(); 
        String rol = comboRol.getValue();
        String departamento = comboDepartamento.getValue();
        boolean activo = comboEstado.getValue().equals("Activo");

        // Validaciones de campos
        if (!validarCampos(nombreCompleto, correoElectronico, nombreUsuario, contrasena, rol, departamento)) {
            return; 
        }

        Connection conn = null;
        try {
            conn = getConnection();
            if (existeCorreo(conn, correoElectronico, null)) {
                mostrarAlerta("Error de Validación", "El correo electrónico ya está registrado.");
                return;
            }
            if (existeNombreUsuario(conn, nombreUsuario, null)) {
                mostrarAlerta("Error de Validación", "El nombre de usuario ya existe.");
                return;
            }
            if (!rol.equalsIgnoreCase("Técnico")) {
                departamento = null;
            }
        } catch (SQLException e) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al verificar unicidad", e);
            mostrarAlerta("Error de BD", "Ocurrió un error al verificar la unicidad de los datos.");
            return;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar conexión de unicidad", e);
            }
        }

        String contrasenaAlmacenar = contrasena;

        conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO usuarios (nombrecompleto, correoelectronico, nombreusuario, contraseña, rolasignado, departamento, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, correoElectronico);
            pstmt.setString(3, nombreUsuario);
            pstmt.setString(4, contrasenaAlmacenar); 
            pstmt.setString(5, rol);
            pstmt.setString(6, departamento != null && !departamento.isEmpty() ? departamento : null);
            pstmt.setBoolean(7, activo);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                mostrarAlertaInfo("Éxito", "Usuario creado exitosamente.");
                enviarCredencialesPorCorreo(correoElectronico, nombreUsuario, contrasena);
                cargarUsuarios();
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "No se pudo crear el usuario.");
            }
        } catch (SQLException e) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al insertar usuario", e);
            mostrarAlerta("Error de BD", "Error al crear usuario: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de inserción", e);
            }
        }
    }

    @FXML
    private void editarUsuario(ActionEvent event) {
        Usuarios usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un usuario de la tabla para editar.");
            return;
        }

        String nombreCompleto = txtNombreCompleto.getText().trim();
        String correoElectronico = txtCorreoElectronico.getText().trim();
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String nuevaContrasena = txtContrasena.getText(); 
        String rol = comboRol.getValue();
        String departamento = comboDepartamento.getValue();
        boolean activo = comboEstado.getValue().equals("Activo");

        String contrasenaAValidar = nuevaContrasena.isEmpty() ? usuarioSeleccionado.getContrasena() : nuevaContrasena;
        if (!validarCampos(nombreCompleto, correoElectronico, nombreUsuario, contrasenaAValidar, rol, departamento)) {
            return;
        }

        Connection conn = null;
        try {
            conn = getConnection();
            if (existeCorreo(conn, correoElectronico, usuarioSeleccionado.getId())) {
                mostrarAlerta("Error de Validación", "El correo electrónico ya está registrado por otro usuario.");
                return;
            }
            if (existeNombreUsuario(conn, nombreUsuario, usuarioSeleccionado.getId())) {
                mostrarAlerta("Error de Validación", "El nombre de usuario ya existe para otro usuario.");
                return;
            }
        } catch (SQLException e) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al verificar unicidad para edición", e);
            mostrarAlerta("Error de BD", "Ocurrió un error al verificar la unicidad de los datos.");
            return;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar conexión de unicidad", e);
            }
        }

        String contrasenaFinal = nuevaContrasena.isEmpty() ? usuarioSeleccionado.getContrasena() : nuevaContrasena;

        //Actualizar en BD
        conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            String sql = "UPDATE usuarios SET nombrecompleto=?, correoelectronico=?, nombreusuario=?, contraseña=?, rolasignado=?, departamento=?, activo=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, correoElectronico);
            pstmt.setString(3, nombreUsuario);
            pstmt.setString(4, contrasenaFinal); // Contraseña en texto plano
            pstmt.setString(5, rol);
            pstmt.setString(6, departamento != null && !departamento.isEmpty() ? departamento : null);
            pstmt.setBoolean(7, activo);
            pstmt.setInt(8, usuarioSeleccionado.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                mostrarAlertaInfo("Éxito", "Usuario actualizado exitosamente.");
                cargarUsuarios();
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el usuario.");
            }
        } catch (SQLException e) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al actualizar usuario", e);
            mostrarAlerta("Error de BD", "Error al actualizar usuario: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de actualización", e);
            }
        }
    }

    @FXML
    private void eliminarUsuario(ActionEvent event) {
        Usuarios usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem(); 
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un usuario de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Estás seguro de eliminar a " + usuarioSeleccionado.getNombrecompleto() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = getConnection();
                String sql = "DELETE FROM usuarios WHERE id=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, usuarioSeleccionado.getId());
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    mostrarAlertaInfo("Éxito", "Usuario eliminado exitosamente.");
                    cargarUsuarios();
                    limpiarCampos();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el usuario.");
                }
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al eliminar usuario", e);
                mostrarAlerta("Error de BD", "Error al eliminar usuario: " + e.getMessage());
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de eliminación", e);
                }
            }
        }
    }

    // Metodos Navegacion
    @FXML
    private void mostrarMenu(ActionEvent event) {
        try {
            Parent menuAdminParent = FXMLLoader.load(getClass().getResource("/vista/menuAdmin.fxml"));
            Scene menuAdminScene = new Scene(menuAdminParent);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(menuAdminScene);
            currentStage.show();
        } catch (IOException ex) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cargar menuAdmin.fxml", ex);
            mostrarAlerta("Error de Carga", "No se pudo cargar la ventana del menú principal.");
        }
    }

    private boolean validarCampos(String nombreCompleto, String correoElectronico, String nombreUsuario, String contrasena, String rol, String departamento) {

        if (nombreCompleto.length() < 3 || nombreCompleto.length() > 100) {
            mostrarAlerta("Error de Validación", "El nombre completo debe contener entre 3 y 100 caracteres.");
            return false;
        }

        if (!correoElectronico.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            mostrarAlerta("Error de Validación", "El correo electrónico no es válido.");
            return false;
        }

        if (nombreUsuario.length() < 5 || nombreUsuario.length() > 30) {
            mostrarAlerta("Error de Validación", "El nombre de usuario debe contener entre 5 y 30 caracteres.");
            return false;
        }

        if (!validarContrasena(contrasena)) {
            mostrarAlerta("Error de Validación", "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial.");
            return false;
        }

        if (rol == null || rol.isEmpty()) {
            mostrarAlerta("Error de Validación", "Debe seleccionar un rol.");
            return false;
        }

        if (rol.equalsIgnoreCase("Tecnico") && (departamento == null || departamento.isEmpty())) {
            mostrarAlerta("Error de Validación", "Para el rol 'Técnico', debe seleccionar un departamento.");
            return false;
        }
        return true;
    }

    private boolean validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }
        boolean tieneMayuscula = false;
        boolean tieneNumero = false;
        boolean tieneCaracterEspecial = false;
        String caracteresEspeciales = "!@#$%^&*()_-+={}[]|;:<>,.?/~";
        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            } else if (Character.isDigit(c)) {
                tieneNumero = true;
            } else if (caracteresEspeciales.contains(String.valueOf(c))) {
                tieneCaracterEspecial = true;
            }
        }
        return tieneMayuscula && tieneNumero && tieneCaracterEspecial;
    }

    private boolean existeDepartamento(Connection conn, String nombreDepartamento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM departamentos WHERE nombre = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreDepartamento);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    private boolean existeCorreo(Connection conn, String correo, Integer idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correoelectronico = ?";
        if (idExcluir != null) {
            sql += " AND id != ?";
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            if (idExcluir != null) {
                pstmt.setInt(2, idExcluir);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void registrarLogUsuario(int idUsuarioAfectado, String tipoCambio, String descripcionCambio) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO usuarios_log (id_usuario_afectado, tipo_cambio, descripcion_cambio, usuario_que_cambio) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idUsuarioAfectado);
            pstmt.setString(2, tipoCambio);
            pstmt.setString(3, descripcionCambio);
            pstmt.setString(4, obtenerUsuarioActual()); // Obtener el usuario actual
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al registrar log de usuario", e);

        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(gestionRolesControlador.class.getName()).log(Level.SEVERE, "Error al cerrar recursos de log", e);
            }
        }
    }
    
    private String obtenerUsuarioActual() {
        return "ADMIN_ACTUAL"; 
    }
    
    private boolean existeNombreUsuario(Connection conn, String nombreUsuario, Integer idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombreusuario = ?";
        if (idExcluir != null) {
            sql += " AND id != ?";
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            if (idExcluir != null) {
                pstmt.setInt(2, idExcluir);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
