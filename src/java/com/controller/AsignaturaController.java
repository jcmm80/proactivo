/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Area;
import com.entity.Asignatura;
import com.entity.Periodo;
import com.entity.Profesor;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.entity.Semestre;
import com.entity.Tutoria;
import com.services.AsignaturaServices;
import java.io.Serializable;
import com.services.SeccionServices;
import com.services.TutoriaServices;
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
public class AsignaturaController implements Serializable {

    private List<Semestre> semestres = new LinkedList();
    private List<Asignatura> asignaturas = new LinkedList();
    private List<Asignatura> asignaturasAA = new LinkedList();
    private List<Asignatura> asignaturasp;
    ;
    private List<Area> areas = new LinkedList();
    private List<Seccion> secciones = new LinkedList();

    private Asignatura asignatura = new Asignatura();
    private Seccion seccion = new Seccion();
    private Periodo periodo = new Periodo();
    private Semestre semestre = new Semestre();

    ProgramaAcademico programa = new ProgramaAcademico();
    TutoriaServices tutser = new TutoriaServices();
    SeccionServices secser = new SeccionServices();
    AsignaturaServices asigser = new AsignaturaServices();

    @ManagedProperty("#{semestreController}")
    private SemestreController semcon;
    @ManagedProperty("#{seccionController}")
    private SeccionController secccon;

    private boolean mostPCsecc1 = false;
    private int activeIndex = 0;
    private int activeIndexAA = 0;
    private boolean mostPprofesores = false;
//    private String visorMallaCurricular = "GUIMallaCurricular.xhtml";

    public AsignaturaController() {
    }

    public void agregarPrograma(ProgramaAcademico pa, int cu) {
        setPrograma(pa);
        asignaturasDelPrograma(pa);
        semestres = generarMallaAcademica();
        if (cu == 1) {
            setActiveIndex(1);
        }
        if (cu == 2) {
            activeIndexAA = 1;
        }
    }

    public void asignaturasDelPrograma(ProgramaAcademico p) {
        asignaturasp = new LinkedList();
        for (Asignatura a : asignaturas) {
            if (a.getSeccion().getPrograma().getId().equals(p.getId())) {
                System.out.println("" + a.getNombre() + " Es del programa " + p.getNombre());
                asignaturasp.add(a);
            }
        }
    }

    public void agregarSeccion(Seccion s) {
        activeIndexAA = 2;
        asignaturasAA = obtenerAsignaturasSeccion(s);
    }

    public List<Asignatura> obtenerAsignaturasSeccion(Seccion s) {
        List<Asignatura> asignats = new LinkedList();
        for (Asignatura asi : asignaturas) {
            if (asi.getSeccion().getId().equals(s.getId())) {
                asignats.add(asi);
            }
        }
        return asignats;
    }

    public void eliminar(Asignatura a) {
        if (!tieneProcesos(a)) {
            asigser.eliminar(a);
            consultarAsignaturas(periodo);
            asignaturasDelPrograma(getPrograma());
            semestres = generarMallaAcademica();
        }
    }

    public boolean tieneProcesos(Asignatura a) {
        boolean tiene = false;
        try {
            if (a.getProfesor().getId() != null) {
                tiene = true;
                FacesUtil.addErrorMessage("No se puede eliminar la asignatura porque ya tiene profesor asignado");
            }
        } catch (java.lang.NullPointerException npe) {
            tiene = false;
        }
        List<Tutoria> tutorias = tutser.obtenerTutoriasXAsignatura(a);
        if (tutorias.size() > 0) {
            tiene = true;
            FacesUtil.addErrorMessage("No se puede eliminar la asignatura porque ya tiene tutorias asignadas");
        }
        return tiene;
    }

    public void obtenerAsignaturasXSeccion(Seccion s) {
        asignaturas = asigser.obtenerAsignaturasXSeccion(s);
    }

    public void obtenerAsignaturasXEstudiante(Seccion s) {
        asignaturas = asigser.obtenerAsignaturasXSeccion(s);
    }

    public void obtenerAsignaturasXProfesor(Periodo pe, Profesor p) {
        asignaturas = asigser.obtenerAsignaturasXProfesor(pe, p);
    }

