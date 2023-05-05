/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dao;

import java.util.List;

/**
 *
 * @author admin
 */
public interface IDao<T,ID> {
    public void crear(T entity);
    public <T> T consultar(Class<T> entityClass, Object primaryKey);
    public <T> T modificar(T entity);
    public void eliminar(Object entity);
    public List<T> consultarTodo(Class<T> entityClass);
}
