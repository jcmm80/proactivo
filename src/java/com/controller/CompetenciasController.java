/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Competencia;
import com.entity.Criterio;
import com.entity.Profesor;
import com.entity.TipoCompetencia;
import com.entity.Tipo_Entregable;
import com.entity.UnidadCompetencia;
import com.services.CompetenciaServices;
import com.services.TipoCompetenciaServices;
import com.services.UnidadCompetenciaServices;
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
public class CompetenciasController implements Serializable {

    private UnidadCompetencia unidad = new UnidadCompetencia();
    private Asignatura asignatura = new Asignatura();
    private Competencia competencia = new Competencia();
  

    private List<UnidadCompetencia> unidadesAsignatura = new LinkedList();
    private List<UnidadCompetencia> unidades = new LinkedList();
    private List<TipoCompetencia> tiposcompetencia = new LinkedList();
    private List<Competencia> competencias = new LinkedList();
    private List<Competencia> competenciasXUnidad = new LinkedList();
    List<Criterio> criteriosGlobales = new LinkedList();

    UnidadCompetenciaServices uncoser = new UnidadCompetenciaServices();
    TipoCompetenciaServices tcser = new TipoCompetenciaServices();
    CompetenciaServices compser = new CompetenciaServices();

    @ManagedProperty("#{criteriosController}")
    private CriteriosController cricon;

    private boolean mostPcompetencias;
    private boolean mostPcompetencia;
    private int activeIndex = 0;

    /**
     * Creates a new instance of CompetenciasController
     */
    public CompetenciasController() {
    }

    public void registrarUnidad() {
        if (unidad.validarUnidadCompetencia()) {
            uncoser.crear(unidad);
            unidad = new UnidadCompetencia();
            consultarUnidadesCOmpetencia(asignatura.getProfesor());
            consultarUnidadesCompetenciaAsignatura();
        }
    }

    public void registrarCompetencia() {
        competencia.setUnidad(unidad);
        try {
            if (competencia.validarCompetencia()) {
                compser.crear(competencia);
                consultarCompetenciasProfesor(asignatura.getProfesor());
                consultarCompetenciasUnidad();
                competencia = new Competencia();
                salirCrearCompetencia();
            }
        } catch (java.lang.NullPointerException npe) {
            FacesUtil.addErrorMessage("Falta algun componente de la competencia");
        }
    }

    public void consultarUnidadesCompetenciaAsignatura() {
        unidadesAsignatura = new LinkedList();
        for (UnidadCompetencia uc : unidades) {
            if (uc.getAsignatura().getId().equals(asignatura.getId())) {
                if(!esglobal(uc)){
                unidadesAsignatura.add(uc);
                }
            }
        }
    }

    public boolean esglobal(UnidadCompetencia uc){
        boolean global=false;
        for(Criterio c:criteriosGlobales){
            if(c.getCompetencia().getUnidad().getId().equals(uc.getId())){
                global=true;break;
                
            }
//            System.out.println(uc.getId()+"  "+c.getCompetencia().getUnidad().getId()+" Es Global: "+global);
        }
        return global;
    }
    
    public void consultarCompetenciasUnidad() {
        competenciasXUnidad = new LinkedList();
        for (Competencia c : competencias) {
            if (c.getUnidad().getId().equals(unidad.getId())) {
                competenciasXUnidad.add(c);
            }
        }
    }

    public void consultarTiposCompetencias() {
        tiposcompetencia = tcser.consultarTodo(TipoCompetencia.class);
    }

    public void consultarCompetenciasProfesor(Profesor p) {
        competencias = compser.obtenerUnidadCompetenciaXProfesor(p);
    }

    public void consultarUnidadesCOmpetencia(Profesor p) {
        unidades = uncoser.obtenerUnidadCompetenciaXProfesor(p);
    }

    //ojo buscar que sea mas optimo 
    public void consultarUnidadesCompetencia(Asignatura a) {
        unidades = uncoser.obtenerUnidadCompetenciaXAsignatura(a);
    }

  public void consultarCriteriosGlobalesProfesor(Profesor p){
      criteriosGlobales=cricon.obtenerCriteriosGlobalesProfesor(p);
  }

    public void eliminarUnidad(UnidadCompetencia uni) {
        uncoser.eliminar(uni);
        consultarUnidadesCOmpetencia(asignatura.getProfesor());
        consultarUnidadesCompetenciaAsignatura();
    }

    public void eliminarCompetencia(Competencia comp) {
        compser.eliminar(comp);
        consultarCompetenciasProfesor(asignatura.getProfesor());
        consultarCompetenciasUnidad();
    }

    public void seleccionaTipoCompetencia(TipoCompetencia tc) {
        competencia.setTipo(tc);
    }

    public void seleccionarTipoEntregable(Tipo_Entregable te) {
        competencia.setTipoentregable(te);
    }

