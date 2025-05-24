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
    private String nombreusuario;
    
    public Usuario(int id, String nombrecompleto, String correoelectronico, String contrasena, String nombreusuario){
         super(id, nombrecompleto, correoelectronico, contrasena);
         this.nombreusuario = nombreusuario;
     }

    public String getnombreusuario() {
        return nombreusuario;
    }

    public void setnombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }
    
    public void crearTicket(){
    }
    
    public abstract void mostrarDatos();
    
}
