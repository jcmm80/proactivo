/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Coordinador;
import com.entity.Nucleo;
import com.entity.Profesor;
import com.entity.ProgramaAcademico;
import com.entity.Semestre;
import com.services.NucleoServices;
import com.services.ProgramaAcademicoServices;
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
public class ProgramaController implements Serializable {

    private ProgramaAcademico programa = new ProgramaAcademico();
    private Nucleo nucleo = new Nucleo();
//    private Semestre semestre = new Semestre();

    private List<ProgramaAcademico> programas = new LinkedList();
    private List<Nucleo> nucleos;

    ProgramaAcademicoServices paserv = new ProgramaAcademicoServices();
    NucleoServices nucser = new NucleoServices();

    /**
     * Creates a new instance of ProgramaController
     */
    public ProgramaController() {
    }

    public void obtenerNucleosXPrograma() {
        nucleos = nucser.obtenerNucleosXPrograma(programa);
    }

    public void almacenarNucleo() {
        Nucleo nucleoe = nucser.obtenerNucleoXSemestre(nucleo.getSemestre());

        if (nucleoe.getId() > 0) {
            nucleoe.setPrograma(programa);
            nucleoe.setDescripcion(nucleo.getDescripcion());
            nucleoe.setFecha(new Date());
            if (nucleoe.validarNucleo()) {
                nucleo = nucser.modificar(nucleoe);
                nucleo = new Nucleo();
            } else {
                FacesUtil.addErrorMessage("Faltan datos para definir a un nucleo");
            }
        } else {
            nucleo.setPrograma(programa);
            nucleo.setFecha(new Date());
            if (nucleo.validarNucleo()) {
                nucleo = nucser.modificar(nucleo);
                nucleo = new Nucleo();
            } else {
                FacesUtil.addErrorMessage("Faltan datos para definir a un nucleo");
            }
        }

        obtenerNucleosXPrograma();
    }

    public void agregarSemestreANucleo(Semestre s) {
        nucleo.setSemestre(s);
    }

    public void consultarprograma(ProgramaAcademico p) {
        programa = p;
    }

    public void seleccionarCoordinadorPA(Profesor p) {
        programa.setCoordinadorPA(p);
    }

    public void obtenerProgramaCoordinadorPA(Profesor p) {
        programa = paserv.obtenerProgramaAcademicoxCoordinadorPA(p);
        obtenerNucleosXPrograma();
    }

    public void consultarProgramas() {
        programas = paserv.consultarTodo(ProgramaAcademico.class);
    }

    public void consultarProgramasXCoordinador(Coordinador c) {
        programas = paserv.listarProgramasXCoordinador(c);
    }

    public void agregarCoordinador(Coordinador c) {
        programa.setCoordinador(c);
    }

    public void asignarCoordinadorPA() {
        try {
            if (programa.getCoordinadorPA().getId() > 0) {
                if (programa.getId() > 0) {
                    programa = paserv.modificar(programa);
                    FacesUtil.addInfoMessage("Se asigno el profesor: " + programa.getCoordinadorPA().toString() + " al Programa: " + programa.getNombreCompleto());
                } else {
                    FacesUtil.addErrorMessage("Debe Seleccionar el programa");
                }
            } else {
                FacesUtil.addErrorMessage("Debe Seleccionar el profesor");
            }
        } catch (NullPointerException npe) {
            FacesUtil.addErrorMessage("Debe Seleccionar el programa");
        }
    }

    public void registrar() {
        if (programa.validar()) {
            programa.setEstado("Activo");
            programa = paserv.modificar(programa);
            consultarProgramas();
//            consultarProgramasXCoordinador(programa.getCoordinador());
            programa = new ProgramaAcademico();
        }
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
     * @return the programas
     */
    public List<ProgramaAcademico> getProgramas() {
        return programas;
    }

    /**
     * @param programas the programas to set
     */
    public void setProgramas(List<ProgramaAcademico> programas) {
        this.programas = programas;
    }

    /**
     * @return the nucleos
     */
    public List<Nucleo> getNucleos() {
        return nucleos;
    }

    /**
     * @param nucleos the nucleos to set
     */
    public void setNucleos(List<Nucleo> nucleos) {
        this.nucleos = nucleos;
    }

    /**
     * @return the nucleo
     */
    public Nucleo getNucleo() {
        return nucleo;
    }

    /**
     * @param nucleo the nucleo to set
     */
    public void setNucleo(Nucleo nucleo) {
        this.nucleo = nucleo;
    }

}
