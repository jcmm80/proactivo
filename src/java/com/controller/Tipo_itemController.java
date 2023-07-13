/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Tipo_Entregable;
import com.entity.Tipo_Item;
import com.services.Tipo_EntregableServices;
import com.services.Tipo_ItemServices;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Dimas
 */
@ManagedBean
@SessionScoped
public class Tipo_itemController implements Serializable{

    private Tipo_Item tipo_item = new Tipo_Item();

    private Tipo_ItemServices tipiser = new Tipo_ItemServices();

    private List<Tipo_Item> tipos_Items = new LinkedList();

    /**
     * Creates a new instance of PeriodoController
     */
    public Tipo_itemController() {
    }
   

    public void registrarTipo_Item() {
        if(getTipo_item().validar()){
            setTipo_item(getTipiser().modificar(getTipo_item()));
            setTipo_item(new Tipo_Item());
            consultarTipos_Items();
    
        }
    }
    
//    public boolean existeNombre(){
//        
//    }
    
    public void eliminartipo_Item(Tipo_Item te){
        
        getTipiser().eliminar(te);
          consultarTipos_Items();   
        
    }
        
    public void seleccionarItem(Tipo_Item ti){
        setTipo_item(ti);
    }
    
       public void consultarTipos_Items(){
           System.out.println("consulte los tipos de item");
           setTipos_Items(getTipiser().consultarTodo(Tipo_Item.class));
    }

    /**
     * @return the tipo_item
     */
    public Tipo_Item getTipo_item() {
        return tipo_item;
    }

    /**
     * @param tipo_item the tipo_item to set
     */
    public void setTipo_item(Tipo_Item tipo_item) {
        this.tipo_item = tipo_item;
    }

    /**
     * @return the tipiser
     */
    public Tipo_ItemServices getTipiser() {
        return tipiser;
    }

    /**
     * @param tipiser the tipiser to set
     */
    public void setTipiser(Tipo_ItemServices tipiser) {
        this.tipiser = tipiser;
    }     

    /**
     * @return the tipos_Items
     */
    public List<Tipo_Item> getTipos_Items() {
        return tipos_Items;
    }

    /**
     * @param tipos_Items the tipos_Items to set
     */
    public void setTipos_Items(List<Tipo_Item> tipos_Items) {
        this.tipos_Items = tipos_Items;
    }

  
 
        
    }

