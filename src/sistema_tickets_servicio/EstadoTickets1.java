/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import Controladores.LoginControlador; // Para obtener el ID del usuario logueado
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sistema_tickets_servicio.ConexionBD;

/**
 *
 * @author helmu
 */
public class EstadoTickets1 {

    private static final Logger LOGGER = Logger.getLogger(EstadoTickets1.class.getName());
    private static final int ID_USUARIO_SISTEMA = 0; 


    
    public int insertEstado(EstadoTicket estado, int idUsuarioQueRealizaAccion) {
        String sqlEstado = "INSERT INTO estados_ticket (nombre, descripcion, es_final, fecha_creacion, fecha_ultima_actualizacion) VALUES (?, ?, ?, ?, ?) RETURNING id";
        Connection conn = null;
        PreparedStatement pstmtEstado = null;
        ResultSet rs = null;
        int nuevoId = -1;

        try {
            conn = ConexionBD.getPostgreSQLConnection();
            conn.setAutoCommit(false); 

            // 1. Insertar el estado principal
            pstmtEstado = conn.prepareStatement(sqlEstado);
            pstmtEstado.setString(1, estado.getNombre());
            pstmtEstado.setString(2, estado.getDescripcion());
            pstmtEstado.setBoolean(3, estado.isEsFinal());
            pstmtEstado.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmtEstado.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            rs = pstmtEstado.executeQuery();
            
            if (pstmtEstado.executeUpdate() > 0) {
                rs = pstmtEstado.getGeneratedKeys();
                if (rs.next()) {
                    nuevoId = rs.getInt(1);
                    estado.setId(nuevoId); 
                    LOGGER.log(Level.INFO, "Estado de ticket insertado con ID: {0}", nuevoId);

                    registrarLogEstado(conn, nuevoId, idUsuarioQueRealizaAccion, "CREATE", "Estado creado: " + estado.getNombre());
                    conn.commit(); 
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rb_e) {
                LOGGER.log(Level.SEVERE, "Error al hacer rollback en insertEstado", rb_e);
            }
            LOGGER.log(Level.SEVERE, "Error al insertar estado de ticket: " + estado.getNombre(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtEstado != null) pstmtEstado.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en insertEstado", e);
            }
        }
        return nuevoId;
    }

    public boolean updateEstado(EstadoTicket estado, int idUsuarioQueRealizaAccion) {
        String sqlEstado = "UPDATE estados_ticket SET nombre = ?, descripcion = ?, es_final = ?, fecha_ultima_actualizacion = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmtEstado = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getPostgreSQLConnection();
            conn.setAutoCommit(false); 

            pstmtEstado = conn.prepareStatement(sqlEstado);
            pstmtEstado.setString(1, estado.getNombre());
            pstmtEstado.setString(2, estado.getDescripcion());
            pstmtEstado.setBoolean(3, estado.isEsFinal());
            pstmtEstado.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmtEstado.setInt(5, estado.getId());

            if (pstmtEstado.executeUpdate() > 0) {
                LOGGER.log(Level.INFO, "Estado de ticket actualizado con ID: {0}", estado.getId());

                registrarLogEstado(conn, estado.getId(), idUsuarioQueRealizaAccion, "UPDATE", "Estado actualizado: " + estado.getNombre());
                
                conn.commit();
                exito = true;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rb_e) {
                LOGGER.log(Level.SEVERE, "Error al hacer rollback en updateEstado", rb_e);
            }
            LOGGER.log(Level.SEVERE, "Error al actualizar estado de ticket con ID: " + estado.getId(), e);
        } finally {
            try {
                if (pstmtEstado != null) pstmtEstado.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en updateEstado", e);
            }
        }
        return exito;
    }

    public boolean deleteEstado(int idEstado, int idUsuarioQueRealizaAccion) {
        String sql = "DELETE FROM estados_ticket WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getPostgreSQLConnection();
            conn.setAutoCommit(false); 

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idEstado);

