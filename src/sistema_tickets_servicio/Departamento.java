/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author helmu
 */
public class Departamento {
    
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty descripcion;
    private final ObservableList<String> tecnicosAsignados;

    public Departamento(int id, String nombre, String descripcion, ObservableList<String> tecnicosAsignados) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.tecnicosAsignados = tecnicosAsignados != null ? tecnicosAsignados : FXCollections.observableArrayList();
    }

    
    public Departamento(String nombre, String descripcion) {
        this(0, nombre, descripcion, FXCollections.observableArrayList()); 
    }

    public Departamento(int id, String nombre) {
        this(id, nombre, null, null);
    }
        

    public int getId() { 
        return id.get(); 
    }
    public SimpleIntegerProperty idProperty() { 
        return id; 
    }

    public String getNombre() { 
        return nombre.get(); 
    }
    public SimpleStringProperty nombreProperty() { 
        return nombre; 
    }

    public String getDescripcion() { 
        return descripcion.get(); 
    }
    public SimpleStringProperty descripcionProperty() { 
        return descripcion; 
    }


    public ObservableList<String> getTecnicosAsignados() {
        return tecnicosAsignados;
    }


    public String getTecnicosAsignadosString() {
        return String.join(", ", tecnicosAsignados);
    }
    public SimpleStringProperty tecnicosAsignadosStringProperty() {
        return new SimpleStringProperty(getTecnicosAsignadosString());
    }

    @Override
    public String toString() {
        return getNombre(); 
    }
}
