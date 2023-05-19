/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Estudiante;
import com.entity.Matricula;
import com.entity.Periodo;
import com.entity.Profesor;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.entity.Semestre;
import com.implDao.ISeccion;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class SeccionServices extends ImplDao<Seccion, Long> implements ISeccion,Serializable{
    public List<Seccion> obtenerSeccionesXSemestre_Periodo_Programa(Semestre s,ProgramaAcademico pa,Periodo p){
         List<Seccion> secciones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();               
             String q="select sec from Seccion sec where sec.semestre.id = ?1 and sec.programa.id = ?2 and sec.periodo.id = ?3";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, s.getId())
                     .setParameter(2, pa.getId())
                     .setParameter(3, p.getId());
             secciones=qu.getResultList();
                em.getTransaction().commit();
            System.out.println("obtenerSeccionesXSemestre_Periodo_Programa");               
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return secciones;
    }
    
     public List<Seccion> obtenerSeccionesXPeriodo_Programa(ProgramaAcademico pa,Periodo p){
         List<Seccion> secciones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             System.out.println(""+pa.getId()+"  "+p.getId());
             String q="select sec from Seccion sec where sec.programa.id = ?1 and sec.periodo.id = ?2 order by sec.semestre";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, pa.getId())
                     .setParameter(2, p.getId());
             secciones=qu.getResultList();
               em.getTransaction().commit();
            System.out.println("obtenerSeccionesXPeriodo_Programa");            
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return secciones;
    }
     
       public List<Seccion> obtenerSeccionesXPeriodo(Periodo p){
         List<Seccion> secciones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
//             System.out.println(""+pa.getId()+"  "+p.getId());
             String q="select sec from Seccion sec where sec.periodo.id = ?1 order by sec.semestre";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, p.getId());
             secciones=qu.getResultList();
               em.getTransaction().commit();
            System.out.println("obtenerSeccionesXPeriodo");              
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return secciones;
    }
     
     public List<Seccion> obtenerSeccionesXProfesor(Profesor p){
         List<Seccion> secciones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
//             System.out.println(""+pa.getId()+"  "+p.getId());
             String q="select sec from Seccion sec where sec.profesor.id = ?1";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, p.getId());
             secciones=qu.getResultList();
             em.getTransaction().commit();
            System.out.println("obtenerSeccionesXProfesor");               
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return secciones;
    }
     
}
