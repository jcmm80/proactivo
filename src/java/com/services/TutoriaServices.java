/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Asignatura;
import com.entity.Profesor;
import com.entity.Proyecto_Aula;
import com.entity.Tutoria;
import com.implDao.ITutoria;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class TutoriaServices extends ImplDao<Tutoria, Long> implements ITutoria, Serializable {

    public Tutoria consultarTutoriaXAsignaturaSolicitada(Asignatura a, Proyecto_Aula p) {
        EntityManager em = ImplDao.getEntityManagger();
        Tutoria tut = new Tutoria();
        em.getTransaction().begin();
        try {
            String q = "select t from Tutoria t where t.asignatura.id = ?1 and t.proyecto.id = ?2 and t.estado = ?3";
//        System.out.println(" Consulta: "+q);
            Query qu = em.createQuery(q)
                    .setParameter(1, a.getId())
                    .setParameter(2, p.getId())
                    .setParameter(3, "Solicitud");
            tut = (Tutoria) qu.getSingleResult();
//        System.out.println(" Usuario: "+usu.getTipo());
        } catch (Exception ex) {
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        } finally {
            em.close();
        }

        return tut;
    }

    public List<Tutoria> obtenerTutoriasXAsignaturaProyecto(Asignatura a, Proyecto_Aula p) {
        List<Tutoria> tutorias = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select t from Tutoria t where t.asignatura.id = ?1 and t.proyecto.id = ?2";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, a.getId())
                    .setParameter(2, p.getId());
            tutorias = qu.getResultList();
            em.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tutorias;
    }
     public List<Tutoria> obtenerTutoriasXProfesor(Profesor p) {
        List<Tutoria> tutorias = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select t from Tutoria t where t.asignatura.profesor.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            tutorias = qu.getResultList();
            em.close();
            ImplDao.cerrarEmf("obtenerTutoriasXProfesor");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tutorias;
    }
     
      public List<Tutoria> obtenerTutoriasXAsignatura(Asignatura a) {
        List<Tutoria> tutorias = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select t from Tutoria t where t.asignatura.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, a.getId());
            tutorias = qu.getResultList();
            em.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tutorias;
    }
     
}
