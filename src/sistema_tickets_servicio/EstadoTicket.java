/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public abstract class EstadoTicket extends Ticket {
    
    public EstadoTicket(int id, String descripcion, boolean estadoTicket, String notas){
    super(id, descripcion, estadoTicket, notas);
    }
     
}
