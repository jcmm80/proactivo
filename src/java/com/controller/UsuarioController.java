/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.EntityManagedSingleton;
import com.entity.Usuario;
import com.services.UsuarioServices;
import com.utilidades.ImageUtils;
import com.utilidades.MArchivos;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
public class UsuarioController implements Serializable {

    //Objetos de Negocio
    private Usuario usuario;

    //Colecciones
    private List<Usuario> usuarios;

    //Servicios
    UsuarioServices ususer = new UsuarioServices();

    //Variables de control
    private boolean mpanelLogin = true;
    private String paginaActual = "";
    private String paginaSU = "";
    private UploadedFile archivoDatos;
    private MArchivos marchivos = new MArchivos();

    //controladores asociados
    @ManagedProperty("#{coordinadorController}")
    private CoordinadorController coorcon;
    @ManagedProperty("#{programaController}")
    private ProgramaController procon;
    @ManagedProperty("#{periodoController}")
    private PeriodoController percon;
    @ManagedProperty("#{estudianteController}")
    private EstudianteController estcon;
    @ManagedProperty("#{profesorController}")
    private ProfesorController profcon;
    @ManagedProperty("#{semestreController}")
    private SemestreController semcon;
    @ManagedProperty("#{seccionController}")
    private SeccionController seccon;

    /**
     * Creates a new instance of UsuarioController
     */
    public UsuarioController() {
        usuario = new Usuario();
    }

    public void iniciar() {
        try {
            setUsuario(ususer.ingresar(getUsuario().getLogin(), getUsuario().getPassword()));
            if (!getUsuario().getIdentificacion().equals("")) {
                percon.establecerPeriodoActual();
                estcon.getTipicon().consultarTipos_Items();
                if (getUsuario().getTipo().equals("Coordinador")) {
                    coorcon.obtenerCoordinador(getUsuario().getId());
                    profcon.obtenerProfesores();
                    estcon.obtenerEstudiantes();
                    percon.obtenerPeriodos();
                    procon.consultarProgramasXCoordinador(coorcon.getCoordinador());
                    coorcon.consultarAreas();
                    seccon.setPeriodo(percon.getPeriodoActual());
                    coorcon.setPeriodo(percon.getPeriodoActual());
                    seccon.obtenerseccionesPeriodo();
                    semcon.obtenerSemestres();
                    coorcon.consultarAsignaturas(percon.getPeriodoActual());
                    coorcon.consultarMatriculasXPeriodo(percon.getPeriodoActual());
                    coorcon.consultarProyectosAulaPeriodo();
                    paginaActual = "/Coordinador/GUICoordinador.xhtml";

                }
                if (getUsuario().getTipo().equals("Estudiante")) {
                    estcon.obtenerEstudiante(getUsuario().getId());
                    estcon.setPeriodo(percon.getPeriodoActual());
                    estcon.consultarMatriculaEstudiante();
                    estcon.consultarProyectoXMatricula();
                    estcon.consultarFases();
                    estcon.consultarTiposEntregable();
                    estcon.consultarAsignaturas();
                    paginaActual = "/Estudiante/GUIEstudiante.xhtml";

                }
                if (getUsuario().getTipo().equals("Profesor")) {
                    profcon.obtenerPrtofesor(getUsuario().getId());
                    profcon.setPeriodo(percon.getPeriodoActual());
                    procon.obtenerProgramaCoordinadorPA(profcon.getProfesor());
                    profcon.consultarMatriculasXPeriodo();
                    profcon.consultarProfesores();//ojo porque consulta profesores
                    procon.consultarProgramas();
                    percon.obtenerPeriodos();
                    profcon.esLiderPA();
                    profcon.consultarProyectosAulaPeriodo();
                    profcon.obtenerAsignaturasXProfesor();
                    profcon.obtenerTutoriasXProfesor();
                    semcon.obtenerSemestres();
                    profcon.getEvacon().setSemestres(semcon.getSemestres());
                    paginaActual = "/Profesor/GUIProfesor.xhtml";

                }
                if (getUsuario().getTipo().equals("Super")) {
                    coorcon.consultarCoordinadores();
                    procon.consultarProgramas();
                    percon.obtenerPeriodos();
                    paginaActual = "/GUISuperUsuario.xhtml";

                }
                mpanelLogin = false;
            }
        } catch (java.lang.NullPointerException npe) {
            FacesUtil.addErrorMessage("Usuario, tipo de usuario o matricula inexistente");
        }

    }

