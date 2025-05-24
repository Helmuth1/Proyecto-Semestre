/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public abstract class Persona {
    int id;
    private String nombrecompleto;
    private String correoelectronico;
    private String contrasena;
    
    public Persona(int id, String nombrecompleto, String correoelectronico, String contrasena){
    this.id = id;
    this.nombrecompleto = nombrecompleto;
    this.correoelectronico = correoelectronico;
    this.contrasena = contrasena;
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getNombrecompleto(){
        return nombrecompleto;
    }
    
    public void setNombrecompleto(String nombrecompleto){
        this.nombrecompleto = nombrecompleto;
    }
    
    public String getCorreoelectronico(){
        return correoelectronico;
    }
    
    public void setCorreoelectronico(String correoelectronico){
        this.correoelectronico = correoelectronico;
    }
    
    public String getContrasena(){
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public void mostrarDatos() {
        System.out.println("Datos - " +
                           " nombre: " + nombrecompleto +
                           " correo: " + correoelectronico +
                           " contrasena: " + contrasena);
    }
}
