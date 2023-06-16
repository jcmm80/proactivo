/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.EntityManagedSingleton;
import com.dao.ImplDao;
import com.entity.Asignatura;
import com.entity.Competencia;
import com.entity.Criterio;
import com.entity.CriterioEvaluacion;
import com.entity.Profesor;
import com.entity.Seccion;
import com.implDao.ICriterio;
import java.io.Serializable;
import java.util.Arrays;
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
            em.getTransaction().commit();
            System.out.println("obtenerCriteriosXCompetencia"); 
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
            String q = "select u from Criterio u where u.competencia.unidad.asignatura.seccion.id = ?1 and u.tipo = 'Especifico'";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, s.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerCriteriosXSeccion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }

    public List<Criterio> obtenerCriteriosGlobalesXProfesor(Profesor p) {//ojo revisar que sea por programa
        List<Criterio> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Criterio u where u.competencia.unidad.asignatura.profesor.id = ?1 and u.tipo = 'Global'";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerCriteriosGlobalesXProfesor");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
    
    public void elimimarCriteriosEvaluacion(List<CriterioEvaluacion> criterios) {
        try {
            List<Long> idsToDelete = new LinkedList();
            for (CriterioEvaluacion c : criterios) {
                idsToDelete.add(c.getCriterio().getId());
            }
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            
            String q = "delete from Criterio c where c.id IN (?1)";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, idsToDelete);
            qu.executeUpdate();
            em.getTransaction().commit();
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
