/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Asignatura;
import com.entity.Profesor;
import com.entity.Tipo_Entregable;
import com.implDao.ITipo_Entregable;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class Tipo_EntregableServices extends ImplDao<Tipo_Entregable, Long> implements ITipo_Entregable,Serializable{
    public List<Tipo_Entregable> obtenerTipo_EntregableAsignaturas(Asignatura a){
         List<Tipo_Entregable> secciones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="select te from Tipo_Entregable te where te.asignatura.id = ?1 ";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, a.getId());
             secciones=qu.getResultList();
             em.close();             
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return secciones;
    }
     public List<Tipo_Entregable> obtenerTipo_EntregableProfesor(Profesor a){
         List<Tipo_Entregable> secciones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="select te from Tipo_Entregable te where te.asignatura.profesor.id = ?1 ";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, a.getId());
             secciones=qu.getResultList();
             em.close(); 
              ImplDao.cerrarEmf("obtenerTipo_EntregableProfesor");
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return secciones;
    }
}
