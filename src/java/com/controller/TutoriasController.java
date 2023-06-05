/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Asistente;
import com.entity.Integrante;
import com.entity.Periodo;
import com.entity.Profesor;
import com.entity.Proyecto_Aula;
import com.entity.Tutoria;
import com.services.AsistenteServices;
import com.services.TutoriaServices;
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
public class TutoriasController implements Serializable {

    private Integrante integrante;
    private Asignatura asignatura;
    private Proyecto_Aula proyecto;

    private Tutoria tutoria = new Tutoria();

    private List<Tutoria> tutorias = new LinkedList();
    private List<Tutoria> tutoriasAsignatura = new LinkedList();
    private List<Tutoria> tutoriasAsignaturaProgramadas = new LinkedList();
    private List<Tutoria> tutoriasAsignaturaRealizadas = new LinkedList();
    private List<Tutoria> tutoriasAsignaturaSolicitadas = new LinkedList();
    private List<Asistente> asistentes = new LinkedList();

    TutoriaServices tutser = new TutoriaServices();
    AsistenteServices asisser = new AsistenteServices();

    /**
     * Creates a new instance of TutoriasController
     */
    public TutoriasController() {
    }

    public void crearTutoria() {
        tutoria.setAsignatura(asignatura);
        tutoria.setIntegrante(integrante);
        tutoria.setProyecto(proyecto);
        tutoria.setFechaSolicitud(new Date());
        tutoria.setEstado("Solicitada");
        if (tutoria.validarTutoria()) {
            tutoria = tutser.modificar(tutoria);
        }
    }

    public void liberarObjetos() {
        this.integrante = null;
        this.asignatura = null;
        this.proyecto = null;
        this.tutoria = null;
        this.tutorias = null;
        this.tutoriasAsignaturaProgramadas = null;
        this.tutoriasAsignaturaRealizadas = null;
        this.tutoriasAsignaturaSolicitadas = null;
        this.asistentes = null;
    }

    public void programarTutoria() {
        tutoria.setEstado("Programada");
        tutoria = tutser.modificar(tutoria);
        consultarTutoriasXProfesor(tutoria.getAsignatura().getProfesor());
        consultarTutoriasXAsignaturaProfesor();
    }

    public void eliminarTutoria(Tutoria t) {
        if (t.getEstado().equals("Solicitada")) {
            tutser.eliminar(t);
        } else {
            FacesUtil.addErrorMessage("No se puede Eliminar: La tutoria ya ha sido programada o realizada");
        }
    }

    public void tutoriaPendiente() {
        try {
            tutoria = tutser.consultarTutoriaXAsignaturaSolicitada(asignatura, proyecto);
        } catch (Exception ex) {
            System.out.println("No hay tutoria pendiente");
        }
    }

    public void realizarTutoria() {
        tutoria.setEstado("Realizada");
        tutoria.setFechaEjecucion(new Date());
        if (tutoria.validarTutoria()) {
            if (tutoria.getRecomendaciones().trim().length() > 0 && tutoria.getCompromisos().trim().length() > 0) {
                tutoria = tutser.modificar(tutoria);
                registrarAsistencia();
            }
        }
    }

    public void registrarAsistencia() {
        for (Asistente a : asistentes) {
            asisser.crear(a);
        }
    }

    public void obtenerTutoriasProyecto(Proyecto_Aula pa) {
        tutorias = tutser.obtenerTutoriasXProyecto(pa);
    }

    public void armarAsistentes() {
        asistentes = new LinkedList();
        for (Integrante i : tutoria.getProyecto().getIntegrantes()) {
            Asistente asi = new Asistente();
            asi.setEstudiante(i);
            asi.setTutoria(tutoria);
            asi.setAsistio(false);
            asistentes.add(asi);
//            System.out.println(""+asi.getEstudiante().getMatricula().getEstudiante().toString());
        }
    }

    public void consultarTutoriasXAsignaturaProyecto() {
        tutorias = tutser.obtenerTutoriasXAsignaturaProyecto(asignatura, proyecto);
    }

    public void consultarAsistenciasXEstudianteProyecto(Integrante i) {
        asistentes = asisser.obtenerAsistenciasXProyecto(i);
    }

    public Asistente asistenciaEstudiante(Tutoria t) {
        Asistente asis = new Asistente();
        for (Asistente a : asistentes) {
            if (a.getTutoria().getId().equals(t.getId())) {
                asis = a;
            }
        }
        return asis;
    }

    public void consultarTutoriasXProfesor(Profesor p) {
        tutorias = tutser.obtenerTutoriasXProfesor(p);
    }

