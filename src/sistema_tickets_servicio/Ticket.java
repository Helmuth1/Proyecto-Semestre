/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public abstract class Ticket {
    private int id;
    private String descripcion;
    private boolean estadoTicket;
    private String notas;
    
    public Ticket(int id, String descripcion, boolean estadoTicket, String notas){
        this.id = id;
        this.descripcion = descripcion;
        this.estadoTicket = estadoTicket;
        this.notas = notas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstadoTicket() {
        return estadoTicket;
    }

    public void setEstadoTicket(boolean estadoTicket) {
        this.estadoTicket = estadoTicket;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public void cambiarEstado(){
    }
    
    public void agregarNota(){
    }
    
}
