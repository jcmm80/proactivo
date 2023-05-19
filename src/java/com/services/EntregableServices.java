/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Entregable;
import com.entity.Proyecto_Aula;
import com.implDao.IEntregable;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class EntregableServices extends ImplDao<Entregable, Long> implements IEntregable,Serializable{
     public List<Entregable> obtenerEntregablesXProyecto(Proyecto_Aula p){
         List<Entregable> avances=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="select ent from Entregable ent where ent.avance.proyecto.id = ?1";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, p.getId());
             avances=qu.getResultList();
                        em.getTransaction().commit();
            System.out.println("obtenerEntregablesXProyecto");             
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return avances;
    }
}
