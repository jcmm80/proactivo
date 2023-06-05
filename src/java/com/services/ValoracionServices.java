/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Asignatura;
import com.entity.CriterioEvaluacion;
import com.entity.Evaluacion;
import com.entity.Integrante;
import com.entity.Periodo;
import com.entity.Valoracion;
import com.implDao.IValoracion;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class ValoracionServices extends ImplDao<Valoracion, Long> implements IValoracion,Serializable{
 public void elimimarValoracionIntegrante(Integrante i, CriterioEvaluacion ce) {
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "delete from Valoracion v where v.integrante.id = ?1 and v.criterio.id = ?2";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, i.getId())
                    .setParameter(2, ce.getId());
            qu.executeUpdate();
            em.getTransaction().commit();
            System.out.println("elimimarValoracionesIntegrante");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
  public List<Valoracion> obtenerValoracionesXEvaluacion(Evaluacion e) {
        List<Valoracion> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Valoracion u where u.criterio.evaluacion.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, e.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerValoracionesXEvaluacion");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
  
  public List<Valoracion> obtenerValoracionesXAsignatura(Asignatura a) {
        List<Valoracion> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Valoracion u where u.criterio.criterio.competencia.unidad.asignatura.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, a.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerValoracionesXEvaluacion");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
  public List<Valoracion> obtenerValoracionesXIntegrante(Integrante i) {
        List<Valoracion> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Valoracion u where u.integrante.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, i.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerValoracionesXEvaluacion");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
  
  public List<Valoracion> obtenerValoracionesXPeriodo(Periodo p) {
        List<Valoracion> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select u from Valoracion u where u.integrante.proyecto.seccion.periodo.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerValoracionesXPeriodo");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
}
