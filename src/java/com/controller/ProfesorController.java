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
import com.entity.Tutoria;
import com.entity.UnidadCompetencia;
import com.services.LiderPAServices;
import com.services.ProfesorServices;
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
public class ProfesorController implements Serializable {

    //objetos de negocio
    private Profesor profesor = new Profesor();
    private Periodo periodo = new Periodo();//para almacenar el el periodo actual
    private LiderPA liderPa;// para conocer si el profesor es lider
    private ProgramaAcademico coordinadorPa;// para conocer si el profesor es coordinador
    ProfesorServices profser = new ProfesorServices();

    //Colecciones
    private List<Profesor> profesores = new LinkedList();
    private List<LiderPA> semestresLider = new LinkedList();//semestres en la cual un profesor es lider
    private List<Proyecto_Aula> proyectosSemestre = new LinkedList();

    //Servicios
    LiderPAServices lidpaser = new LiderPAServices();

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
    private boolean mpanelPCompetencias;//mostrar obciones del modulo de competencias
    private boolean mostPanelEvaluaciones;

    /**
     * Creates a new instance of ProfesorController
     */
    public ProfesorController() {
    }
    
    public void agregarAsignaturaTutoria(Asignatura a) {
        getTutcon().setAsignatura(a);
        getTutcon().consultarTutoriasXAsignaturaProfesor();
        mpanelTutorias = true;
    }
    
    public void agregarAsignaturaCompetencia(Asignatura a) {
        getCompcon().setUnidad(new UnidadCompetencia());
        getCompcon().setAsignatura(a);
        getCompcon().getUnidad().setAsignatura(a);
        getCompcon().consultarUnidadesCompetenciaAsignatura();
        tipecon.setAsignatura(a);
        tipecon.consultarTipos_Entregable();
    }
    
    public void pvolverAsignaturardTutoria() {
        mpanelTutorias = false;
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
    
    public void palmacenarTutoria() {
        tutcon.realizarTutoria();
        tutcon.consultarTutoriasXProfesor(profesor);
        tutcon.consultarTutoriasXAsignaturaProfesor();
        pvolverdeProgramar();
    }
    
    public void programarTutoria() {
        tutcon.programarTutoria();
        pvolverdeProgramar();
    }
    
    public void pvolverdeProgramar() {
        mpanelPTutoria = false;
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
    }
    
    public void obtenerTutoriasXProfesor() {
        System.out.println("Consultare las tutorias");
        tutcon.consultarTutoriasXProfesor(profesor);
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
    
    public void esLiderPA() {
        semestresLider = null;
        semestresLider = lidpaser.obtenersemestresLiderPAXProfesor(profesor, periodo);
        if (semestresLider.size() > 0) {
            mostPanelSemestres = true;

//            proacon.consultarProyectosXPeriodo(periodo);
//            proacon.obtenerIntegrantesXProyectos(periodo);
//            proacon.consultarProyectosXProfesorLider(profesor);
//            proacon.obtenerIntegrantesXProyectos(profesor);
        }
    }
    
    public void consultarFases(ProgramaAcademico pa) {
        fascon.obtenerFasesXPrograma(pa, periodo);
    }
    
    public void consultarProyectosAulaPeriodo() {
        proacon.setProyecto(new Proyecto_Aula());
        proacon.consultarProyectosXPeriodo(periodo);
        proacon.obtenerIntegrantesXProyectos(periodo);
    }
    
    public boolean habilitarLider() {
        if (semestresLider.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public void consultarMatriculasXPeriodo() {
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
    
}
