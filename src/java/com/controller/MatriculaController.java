/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Estudiante;
import com.entity.Integrante;
import com.entity.Matricula;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.entity.Semestre;
import com.services.EstudianteServices;
import com.services.IntegranteServices;
import com.services.MatriculaServices;
import com.services.SeccionServices;
import com.utilidades.ResultadosAsignatura;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author jcmm
 */
@ManagedBean
@SessionScoped
public class MatriculaController implements Serializable {

    //objetos de negocio
    private Matricula matricula = new Matricula();
    private Semestre semestre = new Semestre();
    private Periodo periodo = new Periodo();
    private Seccion seccion = new Seccion();
    private ProgramaAcademico programa = new ProgramaAcademico();
    private Integrante integrante;

    //servicios
    MatriculaServices matser = new MatriculaServices();
    EstudianteServices estser = new EstudianteServices();
    SeccionServices secser = new SeccionServices();
    IntegranteServices intser = new IntegranteServices();

    //colecciones
    private List<Matricula> matriculas = new LinkedList();
    private List<Seccion> secciones = new LinkedList();
    private List<Matricula> matriculasXSeccion = new LinkedList();
    private List<ResultadosAsignatura> resultados = new LinkedList();

    //variables de control
    private int activeIndex = 0;
    private int activeIcest = 0;
    private boolean existematricula = false;

    @ManagedProperty("#{evaluacionController}")
    private EvaluacionController evacon;

    /**
     * Creates a new instance of MatriculaController
     */
    public MatriculaController() {

    }

    public void obtenerMatriculasXSeccion(Seccion s) {
        matriculasXSeccion = new LinkedList();
        for (Matricula m : matriculas) {
            if (m.getSeccion().getId().equals(s.getId())) {
                matriculasXSeccion.add(m);
            }
        }
    }

    public Matricula obtenerMatriculaEstudiante(Estudiante e) {
        Matricula mat = new Matricula();
        for (Matricula m : matriculas) {
            if (m.getEstudiante().getId().equals(e.getId())) {
                mat = m;
            }
        }
        return mat;
    }

    public void consultarResultadosXIntegrante() {
        evacon.consultarValoracionesXIntegrante(integrante);
        resultados = evacon.organizarResultadosIntegrante();
        evacon.organizarValoracionesXDimension();
    }

    public void consultarEstudiantesMatriculadosXPeriodo(Periodo p) {
        matriculas = matser.obtenerMatriculasXperiodo(p);
    }

    public void consultarMatriculaXEstudianteEnPeriodo(Estudiante e, Periodo p) {
        matricula = matser.obtenerMatriculaXPeriodo(p, e);
        consultarEvaluacion();
    }

    public void consultarEvaluacion() {
        evacon.consultarEvaluacion(matricula.getSeccion());
    }

    public void consultarMatriculasXPeriodoYPrograma(ProgramaAcademico pa, Periodo p) {
        matriculas = matser.obtenerMatriculasXperiodoYPrograma(p, pa);
    }

    public void volverEstudiantes() {
        existematricula = false;
        activeIndex = 0;
        matricula = new Matricula();
    }

    public void agregarEstudiante(Estudiante e) {
        Matricula mat = obtenerMatriculaEstudiante(e);
        if (mat.getId() > 0) {
            existematricula = true;
            matricula = mat;
            activeIndex = 0;
        } else {
            existematricula = false;
            getMatricula().setEstudiante(e);
            activeIndex = 1;
        }
    }

    public void agregarPrograma(ProgramaAcademico pa) {
        setPrograma(pa);
        activeIndex = 2;
        activeIcest = 1;
    }

    public void seleccionarPeriodo(Periodo p) {
        setPeriodo(p);
        consultarMatriculasXPeriodoYPrograma(getPrograma(), p);
        activeIcest = 2;
    }

    public void obtenerMatriculadosSeccion(Seccion s) {
        seccion = s;
        obtenerMatriculasXSeccion(s);
        activeIcest = 3;
    }

    public void regresarProgramas() {
        activeIcest = 0;
        matriculas = null;
        secciones = null;
//        semestres=null;

    }

    public void agregarPeriodo(Periodo p) {
        setPeriodo(p);
        activeIndex = 3;
    }

