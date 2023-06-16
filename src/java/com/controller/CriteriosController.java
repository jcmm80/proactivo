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
import com.entity.Profesor;
import com.entity.UnidadCompetencia;
import com.services.CriterioEvaluacionServices;
import com.services.CriterioServices;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author JCMM
 */
@ManagedBean
@SessionScoped
public class CriteriosController implements Serializable {

    private Competencia competencia;
    private Criterio criterio;
    List<CriterioEvaluacion> criteriosevaluacionexistentes;
    private List<Criterio> criterios;
    CriterioServices criser = new CriterioServices();
    CriterioEvaluacionServices crievser = new CriterioEvaluacionServices();

    /**
     * Creates a new instance of CriteriosController
     */
    public CriteriosController() {
        criterio = new Criterio();
    }

    public void seleccionarCompetencia(Competencia c) {
        competencia = c;
        criterio = new Criterio();
        criterio.setCompetencia(competencia);
        criterios = criser.obtenerCriteriosXCompetencia(competencia);
        criterio.setPorcentaje(100 - acumuladoCrterios());
    }

    public void consultarCriteriosEvaluacionAsignatura(Asignatura a) {
        criteriosevaluacionexistentes = crievser.obtenerCriterioEvaluacionXAsignatura(a);
    }
    
    public List<Criterio> obtenerCriteriosGlobalesProfesor(Profesor p){
        return criser.obtenerCriteriosGlobalesXProfesor(p);
    }
    
    public boolean criterioVinculado(Criterio c){
        boolean vinculado=false;
        for(CriterioEvaluacion ce:criteriosevaluacionexistentes){
            if(ce.getCriterio().getId().equals(c.getId())){
//                competencia=ce.getCriterio().getCompetencia();
                vinculado=true;break;
            }
        }
        return vinculado;
    }

  public boolean competenciaVinculada(Competencia c){
        boolean vinculado=false;
        for(CriterioEvaluacion ce:criteriosevaluacionexistentes){
            if(ce.getCriterio().getCompetencia().getId().equals(c.getId())){
//                competencia=ce.getCriterio().getCompetencia();
                vinculado=true;break;
            }
        }
        return vinculado;
    }
    
   public boolean unidadVinculada(UnidadCompetencia c){
        boolean vinculado=false;
        for(CriterioEvaluacion ce:criteriosevaluacionexistentes){
            if(ce.getCriterio().getCompetencia().getUnidad().getId().equals(c.getId())){
//                competencia=ce.getCriterio().getCompetencia();
                vinculado=true;break;
            }
        }
        return vinculado;
    }
  
    public int acumuladoCrterios() {
        int suma = 0;
        for (Criterio c : criterios) {
            suma = suma + c.getPorcentaje();
        }
        return suma;
    }

    public void seleccionarCriterio(Criterio c) {
        criterio = c;
        criterio.setPorcentaje(100 - acumuladoCrterios() + c.getPorcentaje());
    }

    public void aliminarCriterio(Criterio c) {
        criterio = c;
        criser.eliminar(c);
        seleccionarCompetencia(criterio.getCompetencia());
    }

    public void registrarCriterio() {
        criterio.setTipo("Especifico");
        if (criterio.validarCriterio()) {
            criterio = criser.modificar(criterio);
            seleccionarCompetencia(criterio.getCompetencia());
        }
    }

    /**
     * @return the criterios
     */
    public List<Criterio> getCriterios() {
        return criterios;
    }

    /**
     * @param criterios the criterios to set
     */
    public void setCriterios(List<Criterio> criterios) {
        this.criterios = criterios;
    }

    /**
     * @return the criterio
     */
    public Criterio getCriterio() {
        return criterio;
    }

    /**
     * @param criterio the criterio to set
     */
    public void setCriterio(Criterio criterio) {
        this.criterio = criterio;
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

}