    public void gregistroEstudiantes() {
        estcon.setMpanelInscripcion(true);
        paginaActual = "/Estudiante/InscripcionEstudiante.xhtml";
        mpanelLogin = false;
    }

    public void gcoordinadores() {
        paginaSU = "/General/GestorCoordinadores.xhtml";
    }

    public void gprogramas() {
        paginaSU = "/General/GestorProgramas.xhtml";
    }

    public void gperiodos() {
        paginaSU = "/General/GestorPeriodos.xhtml";
    }

    public void gcargamasiva() {
        paginaSU = "/General/CargaMasiva.xhtml";
    }

    public void salir() {
        paginaActual = "";
        mpanelLogin = true;       
        limpiarDatosEstudiante();
        limpiarDatosProfesor();
        limpiarDatosCoordinador();
        procon.setPrograma(null);
        percon.setPeriodo(null);
        usuario = new Usuario();
        EntityManagedSingleton.closeEntityManager();
    }

    public void limpiarDatosEstudiante() {
        estcon.setPaginaActualE("");
//        estcon.setEstudiante(null);
//        estcon.setPeriodo(null);
        estcon.setEstudiantes(null);
//        estcon.getAsigcon().setAsignatura(null);
        estcon.getAsigcon().setAsignaturas(null);
        estcon.getAsigcon().setAsignaturasAA(null);
//        estcon.getAsigcon().setPrograma(null);
//        estcon.getAsigcon().setPeriodo(null);
//        estcon.getAsigcon().setSeccion(null);
        estcon.getAsigcon().setSecciones(null);
//        estcon.getAsigcon().setSemestre(null);
//        estcon.getMatcont().setMatricula(null);
        estcon.getMatcont().setMatriculas(null);
        estcon.getMatcont().setMatriculasXSeccion(null);
//        estcon.getMatcont().setPeriodo(null);
//        estcon.getMatcont().setPrograma(null);
        estcon.getMatcont().setSecciones(null);
//        estcon.getMatcont().setSemestre(null);
//        estcon.getAvancon().setAvance(null);
        estcon.getAvancon().setEntregables(null);
        estcon.getAvancon().setAvances(null);
//        estcon.getAvancon().setEntregable(null);
        estcon.getAvancon().setIntegrante(null);
//        estcon.getAvancon().setProyecto(null);
        estcon.getFascon().setFases(null);
//        estcon.getProacon().setIntegrante(null);
        estcon.getProacon().setIntegrantes(null);
        estcon.getProacon().setItem(null);
        estcon.getProacon().setItenes(null);
        estcon.getProacon().setLider(null);
//        estcon.getProacon().setProyecto(null);
        estcon.getProacon().setProyectos(null);
        estcon.getProacon().setProyectosNoGuardados(null);
        paginaActual = "/Estudiante/GUIEstudiante.xhtml";
    }

    public void limpiarDatosCoordinador() {
        coorcon.setPaginaActualC("");
        coorcon.setCoordinador(null);
        coorcon.setCoordinadores(null);
//        coorcon.setArea(null);
        coorcon.setAreas(null);
//        coorcon.setSeccion(null);
        profcon.setProfesores(null);
//        estcon.setEstudiante(null);
        estcon.setEstudiantes(null);
//        estcon.setPeriodo(null);
        percon.setPeriodoActual(null);
        percon.setPeriodos(null);
//        percon.setPeriodo(null);
//        procon.setPrograma(null);
        procon.setProgramas(null);
//        seccon.setSeccion(null);
//        seccon.setPeriodo(null);
        seccon.setSecciones(null);
//        semcon.setSemestre(null);
        semcon.setSemestres(null);
    }

