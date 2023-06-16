/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.EntityManagedSingleton;
import com.dao.ImplDao;
import com.entity.Asignatura;
import com.entity.CriterioEvaluacion;
import com.entity.Evaluacion;
import com.implDao.ICriterioEvaluacion;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class CriterioEvaluacionServices extends ImplDao<CriterioEvaluacion, Long> implements ICriterioEvaluacion, Serializable {

    public List<CriterioEvaluacion> obtenerCriterioEvaluacionXEvaluacion(Evaluacion e) {
        List<CriterioEvaluacion> criteriose = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select cr from CriterioEvaluacion cr where cr.evaluacion.id = ?1 order by cr.dimension.id";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, e.getId());
            criteriose = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerCriterioEvaluacionXEvaluacion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return criteriose;
    }

    public void elimimarCriteriosEvaluacion(Evaluacion e) {
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "delete from CriterioEvaluacion eva where eva.evaluacion.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, e.getId());
            qu.executeUpdate();
            em.getTransaction().commit();
            System.out.println("elimimarCriteriosEvaluacion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
     public List<CriterioEvaluacion> obtenerCriterioEvaluacionXAsignatura(Evaluacion e,Asignatura a) {
        List<CriterioEvaluacion> criteriose = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select cr from CriterioEvaluacion cr where cr.evaluacion.id = ?1 and cr.criterio.competencia.unidad.asignatura.id = ?2";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, e.getId())
                    .setParameter(2, a.getId());
            criteriose = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerCriterioEvaluacionXAsignatura");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return criteriose;
    }
      public List<CriterioEvaluacion> obtenerCriterioEvaluacionXAsignatura(Asignatura a) {
        List<CriterioEvaluacion> criteriose = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select cr from CriterioEvaluacion cr where cr.criterio.competencia.unidad.asignatura.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, a.getId());
            criteriose = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerCriterioEvaluacionXAsignatura");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return criteriose;
    }
}
