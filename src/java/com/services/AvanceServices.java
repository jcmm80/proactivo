/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Avance;
import com.entity.Proyecto_Aula;
import com.entity.Seccion;
import com.implDao.IAvance;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class AvanceServices extends ImplDao<Avance, Long> implements IAvance, Serializable {

    public List<Avance> obtenerAvancesXProyecto(Proyecto_Aula p) {
        List<Avance> avances = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select ava from Avance ava where ava.proyecto.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            avances = qu.getResultList();

            em.getTransaction().commit();
            System.out.println("obtenerAvancesXProyecto");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return avances;
    }

    public List<Avance> obtenerAvancesXSeccion(Seccion s) {
        List<Avance> avances = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select ava from Avance ava where ava.proyecto.seccion.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, s.getId());
            avances = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerAvancesXSeccion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return avances;
    }
}
