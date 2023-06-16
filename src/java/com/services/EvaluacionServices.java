/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Evaluacion;
import com.entity.Periodo;
import com.entity.Seccion;
import com.implDao.IEvaluacion;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class EvaluacionServices extends ImplDao<Evaluacion, Long> implements IEvaluacion,Serializable{
     public Evaluacion obtenerEvaluacionXSeccion(Seccion s){
        EntityManager em =ImplDao.getEntityManagger();
        Evaluacion eva=new Evaluacion();
        em.getTransaction().begin();        
        try{
        String q="select e from Evaluacion e where e.seccion.id = ?1";        
        Query qu=em.createQuery(q)
                .setParameter(1, s.getId());
        eva=(Evaluacion)qu.getSingleResult();
        }catch(javax.persistence.NoResultException nre){
            
        }
        catch(Exception ex){
//            ex.printStackTrace();
        }finally{
            em.getTransaction().commit();
            System.out.println("obtenerEvaluacionXSeccion");        
        }        
        return  eva;
    }
     
     public Evaluacion obtenerEvaluacionesXPeriodo(Periodo p){
        EntityManager em =ImplDao.getEntityManagger();
        Evaluacion eva=new Evaluacion();
        em.getTransaction().begin();        
        try{
        String q="select e from Evaluacion e where e.seccion.periodo.id = ?1";        
        Query qu=em.createQuery(q)
                .setParameter(1, p.getId());
        eva=(Evaluacion)qu.getSingleResult();
        }catch(javax.persistence.NoResultException nre){
            
        }
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            em.getTransaction().commit();
            System.out.println("obtenerEvaluacionesXPeriodo");        
        }        
        return  eva;
    }
}
