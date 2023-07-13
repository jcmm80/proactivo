/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Integrante;
import com.entity.LiderPA;
import com.entity.Periodo;
import com.entity.Profesor;
import com.entity.ProgramaAcademico;
import com.entity.Proyecto_Aula;
import com.entity.Seccion;
import com.entity.Semestre;
import com.entity.Tutoria;
import com.entity.UnidadCompetencia;
import com.services.LiderPAServices;
import com.services.ProfesorServices;
import com.services.SeccionServices;
import com.utilidades.ImageUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
public class ProfesorController implements Serializable {

    //objetos de negocio
    private Profesor profesor = new Profesor();
    private Periodo periodo = new Periodo();//para almacenar el el periodo actual
    private LiderPA liderPa;// para conocer si el profesor es lider
    private ProgramaAcademico coordinadorPa;// para conocer si el profesor es coordinador
    private ProgramaAcademico programa = new ProgramaAcademico();
    private Seccion seccion = new Seccion();
    //Colecciones
    private List<Profesor> profesores = new LinkedList();
    private List<LiderPA> semestresLider = new LinkedList();//semestres en la cual un profesor es lider
    private List<LiderPA> lideresXseccion = new LinkedList();//lideres con sus secciones en el periodo
    private List<LiderPA> lideresXseccionPrograma=new LinkedList();//lideres del programa por secciones
    
    private List<Proyecto_Aula> proyectosSemestre = new LinkedList();
    private List<Seccion> secciones = new LinkedList();//para consultar resultados cuando es coordinador de proyectos

    //Servicios
    LiderPAServices lidpaser = new LiderPAServices();
    ProfesorServices profser = new ProfesorServices();
    private SeccionServices secser = new SeccionServices();

    //controladores
    @ManagedProperty("#{proyectoAulaController}")
    private ProyectoAulaController proacon;
    @ManagedProperty("#{matriculaController}")
    private MatriculaController matcont;
    @ManagedProperty("#{asignaturaController}")
    private AsignaturaController asigcon;
    @ManagedProperty("#{fasesController}")
    private FasesController fascon;
    @ManagedProperty("#{tutoriasController}")
    private TutoriasController tutcon;
    @ManagedProperty("#{competenciasController}")
    private CompetenciasController compcon;
    @ManagedProperty("#{tipo_EntregableController}")
    private Tipo_EntregableController tipecon;
    @ManagedProperty("#{evaluacionController}")
    private EvaluacionController evacon;
    @ManagedProperty("#{programaController}")
    private ProgramaController progcon;
    @ManagedProperty("#{avanceController}")
    private AvanceController avancon;
    @ManagedProperty("#{semestreController}")
    private SemestreController semcon;
    @ManagedProperty("#{reportesController}")
    private ReportesController repcon;

    //variables de control
    private String paginaActualP = "";
    private String paginaTutoria = "";
    private String paginaActualC = "";//pagina actual para competencias
    private UploadedFile iprofesor;
    private boolean mostPanelSemestres;//mostrar los semestres en la cual un profesor es lider
    private boolean mostPanelProyectoAula;//para mostrar la informacion del proyecto seleccionado
    private boolean mostPAsignatura;//para mostrar la asignatura seleccionada por el profesor
    private boolean mpanelTutorias;
    private boolean mpanelPTutoria;
    private boolean mpanelPCTutoria;//mostrar panel para crear tutorias
    private boolean mpanelPCompetencias;//mostrar obciones del modulo de competencias
    private boolean mostPanelEvaluaciones;
    private int activeIRes = 0;
    private int activeIproy = 0;
    private int activeItut = 0;

    /**
     * Creates a new instance of ProfesorController
     */
    public ProfesorController() {
    }

    public void seleccionarSemestrepTResultados(Semestre s) {

        setSecciones(getSecser().obtenerSeccionesXSemestre_Periodo_Programa(s, getPrograma(), getPeriodo()));

    }

    public void seleccionarProfesor(Profesor p){
        setProfesor(p);
        System.out.println(""+profesor.toString());
    }
    
