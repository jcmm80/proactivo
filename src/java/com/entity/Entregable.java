/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author jcmm
 */
@Entity
public class Entregable implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String descripcion;
    @ManyToOne
    private Tipo_Entregable tipo;
    @ManyToOne
    private Avance avance;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEntrega;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaRevisado;
    private String estado;
    @ManyToOne
    private Asignatura asignatura;
    private int porcentajeEjecucion;//porcentaje que valora el profesor
    private int porcentajeAutoevaluacion;
    private String nombreArchivo;
    private String extencionArchivo;
    private String observaciones;

    public Entregable() {
    }

    public Entregable(Long id, String descripcion, Tipo_Entregable tipo, Date fechaEntrega) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fechaEntrega = fechaEntrega;
    }

    public boolean esdeAsignatura(Asignatura a) {
        boolean pertenece = false;
        if (this.asignatura.getId().equals(a.getId())) {
            pertenece = true;
        }
        return pertenece;
    }

    public boolean validarEntregable() {
        boolean valido = true;
        if (this.getTipo().getId() <= 0) {
            FacesUtil.addErrorMessage("Entregable no tiene un tipo de entregable asociado");
            valido = false;
        }
        if (this.getAsignatura().getId() <= 0) {
            FacesUtil.addErrorMessage("Entregable no tiene asignatura asociada");
            valido = false;
        }

        if (this.descripcion.trim().equals("")) {
            FacesUtil.addErrorMessage("Estos campos son requeridos");
            valido = false;
        }
        return valido;
    }

    public Long getId() {
        return id;
    }
    
    public String colorEstado() {
        String color = "";
        
        if(this.estado.equals("Guardado")){
            color = "#EEE";
        }
        if(this.estado.equals("Entregado")){
            color = "var(--first-color-c)";
        }
        if(this.estado.equals("Revisado")){
            color = "var(--first-color-p)";
        }
        
        
        return color;
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
        if (!(object instanceof Entregable)) {
            return false;
        }
        Entregable other = (Entregable) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Entregable[ id=" + getId() + " ]";
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
     * @return the tipo
     */
    public Tipo_Entregable getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Tipo_Entregable tipo) {
        this.tipo = tipo;
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
     * @return the porcentajeEjecucion
     */
    public int getPorcentajeEjecucion() {
        return porcentajeEjecucion;
    }

    /**
     * @param porcentajeEjecucion the porcentajeEjecucion to set
     */
    public void setPorcentajeEjecucion(int porcentajeEjecucion) {
        this.porcentajeEjecucion = porcentajeEjecucion;
    }

    /**
     * @return the porcentajeAutoevaluacion
     */
    public int getPorcentajeAutoevaluacion() {
        return porcentajeAutoevaluacion;
    }

    /**
     * @param porcentajeAutoevaluacion the porcentajeAutoevaluacion to set
     */
    public void setPorcentajeAutoevaluacion(int porcentajeAutoevaluacion) {
        this.porcentajeAutoevaluacion = porcentajeAutoevaluacion;
    }

    /**
     * @return the avance
     */
    public Avance getAvance() {
        return avance;
    }

    /**
     * @param avance the avance to set
     */
    public void setAvance(Avance avance) {
        this.avance = avance;
    }

    /**
     * @return the fechaRevisado
     */
    public Date getFechaRevisado() {
        return fechaRevisado;
    }

    /**
     * @param fechaRevisado the fechaRevisado to set
     */
    public void setFechaRevisado(Date fechaRevisado) {
        this.fechaRevisado = fechaRevisado;
    }

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo the nombreArchivo to set
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * @return the extencionArchivo
     */
    public String getExtencionArchivo() {
        return extencionArchivo;
    }

    /**
     * @param extencionArchivo the extencionArchivo to set
     */
    public void setExtencionArchivo(String extencionArchivo) {
        this.extencionArchivo = extencionArchivo;
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
