/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Estudiante;
import com.entity.Integrante;
import com.entity.Item_Proyecto;
import com.entity.Matricula;
import com.entity.Periodo;
import com.entity.Proyecto_Aula;
import com.entity.Tipo_Item;
import com.entity.Tutoria;
import com.services.EstudianteServices;
import com.utilidades.ImageUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author jcmm
 */
@ManagedBean
@SessionScoped
public class EstudianteController implements Serializable {

    //objetos de negocio
    private Estudiante estudiante = new Estudiante();
   
    private Periodo periodo = new Periodo();//para almacenar el el periodo actual

    //controlladores
    @ManagedProperty("#{matriculaController}")
    private MatriculaController matcont;
    @ManagedProperty("#{proyectoAulaController}")
    private ProyectoAulaController proacon;
    @ManagedProperty("#{tipo_itemController}")
    private Tipo_itemController tipicon;
    @ManagedProperty("#{fasesController}")
    private FasesController fascon;
    @ManagedProperty("#{tipo_EntregableController}")
    private Tipo_EntregableController tipecon;
    @ManagedProperty("#{asignaturaController}")
    private AsignaturaController asigcon;
    @ManagedProperty("#{tutoriasController}")
    private TutoriasController tutcon;
    @ManagedProperty("#{avanceController}")
    private AvanceController avancon;

    //Servicios
    EstudianteServices estser = new EstudianteServices();

    //colecciones    
    private List<Estudiante> estudiantes = new LinkedList();

    //variable de control
    private boolean mpanelInscripcion = true;
    private String paginaActualE = "";
    private UploadedFile iestudiante;
    private boolean mpanelAItems = false;
    private boolean mpanelItem = false;
    private boolean mpanelTutorias = false;

    /**
     * Creates a new instance of EstudianteController
     */
    public EstudianteController() {
    }

    public void consultarMatriculaEstudiante() {
        try {
            matcont.consultarMatriculaXEstudianteEnPeriodo(estudiante, periodo);
        } catch (java.lang.NullPointerException npe) {
            FacesUtil.addWarnMessage("Usuario, tipo de usuario o matricula inexistente");
        }

    }


    
    public void consultarProyectoXMatricula() {
        try {
            proacon.obtenerProyectoAulaXMatricula(matcont.getMatricula());
            matcont.setIntegrante(proacon.getIntegrante());
            matcont.consultarResultadosXIntegrante();
        } catch (java.lang.NullPointerException npe) {
            paginaActualE = "";
            FacesUtil.addWarnMessage("Usuario, no tiene proyecto de aula asignado en el periodo");
        }
    }

    public void consultarFases() {
        fascon.obtenerFasesXPrograma(matcont.getMatricula().getSeccion().getPrograma(), periodo);
    }

    public void consultarTiposEntregable() {
        tipecon.consultarTipos_Entregable();
    }

    public void consultarAsignaturas() {
        asigcon.obtenerAsignaturasXEstudiante(matcont.getMatricula().getSeccion());
    }

    public void agregarAsignaturaTutoria(Asignatura a) {
        tutcon.setAsignatura(a);
        tutcon.setProyecto(proacon.getProyecto());
        tutcon.tutoriaPendiente();
        tutcon.consultarTutoriasXAsignaturaProyecto();
        tutcon.consultarAsistenciasXEstudianteProyecto(proacon.getIntegrante());
        mpanelTutorias = true;
    }

    public void agregarAsignaturaEntregable(Asignatura a) {
        avancon.agregarAsignatura(a);
        tipecon.setAsignatura(a);
        tipecon.consultarTipos_entregable(a);//mejorar 
        tipecon.consultarTipos_Entregable();
    }

    public void pvolverAsignaturardTutoria() {
        mpanelTutorias = false;
    }

    public void pcrearTutoria() {
        tutcon.crearTutoria();
        tutcon.consultarTutoriasXAsignaturaProyecto();
    }

