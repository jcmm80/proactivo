/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author jcmm
 */
@Entity
public class Fase implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicial;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaFinal;
    private int numero;
    @ManyToOne
    private ProgramaAcademico programa;
    @ManyToOne
    private Periodo periodo;
    @OneToMany(mappedBy = "fase")
    private List<Avance> avances;

    public Fase() {
    }

    public Fase(Long id, Date fechaInicial, Date fechaFinal, int numero, ProgramaAcademico programa) {
        this.id = id;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.numero = numero;
        this.programa = programa;
    }

     public boolean validarFase() {
        boolean valido = true;
        if (this.fechaInicial.toString().equals("") || this.fechaFinal.toString().equals("") || this.numero==0) {
            valido = false;
            FacesUtil.addErrorMessage("La fase que desea ingresar no tienen fechas de inicio o fin");
        }
        if (this.programa.getId() < 0 || this.periodo.getId() < 0) {
            valido = false;
            FacesUtil.addErrorMessage("la fase que desea ingresar no tienen cargado el programa a la cual pertenece o el periodo");
        }
        if(this.fechaInicial.after(this.fechaFinal)){
            valido = false;
            FacesUtil.addErrorMessage("la fecha inicial no puede ser inferior a la fecha final");
        }if(this.fechaInicial.equals(this.fechaFinal)){
            valido = false;
            FacesUtil.addErrorMessage("la fecha inicial no puede ser igual a la fecha final");
        }
        long dias=(this.fechaFinal.getTime()-this.fechaInicial.getTime())/(24*60*60*1000);
        if(dias<8){
            valido = false;
            FacesUtil.addErrorMessage("Rango de fechas muy corto para una fase o ciclo: (Dias: "+dias+")");
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
        if (!(object instanceof Fase)) {
            return false;
        }
        Fase other = (Fase) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Periodo: "+this.getPeriodo().getAnio()+"-"+this.getPeriodo().getNumero()+": Desde: "+this.fechaInicial+" Hasta: "+this.fechaInicial+", Fase:"+this.numero;
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
     * @return the fechaInicial
     */
    public Date getFechaInicial() {
        return fechaInicial;
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
    
}
