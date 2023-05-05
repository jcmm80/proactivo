/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Estudiante;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.implDao.IEstudiante;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class EstudianteServices extends ImplDao<Estudiante, Long> implements IEstudiante,Serializable{
    public List<Estudiante> obtenerEstudiantesMatriculados(ProgramaAcademico pa,Periodo p){
         List<Estudiante> estudiantes=new LinkedList();
         try {             
             EntityManager em =getEntityManagger();
             em.getTransaction().begin();               
             String q="select m.estudiante from Matricula m where m.seccion.programa.id = ?1 and m.seccion.periodo.id = ?2";        
             System.out.println(" Consulta: "+q);
             Query qu=em.createQuery(q)
                     .setParameter(1, pa.getId())
                     .setParameter(2, p.getId());
             estudiantes=qu.getResultList();
             em.close();             
         }catch(Exception ex){
             ex.printStackTrace();
         } 
         return estudiantes;
    }
}