    public void eliminarTutoria(Tutoria t) {
        if (t.getIntegrante().getId().equals(proacon.getIntegrante().getId())) {
            tutcon.eliminarTutoria(t);
            tutcon.consultarTutoriasXAsignaturaProyecto();
        } else {
            FacesUtil.addErrorMessage("No se puede Eliminar: debe ser el Integrante del grupo que la Solicitó");
        }
    }

    public void agregarItem() {
        mpanelAItems = true;
    }

    public void volverItems() {
        mpanelAItems = false;
    }

    public void seleccionarTItem(Tipo_Item ip) {
        //tipicon.setTipo_item(ip);       
        try{
        proacon.getItem().setTipo(ip);
        mpanelItem = true;
        mpanelAItems = false;
        }catch(java.lang.NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void registrarItem() {
        proacon.guardarItem(matcont.getMatricula());
        volverAItenes();
    }

    public void volverAItenes() {
        mpanelItem = false;
    }

    public void seleccionarItem(Item_Proyecto ite) {
        ite.setFechamodificacion(new Date());
        proacon.setItem(ite);
        mpanelItem = true;
        mpanelAItems = false;
    }

    public void eliminarItem(Item_Proyecto ite) {
        proacon.itemser.eliminar(ite);
        proacon.getProyecto().getItenes_Proyecto().remove(ite);
    }

    public void limpiarDatos() {
        matcont.setMatricula(new Matricula());
        proacon.setProyecto(new Proyecto_Aula());
    }

    public void inscribirEstudiante() {
        estudiante.setTipo("Estudiante");
        estudiante.setEstado("Pre-Matricula");
        if (estudiante.validarUsuario()) {
            estser.modificar(estudiante);
            estudiante = new Estudiante();
            mpanelInscripcion = false;
        }
    }

    public void guardarProyectoAula() {
        proacon.guardarPA();
    }

    public void publicarProyectoAula() {
        proacon.publicarPA();
    }

    public void subirImagenEstudiante() {
        try {
//               File destFile= new File(event.getFile().getFileName());           
            System.out.println("" + iestudiante.getFileName());
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = servletContext.getRealPath("/imagenInicial.jpg").replace("imagenInicial.jpg", "Imagenes\\Perfiles\\");
            ImageUtils.copyFile(estudiante.getId() + ".jpg", iestudiante.getInputStream(), path);
            System.out.println("" + path);
            //getEstudiante().getEstudiante().setImagenC(path+event.getFile().getFileName()+".jpg");               
        } catch (IOException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void miperfil() {
        paginaActualE = "/Estudiante/PerfilEstudiante.xhtml";
    }

    public void g_propuesta() {
        try {
            if (!proacon.getProyecto().getCodigo().trim().equals("")) {
                paginaActualE = "/Estudiante/Gestor_Propuestas.xhtml";
            }
        } catch (java.lang.NullPointerException npe) {

        }
    }

    public void verAsignaturas(){
          paginaActualE = "/Estudiante/AsignaturasSeccion.xhtml";
    }
    
    public void g_proyecto() {
        if (!proacon.getProyecto().getCodigo().trim().equals("")) {
            paginaActualE = "/Estudiante/Gestor_Proyecto_Aula.xhtml";
        }
    }
    
      public void verResultados() {
        if (!proacon.getProyecto().getCodigo().trim().equals("")) {
            paginaActualE = "/Estudiante/ResultadosAprendizaje.xhtml";
        }
    }

    public void g_tutoria() {
        try{
        if (!proacon.getProyecto().getCodigo().trim().equals("")) {
            tutcon.setProyecto(proacon.getProyecto());
            tutcon.setIntegrante(proacon.getIntegrante());
            System.out.println("Proyecto:" + tutcon.getProyecto().getCodigo() + " " + "Integrante: " + tutcon.getIntegrante().getMatricula().getEstudiante().toString());
            paginaActualE = "/Estudiante/GestorTutorias.xhtml";
        }
        }catch(NullPointerException npe){
            FacesUtil.addErrorMessage("Estudiante no tiene proyecto de aula asignado");
        }
    }

    public void obtenerEstudiantes() {
        estudiantes = estser.consultarTodo(Estudiante.class);
    }

    public void obtenerEstudiantesNoMatriculados() {
        estudiantes = estser.consultarTodo(Estudiante.class);
    }

    public void obtenerEstudiante(Long id) {
        estudiante = estser.consultar(Estudiante.class, id);
    }

    /**
     * @return the estudiante
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    /**
     * @return the mpanelInscripcion
     */
    public boolean isMpanelInscripcion() {
        return mpanelInscripcion;
    }

    /**
     * @param mpanelInscripcion the mpanelInscripcion to set
     */
    public void setMpanelInscripcion(boolean mpanelInscripcion) {
        this.mpanelInscripcion = mpanelInscripcion;
    }

    /**
     * @return the paginaActualE
     */
    public String getPaginaActualE() {
        return paginaActualE;
    }

    /**
     * @param paginaActualE the paginaActualE to set
     */
    public void setPaginaActualE(String paginaActualE) {
        this.paginaActualE = paginaActualE;
    }

    /**
     * @return the estudiantes
     */
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    /**
     * @param estudiantes the estudiantes to set
     */
    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    /**
     * @return the iestudiante
     */
    public UploadedFile getIestudiante() {
        return iestudiante;
    }

    /**
     * @param iestudiante the iestudiante to set
     */
    public void setIestudiante(UploadedFile iestudiante) {
        this.iestudiante = iestudiante;
    }

    /**
     * @return the periodo
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @return the matcont
     */
    public MatriculaController getMatcont() {
        return matcont;
    }

    /**
     * @param matcont the matcont to set
     */
    public void setMatcont(MatriculaController matcont) {
        this.matcont = matcont;
    }

    /**
     * @return the proacon
     */
    public ProyectoAulaController getProacon() {
        return proacon;
    }

    /**
     * @param proacon the proacon to set
     */
    public void setProacon(ProyectoAulaController proacon) {
        this.proacon = proacon;
    }

    /**
     * @return the mpanelAItems
     */
    public boolean isMpanelAItems() {
        return mpanelAItems;
    }

    /**
     * @param mpanelAItems the mpanelAItems to set
     */
    public void setMpanelAItems(boolean mpanelAItems) {
        this.mpanelAItems = mpanelAItems;
    }

    /**
     * @return the mpanelItem
     */
    public boolean isMpanelItem() {
        return mpanelItem;
    }

    /**
     * @param mpanelItem the mpanelItem to set
     */
    public void setMpanelItem(boolean mpanelItem) {
        this.mpanelItem = mpanelItem;
    }

    /**
     * @return the tipicon
     */
    public Tipo_itemController getTipicon() {
        return tipicon;
    }

    /**
     * @param tipicon the tipicon to set
     */
    public void setTipicon(Tipo_itemController tipicon) {
        this.tipicon = tipicon;
    }

    /**
     * @return the fascon
     */
    public FasesController getFascon() {
        return fascon;
    }

    /**
     * @param fascon the fascon to set
     */
    public void setFascon(FasesController fascon) {
        this.fascon = fascon;
    }

    /**
     * @return the tipecon
     */
    public Tipo_EntregableController getTipecon() {
        return tipecon;
    }

    /**
     * @param tipecon the tipecon to set
     */
    public void setTipecon(Tipo_EntregableController tipecon) {
        this.tipecon = tipecon;
    }

    /**
     * @return the asigcon
     */
    public AsignaturaController getAsigcon() {
        return asigcon;
    }

    /**
     * @param asigcon the asigcon to set
     */
    public void setAsigcon(AsignaturaController asigcon) {
        this.asigcon = asigcon;
    }

    /**
     * @return the tutcon
     */
    public TutoriasController getTutcon() {
        return tutcon;
    }

    /**
     * @param tutcon the tutcon to set
     */
    public void setTutcon(TutoriasController tutcon) {
        this.tutcon = tutcon;
    }

    /**
     * @return the mpanelTutorias
     */
    public boolean isMpanelTutorias() {
        return mpanelTutorias;
    }

    /**
     * @param mpanelTutorias the mpanelTutorias to set
     */
    public void setMpanelTutorias(boolean mpanelTutorias) {
        this.mpanelTutorias = mpanelTutorias;
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



}
