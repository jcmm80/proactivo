/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.LiderPA;
import com.entity.Periodo;
import com.entity.Profesor;
import com.implDao.ILiderPA;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class LiderPAServices extends ImplDao<LiderPA, Long> implements ILiderPA,Serializable{
    public List<LiderPA> obtenersemestresLiderPAXProfesor(Profesor p,Periodo pe){
        EntityManager em =ImplDao.getEntityManagger();
        List<LiderPA> lid=new LinkedList();
        em.getTransaction().begin();        
        try{//tengo que colocar el semestre para que me de un unico resultado
        String q="select lp from LiderPA lp where lp.seccion.periodo.id = ?1 and lp.profesor.id= ?2";        
//        System.out.println(" Consulta: "+q);
        Query qu=em.createQuery(q)
                .setParameter(1, pe.getId())
                .setParameter(2, p.getId());
        lid=qu.getResultList();
//        System.out.println(" Usuario: "+usu.getTipo());
        }catch(javax.persistence.NoResultException ner){
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        }
        finally{
            em.getTransaction().commit();
            System.out.println("obtenersemestresLiderPAXProfesor");
        }
        
        return  lid;
    }
     public List<LiderPA> obtenerSeccionesLideradasPeriodo(Periodo pe){
        EntityManager em =ImplDao.getEntityManagger();
        List<LiderPA> lid=new LinkedList();
        em.getTransaction().begin();        
        try{//tengo que colocar el semestre para que me de un unico resultado
        String q="select lp from LiderPA lp where lp.seccion.periodo.id = ?1";        
//        System.out.println(" Consulta: "+q);
        Query qu=em.createQuery(q)
                .setParameter(1, pe.getId());
        lid=qu.getResultList();
//        System.out.println(" Usuario: "+usu.getTipo());
        }catch(javax.persistence.NoResultException ner){
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        }
        finally{
            em.getTransaction().commit();
            System.out.println("obtenerSeccionesLideradasPeriodo");
        }
        
        return  lid;
    }
}