    public void seleccionarSemestreVProyectos(Semestre s) {
        setSecciones(getSecser().obtenerSeccionesXSemestre_Periodo_Programa(s, getPrograma(), getPeriodo()));
        getRepcon().createBarModel();
        getRepcon().createBarModelSemestre(s);

    }

    public void obtenerProyectosSeccion(Seccion s) {
        proyectosXSeccion(s);
        activeIproy = 1;
    }

    public void consultarProyectoR(Proyecto_Aula pa) {
        proacon.setProyecto(pa);
        activeIproy = 2;
    }

    public void obtenerResultadosSeccion(Seccion s) {
        matcont.setSeccion(s);
        matcont.obtenerMatriculasXSeccion(s);
        activeIRes = 1;
    }

    public void verResultados(Integrante i) {
        matcont.seleccionarMatricula(i);
        activeIRes = 2;
    }

    public void volverEstudiantesSeccion() {
        activeIRes = 1;
    }

    public void agregarAsignaturaTutoria(Asignatura a) {
        getTutcon().setAsignatura(a);
        getTutcon().consultarTutoriasXAsignaturaProfesor();
        proyectosXSeccion(a.getSeccion());
        mpanelTutorias = true;
    }

    public void agregarAsignaturaCompetencia(Asignatura a) {
        getCompcon().setUnidad(new UnidadCompetencia());
        getCompcon().setAsignatura(a);
        getCompcon().getUnidad().setAsignatura(a);
        getCompcon().consultarUnidadesCompetenciaAsignatura();
        getCompcon().getCricon().consultarCriteriosEvaluacionAsignatura(a);
        tipecon.setAsignatura(a);
        tipecon.consultarTipos_Entregable();
    }

    public void pvolverAsignaturardTutoria() {
        mpanelTutorias = false;
    }

    public void crearTutoria() {
        mpanelPCTutoria = true;
        paginaTutoria = "Tutorias/CrearTutoria.xhtml";
    }

    public void volverTutorias() {
        mpanelPCTutoria = false;
    }

    public void pprogramarTutoria(Tutoria t) {
        mpanelPTutoria = true;
        tutcon.setTutoria(t);
        paginaTutoria = "Tutorias/ProgramarTutoria.xhtml";
    }

    public void prealizarTutoria(Tutoria t) {
        mpanelPTutoria = true;
        tutcon.setTutoria(t);
        tutcon.armarAsistentes();
        paginaTutoria = "Tutorias/RealizarTutoria.xhtml";
    }

    public void pcrearTutoria(Proyecto_Aula proyecto) {
        mpanelPTutoria = true;
        Tutoria tutoria = new Tutoria();
        tutoria.setProyecto(proyecto);
        tutoria.setAsignatura(tutcon.getAsignatura());
        tutcon.setTutoria(tutoria);
        tutcon.armarAsistentes();
        paginaTutoria = "Tutorias/RealizarTutoria.xhtml";
    }

    public void palmacenarTutoria() {
        tutcon.realizarTutoria();
        try{
        if(programa.getId() > 0){
            consultarTutoriasPeriodo();
        }else{
            tutcon.consultarTutoriasXProfesor(profesor);
        }
        }catch(java.lang.NullPointerException npe){
             consultarTutoriasPeriodo();
        }
        //si el profesor es coordinador no esta funcionado
        tutcon.consultarTutoriasXAsignaturaProfesor();
        pvolverdeProgramar();
    }

    public void programarTutoria() {
        tutcon.programarTutoria();
        pvolverdeProgramar();
    }

    public void pvolverdeProgramar() {
        mpanelPTutoria = false;
        mpanelPCTutoria = false;
    }

    public void seleccionarAsignatura(Asignatura a) {
        asigcon.setAsignatura(a);
        proyectosXSeccion(a.getSeccion());
        consultarFases(a.getSeccion().getPrograma());
        obtenerAvances(a.getSeccion());
        mostPAsignatura = true;
        mostPanelProyectoAula = true;
        evacon.obtenerEvaluacionXSeccion(a.getSeccion());
        evacon.obtenerValoracionesAsignatura(a);
    }

    public void obtenerAvances(Seccion s) {
        avancon.consultarAvancesXSeccion(s);// la cambie por una instanciada desde proacon
    }

    public void volverPanelAsignaturas() {
        asigcon.setAsignatura(new Asignatura());
        mostPAsignatura = false;
    }

