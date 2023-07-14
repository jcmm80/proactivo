/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author jcmm
 */
@Entity
public class Periodo implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicial;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaFinal;
    private int numero;
    private int anio;
    private boolean actual;
    @OneToMany(mappedBy = "periodo")
    private List<Fase> fases;

    public Periodo() {
    }

    public Periodo(Long id, Date fecha, Date fechaInicial, Date fechaFinal, int numero, int anio) {
        this.id = id;
        this.fecha = fecha;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.numero = numero;
        this.anio = anio;
    }

    public boolean validar() {
        boolean valido = true;
        try{
        if (this.fechaInicial.after(this.fechaFinal)) {
            FacesUtil.addErrorMessage("La fecha inicial no puede ser inferior a la fecha final");
            valido = false;
        }
        if (this.anio < 2022) {
            FacesUtil.addErrorMessage("El año correspondiente al periodo esta fuera de rango");
            valido = false;
        }
        if (this.numero < 1 || this.numero > 2) {
            FacesUtil.addErrorMessage("El periodo asignado esta fuera de rango");
            valido = false;
        }
        }catch(java.lang.NullPointerException npe){
            valido = false;
            FacesUtil.addErrorMessage("Debe ingresar las fechas  de inicio y final del periodo");
        }
        return valido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Periodo)) {
            return false;
        }
        Periodo other = (Periodo) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + this.anio + "-" + this.numero;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the fechaInicial
     */
    public Date getFechaInicial() {
        return fechaInicial;
    }

    public String getFormattedFechaInicial() {
        Date fechaInicial = getFechaInicial();
        return formatDate(fechaInicial);
    }

    public String getFormattedFechaFinal() {
        Date fechaFinal = getFechaFinal();
        return formatDate(fechaFinal);
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    /**
     * @param fechaInicial the fechaInicial to set
     */
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    /**
     * @return the fechaFinal
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
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

    /**
     * @return the anio
     */
    public int getAnio() {
        return anio;
    }

    /**
     * @param anio the anio to set
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * @return the actual
     */
    public boolean isActual() {
        return actual;
    }

    /**
     * @param actual the actual to set
     */
    public void setActual(boolean actual) {
        this.actual = actual;
    }

}