    public void consultarTutoriasXAsignaturaProfesor() {
        tutoriasAsignaturaProgramadas = new LinkedList();
        tutoriasAsignaturaRealizadas = new LinkedList();
        tutoriasAsignaturaSolicitadas = new LinkedList();
        for (Tutoria t : tutorias) {
            System.out.println("clasificare las tutorias " + t.getFechaSolicitud() + "  " + t.getEstado() + " \t" + asignatura.getId() + " \t" + t.getAsignatura().getId() + "\t" + t.getAsignatura().getNombre());
            if (t.getAsignatura().getId().equals(asignatura.getId())) {
                if (t.getEstado().equals("Realizada")) {
                    tutoriasAsignaturaRealizadas.add(t);
                }
                if (t.getEstado().equals("Programada")) {
                    tutoriasAsignaturaProgramadas.add(t);
                }
                if (t.getEstado().equals("Solicitada")) {
                    tutoriasAsignaturaSolicitadas.add(t);
                }
            }
        }
    }

    public void consultarTutoriasXAsignatura(Asignatura a) {
        this.asignatura = a;
        obtenerTutoriasAsignatura(a);
    }

    public void obtenerTutoriasAsignatura(Asignatura a){
        tutoriasAsignatura=new LinkedList();
        for(Tutoria t:tutorias){
            if(t.getAsignatura().getId().equals(a.getId())){
                tutoriasAsignatura.add(t);
            }
        }
    }
    
    public void obtenerTutoriasXPeriodo(Periodo p) {
        tutorias = tutser.obtenerTutoriasXPeriodo(p);
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
     * @return the proyecto
     */
    public Proyecto_Aula getProyecto() {
        return proyecto;
    }

    /**
     * @param proyecto the proyecto to set
     */
    public void setProyecto(Proyecto_Aula proyecto) {
        this.proyecto = proyecto;
    }

    /**
     * @return the tutorias
     */
    public List<Tutoria> getTutorias() {
        return tutorias;
    }

    /**
     * @param tutorias the tutorias to set
     */
    public void setTutorias(List<Tutoria> tutorias) {
        this.tutorias = tutorias;
    }

    /**
     * @return the tutoria
     */
    public Tutoria getTutoria() {
        return tutoria;
    }

    /**
     * @param tutoria the tutoria to set
     */
    public void setTutoria(Tutoria tutoria) {
        this.tutoria = tutoria;
    }

    /**
     * @return the asistentes
     */
    public List<Asistente> getAsistentes() {
        return asistentes;
    }

    /**
     * @param asistentes the asistentes to set
     */
    public void setAsistentes(List<Asistente> asistentes) {
        this.asistentes = asistentes;
    }

    /**
     * @return the tutoriasAsignaturaRealizadas
     */
    public List<Tutoria> getTutoriasAsignaturaRealizadas() {
        return tutoriasAsignaturaRealizadas;
    }

    /**
     * @param tutoriasAsignaturaRealizadas the tutoriasAsignaturaRealizadas to
     * set
     */
    public void setTutoriasAsignaturaRealizadas(List<Tutoria> tutoriasAsignaturaRealizadas) {
        this.tutoriasAsignaturaRealizadas = tutoriasAsignaturaRealizadas;
    }

    /**
     * @return the tutoriasAsignaturaSolicitadas
     */
    public List<Tutoria> getTutoriasAsignaturaSolicitadas() {
        return tutoriasAsignaturaSolicitadas;
    }

    /**
     * @param tutoriasAsignaturaSolicitadas the tutoriasAsignaturaSolicitadas to
     * set
     */
    public void setTutoriasAsignaturaSolicitadas(List<Tutoria> tutoriasAsignaturaSolicitadas) {
        this.tutoriasAsignaturaSolicitadas = tutoriasAsignaturaSolicitadas;
    }

    /**
     * @return the tutoriasAsignaturaProgramadas
     */
    public List<Tutoria> getTutoriasAsignaturaProgramadas() {
        return tutoriasAsignaturaProgramadas;
    }

    /**
     * @param tutoriasAsignaturaProgramadas the tutoriasAsignaturaProgramadas to
     * set
     */
    public void setTutoriasAsignaturaProgramadas(List<Tutoria> tutoriasAsignaturaProgramadas) {
        this.tutoriasAsignaturaProgramadas = tutoriasAsignaturaProgramadas;
    }

    /**
     * @return the tutoriasAsignatura
     */
    public List<Tutoria> getTutoriasAsignatura() {
        return tutoriasAsignatura;
    }

    /**
     * @param tutoriasAsignatura the tutoriasAsignatura to set
     */
    public void setTutoriasAsignatura(List<Tutoria> tutoriasAsignatura) {
        this.tutoriasAsignatura = tutoriasAsignatura;
    }

}
