/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public abstract class FlujoTrabajo extends Ticket {
    private boolean estado;
    
    public FlujoTrabajo(int id, String descripcion, boolean estadoTicket, String notas, boolean estado){
    super(id, descripcion, estadoTicket, notas);
    this.estado = estado;
    }
    
}
