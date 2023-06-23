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
import javax.persistence.OneToOne;

/**
 *
 * @author jcmm
 */
@Entity
public class ProgramaAcademico implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String nombreCompleto;
    private String codigo;
    private String estado;
    @ManyToOne
    private Coordinador coordinador;
    @OneToOne
    private Profesor coordinadorPA;
    @OneToMany(mappedBy = "programa")
    private List<Fase> fases;
    @OneToMany(mappedBy = "programa")
    private List<Dimension> dimensions;

    public ProgramaAcademico() {
    }

    public ProgramaAcademico(Long id, String nombre, String nombreCompleto, String codigo, String estado, Coordinador coordinador, Profesor coordinadorPA) {
        this.id = id;
        this.nombre = nombre;
        this.nombreCompleto = nombreCompleto;
        this.codigo = codigo;
        this.estado = estado;
        this.coordinador = coordinador;
        this.coordinadorPA = coordinadorPA;
    }

    public boolean validar() {
        boolean valido = true;
        if (this.nombre.equals("") || this.nombreCompleto.equals("") || this.getCodigo().equals("")) {
            valido = false;
            FacesUtil.addErrorMessage("Estos campos son requeridos: (Nombre-NombreCompleto-Codigo");
        }
        try {
            if (this.getCoordinador().getId() < 0) {
                valido = false;
                FacesUtil.addErrorMessage("Debes Agregar un coordinador al programa");
            }
        } catch (java.lang.NullPointerException npe) {
            valido = false;
            FacesUtil.addErrorMessage("Debes Agregar un coordinador al programa");
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
        if (!(object instanceof ProgramaAcademico)) {
            return false;
        }
        ProgramaAcademico other = (ProgramaAcademico) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.ProgramaAcademico[ id=" + getId() + " ]";
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
     * @return the nombreCompleto
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * @param nombreCompleto the nombreCompleto to set
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
     * @return the coordinador
     */
    public Coordinador getCoordinador() {
        return coordinador;
    }

    /**
     * @param coordinador the coordinador to set
     */
    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    /**
     * @return the coordinadorPA
     */
    public Profesor getCoordinadorPA() {
        return coordinadorPA;
    }

    /**
     * @param coordinadorPA the coordinadorPA to set
     */
    public void setCoordinadorPA(Profesor coordinadorPA) {
        this.coordinadorPA = coordinadorPA;
    }

}
