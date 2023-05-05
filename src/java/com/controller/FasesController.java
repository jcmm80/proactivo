/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Fase;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.services.FaseServices;
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
public class FasesController implements Serializable{

    private List<Fase> fases=new LinkedList();
    
    
    
    FaseServices fasser=new FaseServices();
    
    /**
     * Creates a new instance of FasesController
     */
    public FasesController() {
    }
    public void obtenerFasesXPrograma(ProgramaAcademico pa,Periodo p){
        setFases(fasser.obtenerFasesXPrograma(pa, p));
    }

    /**
     * @return the fases
     */
    public List<Fase> getFases() {
        return fases;
    }

    /**
     * @param fases the fases to set
     */
    public void setFases(List<Fase> fases) {
        this.fases = fases;
    }
}
