/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Integrante;
import com.entity.Item_Proyecto;
import com.entity.LiderPA;
import com.entity.Matricula;
import com.entity.Periodo;
import com.entity.Profesor;
import com.entity.Proyecto_Aula;
import com.services.IntegranteServices;
import com.services.Item_ProyectoServices;
import com.services.MatriculaServices;
import com.services.Proyecto_AulaServices;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author jcmm
 */
@ManagedBean
@SessionScoped
public class ProyectoAulaController implements Serializable {

    //Objetos de negocio
    private Proyecto_Aula proyecto = new Proyecto_Aula();
    private LiderPA lider = new LiderPA();
    private Item_Proyecto item = new Item_Proyecto();
    private Integrante integrante=new Integrante();

    //colecciones
    private List<Item_Proyecto> itenes = new LinkedList();
    private List<Integrante> integrantes = new LinkedList();
    private List<Proyecto_Aula> proyectos = new LinkedList();
    private List<Proyecto_Aula> proyectosNoGuardados = new LinkedList();

    Proyecto_AulaServices proaser = new Proyecto_AulaServices();
    Item_ProyectoServices itemser = new Item_ProyectoServices();
    IntegranteServices inteser = new IntegranteServices();
    MatriculaServices matser = new MatriculaServices();

    @ManagedProperty("#{avanceController}")
    private AvanceController avancon;
    
    /**
     * Creates a new instance of ProyectoAulaController
     */
    public ProyectoAulaController() {
    }

    public void consultarProyectosXProfesorLider(Profesor p) {
        proyectos = proaser.obtenerProyectosXProfesorLider(p);
        //System.out.println(""+proyectos.size());
    }

    public void consultarProyectosXPeriodo(Periodo p) {
        proyectos = proaser.obtenerProyectosXPeriodo(p);
        //System.out.println(""+proyectos.size());
    }
    
    public void obtenerProyectosNoGuardados(){
        for(Proyecto_Aula p:proyectos){
            if(!p.getEstado().equals("Guardado")){
                proyectosNoGuardados.add(p);
            }
        }
    }
    
    public void obtenerProyectoAulaXMatricula(Matricula m) { 
        integrante=inteser.obtenerIntegranteXMatricula(m);        
        proyecto = proaser.consultar(Proyecto_Aula.class, integrante.getProyecto().getId());
        proyecto.setIntegrantes(inteser.obtenerIntegrantesProyecto(proyecto));
        proyecto.setItenes_Proyecto(itemser.obtenerProyectosXPeriodo_Programa(proyecto));
        avancon.setProyecto(proyecto);
        avancon.setIntegrante(integrante);//para saber que integrante ingresa el avance
        avancon.consultarAvances(proyecto);
    }

    public void guardarItem(Matricula edit) {
        item.setFecharegistro(new Date());
        item.setEditor(edit);
        item.setProyecto(proyecto);
        if (item.validarItemProyecto()) {
            item = itemser.modificar(item);
            item = new Item_Proyecto();
            obtenerItenesProyectoAula();
        }
    }

    public void obtenerItenesProyectoAula() {
        proyecto.setItenes_Proyecto(itemser.obtenerProyectosXPeriodo_Programa(proyecto));
    }

    public void guardarPA() {
        proyecto.setEstado("Guardado");
        proyecto = proaser.modificar(proyecto);
    }
    public void aprobarPA() {
        proyecto.setEstado("Produccion");
        proyecto = proaser.modificar(proyecto);
    }
    public void aplazarPA() {
        proyecto.setEstado("Aplazado");
        proyecto = proaser.modificar(proyecto);
    }


    public void publicarPA() {
        proyecto.setEstado("Propuesta");
        if (proyecto.getItenes_Proyecto().size() > 0) {
            if (proyecto.esvalido()) {
                if (proyecto.validarInfo()) {
                    proyecto = proaser.modificar(proyecto);
                }
            }
        } else {
            FacesUtil.addErrorMessage("No se puede publicar proyecto si no posee Items");
        }
    }

