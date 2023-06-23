/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author jcmm
 */
@Entity
public class Matricula implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Estudiante estudiante; 
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;  
    @ManyToOne
    private Seccion seccion;
    private String estado;//Pre-Matricula -  Matricula  
    private String estadoPA;//Asignado - Libre
    @OneToMany(mappedBy = "editor")
    private List<Item_Proyecto> item_Proyectos;
    @OneToMany(mappedBy = "matricula")
    private List<Integrante> integrantes;

    public Matricula() {
        this.setId(Long.parseLong("0"));
    }

    public Matricula(Long id, Estudiante estudiante, Date fecha, Seccion seccion, String estado, String estadoPA, List<Item_Proyecto> item_Proyectos, List<Integrante> integrantes) {
        this.id = id;
        this.estudiante = estudiante;
        this.fecha = fecha;
        this.seccion = seccion;
        this.estado = estado;
        this.estadoPA = estadoPA;
        this.item_Proyectos = item_Proyectos;
        this.integrantes = integrantes;
    }

   

   

    
    public boolean validarMatricula() {
        boolean valido = true;
        try{
            
        if (this.estudiante.toString().equals("") || this.seccion.getPrograma().getNombreCompleto().equals("")) {
            FacesUtil.addErrorMessage("No hay informacion de los siguientes entes:(estudiante o programa)");
            valido = false;
        }
        if (this.fecha.equals("") || this.estado.equals("")) {
            FacesUtil.addErrorMessage("no se han suministrado los siguientes datos(fecha o estado)");
            valido = false;
        }      
        }catch(NullPointerException npe){
            valido = false;
            FacesUtil.addErrorMessage("No hay informacion de los siguientes entes:(estudiante o programa)");
            
        }
        return valido;
    }

     public boolean habilitarVinculoGrupo() {
        boolean habilitado = false;
        try{
        if (this.getEstadoPA().equals("Libre")) {
            habilitado = true;
        }
        }catch(java.lang.NullPointerException npe){
            
        }
        return habilitado;
    }
    
    public boolean habilitarProyecto(){
        boolean habilitado=true;
        try{
        if(this.estado.equals("Financiera")){
            habilitado=false;
        }    
         }catch(java.lang.NullPointerException npe){
            
        }
        return habilitado;
    } 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Matricula[ id=" + getId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the estudiante
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

   

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

 
    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the estadoPA
     */
    public String getEstadoPA() {
        return estadoPA;
    }

    /**
     * @param estadoPA the estadoPA to set
     */
    public void setEstadoPA(String estadoPA) {
        this.estadoPA = estadoPA;
    }

    /**
     * @return the seccion
     */
    public Seccion getSeccion() {
        return seccion;
    }

    /**
     * @param seccion the seccion to set
     */
    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

   
    
}
