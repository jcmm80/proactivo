/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Area;
import com.entity.Coordinador;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Proyecto_Aula;
import com.entity.Seccion;
import com.entity.Semestre;
import com.services.AreaServices;
import com.services.CoordinadorServices;
import com.services.SeccionServices;
import java.io.Serializable;
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
public class CoordinadorController implements Serializable {

    private List<Coordinador> coordinadores = new LinkedList();
    private Coordinador coordinador = new Coordinador();

    // Areas
    private List<Area> areas = new LinkedList();
    private List<Seccion> secciones = new LinkedList();
    private List<Proyecto_Aula> proyectosSeccion;

    private Area area = new Area();
    //Seccion (para manejar el panel)
    private Seccion seccion = new Seccion();
    private Semestre semestre = new Semestre();
    private Periodo periodo = new Periodo();
    private ProgramaAcademico programa = new ProgramaAcademico();

    //servicios
    CoordinadorServices coordser = new CoordinadorServices();
    AreaServices areaser = new AreaServices();
    SeccionServices secser = new SeccionServices();

    //Controllers
    @ManagedProperty("#{asignaturaController}")
    private AsignaturaController asigcon;
    @ManagedProperty("#{matriculaController}")
    private MatriculaController matcont;
    @ManagedProperty("#{proyectoAulaController}")
    private ProyectoAulaController proacon;

    //variables de control
    private String paginaActualC = "";
    private int activeIproy = 0;
   

    /**
     * Creates a new instance of CoordinadorController
     */
    public CoordinadorController() {
    }

    public void seleccionarPrograma(ProgramaAcademico pa) {
        setPrograma(pa);
        activeIproy = 1;
    }

//    public void seleccionarPeriodo(Periodo p) {
//        setPeriodo(p);
////        consultarMatriculasXPeriodoYPrograma(getPrograma(),  p);
//        activeIproy = 2;
//    }

    public void seleccionarSemestre(Semestre s) {
        setSemestre(s);
        setSecciones(secser.obtenerSeccionesXSemestre_Periodo_Programa(getSemestre(), getPrograma(), getPeriodo()));
    }

    public void obtenerProyectosSeccion(Seccion s) {
        seccion = s;
        proyectosXSeccion(seccion);
        activeIproy = 2;
    }

    public void consultarProyectosAulaPeriodo() {
//        System.out.println("consultare los proyectos por periodo");
        getProacon().consultarProyectosXPeriodo(periodo);
        getProacon().obtenerIntegrantesXProyectos(periodo);
    }

    public void proyectosXSeccion(Seccion s) {
        proyectosSeccion = new LinkedList();
        System.out.println("" + proacon.getProyectos().size());
        for (Proyecto_Aula p : getProacon().getProyectos()) {
            if (p.getSeccion().getId().equals(s.getId())) {
                proyectosSeccion.add(p);
            }
        }
    }

    public void consultarProyecto(Proyecto_Aula pa) {
        proacon.setProyecto(pa);
        //proacon.getAvancon().obtenerAvancesProyecto(pa);
        activeIproy = 3;
    }
    
    public void gprofesores() {
        paginaActualC = "/Profesor/GestorProfesores.xhtml";
    }

    public void gmatriculas() {
        matcont.setExistematricula(false);
        paginaActualC = "/Coordinador/GestorMatricula.xhtml";
    }

    public void gareas() {
        paginaActualC = "/General/GestorAreas.xhtml";
    }

    public void gcoordinadorPA() {
        paginaActualC = "/Coordinador/AsignarCoordinacionPA.xhtml";
    }

    public void matriculadosSeccion() {
        paginaActualC = "/Coordinador/MatriculadosSeccion.xhtml";
    }

    public void verProyectos() {
        paginaActualC = "/Coordinador/ProyectosAulaPrograma.xhtml";
    }

    public void gsecciones() {
        paginaActualC = "/Coordinador/GestorSecciones.xhtml";
    }

    public void gasignacionA() {
        paginaActualC = "/Coordinador/AsignacionAcademica.xhtml";
    }

    public void gasignaturas() {
        paginaActualC = "/Coordinador/GestorAsignatura.xhtml";
    }

    public void consultarCoordinadores() {
        setCoordinadores(coordser.consultarTodo(Coordinador.class));
    }

    public void consultarAreas() {
        setAreas(areaser.consultarTodo(Area.class));
    }

    public void consultarAsignaturas(Periodo periodo) {
        asigcon.consultarAsignaturas(periodo);
    }

    public void EliminarArea(Area area) {
        areaser.eliminar(area);
        consultarAreas();
    }

    public void registrar() {
        if (coordinador.validarUsuario()) {
            coordinador.setEstado("Activo");
            coordinador.setTipo("Coordinador");
            coordser.crear(coordinador);
            coordinador = new Coordinador();
            consultarCoordinadores();
        }
    }

    public void registrarArea() {
        if (area.validarArea()) {
            area.setEstado("Activo");
            areaser.crear(area);
            area = new Area();
            consultarAreas();

        }
    }

    public void obtenerCoordinador(Long id) {
        coordinador = coordser.consultar(Coordinador.class, id);
    }

    public void consultarMatriculasXPeriodo(Periodo p) {
        matcont.consultarEstudiantesMatriculadosXPeriodo(p);
    }

    /**
     * @return the coordinadores
     */
    public List<Coordinador> getCoordinadores() {
        return coordinadores;
    }

    /**
     * @param coordinadores the coordinadores to set
     */
    public void setCoordinadores(List<Coordinador> coordinadores) {
        this.coordinadores = coordinadores;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    /**
     * @return the coordinador
     */
    public Coordinador getCoordinador() {
        return coordinador;
    }

    /**
     * @param coordinador the coordinador to set
     */
    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    /**
     * @return the paginaActualC
     */
    public String getPaginaActualC() {
        return paginaActualC;
    }

    /**
     * @param paginaActualC the paginaActualC to set
     */
    public void setPaginaActualC(String paginaActualC) {
        this.paginaActualC = paginaActualC;
    }

    public AsignaturaController getAsigcon() {
        return asigcon;
    }

    public void setAsigcon(AsignaturaController asigcon) {
        this.asigcon = asigcon;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    /**
     * @return the matcont
     */
    public MatriculaController getMatcont() {
        return matcont;
    }

    /**
     * @param matcont the matcont to set
     */
    public void setMatcont(MatriculaController matcont) {
        this.matcont = matcont;
    }

    /**
     * @return the activeIproy
     */
    public int getActiveIproy() {
        return activeIproy;
    }

    /**
     * @param activeIproy the activeIproy to set
     */
    public void setActiveIproy(int activeIproy) {
        this.activeIproy = activeIproy;
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
     * @return the proacon
     */
    public ProyectoAulaController getProacon() {
        return proacon;
    }

    /**
     * @param proacon the proacon to set
     */
    public void setProacon(ProyectoAulaController proacon) {
        this.proacon = proacon;
    }

    /**
     * @return the proyectosSeccion
     */
    public List<Proyecto_Aula> getProyectosSeccion() {
        return proyectosSeccion;
    }

    /**
     * @param proyectosSeccion the proyectosSeccion to set
     */
    public void setProyectosSeccion(List<Proyecto_Aula> proyectosSeccion) {
        this.proyectosSeccion = proyectosSeccion;
    }

}
