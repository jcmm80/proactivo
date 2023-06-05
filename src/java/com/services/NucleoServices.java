/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Nucleo;
import com.entity.ProgramaAcademico;
import com.entity.Semestre;
import com.implDao.INucleo;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class NucleoServices extends ImplDao<Nucleo, Long> implements INucleo,Serializable{
   public List<Nucleo> obtenerNucleosXPrograma(ProgramaAcademico pa) {
        List<Nucleo> nucleos = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select f from Nucleo f where f.programa.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, pa.getId());
            nucleos = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerNucleosXPrograma");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nucleos;
    }
   
    public Nucleo obtenerNucleoXSemestre(Semestre s){
        EntityManager em =ImplDao.getEntityManagger();
        Nucleo nuc=new Nucleo();
        em.getTransaction().begin();        
        try{
        String q="select u from Nucleo u where u.semestre.id = ?1";        
//        System.out.println(" Consulta: "+q);
        Query qu=em.createQuery(q)
                .setParameter(1, s.getId());
        nuc=(Nucleo)qu.getSingleResult();
//        System.out.println(" Usuario: "+usu.getTipo());
        }catch(javax.persistence.NoResultException nr){
            nuc=new Nucleo();
        }
        catch(Exception ex){
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        }
        finally{
            em.getTransaction().commit();
            System.out.println("obtenerNucleoXSemestre");       
        }
        
        return  nuc;
    }

   
}
