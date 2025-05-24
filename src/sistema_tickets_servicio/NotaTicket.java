/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

import java.time.LocalDateTime;

/**
 *
 * @author helmu
 */
public class NotaTicket {
    private int id;
    private int idTicket;
    private int idUsuarioCreador;
    private String nombreUsuarioCreador;
    private String contenido;
    private String adjuntoRuta;
    private LocalDateTime fechaCreacion;

    public NotaTicket(int id, int idTicket, int idUsuarioCreador, String nombreUsuarioCreador,
                      String contenido, String adjuntoRuta, LocalDateTime fechaCreacion) {
        this.id = id;
        this.idTicket = idTicket;
        this.idUsuarioCreador = idUsuarioCreador;
        this.nombreUsuarioCreador = nombreUsuarioCreador;
        this.contenido = contenido;
        this.adjuntoRuta = adjuntoRuta;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(int idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public String getNombreUsuarioCreador() {
        return nombreUsuarioCreador;
    }

    public void setNombreUsuarioCreador(String nombreUsuarioCreador) {
        this.nombreUsuarioCreador = nombreUsuarioCreador;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getAdjuntoRuta() {
        return adjuntoRuta;
    }

    public void setAdjuntoRuta(String adjuntoRuta) {
        this.adjuntoRuta = adjuntoRuta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    
}
