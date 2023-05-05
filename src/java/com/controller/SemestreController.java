/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Semestre;
import com.services.SemestreServices;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author jcmm
 */
@ManagedBean
@SessionScoped
public class SemestreController implements Serializable{

    private Semestre semestre = new Semestre();
    private List<Semestre> semestres=new LinkedList();    
    SemestreServices semser=new SemestreServices();
    /**
     * Creates a new instance of SemestreController
     */
    public SemestreController() {
        obtenerSemestres();
    }
    
    public void obtenerSemestres(){
        setSemestres(semser.consultarTodo(Semestre.class));
    }
     /**
     * @return the semestres
     */
    public List<Semestre> getSemestres() {
        return semestres;
    }

    /**
     * @param semestres the semestres to set
     */
    public void setSemestres(List<Semestre> semestres) {
        this.semestres = semestres;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
}
