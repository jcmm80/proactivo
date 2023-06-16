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
public class Tipo_Entregable implements Serializable {

    @OneToMany(mappedBy = "tipo")
    private List<Entregable> entregables;

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String descripcion;
    private String nombre;
    private String tipo;
    @ManyToOne
    private Asignatura asignatura;
    @OneToMany(mappedBy = "tipoentregable")
    private List<Competencia> competencias;

    public Tipo_Entregable() {
    }

    public Tipo_Entregable(List<Entregable> entregables, Long id, String descripcion) {
        this.entregables = entregables;
        this.id = id;
        this.descripcion = descripcion;
    }

    public Tipo_Entregable(Long id, String descripcion, String nombre, Asignatura asignatura) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.asignatura = asignatura;
    }

    public Tipo_Entregable(String descripcion, String nombre, Asignatura asignatura) {
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.asignatura = asignatura;
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
        if (!(object instanceof Tipo_Entregable)) {
            return false;
        }
        Tipo_Entregable other = (Tipo_Entregable) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public boolean validar() {
        boolean valido = true;
        try {
            if (this.getNombre().equals("") || this.descripcion.equals("")) {
                valido = false;
                FacesUtil.addErrorMessage("Debe asignar un nombre al tipo de entregable");
            }
            if (this.asignatura.getId() <= 0) {
                valido = false;
                FacesUtil.addErrorMessage("Debe seleccionar una asignatura ");
            }
        } catch (java.lang.NullPointerException npe) {
            valido = false;
            FacesUtil.addErrorMessage("Debe seleccionar una asignatura ");
        }
        return valido;
    }

    @Override
    public String toString() {
        return "com.entity.Tipo_Entregable[ id=" + getId() + " ]";
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
