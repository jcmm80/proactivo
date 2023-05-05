/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades;

import com.entity.Profesor;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author jcmm
 */
public class GestorImagenes {
    private UploadedFile iprofesor;
    private Profesor profesor;
    
    
//     public void subirImagenProfesor() {
//        if (getIprofesor() != null) {
//            try {
//                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//                String path = servletContext.getRealPath("/imagenInicial.jpeg").replace("imagenInicial.jpeg", "Imagenes\\Perfiles\\");
//                System.out.println(path);
//                ImageUtils.copyFile(profesor.getId() + ".jpg", getIprofesor().getInputStream(), path);
//                
//            } catch (IOException ex) {
//                Logger.getLogger(GestorImagenes.class
//                        .getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
      
     public void subirImagenProfesor() {
        try {  
//               File destFile= new File(event.getFile().getFileName());           
               System.out.println(""+iprofesor.getFileName());
               ServletContext servletContext = (ServletContext) 
               FacesContext.getCurrentInstance().getExternalContext().getContext();
               String path=servletContext.getRealPath("/imagenInicial.jpg").replace("imagenInicial.jpg", "Imagenes\\Perfiles\\");
               ImageUtils.copyFile(profesor.getId()+".jpg", iprofesor.getInputStream(),path);
               System.out.println(""+path);
                //getEstudiante().getEstudiante().setImagenC(path+event.getFile().getFileName()+".jpg");               
        } catch (IOException ex) {
            Logger.getLogger(GestorImagenes.class.getName()).log(Level.SEVERE, null, ex);
        }	
    }
     
     
    /**
     * @return the iprofesor
     */
    public UploadedFile getIprofesor() {
        return iprofesor;
    }

    /**
     * @param iprofesor the iprofesor to set
     */
    public void setIprofesor(UploadedFile iprofesor) {
        this.iprofesor = iprofesor;
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
}