    public void limpiarDatosProfesor() {
        profcon.setPaginaActualP("");
//        profcon.setProfesor(null);
//        profcon.setPeriodo(null);
//        procon.setPrograma(null);
//        profcon.setLiderPa(null);
        profcon.setProfesores(null);
        procon.consultarProgramas();
        percon.setPeriodos(null);
        profcon.setSemestresLider(null);
        profcon.getProacon().setProyectos(null);
//        profcon.getProacon().setProyecto(null);
        profcon.getAsigcon().setAsignaturas(null);
        profcon.getAsigcon().setAsignaturasAA(null);
//        profcon.getAsigcon().setAsignatura(null);
        profcon.getAsigcon().setSecciones(null);
        profcon.getTutcon().liberarObjetos();
    }

    public void subirArchivoPlano() {
        try {
            System.out.println("  " + archivoDatos.getFileName());
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = servletContext.getRealPath("/imagenInicial.jpg").replace("imagenInicial.jpg", "Imagenes\\Archivos\\");
            System.out.println(path);
            ImageUtils.copyFile("datos.txt", archivoDatos.getInputStream(), path);
            marchivos.cargarArhivoEstudiante(path + "datos.txt");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void almacenamientoMasivo() {
        marchivos.almacenamiento();
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the mpanelLogin
     */
    public boolean isMpanelLogin() {
        return mpanelLogin;
    }

    /**
     * @param mpanelLogin the mpanelLogin to set
     */
    public void setMpanelLogin(boolean mpanelLogin) {
        this.mpanelLogin = mpanelLogin;
    }

    /**
     * @return the paginaActual
     */
    public String getPaginaActual() {
        return paginaActual;
    }

    /**
     * @param paginaActual the paginaActual to set
     */
    public void setPaginaActual(String paginaActual) {
        this.paginaActual = paginaActual;
    }

    /**
     * @return the usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * @return the paginaSU
     */
    public String getPaginaSU() {
        return paginaSU;
    }

    /**
     * @param paginaSU the paginaSU to set
     */
    public void setPaginaSU(String paginaSU) {
        this.paginaSU = paginaSU;
    }

    /**
     * @return the coorcon
     */
    public CoordinadorController getCoorcon() {
        return coorcon;
    }

    /**
     * @param coorcon the coorcon to set
     */
    public void setCoorcon(CoordinadorController coorcon) {
        this.coorcon = coorcon;
    }

    /**
     * @return the procon
     */
    public ProgramaController getProcon() {
        return procon;
    }

    /**
     * @param procon the procon to set
     */
    public void setProcon(ProgramaController procon) {
        this.procon = procon;
    }

    /**
     * @return the percon
     */
    public PeriodoController getPercon() {
        return percon;
    }

    /**
     * @param percon the percon to set
     */
    public void setPercon(PeriodoController percon) {
        this.percon = percon;
    }

    /**
     * @return the estcon
     */
    public EstudianteController getEstcon() {
        return estcon;
    }

    /**
     * @param estcon the estcon to set
     */
    public void setEstcon(EstudianteController estcon) {
        this.estcon = estcon;
    }

    /**
     * @return the profcon
     */
    public ProfesorController getProfcon() {
        return profcon;
    }

    /**
     * @param profcon the profcon to set
     */
    public void setProfcon(ProfesorController profcon) {
        this.profcon = profcon;
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
     * @return the seccon
     */
    public SeccionController getSeccon() {
        return seccon;
    }

    /**
     * @param seccon the seccon to set
     */
    public void setSeccon(SeccionController seccon) {
        this.seccon = seccon;
    }

    /**
     * @return the archivoDatos
     */
    public UploadedFile getArchivoDatos() {
        return archivoDatos;
    }

    /**
     * @param archivoDatos the archivoDatos to set
     */
    public void setArchivoDatos(UploadedFile archivoDatos) {
        this.archivoDatos = archivoDatos;
    }

    /**
     * @return the marchivos
     */
    public MArchivos getMarchivos() {
        return marchivos;
    }

    /**
     * @param marchivos the marchivos to set
     */
    public void setMarchivos(MArchivos marchivos) {
        this.marchivos = marchivos;
    }

}
