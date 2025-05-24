/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import java.time.LocalDateTime;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author helmu
 */
public abstract class EstadoTicket {
    
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty descripcion;
    private BooleanProperty esFinal;
    private ObjectProperty<LocalDateTime> fechaCreacion;
    private ObjectProperty<LocalDateTime> fechaUltimaActualizacion;
    private ObservableList<EstadoTicket> estadosSiguientesPermitidos;

    // Constructor para crear nuevos estados
    public EstadoTicket(String nombre, String descripcion, boolean esFinal) {
        this.id = new SimpleIntegerProperty(); 
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.esFinal = new SimpleBooleanProperty(esFinal);
        this.fechaCreacion = new SimpleObjectProperty<>(LocalDateTime.now());
        this.fechaUltimaActualizacion = new SimpleObjectProperty<>(LocalDateTime.now());
        this.estadosSiguientesPermitidos = FXCollections.observableArrayList();
    }

    public EstadoTicket(int id, String nombre, String descripcion, boolean esFinal, LocalDateTime fechaCreacion, LocalDateTime fechaUltimaActualizacion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.esFinal = new SimpleBooleanProperty(esFinal);
        this.fechaCreacion = new SimpleObjectProperty<>(fechaCreacion);
        this.fechaUltimaActualizacion = new SimpleObjectProperty<>(fechaUltimaActualizacion);
        this.estadosSiguientesPermitidos = FXCollections.observableArrayList();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public boolean isEsFinal() {
        return esFinal.get();
    }

    public BooleanProperty esFinalProperty() {
        return esFinal;
    }

    public void setEsFinal(boolean esFinal) {
        this.esFinal.set(esFinal);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion.get();
    }

    public ObjectProperty<LocalDateTime> fechaCreacionProperty() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion.set(fechaCreacion);
    }

    public LocalDateTime getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion.get();
    }

    public ObjectProperty<LocalDateTime> fechaUltimaActualizacionProperty() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(LocalDateTime fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion.set(fechaUltimaActualizacion);
    }

    public ObservableList<EstadoTicket> getEstadosSiguientesPermitidos() {
        return estadosSiguientesPermitidos;
    }

    public void setEstadosSiguientesPermitidos(List<EstadoTicket> estadosSiguientesPermitidos) {
        this.estadosSiguientesPermitidos.setAll(estadosSiguientesPermitidos);
    }

    @Override
    public String toString() {
        return nombre.get();
    }
    
     
}
