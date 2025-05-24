/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author helmu
 */
public class TicketTabla {
    private final IntegerProperty numeroTicket;
    private final StringProperty estado;
    private final ObjectProperty<LocalDateTime> fechaCreacion;
    private final StringProperty departamento; 
    private final StringProperty prioridad;
    private final StringProperty resumen;

    public TicketTabla(int id, String estado, LocalDateTime fechaCreacion,
                  String departamento, String prioridad, String resumen) {
        this.numeroTicket = new SimpleIntegerProperty(id);
        this.estado = new SimpleStringProperty(estado);
        this.fechaCreacion = new SimpleObjectProperty<>(fechaCreacion);
        this.departamento = new SimpleStringProperty(departamento);
        this.prioridad = new SimpleStringProperty(prioridad);
        this.resumen = new SimpleStringProperty(resumen);
    }

    public IntegerProperty numeroTicketProperty() { return numeroTicket; }
    public StringProperty estadoProperty() { return estado; }
    public ObjectProperty<LocalDateTime> fechaCreacionProperty() { return fechaCreacion; }
    public StringProperty departamentoProperty() { return departamento; }
    public StringProperty prioridadProperty() { return prioridad; }
    public StringProperty resumenProperty() { return resumen; }
}


