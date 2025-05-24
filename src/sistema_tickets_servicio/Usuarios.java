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
public class Usuarios {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nombreCompleto;
    private final SimpleStringProperty correoElectronico;
    private final SimpleStringProperty nombreUsuario;
    private final SimpleStringProperty contrasenaHash; 
    private final SimpleStringProperty rolAsignado;
    private final SimpleStringProperty departamento;
    private boolean activo;

    public Usuarios(int id, String nombreCompleto, String correoElectronico, String nombreUsuario, String contrasenaHash, String rolAsignado, String departamento, boolean activo) {
        this.id = new SimpleIntegerProperty(id);
        this.nombreCompleto = new SimpleStringProperty(nombreCompleto);
        this.correoElectronico = new SimpleStringProperty(correoElectronico);
        this.nombreUsuario = new SimpleStringProperty(nombreUsuario);
        this.contrasenaHash = new SimpleStringProperty(contrasenaHash);
        this.rolAsignado = new SimpleStringProperty(rolAsignado);
        this.departamento = new SimpleStringProperty(departamento);
        this.activo = activo;
    }

    
    public int getId() { 
        return id.get(); 
    }
    public String getNombrecompleto() { 
        return nombreCompleto.get(); 
    }
    public String getCorreoelectronico() { 
        return correoElectronico.get(); 
    }
    public boolean isActivo() {
        return activo;
    }
    public String getNombreusuario() { 
        return nombreUsuario.get(); 
    }
    public String getContrasena() { 
        return contrasenaHash.get(); 
    } 
    public String getRolasignado() { 
        return rolAsignado.get(); 
    }
    public String getDepartamento() { 
        return departamento.get(); 
    }

    
    public void setId(int value) { 
        id.set(value); 
    }
    public void setNombrecompleto(String value) { 
        nombreCompleto.set(value); 
    }
    public void setCorreoelectronico(String value) { 
        correoElectronico.set(value); 
    }
    public void setNombreusuario(String value) { 
        nombreUsuario.set(value); 
    }
    public void setContrasena(String value) { 
        contrasenaHash.set(value); 
    }
    public void setRolasignado(String value) { 
        rolAsignado.set(value); 
    }
    public void setDepartamento(String value) { 
        departamento.set(value); 
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