    public void crearPA() {
        proyecto.setEstado("Guardado");
        proyecto.setFecha_ingreso(new Date());
        if (validarIntegrantes()) {
            if (proyecto.esvalido()) {
                datosPeriodoPrograma();
                proyecto.generarCodigo();
                proyecto = proaser.modificar(proyecto);
                guardarIntegrates(proyecto);
                FacesUtil.addInfoMessage("Se ha creado un grupo de proyecto de aula");
                consultarProyectosXProfesorLider(lider.getProfesor());
                obtenerIntegrantesXProyectos(lider.getProfesor());
                matser.obtenerMatriculasXperiodo(proyecto.getSeccion().getPeriodo());
                proyecto = new Proyecto_Aula();
                integrantes = new LinkedList();
            }
        }
    }

    public void seleccionarProyecto(Proyecto_Aula pa) {
        proyecto = pa;
        integrantes = pa.getIntegrantes();
    }

    public void eliminarProyecto(Proyecto_Aula pa) {
        ListIterator it = pa.getIntegrantes().listIterator();
        while (it.hasNext()) {
            Integrante inte = (Integrante) it.next();
            Matricula mat = inte.getMatricula();
            mat.setEstadoPA("Libre");
            matser.modificar(mat);
        }
        proaser.eliminar(pa);
        proyectos.remove(pa);
    }

    public void eliminarIntegrante(Integrante inte, Proyecto_Aula pa) {
        Matricula mat = inte.getMatricula();
        mat.setEstadoPA("Libre");
        matser.modificar(mat);
        inteser.eliminar(inte);
        pa.getIntegrantes().remove(inte);
    }

    public void obtenerIntegrantesXProyectos(Profesor p) {
        try {
            integrantes = inteser.obtenerIntegrantesProyectosXProfesorLider(p);            
            for (int i = 0; i < proyectos.size(); i++) {
                ListIterator it = integrantes.listIterator();
                proyectos.get(i).setIntegrantes(new LinkedList());
                while (it.hasNext()) {
                    Integrante inte = (Integrante) it.next();
                    if (inte.getProyecto().getId().equals(proyectos.get(i).getId())) {
                        proyectos.get(i).getIntegrantes().add(inte);
                        it.remove();
                    }
                }
            }
        } catch (java.util.ConcurrentModificationException cme) {
            cme.printStackTrace();
        }
    }

     public void obtenerIntegrantesXProyectos(Periodo p) {
        try {
            integrantes = inteser.obtenerIntegrantesProyectosXPeriodo(p);
            for (int i = 0; i < proyectos.size(); i++) {
                ListIterator it = integrantes.listIterator();
                proyectos.get(i).setIntegrantes(new LinkedList());
                while (it.hasNext()) {
                    Integrante inte = (Integrante) it.next();
                    if (inte.getProyecto().getId().equals(proyectos.get(i).getId())) {
                        proyectos.get(i).getIntegrantes().add(inte);
                        it.remove();
                    }
                }
            }
        } catch (java.util.ConcurrentModificationException cme) {
            cme.printStackTrace();
        }
    }
    
    public void datosPeriodoPrograma() {
        proyecto.setSeccion(lider.getSeccion());
        proyecto.setProfesorLider(lider);
        // System.out.println(proyecto.getPeriodo().getAnio()+" "+proyecto.getProfesorLider().getProfesor().getPrimerNombre());
    }

    public void guardarIntegrates(Proyecto_Aula p) {
        for (Integrante i : integrantes) {
            i.setProyecto(p);
            i.setFechaIngreso(new Date());
            i.setEstado("Activo");
            i.setRol("Estudiante");
            inteser.crear(i);
            modificarMatriculaAsignado(i.getMatricula());
        }
    }