    public void seleccionarUnidad(UnidadCompetencia uni) {
        this.unidad = uni;
        consultarCompetenciasUnidad();
        mostPcompetencias = true;
        activeIndex = 0;
    }

    public void volverUnidadesCompetencias() {
        competencia = new Competencia();
        cricon.setCriterios(null);
        mostPcompetencias = false;
    }

    public void irCrearCompetencia() {
        mostPcompetencia = true;
    }

    public void salirCrearCompetencia() {
        mostPcompetencia = false;
    }

    public void consultarUnidad(UnidadCompetencia uni) {
        this.unidad = uni;
        consultarCompetenciasUnidad();
        mostPcompetencias = false;
    }

    public void consultarCompetencia(Competencia comp) {
        this.competencia = comp;
        mostPcompetencia = true;
        activeIndex = 0;
    }

    public void seleccionarCompetencia(Competencia comp) {
        this.competencia = comp;
        mostPcompetencia = false;
        cricon.seleccionarCompetencia(comp);
        activeIndex = 1;
    }

    public void limpiarDatos() {
        this.setUnidadesAsignatura(null);
        this.setCompetencias(null);
        this.setCompetencias(null);
        this.setUnidades(null);
    }

    public void consultarCompetenciasAsignatura(Asignatura a) {
        this.asignatura = a;
        this.consultarUnidadesCompetencia(a);
    }

    /**
     * @return the unidad
     */
    public UnidadCompetencia getUnidad() {
        return unidad;
    }

    /**
     * @param unidad the unidad to set
     */
    public void setUnidad(UnidadCompetencia unidad) {
        this.unidad = unidad;
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
     * @return the unidadesAsignatura
     */
    public List<UnidadCompetencia> getUnidadesAsignatura() {
        return unidadesAsignatura;
    }

    /**
     * @param unidadesAsignatura the unidadesAsignatura to set
     */
    public void setUnidadesAsignatura(List<UnidadCompetencia> unidadesAsignatura) {
        this.unidadesAsignatura = unidadesAsignatura;
    }

    /**
     * @return the uncoser
     */
    public UnidadCompetenciaServices getUncoser() {
        return uncoser;
    }

    /**
     * @param uncoser the uncoser to set
     */
    public void setUncoser(UnidadCompetenciaServices uncoser) {
        this.uncoser = uncoser;
    }

    /**
     * @return the unidades
     */
    public List<UnidadCompetencia> getUnidades() {
        return unidades;
    }

    /**
     * @param unidades the unidades to set
     */
    public void setUnidades(List<UnidadCompetencia> unidades) {
        this.unidades = unidades;
    }

    /**
     * @return the mostPcompetencias
     */
    public boolean isMostPcompetencias() {
        return mostPcompetencias;
    }

    /**
     * @param mostPcompetencias the mostPcompetencias to set
     */
    public void setMostPcompetencias(boolean mostPcompetencias) {
        this.mostPcompetencias = mostPcompetencias;
    }

    /**
     * @return the competencia
     */
    public Competencia getCompetencia() {
        return competencia;
    }

    /**
     * @param competencia the competencia to set
     */
    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }

    /**
     * @return the tiposcompetencia
     */
    public List<TipoCompetencia> getTiposcompetencia() {
        return tiposcompetencia;
    }

    /**
     * @param tiposcompetencia the tiposcompetencia to set
     */
    public void setTiposcompetencia(List<TipoCompetencia> tiposcompetencia) {
        this.tiposcompetencia = tiposcompetencia;
    }

    /**
     * @return the competencias
     */
    public List<Competencia> getCompetencias() {
        return competencias;
    }

    /**
     * @param competencias the competencias to set
     */
    public void setCompetencias(List<Competencia> competencias) {
        this.competencias = competencias;
    }

    /**
     * @return the competenciasXUnidad
     */
    public List<Competencia> getCompetenciasXUnidad() {
        return competenciasXUnidad;
    }

    /**
     * @param competenciasXUnidad the competenciasXUnidad to set
     */
    public void setCompetenciasXUnidad(List<Competencia> competenciasXUnidad) {
        this.competenciasXUnidad = competenciasXUnidad;
    }

    /**
     * @return the mostPcompetencia
     */
    public boolean isMostPcompetencia() {
        return mostPcompetencia;
    }

    /**
     * @param mostPcompetencia the mostPcompetencia to set
     */
    public void setMostPcompetencia(boolean mostPcompetencia) {
        this.mostPcompetencia = mostPcompetencia;
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
     * @return the cricon
     */
    public CriteriosController getCricon() {
        return cricon;
    }

    /**
     * @param cricon the cricon to set
     */
    public void setCricon(CriteriosController cricon) {
        this.cricon = cricon;
    }

}
