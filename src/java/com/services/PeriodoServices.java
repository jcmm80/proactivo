/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Periodo;
import com.implDao.IPeriodo;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class PeriodoServices extends ImplDao<Periodo, Long> implements IPeriodo,Serializable{
    public Periodo obtenerPeriodoActual(){
        EntityManager em =ImplDao.getEntityManagger();
        Periodo per=new Periodo();
        em.getTransaction().begin();        
        try{
        String q="select p from Periodo p where p.actual = ?1";        
//        System.out.println(" Consulta: "+q);
        Query qu=em.createQuery(q)
                .setParameter(1, true);
        per=(Periodo)qu.getSingleResult();
//        System.out.println(" Usuario: "+usu.getTipo());
        }catch(javax.persistence.NoResultException ner){
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        }
        finally{
             em.getTransaction().commit();
            System.out.println("obtenerPeriodoActual");          
        }
        
        return  per;
    }
}