    public void obtenerseccionesPeriodo(ProgramaAcademico pa) {
        mostPCsecc1 = true;
        secciones = secser.obtenerSeccionesXPeriodo_Programa(pa, periodo);
        seccion.setPrograma(pa);
    }

    public void seleccionarSemestre(Semestre s, int cu) {
        setSemestre(s);
        secciones = new LinkedList();
        for (Seccion sec : secccon.getSecciones()) {
            if (sec.getSemestre().getId().equals(s.getId()) && sec.getPrograma().getId().equals(programa.getId())) {
                System.out.println("" + sec.getSeccion());
                secciones.add(sec);
            }
        }
        if (cu == 1) {
            setActiveIndex(2);
        }
        if (cu == 2) {
            activeIndexAA = 1;
        }
//        secciones = secser.obtenerSeccionesXSemestre_Periodo_Programa(getSemestre(), getPrograma(), getPeriodo());
    }

    public void seleccionarAsignatura(Asignatura a) {
        asignatura = a;
        mostPprofesores = true;
    }

    public void consultarAsignatura(Asignatura a) {
        asignatura = a;
    }

    public void volverPanelAsignaturas() {
        asignatura = new Asignatura();
        mostPprofesores = false;
    }

    public void seleccionarProfesor(Profesor p) {
        asignatura.setProfesor(p);
    }

    public void asignarProfesor() {
        asignatura = asigser.modificar(asignatura);
        FacesUtil.addInfoMessage("Se asigno el profesor: " + asignatura.getProfesor().toString() + " a la asignatura: " + asignatura.getNombre());
        mostPprofesores = false;
    }

