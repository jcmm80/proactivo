/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Area;
import com.entity.Coordinador;
import com.entity.Periodo;
import com.entity.Seccion;
import com.services.AreaServices;
import com.services.CoordinadorServices;
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
    private Area area = new Area();

    //Seccion (para manejar el panel)
    private Seccion seccion = new Seccion();

    //servicios
    CoordinadorServices coordser = new CoordinadorServices();
    AreaServices areaser = new AreaServices();

    //Controllers
    @ManagedProperty("#{asignaturaController}")
    private AsignaturaController asigcon;
    @ManagedProperty("#{matriculaController}")
    private MatriculaController matcont;

    //variables de control
    private String paginaActualC = "";

    /**
     * Creates a new instance of CoordinadorController
     */
    public CoordinadorController() {
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

}
