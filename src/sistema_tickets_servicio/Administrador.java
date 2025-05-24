/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public abstract class Administrador extends Persona {
          
     public Administrador(int id, String nombre, String correo, String contrasena, String id_admin){
         super(id, nombre, correo, contrasena);
     }
     
    @Override
    public abstract void mostrarDatos();
    
    public void gestionarUsuarios(){
    
    }
    
    public void asignarRoles(){
//    
    }
    
    public void gestionarDepartamentos(){
    
    }
    
}
