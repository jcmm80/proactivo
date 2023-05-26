/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

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
public class Item_Proyecto implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Tipo_Item tipo;
    private String contenido;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecharegistro;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechamodificacion;
    @ManyToOne
    private Matricula editor;
    @ManyToOne
    private Proyecto_Aula proyecto;

    public Item_Proyecto() {
        this.tipo=new Tipo_Item();
    }

    public Item_Proyecto(Long id, Tipo_Item tipo, String contenido, Date fecharegistro, Date fechamodificacion, Matricula editor, Proyecto_Aula proyecto) {
        this.id = id;
        this.tipo = tipo;
        this.contenido = contenido;
        this.fecharegistro = fecharegistro;
        this.fechamodificacion = fechamodificacion;
        this.editor = editor;
        this.proyecto = proyecto;
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

    public boolean validarItemProyecto() {
        boolean valido = true;
        if (this.tipo.getId().equals(0) || this.contenido.toString().equals("") || this.fecharegistro.toString().equals("")) {
            valido = false;
        }
        if (this.editor.getId().equals(0) || this.proyecto.getId().equals(0)) {
            valido = false;
        }
        return valido;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item_Proyecto)) {
            return false;
        }
        Item_Proyecto other = (Item_Proyecto) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Item_Proyecto[ id=" + getId() + " ]";
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
     * @return the tipo
     */
    public Tipo_Item getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Tipo_Item tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * @return the fecharegistro
     */
    public Date getFecharegistro() {
        return fecharegistro;
    }

    /**
     * @param fecharegistro the fecharegistro to set
     */
    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    /**
     * @return the fechamodificacion
     */
    public Date getFechamodificacion() {
        return fechamodificacion;
    }

    /**
     * @param fechamodificacion the fechamodificacion to set
     */
    public void setFechamodificacion(Date fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    /**
     * @return the editor
     */
    public Matricula getEditor() {
        return editor;
    }

    /**
     * @param editor the editor to set
     */
    public void setEditor(Matricula editor) {
        this.editor = editor;
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
    
}
