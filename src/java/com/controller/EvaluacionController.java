/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Competencia;
import com.entity.Criterio;
import com.entity.CriterioEvaluacion;
import com.entity.Dimension;
import com.entity.Evaluacion;
import com.entity.LiderPA;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.entity.Semestre;
import com.entity.TipoCompetencia;
import com.entity.Tipo_Entregable;
import com.entity.UnidadCompetencia;
import com.services.CriterioEvaluacionServices;
import com.services.CriterioServices;
import com.services.DimensionServices;
import com.services.EvaluacionServices;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author JCMM
 */
@ManagedBean
@SessionScoped
public class EvaluacionController implements Serializable {

    private Periodo periodo = new Periodo();
    private ProgramaAcademico programa = new ProgramaAcademico();
    private Evaluacion evaluacion = new Evaluacion();
    private Dimension dimencion = new Dimension();
    private Semestre semestre;

    private List<CriterioEvaluacion> criteriosevaluacion;
    private List<Dimension> dimenciones;
    private List<Dimension> dimencionesPeriodo;
    private List<Semestre> semestres;
    private List<Semestre> semestresSeleccionados;
    private List<Evaluacion> evaluaciones;
    private List<Criterio> criteriosSeccion;
    private List<Asignatura> asignaturasSeccion;

    CriterioServices criser = new CriterioServices();
    EvaluacionServices evaser = new EvaluacionServices();
    CriterioEvaluacionServices crievser = new CriterioEvaluacionServices();
    DimensionServices dimser = new DimensionServices();

    private String semestresAgregados = "";
    private int maxPor = 100;
    private boolean mostPanelMod = false;

    /**
     * Creates a new instance of EvaluacionController
     */
    public EvaluacionController() {
    }

    public void actualizarDimensiones() {
        dimser.elimimarDimensiones(semestre, periodo, programa);
        if (semestre != null) {
            if (dimenciones.size() > 0) {
                if (maxPor == 0) {
                    for (Dimension d : dimenciones) {
                        d.setSemestre(semestre);
                        dimser.crear(d);
                    }
                    dimenciones = null;
                    semestresSeleccionados = null;
                    semestre = null;
                    mostPanelMod = false;
                    maxPor = 100;
                    consultarDimensionesProgramaAndPeriodo();
                }
            }
        }
    }

    public void seleccionarDimension(Dimension dim) {
        dimencion = dim;
    }

    public void seleccionarAsignatura(Asignatura a) {
       
       
    }

    public void generarCriterios(Asignatura a, String dimension){
        UnidadCompetencia unidad=new UnidadCompetencia();
        unidad.setAsignatura(a);
        unidad.setDescripcion(dimension);
        
        Competencia competencia=new Competencia();
        competencia.setUnidad(unidad);
        competencia.setEvidencia(dimension);
        competencia.setEstrategia(dimension);
        competencia.setTipo(new TipoCompetencia(Long.parseLong("2"), "Generica"));
        competencia.setTipoentregable(new Tipo_Entregable());
        
        Criterio criterio=new Criterio();
        criterio.setCompetencia(competencia);
        criterio.setDescripcion(dimension);
        criterio.setPorcentaje(100);
        criterio.setTipo("Global");
    }
    
    
    
    public boolean habilitarDimensionGlobal() {
        boolean habilitar = false;
        System.out.println("" + dimencion.getTipo());
        try {
            if (dimencion.getTipo().equals("Global")) {
                habilitar = true;
            }
        } catch (java.lang.NullPointerException npe) {
            habilitar = false;
        }
        return habilitar;
    }

    public void almacenarDimensiones() {
        if (semestresSeleccionados.size() > 0) {
            if (dimenciones.size() > 0) {
                if (maxPor == 0) {
                    for (Semestre s : semestresSeleccionados) {
                        for (Dimension d : dimenciones) {
                            d.setSemestre(s);
                            dimser.crear(d);
                        }
                    }
                    dimenciones = null;
                    semestresSeleccionados = null;
                    mostPanelMod = false;
                    consultarDimensionesProgramaAndPeriodo();
                } else {
                    FacesUtil.addErrorMessage("No estan correctamente establecidos los porcentajes de las dimenciones");
                }
            } else {
                FacesUtil.addErrorMessage("No hay dimensiones agregadas para almacenar");
            }
        } else {
            FacesUtil.addErrorMessage("No se ha seleccionado el o los semestres para las dimensiones");
        }
    }

