/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Avance;
import com.entity.Entregable;
import com.entity.Fase;
import com.entity.Integrante;
import com.entity.Proyecto_Aula;
import com.entity.Seccion;
import com.entity.Tipo_Entregable;
import com.services.AvanceServices;
import com.services.EntregableServices;
import com.utilidades.ImageUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author jcmm
 */
@ManagedBean
@SessionScoped
public class AvanceController implements Serializable {

    private Avance avance = new Avance();
    private Entregable entregable = new Entregable();
    private Proyecto_Aula proyecto = new Proyecto_Aula();
    private Integrante integrante = new Integrante();

    private List<Entregable> entregables = new LinkedList();
    private List<Entregable> entregablesFase = new LinkedList();
    
    
    private List<Avance> avances = new LinkedList();
    private List<Avance> avancesproyecto = new LinkedList();

    private boolean mostPAvance = false;
    private boolean mostPEntregable = false;
    private boolean mostPrevision;
    private int indexTabEntregable = 0;
    private UploadedFile aentregable;
    private StreamedContent file;

    AvanceServices avanser = new AvanceServices();
    EntregableServices entrser = new EntregableServices();

    /**
     * Creates a new instance of AvanceController
     */
    public AvanceController() {
    }

    public void seleccionarFase(Fase f) {
        consultarAvance(f);
        setMostPAvance(true);
        mostPEntregable = false;
        if (avance.getId() > 0) {
           verEntregables(avance);
        } else {
            avance = new Avance();
            avance.setProyecto(proyecto);
            avance.setFase(f);
            entregablesFase=new LinkedList();
            entregable.setTipo(null);
            entregable.setAsignatura(null);
        }
    }

    public void revisarEntregable(Entregable ent){
        setMostPrevision(true);
        entregable=ent;
    }
     public void regresardeRevisar(){
        setMostPrevision(false);
        entregable=new Entregable();
    }
    
     public void valorarEntregable(){
         if(!entregable.getObservaciones().trim().equals("") && entregable.getPorcentajeEjecucion()>0){
             entregable.setEstado("Revisado");
             entregable=entrser.modificar(entregable);
             regresardeRevisar();
         }
     }
     
     
     
    public void consultarAvances(Proyecto_Aula pa) {
        System.out.println("consulte los avances");
        avances = avanser.obtenerAvancesXProyecto(pa);
        entregables = entrser.obtenerEntregablesXProyecto(pa);
    }
    
    public void consultarAvancesXSeccion(Seccion s){
        avances=avanser.obtenerAvancesXSeccion(s);
    }

    public Avance avanceFase(Fase f){
        Avance ava=new Avance();
        for (Avance av : avancesproyecto) {
             if (av.getFase().getId().equals(f.getId())) {
                 ava=av;break;
             }
         }
        return ava;
    }
    
    public void obtenerAvancesProyecto(Proyecto_Aula pa){
        avancesproyecto = new LinkedList();
        for (Avance av : avances) {
            if (av.getProyecto().getId().equals(pa.getId())) {
                avancesproyecto.add(av);
            }
        }
    }
    
    public void consultarAvance(Fase f) {
        try {
            avance=new Avance();
            avance.setProyecto(proyecto);   
          
            for (Avance av : avances) {
                System.out.println("Avance: " + av.getDescripcion() + " " + av.getFase() + "\n");
                if (av.getFase().getId().equals(f.getId())) {
                    avance = av;
////                System.out.println("Avance: " + avanT.getDescripcion());
                    break;
                }
            }
          System.out.println(""+avance.getProyecto().getCodigo());
        } catch (java.lang.NullPointerException npe) {

        }
    }

    public void verEntregables(Avance ava) {
        entregablesFase = new LinkedList();
        for (Entregable e : ava.getEntregables()) {
            if (e.getAvance().getId().equals(ava.getId())) {
                entregablesFase.add(e);
                System.out.println("Entregable: " + e.getDescripcion() + "\n");
            }
        }
    }

    public void volverFases() {
        setMostPAvance(false);
        avance=new Avance();
        resetEntregable();
    }

