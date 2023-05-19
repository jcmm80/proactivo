/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.EntityManagedSingleton;
import com.dao.ImplDao;
import com.entity.Asignatura;
import com.entity.CriterioEvaluacion;
import com.entity.Profesor;
import com.entity.UnidadCompetencia;
import com.implDao.IUnidadCompetencia;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class UnidadCompetenciaServices extends ImplDao<UnidadCompetencia, Long> implements IUnidadCompetencia, Serializable {

    public List<UnidadCompetencia> obtenerUnidadCompetenciaXAsignatura(Asignatura a) {
        List<UnidadCompetencia> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from UnidadCompetencia u where u.asignatura.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, a.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerUnidadCompetenciaXAsignatura");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }

    public List<UnidadCompetencia> obtenerUnidadCompetenciaXProfesor(Profesor p) {
        List<UnidadCompetencia> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from UnidadCompetencia u where u.asignatura.profesor.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerUnidadCompetenciaXProfesor");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }

    public void elimimarUnidadesCompétenciasCriteriosGlobales(List<CriterioEvaluacion> criterios) {
        try {
            List<Long> idsToDelete = new LinkedList();
            for (CriterioEvaluacion c : criterios) {
                idsToDelete.add(c.getCriterio().getCompetencia().getUnidad().getId());
            }
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();

            String q = "delete from UnidadCompetencia c where c.id IN (?1)";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, idsToDelete);
            qu.executeUpdate();
            em.getTransaction().commit();
            System.out.println("elimimarUnidadesCompétenciasCriteriosGlobales");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
