/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jcmm
 */
@Entity
public class Competencia implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private UnidadCompetencia unidad;
    @ManyToOne
    private TipoCompetencia tipo;
    private String evidencia;
    private String bibliografia;
    private String estrategia;
    private int horas;//horas de presencialidad
    @ManyToOne
    private Tipo_Entregable tipoentregable;//tipo de entregable asociado
    @OneToMany(mappedBy = "competencia")
    private List<Criterio> criterios;

    public Competencia() {
//        tipoentregable=new Tipo_Entregable();
//        tipo=new TipoCompetencia();
//        unidad=new UnidadCompetencia();
    }

    public Competencia(Long id, UnidadCompetencia unidad, TipoCompetencia tipo, String evidencia, String bibliografia, String estrategia, int horas, Tipo_Entregable tipoentregable) {
        this.id = id;
        this.unidad = unidad;
        this.tipo = tipo;
        this.evidencia = evidencia;
        this.bibliografia = bibliografia;
        this.estrategia = estrategia;
        this.horas = horas;
        this.tipoentregable = tipoentregable;
    }
    

     public boolean validarCompetencia() {
        boolean valido = true;
        if (this.evidencia.equals("") || this.estrategia.equals("") || this.bibliografia.equals("")) {
            FacesUtil.addErrorMessage("Todos los campos son requeridos");
            valido = false;
        }
        if (this.horas <= 0) {
            FacesUtil.addErrorMessage("creditos de competencia deben ser superiores a 0");
            valido = false;
        }if(this.unidad.getId()<=0){
            FacesUtil.addErrorMessage("Una competencia debe pertenecer a una Unidad de competencia");
            valido = false;
        }if(this.tipo.getId()<=0){
            FacesUtil.addErrorMessage("Una competencia debe pertenecer a un tipo de competecnia");
            valido = false;
        }if(this.tipoentregable.getId()<=0){
            FacesUtil.addErrorMessage("Una competencia debe estar asociada a un tipo de entregable");
            valido = false;
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
        if (!(object instanceof Competencia)) {
            return false;
        }
        Competencia other = (Competencia) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Competencia[ id=" + getId() + " ]";
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
     * @return the tipo
     */
    public TipoCompetencia getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(TipoCompetencia tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the evidencia
     */
    public String getEvidencia() {
        return evidencia;
    }

    /**
     * @param evidencia the evidencia to set
     */
    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

    /**
     * @return the bibliografia
     */
    public String getBibliografia() {
        return bibliografia;
    }

    /**
     * @param bibliografia the bibliografia to set
     */
    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }

    /**
     * @return the estrategia
     */
    public String getEstrategia() {
        return estrategia;
    }

    /**
     * @param estrategia the estrategia to set
     */
    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * @return the horas
     */
    public int getHoras() {
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(int horas) {
        this.horas = horas;
    }

    /**
     * @return the tipoentregable
     */
    public Tipo_Entregable getTipoentregable() {
        return tipoentregable;
    }

    /**
     * @param tipoentregable the tipoentregable to set
     */
    public void setTipoentregable(Tipo_Entregable tipoentregable) {
        this.tipoentregable = tipoentregable;
    }
    
}
