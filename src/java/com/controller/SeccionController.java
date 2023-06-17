/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Matricula;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.entity.Semestre;
import com.services.AsignaturaServices;
import com.services.MatriculaServices;
import com.services.SeccionServices;
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
public class SeccionController implements Serializable {

    private Seccion seccion = new Seccion();
    private Periodo periodo = new Periodo();
    private List<Semestre> semestres = new LinkedList();
    ProgramaAcademico programa = new ProgramaAcademico();
    private List<Seccion> secciones = new LinkedList();

    SeccionServices secser = new SeccionServices();
    AsignaturaServices asigser = new AsignaturaServices();
    MatriculaServices matser = new MatriculaServices();

    private boolean mostPCsecc = false;

    /**
     * Creates a new instance of SeccionController
     */
    public SeccionController() {
    }

    public void obtenerseccionesPeriodo(ProgramaAcademico pa) {
        mostPCsecc = true;
        secciones = secser.obtenerSeccionesXPeriodo_Programa(pa, periodo);
        organizarSecciones();
        programa = pa;
        seccion.setPrograma(pa);
    }

    public void organizarSecciones() {
        for (Semestre sem : semestres) {
            sem.setSecciones(new LinkedList());
            for (Seccion s : secciones) {
                if (s.getSemestre().getId().equals(sem.getId())) {
                    sem.getSecciones().add(s);
                }
            }
        }
    }

    public void eliminar(Seccion sec) {
        if (!tieneProcesos(sec)) {
            secser.eliminar(sec);
            obtenerseccionesPeriodo(programa);
        }
    }

    public boolean tieneProcesos(Seccion sec) {
        boolean tiene = false;
        List<Asignatura> asignaturas = asigser.obtenerAsignaturasXSeccion(sec);
        List<Matricula> matriculas = matser.obtenerMatriculasXSeccion(sec);
        if (asignaturas.size() > 0 || matriculas.size() > 0) {
            tiene = true;
            FacesUtil.addErrorMessage("La seccion no se puede eliminar debido a que ya tiene procesos activos (Matriculas o asignaturas)");
        }
        return tiene;
    }

    public void obtenerseccionesPeriodo() {
        secciones = secser.obtenerSeccionesXPeriodo(periodo);
//        organizarSecciones();
    }

    public void volverprogramas() {
        mostPCsecc = false;
    }

    public void seleccionarSemestre(Semestre sem) {
        seccion.setSemestre(sem);
    }

    public void registrar() {
        seccion.setPeriodo(periodo);
        if (seccion.validarSeccion()) {
            if (!existeSeccion(seccion)) {
                secser.modificar(seccion);
                obtenerseccionesPeriodo(seccion.getPrograma());
                seccion = new Seccion();
                seccion.setPrograma(programa);
            } else {
                FacesUtil.addErrorMessage("La seccion ya esta registrada");
            }
        }
    }

    public boolean existeSeccion(Seccion sec) {
        boolean existe = false;
        for (Seccion s : secciones) {
            if (s.getPeriodo().getId().equals(sec.getPeriodo().getId()) && s.getPrograma().getId().equals(sec.getPrograma().getId()) && s.getSemestre().getId().equals(sec.getSemestre().getId())) {
                if (s.getDenominacion().equals(sec.getDenominacion())) {
                    existe = true;
                }
            }
        }
        return existe;
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
     * @return the mostPCsecc
     */
    public boolean isMostPCsecc() {
        return mostPCsecc;
    }

    /**
     * @param mostPCsecc the mostPCsecc to set
     */
    public void setMostPCsecc(boolean mostPCsecc) {
        this.mostPCsecc = mostPCsecc;
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

}