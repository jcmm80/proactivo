/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Periodo;
import com.services.PeriodoServices;
import java.io.Serializable;
import java.util.Date;
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
public class PeriodoController implements Serializable {

    /**
     * @return the periodoActual
     */
    public Periodo getPeriodoActual() {
        return periodoActual;
    }

    /**
     * @param periodoActual the periodoActual to set
     */
    public void setPeriodoActual(Periodo periodoActual) {
        this.periodoActual = periodoActual;
    }

    private Periodo periodo = new Periodo();
    private Periodo periodoActual=new Periodo();
    

    PeriodoServices perser = new PeriodoServices();

    private List<Periodo> periodos = new LinkedList();

    /**
     * Creates a new instance of PeriodoController
     */
    public PeriodoController() {
    }

    public void registrarPeriodo() {
        getPeriodo().setFecha(new Date());
        if (getPeriodo().validar()) {
            setPeriodo(perser.modificar(getPeriodo()));
            setPeriodo(new Periodo());
            obtenerPeriodos();
        }
    }

    public void establecerPeriodoActual(){
        periodoActual=perser.obtenerPeriodoActual();
    }
    
    public void desactivarPeriodos() {
       // obtenerPeriodos();
        for (Periodo p : periodos) {
            if (p.isActual()) {
                p.setActual(false);
                perser.modificar(p);
            }
        }
    }

    public void activarPeriodo(Periodo p) {
        desactivarPeriodos();
        p.setActual(true);
        p = perser.modificar(p);
        obtenerPeriodos();
    }

    public void obtenerPeriodos() {
        periodos = perser.consultarTodo(Periodo.class);
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
     * @return the periodos
     */
    public List<Periodo> getPeriodos() {
        return periodos;
    }

    /**
     * @param periodos the periodos to set
     */
    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

}
