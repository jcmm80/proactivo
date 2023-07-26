/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades;

import java.util.Date;

/**
 *
 * @author JCMM
 */
public class Licencia {
    private Date fechai;
    private Date fechaf;
    private String key;
    private Date fechacreacion;
    private String tipo;
    private String version;
    private String descripcion;
    private String alcance;
    private String restricciones;

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
     * @return the fechai
     */
    public Date getFechai() {
        return fechai;
    }

    /**
     * @param fechai the fechai to set
     */
    public void setFechai(Date fechai) {
        this.fechai = fechai;
    }

    /**
     * @return the fechaf
     */
    public Date getFechaf() {
        return fechaf;
    }

    /**
     * @param fechaf the fechaf to set
     */
    public void setFechaf(Date fechaf) {
        this.fechaf = fechaf;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the fechacreacion
     */
    public Date getFechacreacion() {
        return fechacreacion;
    }

    /**
     * @param fechacreacion the fechacreacion to set
     */
    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
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

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the alcance
     */
    public String getAlcance() {
        return alcance;
    }

    /**
     * @param alcance the alcance to set
     */
    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    /**
     * @return the restricciones
     */
    public String getRestricciones() {
        return restricciones;
    }

    /**
     * @param restricciones the restricciones to set
     */
    public void setRestricciones(String restricciones) {
        this.restricciones = restricciones;
    }
    
}