    public void volverProyectos() {
        mostPanelProyectoAula = true;
    }

    public void consultarProfesores() {
        setProfesores(profser.consultarTodo(Profesor.class));
    }

    public void limpiarDatos() {
        paginaActualP = "";
        asigcon.limpiarDatos();
        compcon.limpiarDatos();
        tipecon.limpiarDatos();
        profesor = null;
    }

    public void seleccionarLider(LiderPA lider) {
        liderPa = lider;
        proacon.setLider(lider);
        proyectosXSeccion(lider.getSeccion());
        matcont.obtenerMatriculasXSeccion(lider.getSeccion());
        avancon.consultarAvancesXSeccion(lider.getSeccion());//lo coloque porque no hay avances de proyectos
        mostPanelSemestres = false;
    }

    public void seleccionarSeccionLider(LiderPA lider) {
        liderPa = lider;
        evacon.consultarDimensionesProgramaAndPeriodoAndSemestre(liderPa);
        evacon.consultarCriteriosSeccion(liderPa.getSeccion());
        asigcon.obtenerAsignaturasXSeccion(liderPa.getSeccion());
        evacon.setAsignaturasSeccion(asigcon.getAsignaturas());
        evacon.prepararEvaluacion(liderPa.getSeccion());
        mostPanelEvaluaciones = true;
    }

    public void volverSeccionesdesdeEvaluacion() {
        mostPanelEvaluaciones = false;
    }

    public void volverSemestres() {
        mostPanelSemestres = true;
    }

    public void obtenerAsignaturasXProfesor() {
        asigcon.obtenerAsignaturasXProfesor(periodo, profesor);
        compcon.consultarUnidadesCOmpetencia(profesor);
        compcon.consultarTiposCompetencias();
        tipecon.consultarTipos_Entregable(profesor);
        compcon.consultarCompetenciasProfesor(profesor);
        compcon.consultarCriteriosGlobalesProfesor(profesor);
    }

    public void obtenerTutoriasXProfesor() {
        System.out.println("Consultare las tutorias");
        tutcon.consultarTutoriasXProfesor(profesor);
    }

    public void consultarTutoriasPeriodo() {
        tutcon.obtenerTutoriasXPeriodo(periodo);
        repcon.setSemestres(semcon.getSemestres());
        repcon.setTutorias(tutcon.getTutorias());
    }

    public void proyectosXSeccion(Seccion s) {
        proyectosSemestre = new LinkedList();
//        System.out.println("" + proacon.getProyectos().size());
        for (Proyecto_Aula p : proacon.getProyectos()) {
            if (p.getSeccion().getId().equals(s.getId())) {
                proyectosSemestre.add(p);
            }
        }
    }

    public void evaluarProyecto(Proyecto_Aula pa) {
        proacon.setProyecto(pa);
        evacon.setProyectoAula(pa);
        evacon.setAsignaturaEvaluar(asigcon.getAsignatura());
        evacon.prepararCriteriosParaEvaluar();
        paginaActualP = "/Profesor/Evaluacion/EvaluarProyectos.xhtml";
    }

    public void consultarProyecto(Proyecto_Aula pa) {
        proacon.setProyecto(pa);
        proacon.getAvancon().obtenerAvancesProyecto(pa);
//        System.out.println("" + proacon.getProyecto().validarProyectoParaAprobar());
        mostPanelProyectoAula = false;
    }

    
    public void obtenerLideresXseccionPeriodo(){
        lideresXseccion=lidpaser.obtenerSeccionesLideradasPeriodo(periodo);
    }
    
    public void obtenerLideresXPrograma(ProgramaAcademico prog){
        lideresXseccionPrograma=new LinkedList();
        for(LiderPA lpa:lideresXseccion){
            if(lpa.getSeccion().getPrograma().getId().equals(prog.getId())){
                lideresXseccionPrograma.add(lpa);
            }
        }
    }
    