    public void seleccionarSemestre(Semestre s) {
        setSemestre(s);
        secciones = secser.obtenerSeccionesXSemestre_Periodo_Programa(getSemestre(), getPrograma(), getPeriodo());
    }

    public void seleccionarSeccion(Seccion s) {
        matricula.setSeccion(s);
    }

    public void eliminarMatricula() {
        System.out.println("" + matricula.getId());
        Integrante integrante = intser.obtenerIntegranteXMatricula(matricula);
        System.out.println("" + integrante.getId());
        if (integrante != null) {
            intser.eliminar(integrante);
        }
        matser.eliminar(matricula);
        consultarEstudiantesMatriculadosXPeriodo(periodo);
    }

    public void matricular() {
        // Matricula mat = matser.obtenerMatriculaXPeriodo(periodo, matricula.getEstudiante());
        matricula.setEstado("Academica");
        matricula.setEstadoPA("Libre");
        matricula.setFecha(new Date());
//        System.out.println(""+matricula.getId()+" "+matricula.getEstado());
        try {
//            System.out.println(matricula.getSeccion().getPrograma().getNombre()+" "+matricula.getId()+" "+matricula.getEstado()+" "+matricula.getEstudiante().toString());
//            if (mat.getId() > 0) {
//                FacesUtil.addErrorMessage("El estudiante ya esta matriculado en la seccion: " + matricula.getSeccion().getDenominacion());
//            } else {
//                System.out.println(""+matricula.getId()+" "+matricula.getEstado()+" "+matricula.getEstudiante().toString());
            if (matricula.validarMatricula()) {
//                    matricula.setId(null);
                matser.modificar(matricula);
                consultarEstudiantesMatriculadosXPeriodo(periodo);
                matricula.getEstudiante().generarCodigo(matricula);
                matricula.setEstudiante(estser.modificar(matricula.getEstudiante()));
                matricula = new Matricula();
                activeIndex = 0;
                FacesUtil.addErrorMessage("Se creo una matricula para la seccion: " + matricula.getSeccion().getDenominacion());
            }
//            }

        } catch (java.lang.NullPointerException npe) {
//            FacesUtil.addErrorMessage("Falta informacion para registro de matricula");
        }
    }

    /**
     * @return the matricula
     */
    public Matricula getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the matriculas
     */
    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    /**
     * @param matriculas the matriculas to set
     */
    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
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
     * @return the matriculasXSeccion
     */
    public List<Matricula> getMatriculasXSeccion() {
        return matriculasXSeccion;
    }

    /**
     * @param matriculasXSeccion the matriculasXSeccion to set
     */
    public void setMatriculasXSeccion(List<Matricula> matriculasXSeccion) {
        this.matriculasXSeccion = matriculasXSeccion;
    }

    /**
     * @return the activeIndex
     */
    public int getActiveIndex() {
        return activeIndex;
    }

    /**
     * @param activeIndex the activeIndex to set
     */
    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    /**
     * @return the existematricula
     */
    public boolean isExistematricula() {
        return existematricula;
    }

    /**
     * @param existematricula the existematricula to set
     */
    public void setExistematricula(boolean existematricula) {
        this.existematricula = existematricula;
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
     * @return the activeIcest
     */
    public int getActiveIcest() {
        return activeIcest;
    }

    /**
     * @param activeIcest the activeIcest to set
     */
    public void setActiveIcest(int activeIcest) {
        this.activeIcest = activeIcest;
    }

    /**
     * @return the integrante
     */
    public Integrante getIntegrante() {
        return integrante;
    }

    /**
     * @param integrante the integrante to set
     */
    public void setIntegrante(Integrante integrante) {
        this.integrante = integrante;
    }

    /**
     * @return the evacon
     */
    public EvaluacionController getEvacon() {
        return evacon;
    }

    /**
     * @param evacon the evacon to set
     */
    public void setEvacon(EvaluacionController evacon) {
        this.evacon = evacon;
    }

    /**
     * @return the resultados
     */
    public List<ResultadosAsignatura> getResultados() {
        return resultados;
    }

    /**
     * @param resultados the resultados to set
     */
    public void setResultados(List<ResultadosAsignatura> resultados) {
        this.resultados = resultados;
    }

}
