/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jcmm
 */
@Entity
public class UnidadCompetencia implements Serializable {

    @OneToMany(mappedBy = "unidad", fetch = FetchType.EAGER)
    private List<Competencia> competencias;

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String descripcion;
    @ManyToOne
    private Asignatura asignatura;

    public boolean validarUnidadCompetencia() {
        boolean valido = true;
        try {
            if (this.asignatura.getId() < 0) {
                FacesUtil.addErrorMessage("la unidad de competencia debe estar asociada a una asignatura");
                valido = false;
            }
        } catch (java.lang.NullPointerException npe) {
            valido = false;
            FacesUtil.addErrorMessage("la unidad de competencia debe estar asociada a una asignatura");
        }
        return valido;
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
        if (!(object instanceof UnidadCompetencia)) {
            return false;
        }
        UnidadCompetencia other = (UnidadCompetencia) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.UnidadCompetencia[ id=" + getId() + " ]";
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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the asignatura
     */
    public Asignatura getAsignatura() {
        return asignatura;
    }

    /**
     * @param asignatura the asignatura to set
     */
    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    /**
     * @return the competencias
     */
    public List<Competencia> getCompetencias() {
        return competencias;
    }

    /**
     * @param competencias the competencias to set
     */
    public void setCompetencias(List<Competencia> competencias) {
        this.competencias = competencias;
    }

}