    public void consultarDimensionesProgramaAndPeriodo() {
        dimencionesPeriodo = dimser.obtenerDimensionesXProgramaPeriodo(periodo, programa);
        organizarDimensionesPorSemestre();
        mostPanelMod = false;
    }

    public void consultarDimensionesProgramaAndPeriodoAndSemestre(LiderPA lpa) {
        dimencionesPeriodo = dimser.obtenerDimensionesXProgramaPeriodoSemestre(lpa.getSeccion().getPeriodo(), lpa.getSeccion().getPrograma(), lpa.getSeccion().getSemestre());
    }

    public void consultarCriteriosSeccion(Seccion s) {
        criteriosSeccion = criser.obtenerCriteriosXSeccion(s);
    }

    public void seleccionarSemestre(Semestre s) {
        semestre = s;
        dimenciones = s.getDimensions();
        if (dimenciones.size() > 0) {
            maxPor = 0;
        }
        mostPanelMod = true;
    }

    public void organizarDimensionesPorSemestre() {
        for (int i = 0; i < semestres.size(); i++) {
            semestres.get(i).setDimensions(new LinkedList());
            for (Dimension d : dimencionesPeriodo) {
                if (d.getSemestre().getId().equals(semestres.get(i).getId())) {
                    semestres.get(i).getDimensions().add(d);
                }
            }
        }
    }

