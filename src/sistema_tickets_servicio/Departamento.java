/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_tickets_servicio;

/**
 *
 * @author helmu
 */
public class Departamento {
    private String nombreDepartamento;
    private String descripcion;
    private Tecnico [] Tecnico;
    private int cantidadTecnicos;
    
    public Departamento(){
    this.cantidadTecnicos = 0;
    }

    public Departamento(String nombreDepartamento, String descripcion, Tecnico[] Tecnico, int cantidadTecnicos) {
        this.nombreDepartamento = nombreDepartamento;
        this.descripcion = descripcion;
        this.Tecnico = Tecnico;
        this.cantidadTecnicos = cantidadTecnicos;
    }
            
    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadTecnicos() {
        return cantidadTecnicos;
    }

    public void setCantidadTecnicos(int cantidadTecnicos) {
        this.cantidadTecnicos = cantidadTecnicos;
    }
    
    
    public void asignarTecnico(){
    }
    
}
