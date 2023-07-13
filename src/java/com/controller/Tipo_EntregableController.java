/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Profesor;
import com.entity.Tipo_Entregable;
import com.services.Tipo_EntregableServices;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Dimas
 */
@ManagedBean
@SessionScoped
public class Tipo_EntregableController implements Serializable {

    private Tipo_Entregable tipo_entregable = new Tipo_Entregable();
    private Asignatura asignatura = new Asignatura();

    private Tipo_EntregableServices tipenser = new Tipo_EntregableServices();

    private List<Tipo_Entregable> tipos_Entregable = new LinkedList();
    private List<Tipo_Entregable> tipos_EntregableP = new LinkedList();

    /**
     * Creates a new instance of PeriodoController
     */
    public Tipo_EntregableController() {
    }

    public void registrarTipo_Entregable() {
        tipo_entregable.setAsignatura(asignatura);
        tipo_entregable.setTipo("Especifico");
        if (tipo_entregable.validar()) {
            tipo_entregable = tipenser.modificar(tipo_entregable);
            consultarTipos_Entregable(asignatura.getProfesor());
            tipo_entregable = new Tipo_Entregable();
            consultarTipos_Entregable();
        }
    }

    public void eliminartipo_Entregable(Tipo_Entregable te) {
        tipenser.eliminar(te);
        getTipos_EntregableP().remove(te);
        consultarTipos_Entregable();

    }

    public void seleccionarAsignatura(Asignatura a) {
        setAsignatura(a);
        consultarTipos_Entregable();
    }

    public void consultarTipos_Entregable() {
        setTipo_Entregables(new LinkedList());
        for (Tipo_Entregable te : getTipos_EntregableP()) {
            if (te.getAsignatura().getId().equals(asignatura.getId())) {
                getTipos_Entregable().add(te);
            }
        }
    }

    public void consultarTipos_entregable(Asignatura a) {
        setTipos_EntregableP(tipenser.obtenerTipo_EntregableAsignaturas(a));
    }

    public void consultarTipos_Entregable(Profesor p) {
        setTipos_EntregableP(tipenser.obtenerTipo_EntregableProfesor(p));
    }

    public void limpiarDatos() {
        this.setTipo_Entregables(null);
        this.setTipos_EntregableP(null);
    }

    public Tipo_Entregable getTipo_entregable() {
        return tipo_entregable;
    }

    public void setTipo_entregable(Tipo_Entregable tipo_entregable) {
        this.tipo_entregable = tipo_entregable;
    }

    /**
     * @return the tipo_Entregables
     */
    public List<Tipo_Entregable> getTipos_Entregable() {
        return tipos_Entregable;
    }

    /**
     * @param tipo_Entregables the tipo_Entregables to set
     */
    public void setTipo_Entregables(List<Tipo_Entregable> tipo_Entregables) {
        this.tipos_Entregable = tipo_Entregables;
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
     * @return the tipos_EntregableP
     */
    public List<Tipo_Entregable> getTipos_EntregableP() {
        return tipos_EntregableP;
    }

    /**
     * @param tipos_EntregableP the tipos_EntregableP to set
     */
    public void setTipos_EntregableP(List<Tipo_Entregable> tipos_EntregableP) {
        this.tipos_EntregableP = tipos_EntregableP;
    }

}
