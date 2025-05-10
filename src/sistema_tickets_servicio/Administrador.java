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
     private String id_admin;
          
     public Administrador(String nombre, String correo, String contrasena, String id_admin){
         super(nombre, correo, contrasena);
         this.id_admin = id_admin;
     }

    public String getId_admin() {
        return id_admin;
    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
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