    public List<LiderPA> seccionesXLider(Profesor pro){
        List<LiderPA> lideres=new LinkedList();
        for(LiderPA lpa:lideresXseccion){
            if(lpa.getProfesor().getId().equals(pro.getId())){
                lideres.add(lpa);
            }
        }return lideres;
    }
    
    
    public void esLiderPA() {
        semestresLider = null;
        semestresLider=seccionesXLider(profesor);
//        semestresLider = lidpaser.obtenersemestresLiderPAXProfesor(profesor, periodo);
        if (semestresLider.size() > 0) {
            mostPanelSemestres = true;
            System.out.println("El profesor el lider");
          
//            proacon.consultarProyectosXPeriodo(periodo);
//            proacon.obtenerIntegrantesXProyectos(periodo);
//            proacon.consultarProyectosXProfesorLider(profesor);
//            proacon.obtenerIntegrantesXProyectos(profesor);
        }
    }

    public void consultarFases(ProgramaAcademico pa) {
        fascon.setPeriodo(periodo);
        fascon.setPrograma(programa);
        fascon.obtenerFasesXPrograma(pa, periodo);
    }

    public void consultarProyectosAulaPeriodo() {
        proacon.setProyecto(new Proyecto_Aula());
        proacon.consultarProyectosXPeriodo(periodo);
        proacon.obtenerIntegrantesXProyectos(periodo);
        evacon.obtenerValoracionesdelPeriodo(periodo);
        repcon.setProyectos(getProacon().getProyectos());
        repcon.setSemestres(getSemcon().getSemestres());

    }

    public void obtenerTutoriasSeccion(Seccion s) {
        seccion = s;
        repcon.createBarTutoriasSeccion(s);
        tutcon.setTutoriasAsignatura(new LinkedList());
        asigcon.obtenerAsignaturasXSeccion(s);
        //  proyectosXSeccion(seccion);
        activeItut = 1;
    }

    public void seleccionarSemestrepTRTutorias(Semestre s) {
        setSecciones(secser.obtenerSeccionesXSemestre_Periodo_Programa(s, getPrograma(), getPeriodo()));
        repcon.createBarTutorias();
        repcon.createBarTutoriasSemestre(s);
    }

