/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author helmu
 */
public class Prioridad {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nombre;
    private final SimpleIntegerProperty nivel;

    public Prioridad(int id, String nombre, int nivel) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.nivel = new SimpleIntegerProperty(nivel);
    }


    public int getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public int getNivel() {
        return nivel.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public SimpleIntegerProperty nivelProperty() {
        return nivel;
    }

    @Override
    public String toString() {
        return nombre.get(); 
    }
}
