/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.servlet.ServletContext;

/**
 *
 * @author jcmm
 */
@Entity
public class Asignatura implements Serializable {

    @OneToMany(mappedBy = "asignatura")
    private List<Tipo_Entregable> tipo_Entregables;

    @OneToMany(mappedBy = "asignatura")
    private List<UnidadCompetencia> unidadCompetencias;


    @OneToMany(mappedBy = "asignatura")
    private List<Entregable> entregables;

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String codigo;
    private String nombre;
    private String nombreCorto;
    private String estado;
    private int creditos;
    @ManyToOne
    private Area area;
    @ManyToOne
    private Seccion seccion;
    @ManyToOne
    private Profesor profesor;

    public boolean validarAsignatura() {
        boolean valido = true;
        if (this.codigo.equals("") || this.nombre.equals("") || this.nombreCorto.equals("")) {
            FacesUtil.addErrorMessage("Todos los campos son requeridos");
            valido = false;
        }
        if (this.creditos < 0) {
            FacesUtil.addErrorMessage("creditos de asignatura deben ser superiores a 0");
            valido = false;
        }if(this.area.getId()<0){
            FacesUtil.addErrorMessage("debe seleccionar el area o componente de la asignatura");
            valido = false;
        }if(this.seccion.getId()<0){
            FacesUtil.addErrorMessage("No hay seccion o curso seleccionado para esta asignatura");
            valido = false;
        }

        return valido;
    }

    
    public boolean tieneDocumento(){
        boolean existe=false;
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getRealPath("/fuente.png").replace("fuente.png", "Soportes\\Curriculos\\");
        //this.setId(Long.getLong("5"));
        String archivo=path+this.getId()+".pdf";
        File f=new File(archivo);
        if(f.length()>0){
            existe=true;
        }
        return existe;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asignatura)) {
            return false;
        }
        Asignatura other = (Asignatura) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        String info="";        
        try{
            info="Codigo: "+this.codigo
                +" - Asignatura: "+this.nombre
                +" - Area: "+this.area.getNombre()
                +" => Profesor: "+this.profesor.toString()
                +" - Seccion: "+this.seccion.getSeccion();
        }catch(java.lang.NullPointerException npe){
            info="No hay Asignatura seleccionada";
        }        
        return info;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the nombreCorto
     */
    public String getNombreCorto() {
        return nombreCorto;
    }

    /**
     * @param nombreCorto the nombreCorto to set
     */
    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    /**
     * @return the creditos
     */
    public int getCreditos() {
        return creditos;
    }

    /**
     * @param creditos the creditos to set
     */
    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    /**
     * @return the area
     */
    public Area getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the profesor
     */
    public Profesor getProfesor() {
        return profesor;
    }

    /**
     * @param profesor the profesor to set
     */
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
    /**
     * @param seccion the seccion to set
     */
    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    /**
     * @return the seccion
     */
    public Seccion getSeccion() {
        return seccion;
    }

}
