/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagedSingleton {
    private static final String PERSISTENCE_UNIT_NAME = "SgpaPU";
    private static EntityManagerFactory emFactory;
    private static EntityManager entityManager;

    private EntityManagedSingleton() {}

    public static EntityManager getEntityManager() {
        if (entityManager == null || !entityManager.isOpen()) {
            System.out.println("abrire la conexion ");
            createEntityManager();
        }
        return entityManager;
    }

    private static void createEntityManager() {
//        System.out.println("Estoy creando un nuevo entitymanaged");
        emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        entityManager = emFactory.createEntityManager();
    }

    public static void closeEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
            emFactory.close();
        }
    }
}