    public void descargarArchivo(Entregable en) throws Exception {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getRealPath("/fuente.png").replace("fuente.png", "Soportes\\Entregables\\");
        File origen = new File(path + en.getNombreArchivo() + "." + conversor(en.getExtencionArchivo()));
        String nombre = origen.getName();
        File destino = new File("C:\\Users\\jcmm\\Downloads\\" + en.getNombreArchivo() + conversor(en.getExtencionArchivo()));

        try {
            InputStream in = new FileInputStream(origen);
            OutputStream out = new FileOutputStream(destino);
            System.out.println(origen);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                System.out.println("" + len);
                out.write(buf, 0, len);
            }
            // System.out.println(out.toString());
            in.close();
            out.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void descargar(Entregable en) throws IOException {
//      File ficheroXLS = new File(strPathXLS);
        FacesContext ctx = FacesContext.getCurrentInstance();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getRealPath("/fuente.png").replace("fuente.png", "Soportes\\Entregables\\");
        File origen = new File(path + en.getNombreArchivo() + "." + conversor(en.getExtencionArchivo()));
        String nombre = origen.getName();
        File destino = new File(path + en.getNombreArchivo() + "." + conversor(en.getExtencionArchivo()));
        FileInputStream fis = new FileInputStream(destino);

        byte[] bytes = new byte[1000];
        int read = 0;
        if (!ctx.getResponseComplete()) {
            String fileName = origen.getName();
            String contentType = en.getExtencionArchivo();
            //String contentType = "application/pdf";
            HttpServletResponse response
                    = (HttpServletResponse) ctx.getExternalContext().getResponse();
            response.setContentType(contentType);
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream out = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            System.out.println("\nDescargado\n");
            ctx.responseComplete();
        }
    }

    public void resetEntregable() {
        entregable.setPorcentajeAutoevaluacion(0);
        entregable.setTipo(null);
        entregable.setAsignatura(null);
    }

    public void gEntregable() {
        indexTabEntregable = 1;
        resetEntregable();
    }

    public void volverAvance() {
        indexTabEntregable = 0;
    }

    public void agregarTipoEntregable(Tipo_Entregable te) {
        entregable.setTipo(te);
    }

    public void agregarAsignatura(Asignatura asi) {
        entregable.setAsignatura(asi);
    }

    public void agregarEntregable() {
        if (entregable.getTipo() != null && entregable.getAsignatura() != null) {
            mostPEntregable = true;
        } else {
            mostPEntregable = false;
            FacesUtil.addWarnMessage("Debe agregar el tipo de entregable y la asignatura");
        }
    }

    public void desvincularEntregable(Entregable e) {
        if(avance.getId()>0){//si ya el avance existe eliminarlo solo si no esta revisado
            if(e.getEstado().equals("Entregado")){
                avance.getEntregables().remove(e);
                entrser.eliminar(e);                
            }
        }
        eliminarArchivoEntregable(e);
        entregablesFase.remove(e);
    }

    public boolean avanceRegistrado(){
        if(avance.getId()>0){
            return true;
        }else{
            return false;
        }
    }
    
    public void eliminarAvance(){
        if(entregablesRevisados(avance)){
            FacesUtil.addErrorMessage("No se puede eliminar un avance con entregables revisados");
        }else{
            avances.remove(avance);
            avanser.eliminar(avance);
            volverFases();
        }
    }
    

    
     public boolean entregablesRevisados(Avance avance) {
        boolean revisado = false;
        for (Entregable ent : avance.getEntregables()) {
            if (!ent.getEstado().equals("Entregado")) {
                revisado = true;
                break;
            }
        }
        return revisado;
    }
    
    public void almacenarAvance() {        
        if (entregablesFase.size() > 0) {
            avance.setFechaEntrega(new Date());
            avance.setEstado("Guardado");
            avance.setIntegrante(integrante);
            avance.setNumero(avance.getFase().getNumero());
            if (avance.validarAvance()) {
                if (validarEntregables(avance)) {
                    avance = avanser.modificar(avance);
                    almacenarEntregables(avance);
                    consultarAvances(avance.getProyecto()); 
                    limpiarAvance();
                }
            }
        } else {
            FacesUtil.addWarnMessage("No se puede almacenar un avance sin entregables");
        }
    }

    public void limpiarAvance() {
        entregablesFase = new LinkedList();
        volverFases();
    }

    public boolean validarEntregables(Avance avance) {
        boolean validos = true;
        for (Entregable ent : entregablesFase) {
            if (!ent.validarEntregable()) {
                validos = false;
                break;
            }
        }
        return validos;
    }

    public void almacenarEntregables(Avance avance) {
        for (Entregable ent : entregablesFase) {
            ent.setAvance(avance);
            entrser.crear(ent);
        }
    }

    public void eliminarArchivoEntregable(Entregable e) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = servletContext.getRealPath("/fuente.png").replace("fuente.png", "Soportes\\Entregables\\");
            File origen = new File(path + e.getNombreArchivo() + "." + conversor(e.getExtencionArchivo()));

            boolean result = Files.deleteIfExists(origen.toPath());
            if (result) {
                System.out.println("File is successfully deleted.");
            } else {
                System.out.println("File deletion failed.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void vincularEntregable() {
        if (entregable.getExtencionArchivo() != null) {
            entregable.setAvance(avance);
            entregable.setFechaEntrega(new Date());
            entregable.setEstado("Entregado");
            entregablesFase.add(entregable); 
            if(avance.getId()>0){//si ya el avance existe registrarlo
                entrser.crear(entregable);  
                consultarAvances(avance.getProyecto()); 
            }            
            entregable = new Entregable();
            mostPEntregable = false;  
            volverAvance();
        } else {
            FacesUtil.addWarnMessage("Debe agregar el Archivo a Adjuntar");
        }

        //resetEntregable();
    }

    public void volverAsignaturas() {
        mostPEntregable = false;
        resetEntregable();
    }

    public void subirArchivoEntregable() {
        try {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = servletContext.getRealPath("/fuente.png").replace("fuente.png", "Soportes\\Entregables\\");
            //int nc = aentregable.getContentType().length();
            // System.out.println("" + aentregable.getContentType());
            String extencion = aentregable.getContentType();
            System.out.println(""+avance.getProyecto().getCodigo());
            String nombreArchivo = avance.getProyecto().getCodigo() + entregable.getTipo().getId() + entregable.getAsignatura().getCodigo();
            ImageUtils.copyFile(nombreArchivo + "." + conversor(extencion), aentregable.getInputStream(), path);
//            System.out.println("" + path);
            entregable.setNombreArchivo(nombreArchivo);
            entregable.setExtencionArchivo(extencion);
            //getEstudiante().getEstudiante().setImagenC(path+event.getFile().getFileName()+".jpg");               
        } catch (IOException ex) {
            Logger.getLogger(AvanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String conversor(String ext) {
        String extencion = "";
        switch (ext.toLowerCase()) {
            case "image/png":
                extencion = "png";
                break;
            case "image/jpeg":
                extencion = "jpeg";
                break;
            case "image/tiff":
                extencion = "tiff";
                break;
            case "text/plain":
                extencion = "txt";
                break;
            case "text/csv":
                extencion = "csv";
                break;
            case "text/rtf":
                extencion = "rtf";
                break;
            case "text/html":
                extencion = "html";
                break;
            case "application/pdf":
                extencion = "pdf";
                break;
            case "application/msword":
                extencion = "doc";
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                extencion = "docx";
                break;
            case "application/vnd.ms-excel":
                extencion = "xls";
                break;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                extencion = "xlsx";
                break;
            case "application/vnd.ms-powerpoint":
                extencion = "ppt";
                break;
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                extencion = "pptx";
                break;
            case "application/zip":
                extencion = "zip";
                break;
            case "application/octet-stream":
                extencion = "rar";
                break;
            case "video/mp4":
                extencion = "mp4";
                break;
            case "audio/mpeg":
                extencion = "mp3";
                break;
            case "image/bmp":
                extencion = "bmp";
                break;
            case "application/x-msdownload":
                extencion = "exe";
                break;
        }
        return extencion;
    }

    public StreamedContent getFile() {
        return file;
    }

    /**
     * @return the mostPAvance
     */
    public boolean isMostPAvance() {
        return mostPAvance;
    }

    /**
     * @param mostPAvance the mostPAvance to set
     */
    public void setMostPAvance(boolean mostPAvance) {
        this.mostPAvance = mostPAvance;
    }

    /**
     * @return the avance
     */
    public Avance getAvance() {
        return avance;
    }

    /**
     * @param avance the avance to set
     */
    public void setAvance(Avance avance) {
        this.avance = avance;
    }

    /**
     * @return the entregables
     */
    public List<Entregable> getEntregables() {
        return entregables;
    }

    /**
     * @param entregables the entregables to set
     */
    public void setEntregables(List<Entregable> entregables) {
        this.entregables = entregables;
    }

    /**
     * @return the indexTabEntregable
     */
    public int getIndexTabEntregable() {
        return indexTabEntregable;
    }

    /**
     * @param indexTabEntregable the indexTabEntregable to set
     */
    public void setIndexTabEntregable(int indexTabEntregable) {
        this.indexTabEntregable = indexTabEntregable;
    }

    /**
     * @return the mostPEntregable
     */
    public boolean isMostPEntregable() {
        return mostPEntregable;
    }

    /**
     * @param mostPEntregable the mostPEntregable to set
     */
    public void setMostPEntregable(boolean mostPEntregable) {
        this.mostPEntregable = mostPEntregable;
    }

    /**
     * @return the entregable
     */
    public Entregable getEntregable() {
        return entregable;
    }

    /**
     * @param entregable the entregable to set
     */
    public void setEntregable(Entregable entregable) {
        this.entregable = entregable;
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
     * @return the aentregable
     */
    public UploadedFile getAentregable() {
        return aentregable;
    }

    /**
     * @param aentregable the aentregable to set
     */
    public void setAentregable(UploadedFile aentregable) {
        this.aentregable = aentregable;
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

    /**
     * @return the avances
     */
    public List<Avance> getAvances() {
        return avances;
    }

    /**
     * @param avances the avances to set
     */
    public void setAvances(List<Avance> avances) {
        this.avances = avances;
    }

    /**
     * @return the entregablesFase
     */
    public List<Entregable> getEntregablesFase() {
        return entregablesFase;
    }

    /**
     * @param entregablesFase the entregablesFase to set
     */
    public void setEntregablesFase(List<Entregable> entregablesFase) {
        this.entregablesFase = entregablesFase;
    }

    /**
     * @return the avancesproyecto
     */
    public List<Avance> getAvancesproyecto() {
        return avancesproyecto;
    }

    /**
     * @param avancesproyecto the avancesproyecto to set
     */
    public void setAvancesproyecto(List<Avance> avancesproyecto) {
        this.avancesproyecto = avancesproyecto;
    }

    /**
     * @return the mostPrevision
     */
    public boolean isMostPrevision() {
        return mostPrevision;
    }

    /**
     * @param mostPrevision the mostPrevision to set
     */
    public void setMostPrevision(boolean mostPrevision) {
        this.mostPrevision = mostPrevision;
    }

}
