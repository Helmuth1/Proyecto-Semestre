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
     private String identificacion;
     
     public Administrador(String nombre, String correo, String contrasena, String identificacion){
         super(nombre, correo, contrasena);
         this.identificacion = identificacion;
     }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
     
    @Override
    public abstract void mostrarDatos();
}