    public void modificarMatriculaAsignado(Matricula m) {
        m.setEstadoPA("Asignado");
        m = matser.modificar(m);
    }

    public void publicar() {

    }

    public void quitarIntegrante(Integrante inte) {
        if (existeIntegrante(inte.getMatricula())) {
            integrantes.remove(inte);
        }
    }

    public boolean validarIntegrantes() {
        boolean valido = true;
        int nintegrantes = integrantes.size();
        if (nintegrantes > 5) {
            valido = false;
            FacesUtil.addErrorMessage("EL numero de integrantes es superior al reglamentado");
        }
        if (nintegrantes <= 0) {
            valido = false;
            FacesUtil.addErrorMessage("No hay integrantes asignados al grupo");
        }
        return valido;
    }

    public void agregarIntegrante(Matricula m) {
        Integrante integrante = new Integrante();
        integrante.setMatricula(m);
        integrante.setProyecto(proyecto);
        integrante.setFechaIngreso(new Date());
        integrante.setEstado("Activo");
        integrante.setRol("Estudiante");
        if (!existeIntegrante(m)) {
            integrantes.add(integrante);
        }
    }

    public boolean existeIntegrante(Matricula m) {
        boolean existe = false;
        if (integrantes.size() <= 0) {
            existe = false;
        } else {
            for (Integrante inte : integrantes) {
                if (inte.getMatricula().getId().equals(m.getId())) {
                    existe = true;
                }
            }
        }
        return existe;
    }

    /**
     * @return the proyecto
     */
    public Proyecto_Aula getProyecto() {
        return proyecto;
    }

    /**
     * @param proyecto the proyecto to set
     */
    public void setProyecto(Proyecto_Aula proyecto) {
        this.proyecto = proyecto;
    }

    /**
     * @return the itenes
     */
    public List<Item_Proyecto> getItenes() {
        return itenes;
    }

    /**
     * @param itenes the itenes to set
     */
    public void setItenes(List<Item_Proyecto> itenes) {
        this.itenes = itenes;
    }

    /**
     * @return the integrantes
     */
    public List<Integrante> getIntegrantes() {
        return integrantes;
    }

    /**
     * @param integrantes the integrantes to set
     */
    public void setIntegrantes(List<Integrante> integrantes) {
        this.integrantes = integrantes;
    }

    /**
     * @return the lider
     */
    public LiderPA getLider() {
        return lider;
    }

    /**
     * @param lider the lider to set
     */
    public void setLider(LiderPA lider) {
        this.lider = lider;
    }

    /**
     * @return the proyectos
     */
    public List<Proyecto_Aula> getProyectos() {
        return proyectos;
    }

    /**
     * @param proyectos the proyectos to set
     */
    public void setProyectos(List<Proyecto_Aula> proyectos) {
        this.proyectos = proyectos;
    }

    /**
     * @return the item
     */
    public Item_Proyecto getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item_Proyecto item) {
        this.item = item;
    }

    /**
     * @return the proyectosNoGuardados
     */
    public List<Proyecto_Aula> getProyectosNoGuardados() {
        return proyectosNoGuardados;
    }

    /**
     * @param proyectosNoGuardados the proyectosNoGuardados to set
     */
    public void setProyectosNoGuardados(List<Proyecto_Aula> proyectosNoGuardados) {
        this.proyectosNoGuardados = proyectosNoGuardados;
    }

    /**
     * @return the avancon
     */
    public AvanceController getAvancon() {
        return avancon;
    }

    /**
     * @param avancon the avancon to set
     */
    public void setAvancon(AvanceController avancon) {
        this.avancon = avancon;
    }

    /**
     * @return the integrante
     */
    public Integrante getIntegrante() {
        return integrante;
    }

    /**
     * @param integrante the integrante to set
     */
    public void setIntegrante(Integrante integrante) {
        this.integrante = integrante;
    }

}
