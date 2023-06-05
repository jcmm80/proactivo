/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Proyecto_Aula;
import com.entity.Seccion;
import com.entity.Semestre;
import com.entity.Tutoria;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author JCMM
 */
@ManagedBean
@SessionScoped
public class ReportesController implements Serializable {

    private BarChartModel barMProyectos;
    private BarChartModel barMProyectosxSeccion;
    private BarChartModel barMProyectosxSignaturas;
    private BarChartModel barTutoriasxSeccion;
    private BarChartModel barTutoriasxSemestre;
    private List<Proyecto_Aula> proyectos;
    private List<Tutoria> tutorias;
    private List<Semestre> semestres;

    /**
     * Creates a new instance of ReportesController
     */
    public ReportesController() {
        proyectos = new LinkedList();
        semestres = new LinkedList();
    }

    @PostConstruct
    public void init() {
        createBarModel();
        barMProyectosxSeccion = new BarChartModel();
        barTutoriasxSeccion = new BarChartModel();
        barTutoriasxSemestre = new BarChartModel();
        barMProyectosxSignaturas = new BarChartModel();
    }

    public int numeroProyectos(Semestre s) {
        int np = 0;
        Predicate<Proyecto_Aula> filterCondition = proyecto -> proyecto.getSeccion().getSemestre().getId().equals(s.getId());
        List<Proyecto_Aula> proyporsem = filtrarProyectos(proyectos, filterCondition);
        np = proyporsem.size();
        return np;
    }

    public int numeroProyectosSec(Seccion s) {
        int np = 0;
        Predicate<Proyecto_Aula> filterCondition = proyecto -> proyecto.getSeccion().getId().equals(s.getId());
        List<Proyecto_Aula> proyporsec = filtrarProyectos(proyectos, filterCondition);
        np = proyporsec.size();
        return np;
    }

    public void mostrarNproyectosXSemestre() {
        for (Semestre s : getSemestres()) {
            System.out.println(s.getDenominacion() + ": " + numeroProyectos(s));
        }
    }

    public static List<Proyecto_Aula> filtrarProyectos(List<Proyecto_Aula> list, Predicate<Proyecto_Aula> condition) {
        List<Proyecto_Aula> filteredList = new ArrayList<>();
        for (Proyecto_Aula proyecto : list) {
            if (condition.test(proyecto)) {
                filteredList.add(proyecto);
            }
        }
        return filteredList;
    }

    public int numeroTutorias(Semestre s) {
        int np = 0;
        Predicate<Tutoria> filterCondition = tutoria -> tutoria.getAsignatura().getSeccion().getSemestre().getId().equals(s.getId());
        List<Tutoria> tutporsem = filtrarTutorias(tutorias, filterCondition);
        np = tutporsem.size();
        return np;
    }

    public int numeroTutoriasAsignatura(Asignatura s) {
        int np = 0;
        Predicate<Tutoria> filterCondition = tutoria -> tutoria.getAsignatura().getId().equals(s.getId());
        List<Tutoria> tutporasig = filtrarTutorias(tutorias, filterCondition);
        np = tutporasig.size();
        return np;
    }

    public int numeroTutoriasSeccion(Seccion s) {
        int np = 0;
        Predicate<Tutoria> filterCondition = tutoria -> tutoria.getAsignatura().getSeccion().getId().equals(s.getId());
        List<Tutoria> tutporsec = filtrarTutorias(tutorias, filterCondition);
        np = tutporsec.size();
        return np;
    }

    public static List<Tutoria> filtrarTutorias(List<Tutoria> list, Predicate<Tutoria> condition) {
        List<Tutoria> filteredList = new ArrayList<>();
        for (Tutoria tutoria : list) {
            if (condition.test(tutoria)) {
                filteredList.add(tutoria);
            }
        }
        return filteredList;
    }

