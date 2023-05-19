/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.EntityManagedSingleton;
import com.dao.ImplDao;
import com.entity.Area;
import com.entity.Asignatura;
import com.entity.Competencia;
import com.entity.CriterioEvaluacion;
import com.entity.Profesor;
import com.implDao.IArea;
import com.implDao.ICompetencia;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class CompetenciaServices extends ImplDao<Competencia, Long> implements ICompetencia, Serializable {

    public List<Competencia> obtenerUnidadCompetenciaXProfesor(Profesor p) {
        List<Competencia> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Competencia u where u.unidad.asignatura.profesor.id = ?1";
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

    public List<Competencia> obtenerCompetenciaXAsignatura(Asignatura p) {//ojo revisar que sea por programa
        List<Competencia> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Competencia u where u.unidad.asignatura.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerCompetenciaXAsignatura");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }

    public void elimimarCompétenciasCriteriosGlobales(List<CriterioEvaluacion> criterios) {
        try {
            List<Long> idsToDelete = new LinkedList();
            for (CriterioEvaluacion c : criterios) {
                idsToDelete.add(c.getCriterio().getCompetencia().getId());
            }
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();

            String q = "delete from Competencia c where c.id IN (?1)";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, idsToDelete);
            qu.executeUpdate();
            em.getTransaction().commit();
            System.out.println("elimimarCompétenciasCriteriosGlobales"); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
