/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Item_Proyecto;
import com.entity.Proyecto_Aula;
import com.implDao.IItem_Proyecto;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jcmm
 */
public class Item_ProyectoServices extends ImplDao<Item_Proyecto, Long> implements IItem_Proyecto, Serializable {

    public List<Item_Proyecto> obtenerProyectosXPeriodo_Programa(Proyecto_Aula pa) {
        List<Item_Proyecto> itenes = new LinkedList();
        try {
            EntityManager em = getEntityManagger();
            em.getTransaction().begin();
            String q = "select ite from Item_Proyecto ite where ite.proyecto.id = ?1";
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, pa.getId());
            itenes = qu.getResultList();
            em.getTransaction().commit();
            System.out.println("obtenerProyectosXPeriodo_Programa");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return itenes;
    }
}
