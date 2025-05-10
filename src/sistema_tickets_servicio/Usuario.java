/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public abstract class Usuario extends Persona {
    private String usuario;
    
    public Usuario(String nombre, String correo, String contrasena, String Usuario){
         super(nombre, correo, contrasena);
         this.usuario = usuario;
     }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public void crearTicket(){
    }
    
    public abstract void mostrarDatos();
    
}
