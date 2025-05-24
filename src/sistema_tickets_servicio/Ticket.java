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
public abstract class Ticket {
    
    private int id;
    private String titulo;
    private String descripcion;
    private int departamentoAsignadoId; 
    private String prioridad;
    private String adjuntosRuta; 
    private String estado;
    private LocalDateTime fechaCreacion;
    private int creadoPorUsuarioId;
    private Integer asignadoATecnicoId; 
    private LocalDateTime fechaUltimaActualizacion;

    
    public Ticket(int id, String titulo, String descripcion, int departamentoAsignadoId, String prioridad, String adjuntosRuta, String estado, LocalDateTime fechaCreacion, int creadoPorUsuarioId,
                  Integer asignadoATecnicoId, LocalDateTime fechaUltimaActualizacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.departamentoAsignadoId = departamentoAsignadoId;
        this.prioridad = prioridad;
        this.adjuntosRuta = adjuntosRuta;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.creadoPorUsuarioId = creadoPorUsuarioId;
        this.asignadoATecnicoId = asignadoATecnicoId;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    // Constructor para la creacion tickets
    public Ticket(String titulo, String descripcion, int departamentoAsignadoId,
                  String prioridad, String adjuntosRuta, int creadoPorUsuarioId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.departamentoAsignadoId = departamentoAsignadoId;
        this.prioridad = prioridad;
        this.adjuntosRuta = adjuntosRuta;
        this.creadoPorUsuarioId = creadoPorUsuarioId;
        this.estado = "Pendiente";
        this.fechaCreacion = LocalDateTime.now(); 
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDepartamentoAsignadoId() {
        return departamentoAsignadoId;
    }

    public void setDepartamentoAsignadoId(int departamentoAsignadoId) {
        this.departamentoAsignadoId = departamentoAsignadoId;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getAdjuntosRuta() {
        return adjuntosRuta;
    }

    public void setAdjuntosRuta(String adjuntosRuta) {
        this.adjuntosRuta = adjuntosRuta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getCreadoPorUsuarioId() {
        return creadoPorUsuarioId;
    }

    public void setCreadoPorUsuarioId(int creadoPorUsuarioId) {
        this.creadoPorUsuarioId = creadoPorUsuarioId;
    }

    public Integer getAsignadoATecnicoId() {
        return asignadoATecnicoId;
    }

    public void setAsignadoATecnicoId(Integer asignadoATecnicoId) {
        this.asignadoATecnicoId = asignadoATecnicoId;
    }

    public LocalDateTime getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(LocalDateTime fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }


}

