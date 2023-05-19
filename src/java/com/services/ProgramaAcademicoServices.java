/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Coordinador;
import com.entity.Profesor;
import com.entity.ProgramaAcademico;
import com.implDao.IProgramaAcademico;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class ProgramaAcademicoServices extends ImplDao<ProgramaAcademico, Long> implements IProgramaAcademico, Serializable {

    public List<ProgramaAcademico> listarProgramasXCoordinador(Coordinador c) {
        List<ProgramaAcademico> programa = new LinkedList();
        EntityManager em = getEntityManagger();
        //System.out.println(l+" y contraceña: "+c);
        em.getTransaction().begin();
        String q = "select p from ProgramaAcademico p where p.coordinador.id = ?1";
        System.out.println(" Consulta: " + q);
        Query qu = em.createQuery(q)
                .setParameter(1, c.getId());
        programa = qu.getResultList();
           em.getTransaction().commit();
            System.out.println("listarProgramasXCoordinador");   
        return programa;
    }
    
    public ProgramaAcademico obtenerProgramaAcademicoxCoordinadorPA(Profesor p){
        EntityManager em =ImplDao.getEntityManagger();
        ProgramaAcademico pro=new ProgramaAcademico();
        em.getTransaction().begin();        
        try{
        String q="select p from ProgramaAcademico p where p.coordinadorPA.id = ?1";        
//        System.out.println(" Consulta: "+q);
        Query qu=em.createQuery(q)
                .setParameter(1, p.getId());
        pro=(ProgramaAcademico)qu.getSingleResult();
//        System.out.println(" Usuario: "+usu.getTipo());
        }catch(javax.persistence.NoResultException ner){
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        }
        finally{
            em.getTransaction().commit();
            System.out.println("obtenerProgramaAcademicoxCoordinadorPA");
//            ImplDao.cerrarEmf("obtenerProgramaAcademicoxCoordinadorPA");
        }
        
        return  pro;
    }
}
