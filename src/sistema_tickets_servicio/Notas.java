/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import java.util.Date;

/**
 *
 * @author helmu
 */
public abstract class Notas extends Ticket {
    private String contenido;
    private Date fecha;

    public Notas(int id, String descripcion, boolean estadoTicket, String notas, String contenido, Date fecha){
        super(id, descripcion, estadoTicket, notas);
        this.contenido = contenido;
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
