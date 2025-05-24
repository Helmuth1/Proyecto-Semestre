/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import sistema_tickets_servicio.Usuarios;
/**
 *
 * @author helmu
 */
public class ControladorSesion {

    private static ControladorSesion instancia; 
    private Usuarios usuarioLogueado;      

 
    private ControladorSesion() {
        
    }

    public static ControladorSesion getInstancia() {
        if (instancia == null) {
            instancia = new ControladorSesion();
        }
        return instancia;
    }

    public void setUsuarioLogueado(Usuarios usuario) {
        this.usuarioLogueado = usuario;
    }

    public Usuarios getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public boolean isSesionActiva() {
        return usuarioLogueado != null;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
        System.out.println("Sesión cerrada.");
    }
}