    public void createBarModelSemestre(Semestre s) {
        barMProyectosxSeccion = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Numero de Proyectos por Seccion");
        for (Seccion se : s.getSecciones()) {
            System.out.println(se.getSeccion() + "   " + numeroProyectosSec(se));
            series.set(se.getSeccion(), numeroProyectosSec(se));
        }
        barMProyectosxSeccion.addSeries(series);

    }

    public void createBarTutoriasSemestre(Semestre s) {
        barTutoriasxSeccion = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Numero de Tutorias por Semestre");
        for (Seccion se : s.getSecciones()) {
//            System.out.println(se.getSeccion() + "   " + numeroProyectosSec(se));
            series.set(se.getSeccion(), numeroTutoriasSeccion(se));
        }
        barTutoriasxSeccion.addSeries(series);
    }

    public void createBarTutoriasSeccion(Seccion s) {
        barMProyectosxSignaturas = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Numero de Tutorias por Seccion");
        for (Asignatura a : s.getAsignaturas()) {
//            System.out.println(se.getSeccion() + "   " + numeroProyectosSec(se));
            series.set(a.getNombre(), numeroTutoriasAsignatura(a));
        }
        barMProyectosxSignaturas.addSeries(series);
    }

    public void createBarTutorias() {
        barTutoriasxSemestre = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Numero de Proyectos por Semestre");

        for (Semestre s : getSemestres()) {
            series.set(s.getDenominacion(), numeroTutorias(s));
        }

        barTutoriasxSemestre.addSeries(series);

    }

    public void createBarModel() {
        barMProyectos = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Numero de Proyectos por Semestre");

        for (Semestre s : getSemestres()) {
            series.set(s.getDenominacion(), numeroProyectos(s));
        }

        barMProyectos.addSeries(series);

    }

    /**
     * @return the barMProyectos
     */
    public BarChartModel getBarMProyectos() {
        return barMProyectos;
    }

    /**
     * @param barMProyectos the barMProyectos to set
     */
    public void setBarMProyectos(BarChartModel barMProyectos) {
        this.barMProyectos = barMProyectos;
    }

    /**
     * @return the proyectos
     */
    public List<Proyecto_Aula> getProyectos() {
        return proyectos;
    }

    /**
     * @param proyectos the proyectos to set
     */
    public void setProyectos(List<Proyecto_Aula> proyectos) {
        this.proyectos = proyectos;
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

    /**
     * @return the barMProyectosxSeccion
     */
    public BarChartModel getBarMProyectosxSeccion() {
        return barMProyectosxSeccion;
    }

    /**
     * @param barMProyectosxSeccion the barMProyectosxSeccion to set
     */
    public void setBarMProyectosxSeccion(BarChartModel barMProyectosxSeccion) {
        this.barMProyectosxSeccion = barMProyectosxSeccion;
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
     * @return the barTutoriasxSeccion
     */
    public BarChartModel getBarTutoriasxSeccion() {
        return barTutoriasxSeccion;
    }

    /**
     * @param barTutoriasxSeccion the barTutoriasxSeccion to set
     */
    public void setBarTutoriasxSeccion(BarChartModel barTutoriasxSeccion) {
        this.barTutoriasxSeccion = barTutoriasxSeccion;
    }

    /**
     * @return the barTutoriasxSemestre
     */
    public BarChartModel getBarTutoriasxSemestre() {
        return barTutoriasxSemestre;
    }

    /**
     * @param barTutoriasxSemestre the barTutoriasxSemestre to set
     */
    public void setBarTutoriasxSemestre(BarChartModel barTutoriasxSemestre) {
        this.barTutoriasxSemestre = barTutoriasxSemestre;
    }

    /**
     * @return the barMProyectosxSignaturas
     */
    public BarChartModel getBarMProyectosxSignaturas() {
        return barMProyectosxSignaturas;
    }

    /**
     * @param barMProyectosxSignaturas the barMProyectosxSignaturas to set
     */
    public void setBarMProyectosxSignaturas(BarChartModel barMProyectosxSignaturas) {
        this.barMProyectosxSignaturas = barMProyectosxSignaturas;
    }

}
