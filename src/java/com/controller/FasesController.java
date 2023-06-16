/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Avance;
import com.entity.Fase;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.services.AvanceServices;
import com.services.FaseServices;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
public class FasesController implements Serializable {

    private Fase fase = new Fase();
    private List<Fase> fases = new LinkedList();
    List<Avance> avancesPeriodo;
    private Periodo periodo;
    private ProgramaAcademico programa;

    private Date fechaIfase = new Date();
    private Date fechaFfase = new Date();

    private Date fechaInicialdisponible;
    private Date fechaFinaldisponible;
    private int numero;

    FaseServices fasser = new FaseServices();
    AvanceServices avaser = new AvanceServices();

    /**
     * Creates a new instance of FasesController
     */
    public FasesController() {
    }

    public void obtenerFasesXPrograma(ProgramaAcademico pa, Periodo p) {
        setFases(fasser.obtenerFasesXPrograma(pa, p));
        disponibilidad();
        avancesPeriodo();
    }

    public void avancesPeriodo() {
        avancesPeriodo = avaser.obtenerAvancesXPeriodo(periodo);
    }

    public int avancesXfase(Fase f) {
        int na = 0;
        if (avancesPeriodo.size() > 0) {
            for (Avance a : avancesPeriodo) {
                if (a.getFase().getId().equals(f.getId())) {
                    na++;
                }
            }
        } else {
            na = 0;
        }
        return na;
    }

    public boolean habilitarELiminar(Fase f) {
        boolean habilitar = true;
        if (avancesXfase(f) > 0) {
            habilitar = false;
        }
        return habilitar;
    }

    public void disponibilidad() {
        if (fases.size() > 0) {
            fechaInicialdisponible = fechaMayor();
            fechaFinaldisponible = periodo.getFechaFinal();
            setNumero(fases.size() + 1);
        } else {
            setNumero(1);
            fechaInicialdisponible = periodo.getFechaInicial();
            fechaFinaldisponible = periodo.getFechaFinal();
        }
    }

    public boolean habilitarCreacion() {
        boolean habilitar = true;
        long dias = (this.periodo.getFechaFinal().getTime() - this.fechaInicialdisponible.getTime()) / (24 * 60 * 60 * 1000);
        
        
        if (dias < 8) {
            habilitar = false;
            FacesUtil.addWarnMessage("No existe tiempo disponible en el periodo para agregar mas fases o es muy corto");
        }
        return habilitar;
    }

    public Date fechaMayor() {
        Date fm = new Date();
        fm = fases.get(0).getFechaFinal();
        for (Fase f : fases) {
            if (f.getFechaFinal().after(fm)) {
                fm = f.getFechaFinal();
            }
        }
        return fm;
    }

    public boolean validarCruse(Fase f) {
        boolean valido = true;
        Fase siguiente = obtenerFase(f.getNumero() + 1);
        if (siguiente != null) {
            if (f.getFechaFinal().after(siguiente.getFechaInicial())) {
                valido = false;
                FacesUtil.addErrorMessage("La fecha final se cruza con la siguiente fase creada en el sistema");
            }
        }
        Fase anterior = obtenerFase(f.getNumero() - 1);
        if (anterior != null) {
            if (f.getFechaInicial().before(anterior.getFechaFinal())) {
                valido = false;
                FacesUtil.addErrorMessage("La fecha inicial se cruza con la anterior fase creada en el sistema");
            }
        }
        return valido;
    }

    public Fase obtenerFase(int numero) {
        Fase fas = null;
        for (Fase f : fases) {
            if (f.getNumero() == numero) {
                fas = f;
                break;
            }
        }
        return fas;
    }

    public void agregar() {
        fase.setNumero(getNumero());
        fase.setPeriodo(periodo);
        fase.setPrograma(getPrograma());
        fase.setFechaInicial(fechaIfase);
        fase.setFechaFinal(fechaFfase);
        if (validarCruse(fase)) {
            if (fase.validarFase()) {
                fasser.crear(fase);
                fase = new Fase();
                obtenerFasesXPrograma(programa, periodo);
            }
        }
    }

    public void seleccionar(Fase f) {
        fase = f;
        fechaInicialdisponible = periodo.getFechaInicial();
        fechaFinaldisponible = periodo.getFechaFinal();
        setNumero(fase.getNumero());
        boolean habilitar = true;
    }

    public void eliminarFase(Fase f) {
        fasser.eliminar(f);
        fase = new Fase();
        obtenerFasesXPrograma(programa, periodo);
    }

    public String getFormattedfechaIfase() {
        Date fechaInicial = fechaIfase;
        return formatDate(fechaInicial);
    }

    public String getFormattedfechaFfase() {
        Date fechaFinal = fechaFfase;
        return formatDate(fechaFinal);
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    /**
     * @return the fases
     */
    public List<Fase> getFases() {
        return fases;
    }

    /**
     * @param fases the fases to set
     */
    public void setFases(List<Fase> fases) {
        this.fases = fases;
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
     * @return the fechaInicialdisponible
     */
    public Date getFechaInicialdisponible() {
        return fechaInicialdisponible;
    }

    /**
     * @param fechaInicialdisponible the fechaInicialdisponible to set
     */
    public void setFechaInicialdisponible(Date fechaInicialdisponible) {
        this.fechaInicialdisponible = fechaInicialdisponible;
    }

    /**
     * @return the fechaFinaldisponible
     */
    public Date getFechaFinaldisponible() {
        return fechaFinaldisponible;
    }

    /**
     * @param fechaFinaldisponible the fechaFinaldisponible to set
     */
    public void setFechaFinaldisponible(Date fechaFinaldisponible) {
        this.fechaFinaldisponible = fechaFinaldisponible;
    }

    /**
     * @return the fase
     */
    public Fase getFase() {
        return fase;
    }

    /**
     * @param fase the fase to set
     */
    public void setFase(Fase fase) {
        this.fase = fase;
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
     * @return the fechaIfase
     */
    public Date getFechaIfase() {
        return fechaIfase;
    }

    /**
     * @param fechaIfase the fechaIfase to set
     */
    public void setFechaIfase(Date fechaIfase) {
        this.fechaIfase = fechaIfase;
    }

    /**
     * @return the fechaFfase
     */
    public Date getFechaFfase() {
        return fechaFfase;
    }

    /**
     * @param fechaFfase the fechaFfase to set
     */
    public void setFechaFfase(Date fechaFfase) {
        this.fechaFfase = fechaFfase;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }
}
