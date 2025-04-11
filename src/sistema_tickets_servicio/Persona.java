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
    private String nombre;
    private String correo;
    private String contrasena;
    
    public Persona(String nombre, String correo, String contrasena){
    this.nombre = nombre;
    this.correo = correo;
    this.contrasena = contrasena;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getCorreo(){
        return correo;
    }
    
    public void setCorreo(String correo){
        this.correo = correo;
    }
    
    public String getContrasena(){
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public void mostrarDatos() {
        System.out.println("Datos - " +
                           " nombre: " + nombre +
                           " correo: " + correo +
                           " contrasena: " + contrasena);
    }
}