    public boolean existeNombreAsignatura(Asignatura a) {
        boolean existe = false;
        for (Asignatura asi : asignaturas) {
            if (asi.getNombre().equals(a.getNombre()) && asi.getSeccion().getId().equals(a.getSeccion().getId())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public void reistrarAsignatura() {
        try {
            if (asignatura.getId() != null) {
                asignatura = asigser.modificar(asignatura);
            } else {
//               
                if (secciones.size() > 0) {
                    for (Seccion s : secciones) {
                        asignatura.setSeccion(s);
                        asignatura.setEstado("Activa");
                        if (asignatura.validarAsignatura()) {
                            Asignatura asign = asignatura;
                            asigser.modificar(asign);
                            asign = new Asignatura();
                        }
                    }
                } else {
                    FacesUtil.addErrorMessage("No se puede almacenar asignatura: No hay secciones registradas para el semestre seleccionado.");
                }
                if (asignatura.getNombre() != null) {
                    consultarAsignaturas(asignatura.getSeccion().getPeriodo());
                    asignaturasDelPrograma(getPrograma());
                    semestres = generarMallaAcademica();
                    asignatura = new Asignatura();
                    secciones = new LinkedList();
                }
            }
        } catch (NullPointerException npe) {

        }

    }

    public List<Semestre> generarMallaAcademica() {
        List<Seccion> secciones = new LinkedList();
        List<Semestre> semests = new LinkedList();
        for (Asignatura asi : asignaturasp) {//fue cambiada por una coleccion de asignaturas del programa
            if (asi.getSeccion().getPrograma().getId().equals(programa.getId()) && asi.getSeccion().getPeriodo().getId().equals(periodo.getId())) {//validar que la seccion pertenezca al programa
                if (!existeSeccion(secciones, asi.getSeccion())) {
                    secciones.add(asi.getSeccion());
                }
                if (!existeSemestre(semests, asi.getSeccion().getSemestre())) {
                    semests.add(asi.getSeccion().getSemestre());
                }
                System.out.println("" + asi.toString());
            }
        }
        ordenarAsignaturasenSecciones(secciones);
        for (int i = 0; i < semests.size(); i++) {
            semests.get(i).setSecciones(new LinkedList());
            for (Seccion s : secciones) {
                if (s.getSemestre().getId().equals(semests.get(i).getId())) {
                    semests.get(i).getSecciones().add(s);
                }
            }
        }

        System.out.println(secciones.size() + "  " + semests.size());

        return semests;
    }

    public void ordenarAsignaturasenSecciones(List<Seccion> seccions) {    
        for (int i = 0; i < seccions.size(); i++) {
            seccions.get(i).setAsignaturas(new LinkedList());
        }
        for (Asignatura asi : asignaturasp) {
            for (int i = 0; i < seccions.size(); i++) {
                if (asi.getSeccion().getId().equals(seccions.get(i).getId())) {
                    seccions.get(i).getAsignaturas().add(asi);break;
                }
            }
        }
    }

    public boolean existeSemestre(List<Semestre> semestres, Semestre s) {
        boolean existe = false;
        for (Semestre sem : semestres) {
            if (sem.getId().equals(s.getId())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public boolean existeSeccion(List<Seccion> secciones, Seccion s) {
        boolean existe = false;
        for (Seccion sec : secciones) {
            if (sec.getId().equals(s.getId())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public void limpiarDatos() {
        this.setAsignaturas(null);
        this.setAsignaturasAA(null);
        this.setSecciones(null);
    }

    public void consultarAsignaturas(Periodo p) {
        periodo = p;
        asignaturas = asigser.obtenerAsignaturasXPrograma(p);
//        semestres = generarMallaAcademica();
    }

    public void volverprogramas() {
        mostPCsecc1 = false;
    }

    public void agregarArea(Area a) {
        getAsignatura().setArea(a);
        setActiveIndex(3);
    }

    public void agregarAsignatura(Asignatura a) {
        asigser.crear(a);

    }

    public List<Semestre> getSemestres() {
        return semestres;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public SemestreController getSemcon() {
        return semcon;
    }

    public void setSemcon(SemestreController semcon) {
        this.semcon = semcon;
    }

    public boolean isMostPCsecc() {
        return mostPCsecc1;
    }

    public void setMostPCsecc(boolean mostPCsecc) {
        this.mostPCsecc1 = mostPCsecc;
    }

    public SeccionController getSecccon() {
        return secccon;
    }

    public void setSecccon(SeccionController secccon) {
        this.secccon = secccon;
    }

    public AsignaturaServices getAsigser() {
        return asigser;
    }

    public void setAsigser(AsignaturaServices asigser) {
        this.asigser = asigser;
    }

    public SeccionServices getSecser() {
        return secser;
    }

    public void setSecser(SeccionServices secser) {
        this.secser = secser;
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public ProgramaAcademico getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaAcademico programa) {
        this.programa = programa;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
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
     * @return the activeIndexAA
     */
    public int getActiveIndexAA() {
        return activeIndexAA;
    }

    /**
     * @param activeIndexAA the activeIndexAA to set
     */
    public void setActiveIndexAA(int activeIndexAA) {
        this.activeIndexAA = activeIndexAA;
    }

    /**
     * @return the asignaturasAA
     */
    public List<Asignatura> getAsignaturasAA() {
        return asignaturasAA;
    }

    /**
     * @param asignaturasAA the asignaturasAA to set
     */
    public void setAsignaturasAA(List<Asignatura> asignaturasAA) {
        this.asignaturasAA = asignaturasAA;
    }

    /**
     * @return the mostPprofesores
     */
    public boolean isMostPprofesores() {
        return mostPprofesores;
    }

    /**
     * @param mostPprofesores the mostPprofesores to set
     */
    public void setMostPprofesores(boolean mostPprofesores) {
        this.mostPprofesores = mostPprofesores;
    }

//    /**
//     * @return the visorMallaCurricular
//     */
//    public String getVisorMallaCurricular() {
//        return visorMallaCurricular;
//    }
//
//    /**
//     * @param visorMallaCurricular the visorMallaCurricular to set
//     */
//    public void setVisorMallaCurricular(String visorMallaCurricular) {
//        this.visorMallaCurricular = visorMallaCurricular;
//    }
    /**
     * @return the asignaturasp
     */
    public List<Asignatura> getAsignaturasp() {
        return asignaturasp;
    }

    /**
     * @param asignaturasp the asignaturasp to set
     */
    public void setAsignaturasp(List<Asignatura> asignaturasp) {
        this.asignaturasp = asignaturasp;
    }
}