            if (pstmt.executeUpdate() > 0) {
                LOGGER.log(Level.INFO, "Estado de ticket eliminado con ID: {0}", idEstado);
               
                registrarLogEstado(conn, idEstado, idUsuarioQueRealizaAccion, "DELETE", "Estado eliminado (ID: " + idEstado + ")");
                
                conn.commit(); 
                exito = true;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rb_e) {
                LOGGER.log(Level.SEVERE, "Error al hacer rollback en deleteEstado", rb_e);
            }
            LOGGER.log(Level.SEVERE, "Error al eliminar estado de ticket con ID: " + idEstado, e);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en deleteEstado", e);
            }
        }
        return exito;
    }

    
        public List<EstadoTicket> getAllEstados() throws SQLException {
        Connection conn = null;
        List<EstadoTicket> estados = new ArrayList<>();
        try {
            conn = ConexionBD.getPostgreSQLConnection();
            return estados;
        } finally {
            ConexionBD.closeConnection(conn);
        }
    }

        public boolean estadoExists(String nombreEstado, int idActual) {
        Connection conn = null; 
        if (conn == null) {
        LOGGER.severe("No se pudo obtener una conexión a la base de datos.");
        return false;
        }
        String sql = "SELECT COUNT(*) FROM estados_ticket WHERE nombre = ? AND id <> ?"; // Ajusta la columna del ID
        try {
            conn = ConexionBD.getPostgreSQLConnection(); 
            try (PreparedStatement stmt = conn.prepareStatement(sql)) { 
                stmt.setString(1, nombreEstado);
                stmt.setInt(2, idActual);
                try (ResultSet rs = stmt.executeQuery()) { 
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar si el estado '" + nombreEstado + "' existe. ID: " + idActual, e);
            return false;
        } finally {
            ConexionBD.closeConnection(conn);
        }
        return false;
    }

    public boolean isEstadoInUse(int idEstado) {
        String sql = "SELECT COUNT(*) FROM tickets WHERE estado = ?"; 
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean inUse = false;

        try {
            conn = ConexionBD.getPostgreSQLConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, idEstado); 

            rs = pstmt.executeQuery();
            if (rs.next()) {
                inUse = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar si el estado está en uso: " + idEstado, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en isEstadoInUse", e);
            }
        }
        return inUse;
    }
    
    public boolean setTransicionesEstado(int idEstadoOrigen, List<EstadoTicket> estadosDestino, int idUsuarioQueRealizaAccion) {
        String sqlDelete = "DELETE FROM transiciones_estado WHERE id_estado_origen = ?";
        String sqlInsert = "INSERT INTO transiciones_estado (id_estado_origen, id_estado_destino) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmtDelete = null;
        PreparedStatement pstmtInsert = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getPostgreSQLConnection();
            conn.setAutoCommit(false); 

            pstmtDelete = conn.prepareStatement(sqlDelete);
            pstmtDelete.setInt(1, idEstadoOrigen);
            pstmtDelete.executeUpdate();

            pstmtInsert = conn.prepareStatement(sqlInsert);
            for (EstadoTicket destino : estadosDestino) {
                pstmtInsert.setInt(1, idEstadoOrigen);
                pstmtInsert.setInt(2, destino.getId());
                pstmtInsert.addBatch(); 
            }
            pstmtInsert.executeBatch(); 

            String desc = "Transiciones actualizadas para el estado ID " + idEstadoOrigen + ". Destinos: " + 
                          estadosDestino.stream().map(EstadoTicket::getNombre).collect(java.util.stream.Collectors.joining(", "));
            registrarLogEstado(conn, idEstadoOrigen, idUsuarioQueRealizaAccion, "UPDATE_TRANSITIONS", desc);

            conn.commit(); 
            exito = true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rb_e) {
                LOGGER.log(Level.SEVERE, "Error al hacer rollback en setTransicionesEstado", rb_e);
            }
            LOGGER.log(Level.SEVERE, "Error al establecer transiciones para el estado ID: " + idEstadoOrigen, e);
        } finally {
            try {
                if (pstmtDelete != null) pstmtDelete.close();
                if (pstmtInsert != null) pstmtInsert.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en setTransicionesEstado", e);
            }
        }
        return exito;
    }

    public List<EstadoTicket> getEstadosSiguientesPermitidos(int idEstadoOrigen, List<EstadoTicket> todosLosEstados) {
        List<EstadoTicket> destinos = new ArrayList<>();
        String sql = "SELECT id_estado_destino FROM transiciones_estado WHERE id_estado_origen = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getPostgreSQLConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idEstadoOrigen);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idDestino = rs.getInt("id_estado_destino");
                todosLosEstados.stream()
                               .filter(e -> e.getId() == idDestino)
                               .findFirst()
                               .ifPresent(destinos::add);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener transiciones para el estado: " + idEstadoOrigen, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en getEstadosSiguientesPermitidos", e);
            }
        }
        return destinos;
    }

    private void registrarLogEstado(Connection conn, int idEstado, int idUsuarioQueCambio, String tipoCambio, String descripcionCambio) throws SQLException {
        String sql = "INSERT INTO auditoria_estados_ticket (id_estado, id_usuario_que_cambio, tipo_cambio, descripcion_cambio, fecha_cambio) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) { 
            pstmt.setInt(1, idEstado);
            pstmt.setInt(2, idUsuarioQueCambio);
            pstmt.setString(3, tipoCambio);
            pstmt.setString(4, descripcionCambio);
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Log de auditoría para estado ID {0} registrado: {1}", new Object[]{idEstado, descripcionCambio});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al registrar log de estado para ID " + idEstado, e);
            throw e; 
        }
    }
}

