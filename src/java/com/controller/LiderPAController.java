/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.LiderPA;
import com.entity.Periodo;
import com.entity.Profesor;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.services.LiderPAServices;
import com.services.SeccionServices;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author sgpaAdmin2
 */
@ManagedBean
@SessionScoped
public class LiderPAController implements Serializable {

    private LiderPA liderPA = new LiderPA();
    private Periodo periodo = new Periodo();//para almacenar el el periodo actual
    private ProgramaAcademico programa=new ProgramaAcademico();
//    private Semestre semestre=new Semestre();

    LiderPAServices lidser = new LiderPAServices();
    SeccionServices secser=new SeccionServices();

    private List<LiderPA> lideresPA = new LinkedList();
    private List<Profesor> profesores = new LinkedList();
    private List<Seccion> secciones=new LinkedList();

    //controladores
    @ManagedProperty("#{periodoController}")
    private PeriodoController percont;
    @ManagedProperty("#{programaController}")
    private ProgramaController progcont;
    @ManagedProperty("#{profesorController}")
    private ProfesorController profcont;

    public void agregarProfesor(Profesor p) {
        getLiderPA().setProfesor(p);
    }

    public void agregarPrograma(ProgramaAcademico pa) {
        setPrograma(pa);
    }

    public void agregarPeriodo(Periodo p) {
        setPeriodo(p);
        obtenerseccionesPeriodo();
    }

     public void obtenerseccionesPeriodo(){       
        secciones=secser.obtenerSeccionesXPeriodo_Programa(programa, periodo);        
    }
    
     public void agregarSeccion(Seccion sec){
         liderPA.setSeccion(sec);
     }
    
    public void registrarLiderPA() {
//        liderPA.setSemestre(getSemestre());
        try {
            if (liderPA.validarliderPA()) {
                lidser.crear(liderPA);
                liderPA = new LiderPA();
                
            }
        }catch(java.lang.NullPointerException npe){
            
        }
    }

    public LiderPAController() {
    }

    public LiderPA getLiderPA() {
        return liderPA;
    }

    public void setLiderPA(LiderPA liderPA) {
        this.liderPA = liderPA;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public List<LiderPA> getLideresPA() {
        return lideresPA;
    }

    public void setLideresPA(List<LiderPA> lideresPA) {
        this.lideresPA = lideresPA;
    }

    public PeriodoController getPercont() {
        return percont;
    }

    public void setPercont(PeriodoController percont) {
        this.percont = percont;
    }

    public ProgramaController getProgcont() {
        return progcont;
    }

    public void setProgcont(ProgramaController progcont) {
        this.progcont = progcont;
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }

    public ProfesorController getProfcont() {
        return profcont;
    }

    public void setProfcont(ProfesorController profcont) {
        this.profcont = profcont;
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
     * @return the secciones
     */
    public List<Seccion> getSecciones() {
        return secciones;
    }

    /**
     * @param secciones the secciones to set
     */
    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }
}
