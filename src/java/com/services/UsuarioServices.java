/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Usuario;
import com.implDao.IUsuario;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Jcmm
 */
public class UsuarioServices extends ImplDao<Usuario, Long> implements IUsuario,Serializable{
   
     public Usuario ingresar(String l,String c){
        EntityManager em =ImplDao.getEntityManagger();
        Usuario usu=new Usuario();
        em.getTransaction().begin();        
        try{
        String q="select u from Usuario u where u.login = ?1 and u.password = ?2";        
//        System.out.println(" Consulta: "+q);
        Query qu=em.createQuery(q)
                .setParameter(1, l)
                .setParameter(2, c);
        usu=(Usuario)qu.getSingleResult();
//        System.out.println(" Usuario: "+usu.getTipo());
        }catch(Exception ex){
            ex.printStackTrace();
            //FacesUtil.addErrorMessage("Error Inicio Session",ex.getMessage() );
        }finally{
            em.getTransaction().commit();
            System.out.println("ingresar");       
        }
        
        return  usu;
    }

}