    public boolean habilitarLider() {
        if (semestresLider.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void consultarMatriculasXPeriodo() {
//        matcont.consultarMatriculasXPeriodoYPrograma(programa, periodo);
        matcont.consultarEstudiantesMatriculadosXPeriodo(periodo);
    }

    public void registrarprofesor() {
        profesor.setTipo("Profesor");
        profesor.setEstado("Activo");
        if (profesor.validarUsuario()) {
            profser.modificar(profesor);
            profesor = new Profesor();
            obtenerProfesores();
        }
    }

    public void actualizarprofesor() {
        profesor.setEstado("Activo");
        if (profesor.validarUsuario()) {
            profesor = profser.modificar(profesor);
        }
    }

    public void guardarProyectoAula() {
        proacon.getProyecto().setSeccion(liderPa.getSeccion());
        proacon.crearPA();
        proyectosXSeccion(liderPa.getSeccion());
    }

    public void aprobarProyectoAula() {
        if (proacon.getProyecto().validarProyectoParaAprobar()) {
            proacon.getProyecto().setFecha_aprobacion(new Date());
            proacon.aprobarPA();
        }
    }

    public void aplazarProyectoAula() {
        proacon.getProyecto().setFecha_aprobacion(new Date());
        proacon.aplazarPA();
    }

    public void desvincularIntegrante(Integrante inte, Proyecto_Aula pa) {
        proacon.eliminarIntegrante(inte, pa);
        matcont.consultarEstudiantesMatriculadosXPeriodo(periodo);
    }

    public void eliminarProyectoAula(Proyecto_Aula pa) {
        if (!proacon.tieneProcesos(pa)) {
            proacon.eliminarProyecto(pa);
            matcont.consultarEstudiantesMatriculadosXPeriodo(periodo);
            proacon.consultarProyectosXProfesorLider(profesor);
            proyectosXSeccion(liderPa.getSeccion());
            proacon.setIntegrantesseleccionados(new LinkedList());
            proacon.setProyecto(new Proyecto_Aula());
        }
    }

    public void subirImagenProfesor() {
        try {
//               File destFile= new File(event.getFile().getFileName());           
            System.out.println("" + iprofesor.getFileName());
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = servletContext.getRealPath("/imagenInicial.jpg").replace("imagenInicial.jpg", "Imagenes\\Perfiles\\");
            ImageUtils.copyFile(profesor.getId() + ".jpg", iprofesor.getInputStream(), path);
            System.out.println("" + path);
            //getEstudiante().getEstudiante().setImagenC(path+event.getFile().getFileName()+".jpg");               
        } catch (IOException ex) {
            Logger.getLogger(ProfesorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gtipo_Entregable() {
        paginaActualP = "/Profesor/GestorTipo_Entregable.xhtml";
    }

    public void gtipo_Item() {
        paginaActualP = "/Profesor/GestorTipo_Item.xhtml";
    }

    public void ggrupos() {
        mostPanelSemestres = true;
        paginaActualP = "/Profesor/GestorGrupos.xhtml";
    }

    public void gproyectosaula() {
        mostPanelSemestres = true;
        mostPanelProyectoAula = true;
        paginaActualP = "/Profesor/GUIProyectosAula.xhtml";
    }

    public void g_tutoria() {
        mpanelTutorias = false;
        paginaActualP = "/Profesor/GestorTutorias.xhtml";
    }

    public void asignaturasProfesor() {
        mostPAsignatura = false;
        paginaActualP = "/Profesor/AsignaturasProfesor.xhtml";
    }

    public void gCompetenciasProfesor() {
        paginaActualP = "/Profesor/Competencias/GestorUnidadesCompetencia.xhtml";
    }

//    public void gUnidadesCompetencia() {
//        paginaActualC = "/Profesor/Competencias/GestorUnidadesCompetencia.xhtml";
//    }
//    
    public void gCompetencias() {
        paginaActualC = "/Profesor/Competencias/GestorCompetencias.xhtml";
    }

    public void gTiposCompetencia() {
        paginaActualC = "/Profesor/Competencias/GestorTiposCompetencias.xhtml";
    }

    public void gproflider() {
        paginaActualP = "/Profesor/GestorProfesorLider.xhtml";
    }

    public void gDimensiones() {
        evacon.setPeriodo(periodo);
        evacon.setPrograma(progcon.getPrograma());
        evacon.getDimencion().setPeriodo(periodo);
        evacon.getDimencion().setPrograma(progcon.getPrograma());
        evacon.setSemestresSeleccionados(new LinkedList());
        evacon.setDimenciones(new LinkedList());
        evacon.setMaxPor(100);
        evacon.consultarDimensionesProgramaAndPeriodo();
        paginaActualP = "/Profesor/Evaluacion/GestorDimensiones.xhtml";
    }

    public void gNucleos() {
        paginaActualP = "/Profesor/GestorNucleos.xhtml";
    }

    public void gRproyectos() {
        activeIproy = 0;
        paginaActualP = "/Profesor/ProyectosAulaPrograma.xhtml";
    }

    public void gResultadosA() {
        activeIRes = 0;
        paginaActualP = "/Profesor/ResultadosAprendizaje.xhtml";
    }

    public void gRtutorias() {
        activeItut = 0;
        paginaActualP = "/Profesor/TutoriasPrograma.xhtml";
    }

    public void gFases() {

        paginaActualP = "/Profesor/GestorFases.xhtml";
    }

    public void gEvaluaciones() {
        mostPanelEvaluaciones = false;
        evacon.setIndTavEvaluacion(0);
        evacon.setCriteriosevaluacion(new LinkedList());
        paginaActualP = "/Profesor/Evaluacion/GestorEvaluacion.xhtml";
    }

    public void miperfil() {
        paginaActualP = "/Profesor/PerfilProfesor.xhtml";
    }

//    public void probarPath(){
//        System.out.println(""+profesor.imagenPerfil());
//    }
    public void obtenerPrtofesor(Long id) {
        profesor = profser.consultar(Profesor.class, id);
    }

    public void obtenerProfesores() {
        profesores = profser.consultarTodo(Profesor.class);
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
     * @return the profesores
     */
    public List<Profesor> getProfesores() {
        return profesores;
    }

    /**
     * @param profesores the profesores to set
     */
    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }

    /**
     * @return the paginaActualP
     */
    public String getPaginaActualP() {
        return paginaActualP;
    }

    /**
     * @param paginaActualP the paginaActualP to set
     */
    public void setPaginaActualP(String paginaActualP) {
        this.paginaActualP = paginaActualP;
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
     * @return the liderPa
     */
    public LiderPA getLiderPa() {
        return liderPa;
    }

    /**
     * @return the coordinadorPa
     */
    public ProgramaAcademico getcoordinadorPa() {
        return coordinadorPa;
    }

    /**
     * @param liderPa the liderPa to set
     */
    public void setLiderPa(LiderPA liderPa) {
        this.liderPa = liderPa;
    }

    public ProgramaAcademico getCoordinadorPa() {
        return coordinadorPa;
    }

    public void setCoordinadorPa(ProgramaAcademico coordinadorPa) {
        this.coordinadorPa = coordinadorPa;
    }

    /**
     * @return the semestresLider
     */
    public List<LiderPA> getSemestresLider() {
        return semestresLider;
    }

    /**
     * @param semestresLider the semestresLider to set
     */
    public void setSemestresLider(List<LiderPA> semestresLider) {
        this.semestresLider = semestresLider;
    }

    /**
     * @return the mostPanelSemestres
     */
    public boolean isMostPanelSemestres() {
        return mostPanelSemestres;
    }

    /**
     * @param mostPanelSemestres the mostPanelSemestres to set
     */
    public void setMostPanelSemestres(boolean mostPanelSemestres) {
        this.mostPanelSemestres = mostPanelSemestres;
    }

    /**
     * @return the proyectosSemestre
     */
    public List<Proyecto_Aula> getProyectosSemestre() {
        return proyectosSemestre;
    }

    /**
     * @param proyectosSemestre the proyectosSemestre to set
     */
    public void setProyectosSemestre(List<Proyecto_Aula> proyectosSemestre) {
        this.proyectosSemestre = proyectosSemestre;
    }

    /**
     * @return the mostPanelProyectoAula
     */
    public boolean isMostPanelProyectoAula() {
        return mostPanelProyectoAula;
    }

    /**
     * @param mostPanelProyectoAula the mostPanelProyectoAula to set
     */
    public void setMostPanelProyectoAula(boolean mostPanelProyectoAula) {
        this.mostPanelProyectoAula = mostPanelProyectoAula;
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
     * @return the mostPAsignatura
     */
    public boolean isMostPAsignatura() {
        return mostPAsignatura;
    }

    /**
     * @param mostPAsignatura the mostPAsignatura to set
     */
    public void setMostPAsignatura(boolean mostPAsignatura) {
        this.mostPAsignatura = mostPAsignatura;
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
     * @return the mpanelPTutoria
     */
    public boolean isMpanelPTutoria() {
        return mpanelPTutoria;
    }

    /**
     * @param mpanelPTutoria the mpanelPTutoria to set
     */
    public void setMpanelPTutoria(boolean mpanelPTutoria) {
        this.mpanelPTutoria = mpanelPTutoria;
    }

    /**
     * @return the paginaTutoria
     */
    public String getPaginaTutoria() {
        return paginaTutoria;
    }

    /**
     * @param paginaTutoria the paginaTutoria to set
     */
    public void setPaginaTutoria(String paginaTutoria) {
        this.paginaTutoria = paginaTutoria;
    }

    /**
     * @return the mpanelPCompetencias
     */
    public boolean isMpanelPCompetencias() {
        return mpanelPCompetencias;
    }

    /**
     * @param mpanelPCompetencias the mpanelPCompetencias to set
     */
    public void setMpanelPCompetencias(boolean mpanelPCompetencias) {
        this.mpanelPCompetencias = mpanelPCompetencias;
    }

    /**
     * @return the paginaActualC
     */
    public String getPaginaActualC() {
        return paginaActualC;
    }

    /**
     * @param paginaActualC the paginaActualC to set
     */
    public void setPaginaActualC(String paginaActualC) {
        this.paginaActualC = paginaActualC;
    }

    /**
     * @return the compcon
     */
    public CompetenciasController getCompcon() {
        return compcon;
    }

    /**
     * @param compcon the compcon to set
     */
    public void setCompcon(CompetenciasController compcon) {
        this.compcon = compcon;
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
     * @return the evacon
     */
    public EvaluacionController getEvacon() {
        return evacon;
    }

    /**
     * @param evacon the evacon to set
     */
    public void setEvacon(EvaluacionController evacon) {
        this.evacon = evacon;
    }

    /**
     * @return the progcon
     */
    public ProgramaController getProgcon() {
        return progcon;
    }

    /**
     * @param progcon the progcon to set
     */
    public void setProgcon(ProgramaController progcon) {
        this.progcon = progcon;
    }

    /**
     * @return the mostPanelEvaluaciones
     */
    public boolean isMostPanelEvaluaciones() {
        return mostPanelEvaluaciones;
    }

    /**
     * @param mostPanelEvaluaciones the mostPanelEvaluaciones to set
     */
    public void setMostPanelEvaluaciones(boolean mostPanelEvaluaciones) {
        this.mostPanelEvaluaciones = mostPanelEvaluaciones;
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
     * @return the programa
     */
    public ProgramaAcademico getPrograma() {
        return programa;
    }

    /**
     * @param programa the programa to set
     */
    public void setPrograma(ProgramaAcademico programa) {
        this.programa = programa;
    }

    /**
     * @return the secciones
     */
    public List<Seccion> getSecciones() {
        return secciones;
    }

    /**
     * @param secciones the secciones to set
     */
    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    /**
     * @return the activeIRes
     */
    public int getActiveIRes() {
        return activeIRes;
    }

    /**
     * @param activeIRes the activeIRes to set
     */
    public void setActiveIRes(int activeIRes) {
        this.activeIRes = activeIRes;
    }

    /**
     * @return the activeIproy
     */
    public int getActiveIproy() {
        return activeIproy;
    }

    /**
     * @param activeIproy the activeIproy to set
     */
    public void setActiveIproy(int activeIproy) {
        this.activeIproy = activeIproy;
    }

    /**
     * @return the activeItut
     */
    public int getActiveItut() {
        return activeItut;
    }

    /**
     * @param activeItut the activeItut to set
     */
    public void setActiveItut(int activeItut) {
        this.activeItut = activeItut;
    }

    /**
     * @return the repcon
     */
    public ReportesController getRepcon() {
        return repcon;
    }

    /**
     * @param repcon the repcon to set
     */
    public void setRepcon(ReportesController repcon) {
        this.repcon = repcon;
    }

    /**
     * @return the secser
     */
    public SeccionServices getSecser() {
        return secser;
    }

    /**
     * @param secser the secser to set
     */
    public void setSecser(SeccionServices secser) {
        this.secser = secser;
    }

    /**
     * @return the seccion
     */
    public Seccion getSeccion() {
        return seccion;
    }

    /**
     * @param seccion the seccion to set
     */
    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    /**
     * @return the semcon
     */
    public SemestreController getSemcon() {
        return semcon;
    }

    /**
     * @param semcon the semcon to set
     */
    public void setSemcon(SemestreController semcon) {
        this.semcon = semcon;
    }

    /**
     * @return the mpanelPCTutoria
     */
    public boolean isMpanelPCTutoria() {
        return mpanelPCTutoria;
    }

    /**
     * @param mpanelPCTutoria the mpanelPCTutoria to set
     */
    public void setMpanelPCTutoria(boolean mpanelPCTutoria) {
        this.mpanelPCTutoria = mpanelPCTutoria;
    }

    /**
     * @return the lideresXseccion
     */
    public List<LiderPA> getLideresXseccion() {
        return lideresXseccion;
    }

    /**
     * @param lideresXseccion the lideresXseccion to set
     */
    public void setLideresXseccion(List<LiderPA> lideresXseccion) {
        this.lideresXseccion = lideresXseccion;
    }

    /**
     * @return the lideresXseccionPrograma
     */
    public List<LiderPA> getLideresXseccionPrograma() {
        return lideresXseccionPrograma;
    }

    /**
     * @param lideresXseccionPrograma the lideresXseccionPrograma to set
     */
    public void setLideresXseccionPrograma(List<LiderPA> lideresXseccionPrograma) {
        this.lideresXseccionPrograma = lideresXseccionPrograma;
    }


}