    public boolean tieneDimensiones(Semestre sem) {
        boolean tiene = false;
        for (Dimension d : dimencionesPeriodo) {
            if (d.getSemestre().getId().equals(sem.getId())) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    public void agregarDimension() {
        if (!existeDimencion(dimencion)) {
            if (dimencion.getPorcentaje() > 0) {
                if (!dimencion.getTipo().trim().equals("")) {
                    dimenciones.add(dimencion);
                    maxPor = maxPor - dimencion.getPorcentaje();
                    limpiardimencion();
                } else {
                    FacesUtil.addErrorMessage("Debe seleccionar el tipo de dimension(Particular-Global)");
                }
            } else {
                FacesUtil.addErrorMessage("El porcentaje de la dimension debe ser superior a 0%");
            }
        }
    }

    public void quitarDimension(Dimension d) {
        maxPor = maxPor + d.getPorcentaje();
        for (int i = 0; i < dimenciones.size(); i++) {
            if (dimenciones.get(i).getNombre().equals(d.getNombre())) {
                dimenciones.remove(i);
                break;
            }
        }
    }

    public boolean habilitarAlmacenamientoDimenciones() {
        if (maxPor == 0) {
            return true;
        } else {
            if (maxPor < 0) {
                FacesUtil.addErrorMessage("La asignacion de Porcentajes supera el 100%");
            }
            return false;
        }
    }

    public boolean existeDimencion(Dimension d) {
        boolean e = false;
        if (dimenciones != null) {
            for (Dimension di : dimenciones) {
                if (di.getNombre().trim().equals(d.getNombre())) {
                    e = true;
                    break;
                }
            }
        } else {
            dimenciones = new LinkedList();
        }

        return e;
    }

    public void limpiardimencion() {
        Periodo per = dimencion.getPeriodo();
        ProgramaAcademico pra = dimencion.getPrograma();
        dimencion = new Dimension();
        dimencion.setPeriodo(per);
        dimencion.setPrograma(pra);
    }

    public void agregarSemestre(Semestre s) {
        if (!existeSemestre(s)) {
            semestresSeleccionados.add(s);
//            semestresAgregados = semestresAgregados + " - " + s.getDenominacion();
        }
    }

    public void quitarSemestre(Semestre s) {
        semestresSeleccionados.remove(s);
    }

    public boolean existeSemestre(Semestre s) {
        boolean e = false;
        if (semestresSeleccionados != null) {
            for (Semestre sem : semestresSeleccionados) {
                if (sem.getId().equals(s.getId())) {
                    e = true;
                }
            }
        } else {
            semestresSeleccionados = new LinkedList();
        }

        return e;
    }

    /**
     * @return the evaluacion
     */
    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    /**
     * @param evaluacion the evaluacion to set
     */
    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    /**
     * @return the criteriosevaluacion
     */
    public List<CriterioEvaluacion> getCriteriosevaluacion() {
        return criteriosevaluacion;
    }

    /**
     * @param criteriosevaluacion the criteriosevaluacion to set
     */
    public void setCriteriosevaluacion(List<CriterioEvaluacion> criteriosevaluacion) {
        this.criteriosevaluacion = criteriosevaluacion;
    }

    /**
     * @return the dimenciones
     */
    public List<Dimension> getDimenciones() {
        return dimenciones;
    }

    /**
     * @param dimenciones the dimenciones to set
     */
    public void setDimenciones(List<Dimension> dimenciones) {
        this.dimenciones = dimenciones;
    }

    /**
     * @return the evaluaciones
     */
    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    /**
     * @param evaluaciones the evaluaciones to set
     */
    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    /**
     * @return the dimencion
     */
    public Dimension getDimencion() {
        return dimencion;
    }

    /**
     * @param dimencion the dimencion to set
     */
    public void setDimencion(Dimension dimencion) {
        this.dimencion = dimencion;
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
     * @return the semestresAgregados
     */
    public String getSemestresAgregados() {
        return semestresAgregados;
    }

    /**
     * @param semestresAgregados the semestresAgregados to set
     */
    public void setSemestresAgregados(String semestresAgregados) {
        this.semestresAgregados = semestresAgregados;
    }

    /**
     * @return the maxPor
     */
    public int getMaxPor() {
        return maxPor;
    }

    /**
     * @param maxPor the maxPor to set
     */
    public void setMaxPor(int maxPor) {
        this.maxPor = maxPor;
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
     * @return the dimencionesPeriodo
     */
    public List<Dimension> getDimencionesPeriodo() {
        return dimencionesPeriodo;
    }

    /**
     * @param dimencionesPeriodo the dimencionesPeriodo to set
     */
    public void setDimencionesPeriodo(List<Dimension> dimencionesPeriodo) {
        this.dimencionesPeriodo = dimencionesPeriodo;
    }

    /**
     * @return the semestresSeleccionados
     */
    public List<Semestre> getSemestresSeleccionados() {
        return semestresSeleccionados;
    }

    /**
     * @param semestresSeleccionados the semestresSeleccionados to set
     */
    public void setSemestresSeleccionados(List<Semestre> semestresSeleccionados) {
        this.semestresSeleccionados = semestresSeleccionados;
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
     * @return the mostPanelMod
     */
    public boolean isMostPanelMod() {
        return mostPanelMod;
    }

    /**
     * @param mostPanelMod the mostPanelMod to set
     */
    public void setMostPanelMod(boolean mostPanelMod) {
        this.mostPanelMod = mostPanelMod;
    }

    /**
     * @return the criteriosSeccion
     */
    public List<Criterio> getCriteriosSeccion() {
        return criteriosSeccion;
    }

    /**
     * @param criteriosSeccion the criteriosSeccion to set
     */
    public void setCriteriosSeccion(List<Criterio> criteriosSeccion) {
        this.criteriosSeccion = criteriosSeccion;
    }

    /**
     * @return the asignaturasSeccion
     */
    public List<Asignatura> getAsignaturasSeccion() {
        return asignaturasSeccion;
    }

    /**
     * @param asignaturasSeccion the asignaturasSeccion to set
     */
    public void setAsignaturasSeccion(List<Asignatura> asignaturasSeccion) {
        this.asignaturasSeccion = asignaturasSeccion;
    }

}
