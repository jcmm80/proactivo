/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Competencia;
import com.entity.Criterio;
import com.entity.Seccion;
import com.implDao.ICriterio;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class CriterioServices extends ImplDao<Criterio, Long> implements ICriterio, Serializable {

    public List<Criterio> obtenerCriteriosXCompetencia(Competencia p) {//ojo revisar que sea por programa
        List<Criterio> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Criterio u where u.competencia.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            secciones = qu.getResultList();
            em.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
    
    public List<Criterio> obtenerCriteriosXSeccion(Seccion s) {//ojo revisar que sea por programa
        List<Criterio> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Criterio u where u.competencia.unidad.asignatura.seccion.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, s.getId());
            secciones = qu.getResultList();
            em.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
}
