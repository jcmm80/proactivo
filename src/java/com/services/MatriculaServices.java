/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Estudiante;
import com.entity.Matricula;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.implDao.IMatricula;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class MatriculaServices extends ImplDao<Matricula, Long> implements IMatricula, Serializable {

    public Matricula obtenerMatriculaXPeriodo(Periodo p, Estudiante e) {
        EntityManager em = ImplDao.getEntityManagger();
        Matricula mat = new Matricula();
        em.getTransaction().begin();
        try {
            String q = "select m from Matricula m where m.seccion.periodo.id = ?1 and m.estudiante.id= ?2";

//        System.out.println(" Consulta: "+q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId())
                    .setParameter(2, e.getId());
            mat = (Matricula) qu.getSingleResult();
//        System.out.println(" Usuario: "+usu.getTipo());
        } catch (javax.persistence.NoResultException ner) {
            mat = new Matricula();
        } catch (Exception ex) {
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        } finally {
            em.getTransaction().commit();
        }

        return mat;
    }

    public List<Matricula> obtenerMatriculasXperiodo(Periodo p) {
        List<Matricula> matriculas = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select m from Matricula m where m.seccion.periodo.id = ?1 and m.estado not in('Financiera')";
//             System.out.println(" Consulta: "+q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            matriculas = qu.getResultList();

            em.getTransaction().commit();
            System.out.println("obtenerMatriculasXperiodo");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return matriculas;
    }

     public List<Matricula> obtenerMatriculasXSeccion(Seccion p) {
        List<Matricula> matriculas = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select m from Matricula m where m.seccion.id = ?1";
//             System.out.println(" Consulta: "+q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId());
            matriculas = qu.getResultList();

            em.getTransaction().commit();
            System.out.println("obtenerMatriculasXSeccion");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return matriculas;
    }

    
    public List<Matricula> obtenerMatriculasXperiodoYPrograma(Periodo p, ProgramaAcademico pa) {
        List<Matricula> matriculas = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select m from Matricula m where m.seccion.periodo.id = ?1 and m.seccion.programa.id = ?2";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, p.getId())
                    .setParameter(2, pa.getId());
            matriculas = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerMatriculasXperiodoYPrograma");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return matriculas;
    }
}
