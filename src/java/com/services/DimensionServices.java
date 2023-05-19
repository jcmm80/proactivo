/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.EntityManagedSingleton;
import com.dao.ImplDao;
import com.entity.Dimension;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Semestre;
import com.implDao.IDimension;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class DimensionServices extends ImplDao<Dimension, Long> implements IDimension,Serializable{
    public List<Dimension> obtenerDimensionesXProgramaPeriodo(Periodo p,ProgramaAcademico pa){
         List<Dimension> dimensiones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="select dim from Dimension dim where dim.periodo.id = ?1 and dim.programa.id=?2 order by dim.semestre.id";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, p.getId())
                     .setParameter(2, pa.getId());
             dimensiones=qu.getResultList();
           em.getTransaction().commit();
            System.out.println("obtenerDimensionesXProgramaPeriodo"); 
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return dimensiones;
    }
    
     public List<Dimension> obtenerDimensionesXProgramaPeriodoSemestre(Periodo p,ProgramaAcademico pa, Semestre s){
         List<Dimension> dimensiones=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="select dim from Dimension dim where dim.periodo.id = ?1 and dim.programa.id=?2 and dim.semestre.id=?3 order by dim.porcentaje";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, p.getId())
                     .setParameter(2, pa.getId())
                     .setParameter(3, s.getId());
             dimensiones=qu.getResultList();
              em.getTransaction().commit();
            System.out.println("obtenerDimensionesXProgramaPeriodoSemestre");
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return dimensiones;
    }
    
    
    public void elimimarDimensiones(Semestre s,Periodo p,ProgramaAcademico pa){
        try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();      
             String q="delete from Dimension dim where dim.periodo.id = ?1 and dim.programa.id=?2 and dim.semestre.id=?3";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, p.getId())
                     .setParameter(2, pa.getId())
                     .setParameter(3, s.getId());
             qu.executeUpdate();
             em.getTransaction().commit();
            System.out.println("elimimarDimensiones");      
         }catch(Exception ex){
             ex.printStackTrace();
         } 
    }
}
