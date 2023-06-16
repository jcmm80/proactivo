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
    private String visorMallaCurricular = "GUIMallaCurricular.xhtml";

    public AsignaturaController() {
    }

    public void agregarPrograma(ProgramaAcademico pa, int cu) {
        setPrograma(pa);
        if (cu == 1) {
            setActiveIndex(1);
        }
        if (cu == 2) {
            activeIndexAA = 1;
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
            if (sec.getSemestre().getId().equals(s.getId())) {
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

    public void reistrarAsignatura() {
        try {
            if (asignatura.getId() != null) {
                asignatura = asigser.modificar(asignatura);
            } else {
                for (Seccion s : secciones) {
                    asignatura.setSeccion(s);
                    asignatura.setEstado("Activa");
                    if (asignatura.validarAsignatura()) {
                        Asignatura asign = asignatura;
                        asigser.modificar(asign);
                        asign = new Asignatura();
                    }
                }
                if (asignatura.getNombre() != null) {
                    consultarAsignaturas(asignatura.getSeccion().getPeriodo());
                    asignatura = new Asignatura();
                    secciones = new LinkedList();
                }
            }
        } catch (NullPointerException npe) {

        }

    }

    public List<Semestre> generarMallaAcademica() {
        List<Seccion> secciones = new LinkedList();
        List<Semestre> semestres = new LinkedList();
        for (Asignatura asi : asignaturas) {
            if (!existeSeccion(secciones, asi.getSeccion())) {
//                System.out.println("Agregue Seccion: "+asi.getSeccion().getDenominacion());
                secciones.add(asi.getSeccion());
            }
            if (!existeSemestre(semestres, asi.getSeccion().getSemestre())) {
                semestres.add(asi.getSeccion().getSemestre());
//                System.out.println("Agregue Semestre: "+asi.getSeccion().getSemestre().getDenominacion());
            }
        }
        System.out.println(secciones.size() + "  " + semestres.size());
//        for (Asignatura a : asignaturas) {
//            for (int i = 0; i < secciones.size(); i++) {
//                if (a.getSeccion().getId().equals(secciones.get(i).getId())) {
//                    secciones.get(i).getAsignaturas().add(a);
//                    System.out.println("Agregue asignatura: " + a.getNombre() + " a:  " + secciones.get(i).getDenominacion()+" - "+secciones.get(i).getSemestre().getDenominacion());
//                }
//            }
//        }

//        for(int i=0;i<semestres.size();i++){
//             for(Seccion s:semestres.get(i).getSecciones()){
//                 System.out.println(""+s.getSemestre().getDenominacion()+" "+s.getDenominacion());
//             }
//         }
//        
//        for (Seccion sec : secciones) {
//            for (int i = 0; i < semestres.size(); i++) {
//                if (sec.getSemestre().getId().equals(semestres.get(i).getId())) {
//                    semestres.get(i).getSecciones().add(sec);
//                    System.out.println("Agregue seccion: " + sec.getDenominacion() + " a " + semestres.get(i).getDenominacion());
//                }
////            }
////        }
//         System.out.println(secciones.size()+"  "+semestres.size());
//         for(int i=0;i<semestres.size();i++){
//             for(Seccion s:semestres.get(i).getSecciones()){
//                 System.out.println(""+s.getSemestre().getDenominacion()+" "+s.getDenominacion());
//             }
//         }
        return semestres;
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
        semestres = generarMallaAcademica();
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

    /**
     * @return the visorMallaCurricular
     */
    public String getVisorMallaCurricular() {
        return visorMallaCurricular;
    }

    /**
     * @param visorMallaCurricular the visorMallaCurricular to set
     */
    public void setVisorMallaCurricular(String visorMallaCurricular) {
        this.visorMallaCurricular = visorMallaCurricular;
    }
}
