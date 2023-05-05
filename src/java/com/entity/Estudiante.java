/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.controller.FacesUtil;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.servlet.ServletContext;

/**
 *
 * @author jcmm
 */
@Entity
public class Estudiante extends Usuario implements Serializable {

    private String codigo;
    @OneToMany(mappedBy = "estudiante")
    private List<Matricula> matriculas;

    public Estudiante() {
    }

    public Estudiante(String codigo) {
        this.codigo = codigo;
    }

    public Estudiante(String codigo, Long id, String estado, String login, String password, String tipo, String tipo_ide, String identificacion, String email, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String direccion, String telefono, String genero) {
        super(id, estado, login, password, tipo, tipo_ide, identificacion, email, primerNombre, segundoNombre, primerApellido, segundoApellido, direccion, telefono, genero);
        this.codigo = codigo;
    }

    public void generarCodigo(Matricula m) {
        this.codigo = m.getSeccion().getPrograma().getCodigo() + this.getIdentificacion().substring(0, 5) + this.getId();
    }
    
    
    public String imagenPerfil(){        
        String img="";
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getRealPath("/imagenInicial.jpeg").replace("imagenInicial.jpeg", "Imagenes\\Perfiles\\");
        //this.setId(Long.getLong("5"));
        String imagen=path+this.getId()+".jpg";
        File f=new File(imagen);
        //System.out.println(""+f.toString()+" "+f.exists()+" "+f.length()+" ");
        if(f.length()>0){
            img=this.getId()+".jpg";;
        }else{
            if(this.getGenero().equals("Masculino")){
                img="iperfilh.jpg";
            }if(this.getGenero().equals("Femenino")){
                img="iperfilm.jpg";
            }
           // System.out.println(""+img);
        }
           return img;
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

}
