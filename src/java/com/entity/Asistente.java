/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author jcmm
 */
@Entity
public class Asistente implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Integrante estudiante;
    @ManyToOne
    private Tutoria tutoria;
    private boolean asistio;
    private String motivo;//motivo inasistencia

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String infoAsistencia(){
        if(this.asistio){
            return "Asistió";
        }else{
            if(this.getMotivo()!=null){
                return "No Asistio:\n"+this.getMotivo();
            }else{
                return "";
            }
            
        }
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
        if (!(object instanceof Asistente)) {
            return false;
        }
        Asistente other = (Asistente) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Asistente[ id=" + getId() + " ]";
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
    public Integrante getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(Integrante estudiante) {
        this.estudiante = estudiante;
    }

    /**
     * @return the tutoria
     */
    public Tutoria getTutoria() {
        return tutoria;
    }

    /**
     * @param tutoria the tutoria to set
     */
    public void setTutoria(Tutoria tutoria) {
        this.tutoria = tutoria;
    }

    /**
     * @return the asistio
     */
    public boolean isAsistio() {
        return asistio;
    }

    /**
     * @param asistio the asistio to set
     */
    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
}
