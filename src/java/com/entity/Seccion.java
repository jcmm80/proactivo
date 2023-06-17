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
import javax.persistence.Transient;

/**
 *
 * @author jcmm
 */
@Entity
public class Seccion implements Serializable {

    @OneToMany(mappedBy = "seccion")
    private List<Evaluacion> evaluacions;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String denominacion;
    @ManyToOne
    private Semestre semestre;
    @ManyToOne
    private Periodo periodo;
    @ManyToOne
    private ProgramaAcademico programa;

    @OneToMany(mappedBy = "seccion", fetch = FetchType.LAZY)
    private List<Asignatura> asignaturas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "com.entity.Seccion[ id=" + id + " ]";
    }

    public boolean validarSeccion() {
        boolean habilitar = true;
        try {
            if (this.getPeriodo().getId() < 0 || this.getPrograma().getId() < 0 || this.getSemestre().getId() < 0) {
                habilitar = false;
                FacesUtil.addErrorMessage("Falta informacion para registrar una sección");
            }
        } catch (java.lang.NullPointerException npe) {
            habilitar = false;
            FacesUtil.addErrorMessage("Falta informacion para registrar una sección");
        }
        return habilitar;
    }

    /**
     * @return the denominacion
     */
    public String getDenominacion() {
        return denominacion;
    }

    /**
     * @param denominacion the denominacion to set
     */
    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    /**
     * @return the semestre
     */
    public Semestre getSemestre() {
        return semestre;
    }

    /**
     * @param semestre the semestre to set
     */
    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    /**
     * @return the periodo
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @return the programa
     */
    public ProgramaAcademico getPrograma() {
        return programa;
    }

    /**
     * @param programa the programa to set
     */
    public void setPrograma(ProgramaAcademico programa) {
        this.programa = programa;
    }

    /**
     * @return the asignaturas
     */
    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    /**
     * @param asignaturas the asignaturas to set
     */
    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public String getSeccion() {
        String sec = "";
        try {
            sec = this.semestre.getDenominacion() + "-" + this.denominacion;
        } catch (NullPointerException npe) {
            sec = "";
        }
        return sec;
    }

}