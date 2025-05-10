/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public abstract class Tecnico extends Persona{
    private String carne;
    
    public Tecnico(String nombre, String correo, String contrasena, String carne){
        super(nombre, correo, contrasena);
        this.carne = carne;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }
    
    
    public void crearTicket(){
    }
    
    public abstract void mostrarDatos();
}
