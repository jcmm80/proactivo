/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades;

import com.entity.Asignatura;
import com.entity.Valoracion;
import java.util.List;

/**
 *
 * @author JCMM
 */
public class ResultadosAsignatura {
    private Asignatura asignatura;
    private List<Valoracion> valoraciones;

    public ResultadosAsignatura() {
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
     * @return the valoraciones
     */
    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    /**
     * @param valoraciones the valoraciones to set
     */
    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }
}
