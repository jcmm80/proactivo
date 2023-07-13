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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Avance implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Proyecto_Aula proyecto;
    @ManyToOne
    private Fase fase;
    private int numero;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEntrega;
    @ManyToOne
    private Integrante integrante;
    private String estado;
    //Date fechaUltimaModificacion;
    private String descripcion;
    @OneToMany(mappedBy = "avance",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<Entregable> entregables;

    public Avance() {
        this.id=Long.parseLong("0");
    }

    public Avance(Long id, Proyecto_Aula proyecto, Fase fase, int numero, Date fechaEntrega, Integrante integrante, String estado, String descripcion) {
        this.id = id;
        this.proyecto = proyecto;
        this.fase = fase;
        this.numero = numero;
        this.fechaEntrega = fechaEntrega;
        this.integrante = integrante;
        this.estado = estado;
        this.descripcion = descripcion;
    }
    
    
    public boolean validarAvance() {
        boolean valido = true;               
        if(this.getProyecto().getId()<=0){
            FacesUtil.addErrorMessage("Avance no tiene proyecto asociado");
            valido = false;
        }
        if (this.getFase().getId()<=0) {
            FacesUtil.addErrorMessage("Avance no tiene fase o ciclo asociado");
            valido = false;
        } 
        if (this.getIntegrante().getId()<=0) {
            FacesUtil.addErrorMessage("Avance no tiene autor vinculado");
            valido = false;
        }
        if (this.descripcion.trim().equals("")) {
            FacesUtil.addErrorMessage("Debe agregar la descripcion del avance");
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
        if (!(object instanceof Avance)) {
            return false;
        }
        Avance other = (Avance) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Avance[ id=" + getId() + " ]";
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
     * @return the proyecto
     */
    public Proyecto_Aula getProyecto() {
        return proyecto;
    }

    /**
     * @param proyecto the proyecto to set
     */
    public void setProyecto(Proyecto_Aula proyecto) {
        this.proyecto = proyecto;
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
     * @return the fechaEntrega
     */
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * @param fechaEntrega the fechaEntrega to set
     */
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * @return the integrante
     */
    public Integrante getIntegrante() {
        return integrante;
    }

    /**
     * @param integrante the integrante to set
     */
    public void setIntegrante(Integrante integrante) {
        this.integrante = integrante;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the entregables
     */
    public List<Entregable> getEntregables() {
        return entregables;
    }

    /**
     * @param entregables the entregables to set
     */
    public void setEntregables(List<Entregable> entregables) {
        this.entregables = entregables;
    }
    
}
