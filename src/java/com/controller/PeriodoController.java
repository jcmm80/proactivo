/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Periodo;
import com.entity.Seccion;
import com.services.PeriodoServices;
import com.services.SeccionServices;
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
    private Periodo periodoActual = new Periodo();

    SeccionServices secser = new SeccionServices();
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
            if (!existe(getPeriodo())) {
                setPeriodo(perser.modificar(getPeriodo()));
                setPeriodo(new Periodo());
                obtenerPeriodos();
            }else{
                FacesUtil.addErrorMessage("Ya existe un periodo con el año y denominacion seleccionada.");
            }
        }
    }

    public void seleccionarPeriodo(Periodo p) {
        setPeriodo(p);
    }

    public void aliminar(Periodo p) {
        if (habilitarEliminar(p)) {
            perser.eliminar(p);
            obtenerPeriodos();
            FacesUtil.addInfoMessage("Se elimino el periodo con exito");
        }
    }

    public boolean habilitarEliminar(Periodo p) {
        boolean habilitar = true;
        if (p.isActual()) {
            habilitar = false;
            FacesUtil.addErrorMessage("No se puede eliminar el periodo si esta activo o es el actual");
        }
        List<Seccion> secciones = secser.obtenerSeccionesXPeriodo(p);
        if (secciones.size() > 0) {
            habilitar = false;
            FacesUtil.addErrorMessage("No se puede eliminar el periodo ya tiene secciones vinculadas ");
        }
        return habilitar;
    }

    public void establecerPeriodoActual() {
        periodoActual = perser.obtenerPeriodoActual();
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
    
    public String getEstadoIcono(Periodo p) {
        String icon = "";
        if(p.isActual()){
            icon = "ri-toggle-fill" ;
        } else {
            icon = "ri-toggle-line";
        }
        return icon;
    }

    public boolean existe(Periodo p) {
        boolean existe = false;
        for (Periodo pe : periodos) {
            if (pe.getAnio() == p.getAnio() && pe.getNumero() == p.getNumero()) {
                existe = true;
            }
        }
        return existe;
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