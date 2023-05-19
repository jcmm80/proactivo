/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.EntityManagedSingleton;
import com.dao.ImplDao;
import com.entity.Asignatura;
import com.entity.Periodo;
import com.entity.Profesor;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.implDao.IAsignatura;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class AsignaturaServices extends ImplDao<Asignatura, Long> implements IAsignatura, Serializable {

    public List<Asignatura> obtenerAsignaturasXPrograma(Periodo p) {
        List<Asignatura> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select asi from Asignatura asi where asi.seccion.periodo.id = ?1 order by asi.seccion.semestre.id";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerAsignaturasXPrograma");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }

    public List<Asignatura> obtenerAsignaturasXSeccion(Seccion s) {
        List<Asignatura> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select asi from Asignatura asi where asi.seccion.id = ?1 order by asi.nombre";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, s.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerAsignaturasXSeccion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }

    public List<Asignatura> obtenerAsignaturasXProfesor(Periodo p, Profesor pr) {
        List<Asignatura> secciones = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select asi from Asignatura asi where asi.seccion.periodo.id = ?1 and asi.profesor.id = ?2 order by asi.nombre";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId())
                    .setParameter(2, pr.getId());
            secciones = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerAsignaturasXProfesor");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secciones;
    }
}
