/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import static com.dao.ImplDao.getEntityManagger;
import com.entity.Asistente;
import com.entity.Integrante;
import com.entity.Tutoria;
import com.implDao.IAsistente;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class AsistenteServices extends ImplDao<Asistente, Long> implements IAsistente,Serializable{
    public List<Asistente> obtenerAsistenciasXProyecto(Integrante i){
         List<Asistente> asistencias=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="select asi from Asistente asi where asi.estudiante.id = ?1";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, i.getId());
             asistencias=qu.getResultList();
                         em.getTransaction().commit();
            System.out.println("obtenerAsistenciasXProyecto");            
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return asistencias;
    }
     public List<Asistente> obtenerAsistenciasXTutoria(Tutoria t){
         List<Asistente> asistencias=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="select asi from Asistente asi where asi.tutoria.id = ?1";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, t.getId());
             asistencias=qu.getResultList();
                         em.getTransaction().commit();
            System.out.println("obtenerAsistenciasXTutoria");            
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return asistencias;
    }
}
