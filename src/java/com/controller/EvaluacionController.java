/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Asignatura;
import com.entity.Competencia;
import com.entity.Criterio;
import com.entity.CriterioEvaluacion;
import com.entity.Dimension;
import com.entity.Evaluacion;
import com.entity.Integrante;
import com.entity.LiderPA;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Proyecto_Aula;
import com.entity.Seccion;
import com.entity.Semestre;
import com.entity.TipoCompetencia;
import com.entity.Tipo_Entregable;
import com.entity.UnidadCompetencia;
import com.entity.Valoracion;
import com.services.CompetenciaServices;
import com.services.CriterioEvaluacionServices;
import com.services.CriterioServices;
import com.services.DimensionServices;
import com.services.EvaluacionServices;
import com.services.Tipo_EntregableServices;
import com.services.UnidadCompetenciaServices;
import com.services.ValoracionServices;
import com.utilidades.ResultadosAsignatura;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author JCMM
 */
@ManagedBean
@SessionScoped
public class EvaluacionController implements Serializable {

    private Periodo periodo = new Periodo();
    private ProgramaAcademico programa = new ProgramaAcademico();
    private Evaluacion evaluacion;
    private Dimension dimencion = new Dimension();
    private Semestre semestre;
    private Seccion seccion;
    private Proyecto_Aula proyectoAula;
    private Asignatura asignaturaEvaluar;

    private List<CriterioEvaluacion> criteriosevaluacion;
    private List<CriterioEvaluacion> criteriosevaluacionParticulares;
    private List<CriterioEvaluacion> criteriosevaluacionGlobales;
    private List<Dimension> dimenciones;
    private List<Dimension> dimencionesPeriodo;
    private List<Dimension> dimencionesPeriodoNoGlobales;
    private List<Dimension> dimencionesPeriodoGlobales;
    private List<Semestre> semestres;
    private List<Semestre> semestresSeleccionados;
    private List<Evaluacion> evaluaciones;
    private List<Criterio> criteriosSeccion;
    private List<Asignatura> asignaturasSeccion;
    private List<Asignatura> asignaturasEvaluacion;
    private List<Valoracion> valoraciones;
    private List<Valoracion> valoracionesAsignatura;

    Hashtable<Long, List<Valoracion>> valoracionesXdimensiones;

    CriterioServices criser = new CriterioServices();
    EvaluacionServices evaser = new EvaluacionServices();
    CriterioEvaluacionServices crievser = new CriterioEvaluacionServices();
    DimensionServices dimser = new DimensionServices();
    UnidadCompetenciaServices uniser = new UnidadCompetenciaServices();
    CompetenciaServices comser = new CompetenciaServices();
    Tipo_EntregableServices teser = new Tipo_EntregableServices();
    ValoracionServices valser = new ValoracionServices();

    private String semestresAgregados = "";
    private int maxPor = 100;
    private boolean mostPanelMod = false;
    private int indTavEvaluacion = 0;
    private boolean habilitarReprogramacion = false;

    /**
     * Creates a new instance of EvaluacionController
     */
    public EvaluacionController() {
    }

    public void generarValoracionesIntegrantes() {

        for (int i = 0; i < proyectoAula.getIntegrantes().size(); i++) {

        }
    }

    public void actualizarDimensiones() {
        dimser.elimimarDimensiones(semestre, periodo, programa);
        if (semestre != null) {
            if (dimenciones.size() > 0) {
                if (maxPor == 0) {
                    for (Dimension d : dimenciones) {
                        d.setSemestre(semestre);
                        dimser.crear(d);
                    }
                    dimenciones = null;
                    semestresSeleccionados = null;
                    semestre = null;
                    mostPanelMod = false;
                    maxPor = 100;
                    consultarDimensionesProgramaAndPeriodo();
                }
            }
        }
    }

    public void seleccionarDimension(Dimension dim) {
        dimencion = dim;
//        asignaturasEvaluacion = new LinkedList();
    }

    public void seleccionarAsignatura(Asignatura a) {
        if (!asignaturaSeleccionada(a)) {
            asignaturasEvaluacion.add(a);
        }
    }

    public void quitarAsignatura(Asignatura a) {
        if (asignaturaSeleccionada(a)) {
            asignaturasEvaluacion.remove(a);
        }
    }

    public boolean asignaturaSeleccionada(Asignatura a) {
        boolean asigs = false;
        if (asignaturasEvaluacion != null) {
            for (Asignatura asi : asignaturasEvaluacion) {
                if (asi.getId().equals(a.getId())) {
                    asigs = true;
                }
            }
        }
        return asigs;
    }

    public void seleccionarCriterio(Criterio criterio) {
        if (criteriosevaluacionParticulares.size() <= 0) {
            criteriosevaluacionParticulares = new LinkedList();
        }
        if (!criterioEstaSeleccionado(criterio)) {
            if (dimencion.getId() != null) {
                CriterioEvaluacion crie = new CriterioEvaluacion();
                crie.setCriterio(criterio);
                crie.setDimension(dimencion);
                criteriosevaluacionParticulares.add(crie);
                criteriosSeccion.remove(criterio);
            } else {
                FacesUtil.addErrorMessage("Debes seleccionar la dimension para clasificar el criterio");
            }
        }
    }

    public void quitarCriterio(Criterio criterio) {
        for (int i = 0; i < criteriosevaluacionParticulares.size(); i++) {
            if (criteriosevaluacionParticulares.get(i).getCriterio().getId().equals(criterio.getId())) {
                criteriosSeccion.add(criterio);
                criteriosevaluacionParticulares.remove(i);
                break;
            }
        }
    }

    public void consultarEvaluacion(Seccion s) {
        try {
            evaluacion = evaser.obtenerEvaluacionXSeccion(s);
        } catch (java.lang.NullPointerException npe) {
            evaluacion = new Evaluacion();
        }
    }

    public List<CriterioEvaluacion> criteriosXDimension(Dimension dim) {
        List<CriterioEvaluacion> criterios = new LinkedList();
        for (CriterioEvaluacion ce : criteriosevaluacionParticulares) {
            if (ce.getDimension().getId().equals(dim.getId())) {
                criterios.add(ce);
            }
        }
        return criterios;
    }

    public boolean criterioEstaSeleccionado(Criterio c) {
        boolean cris = false;
        if (criteriosevaluacionParticulares != null) {
            for (CriterioEvaluacion ce : criteriosevaluacionParticulares) {
                if (ce.getCriterio().getId().equals(c.getId())) {
                    cris = true;
                }
            }
        }
        return cris;
    }

    public void irDimensiones() {
        if (asignaturasEvaluacion.size() > 0) {
            indTavEvaluacion = 2;
        } else {
            FacesUtil.addErrorMessage("Debes Seleccionar las asignaturas que evaluaran los proyectos de aula");
        }
    }

    public void publicarEvaluacion() {
        evaluacion.setEstado("Publicada");
        evaluacion.setFechapublicacion(new Date());
        evaluacion = evaser.modificar(evaluacion);
    }

    public void almacenarCriteriosEvaluacion() {
        criteriosevaluacion = new LinkedList();
        //cuando se consulta se estan perdiendo los criterios especificos porque se deben volver a obtener y clasificar
        if (evaluacion.getId() != null) {
            crievser.elimimarCriteriosEvaluacion(evaluacion);//elimina de bd los criterios de evaluacion 
            criser.elimimarCriteriosEvaluacion(criteriosevaluacionGlobales);//elimina los criterios globales creados para ser asignados a la evaluiacion
            comser.elimimarCompétenciasCriteriosGlobales(criteriosevaluacionGlobales);//elimina las competencias de criterios globales que se crearon en la evaluacion
            uniser.elimimarUnidadesCompétenciasCriteriosGlobales(criteriosevaluacionGlobales);//elimina las unidades de competencia de los criterios globales que se crearon en la evaluacion

        }

        //obteniendo dimensiones globales
        obtenerDimensionesProgramaAndPeriodoSemestreGlobales();
        //adicionando criterios globales
        for (Asignatura a : asignaturasEvaluacion) {
            System.out.println("Asignatura: " + a.getNombre());
            for (Dimension d : dimencionesPeriodoGlobales) {
                CriterioEvaluacion ceva = generarCriterioGlobal(a, d);
                criteriosevaluacion.add(ceva);
                System.out.println("generando criterio global: " + ceva.getCriterio().getDescripcion() + " Dimension: " + ceva.getDimension().getNombre());
            }
        }

        //adicionando criterios Especificos
        for (CriterioEvaluacion cevp : getCriteriosevaluacionParticulares()) {
            System.out.println("----------------------------------------------------");
            System.out.println("Criterio: " + cevp.getCriterio().getDescripcion() + " Dimension: " + cevp.getDimension().getNombre());
            criteriosevaluacion.add(cevp);
        }

        System.out.println("" + criteriosevaluacion.size());
        //almacenando evaluacion
        evaluacion.setEstado("Programada");
        evaluacion.setFechacreacion(new Date());
        evaluacion.setSeccion(getSeccion());
        if (evaluacion.getSeccion().getId() != null) {
            evaluacion = evaser.modificar(evaluacion);
            System.out.println("Almacenando Evaluacion: " + evaluacion.getFechacreacion());
            //almacenando criterios de evaluacion
            for (CriterioEvaluacion ce : criteriosevaluacion) {
                CriterioEvaluacion crite = new CriterioEvaluacion();
                crite.setCriterio(ce.getCriterio());
                crite.setEvaluacion(evaluacion);
                crite.setDimension(ce.getDimension());
                crievser.modificar(crite);
                System.out.println("Almacenando Criterio de evaluacion: " + ce.getCriterio().getDescripcion());
            }

            indTavEvaluacion = 0;
        }
    }

    public List<CriterioEvaluacion> quitarCriteriosGlobales(List<CriterioEvaluacion> crieva) {
        List<CriterioEvaluacion> crg = new LinkedList();
        System.out.println("" + crieva.size());
        for (CriterioEvaluacion crt : crieva) {
            System.out.println("" + crt.getCriterio().getId());
            if (crt.getCriterio().getTipo().equals("Global")) {
                crg.add(crt);
            }
        }
//        for (CriterioEvaluacion crtg : crg) {
//            crieva.remove(crtg);//cada ves que se elimina se pierde un indice
//        }
        return crg;
    }

    public CriterioEvaluacion generarCriterioGlobal(Asignatura a, Dimension dimension) {
        UnidadCompetencia unidad = new UnidadCompetencia();
        unidad.setAsignatura(a);
        unidad.setDescripcion(dimension.getNombre());
        unidad = uniser.modificar(unidad);

        Competencia competencia = new Competencia();
        competencia.setUnidad(unidad);
        competencia.setEvidencia(dimension.getNombre());
        competencia.setHoras(1);
        competencia.setEstrategia(dimension.getNombre());
        competencia.setTipo(new TipoCompetencia(Long.parseLong("2"), "Generica"));
        Tipo_Entregable tipoentregable = new Tipo_Entregable(dimension.getNombre(), dimension.getDescripcion(), a);
        tipoentregable.setTipo("Global");
        tipoentregable = teser.modificar(tipoentregable);
        competencia.setTipoentregable(tipoentregable);
        competencia = comser.modificar(competencia);

        Criterio criterio = new Criterio();
        criterio.setCompetencia(competencia);
        criterio.setDescripcion(dimension.getNombre());
        criterio.setPorcentaje(100);
        criterio.setTipo("Global");
        criterio = criser.modificar(criterio);

        CriterioEvaluacion criterioe = new CriterioEvaluacion();
        criterioe.setCriterio(criterio);
        criterioe.setDimension(dimension);
        criterioe.setEvaluacion(evaluacion);
        return criterioe;
    }

    public boolean habilitarDimensionGlobal() {
        boolean habilitar = false;
        System.out.println("" + dimencion.getTipo());
        try {
            if (dimencion.getTipo().equals("Global")) {
                habilitar = true;
            }
        } catch (java.lang.NullPointerException npe) {
            habilitar = false;
        }
        return habilitar;
    }

    public void almacenarDimensiones() {
        if (semestresSeleccionados.size() > 0) {
            if (dimenciones.size() > 0) {
                if (maxPor == 0) {
                    for (Semestre s : semestresSeleccionados) {
                        for (Dimension d : dimenciones) {
                            d.setSemestre(s);
                            dimser.crear(d);
                        }
                    }
                    dimenciones = null;
                    semestresSeleccionados = null;
                    mostPanelMod = false;
                    consultarDimensionesProgramaAndPeriodo();
                } else {
                    FacesUtil.addErrorMessage("No estan correctamente establecidos los porcentajes de las dimenciones");
                }
            } else {
                FacesUtil.addErrorMessage("No hay dimensiones agregadas para almacenar");
            }
        } else {
            FacesUtil.addErrorMessage("No se ha seleccionado el o los semestres para las dimensiones");
        }
    }

    public void consultarDimensionesProgramaAndPeriodo() {
        dimencionesPeriodo = dimser.obtenerDimensionesXProgramaPeriodo(periodo, programa);
        organizarDimensionesPorSemestre();
        mostPanelMod = false;
    }

    public void consultarDimensionesProgramaAndPeriodoAndSemestre(LiderPA lpa) {
        dimencionesPeriodo = dimser.obtenerDimensionesXProgramaPeriodoSemestre(lpa.getSeccion().getPeriodo(), lpa.getSeccion().getPrograma(), lpa.getSeccion().getSemestre());
    }

    public void obtenerEvaluacionXSeccion(Seccion s) {
        evaluacion = evaser.obtenerEvaluacionXSeccion(s);
        obtenerValoracionesEvaluacion();
    }

    public void obtenerValoracionesAsignatura(Asignatura a) {
//        setValoracionesAsignatura(valser.obtenerValoracionesXAsignatura(a));
        obtenerEvaluacionXSeccion(a.getSeccion());
        organizarValoracionesXDimension();
    }

    public void organizarValoracionesXDimension() { //metodo que organiza valoraciones por dimension
        valoracionesXdimensiones = new Hashtable<Long, List<Valoracion>>();
        //organizar valoraciones por dimension
        for (Valoracion v : valoraciones) {
            agregarValoracionDimension(v.getCriterio().getDimension(), v, valoracionesXdimensiones);
        }
    }

    public void obtenerValoracionesdelPeriodo(Periodo p) {
        valoraciones = valser.obtenerValoracionesXPeriodo(p);
        organizarValoracionesXDimension();
    }

    public double notaFIntegrante(Integrante inte) {
        //porcentaje de dimension;

        Enumeration<Long> keys = valoracionesXdimensiones.keys();
        double nfinal = 0;
        try {
            if (inte.getId() > 0) {
                System.out.println("Integrante: " + inte.getMatricula().getEstudiante().toString());
                while (keys.hasMoreElements()) {
                    List<Valoracion> valoracionesd = new LinkedList();
                    Long iddim = keys.nextElement();

                    for (Valoracion v : valoracionesXdimensiones.get(iddim)) {
                        if (v.getIntegrante().getId().equals(inte.getId())) {
                            valoracionesd.add(v);
                        }
                    }
                    try {
                        double pdim = valoracionesd.get(0).getCriterio().getDimension().getPorcentaje() / valoracionesd.size();
                        System.out.println("Dimencion: " + iddim + "\t" + valoracionesd.get(0).getCriterio().getDimension().getPorcentaje() + "\t" + valoracionesd.size() + "\t" + pdim);

                        for (Valoracion val : valoracionesd) {
                            nfinal = nfinal + (val.getValor() * pdim / 100);
                            System.out.println("calculando: " + val.getValor() + " * " + pdim + " / 100 =" + nfinal);
                        }
                        System.out.println("Nota: " + nfinal);
                    } catch (java.lang.IndexOutOfBoundsException iobe) {
                        System.out.println("" + iobe.getMessage() + " " + iobe.getCause());
                    }
                }
            } else {
                nfinal = 0;
            }
        } catch (java.lang.NullPointerException npe) {
            nfinal = 0;
        }
        return nfinal;
    }

    public void consultarValoracionesXIntegrante(Integrante i) {
        valoraciones = valser.obtenerValoracionesXIntegrante(i);
    }

    public List<ResultadosAsignatura> organizarResultadosIntegrante() {//metodo que organiza los resultados de un integrante por asignatura
        List<ResultadosAsignatura> resultados = new LinkedList();
        Hashtable<Long, Asignatura> asignaturasEvaluadas = new Hashtable<Long, Asignatura>();

        for (Valoracion v : valoraciones) {
            asignaturasEvaluadas.put(v.getCriterio().getCriterio().getCompetencia().getUnidad().getAsignatura().getId(), v.getCriterio().getCriterio().getCompetencia().getUnidad().getAsignatura());
            System.out.println("" + v.getCriterio().getCriterio().getCompetencia().getUnidad().getAsignatura().getNombre());
        }
        Enumeration<Long> keys = asignaturasEvaluadas.keys();
        while (keys.hasMoreElements()) {
            Long idasi = keys.nextElement();
            ResultadosAsignatura ra = new ResultadosAsignatura();
            Asignatura asig = asignaturasEvaluadas.get(idasi);
            ra.setAsignatura(asig);
            ra.setValoraciones(agregarValoracionesAsignatura(valoraciones, asig));
            resultados.add(ra);
        }
        return resultados;
    }

    public List<Valoracion> agregarValoracionesAsignatura(List<Valoracion> valors, Asignatura asig) {
        List<Valoracion> valoras = new LinkedList();
        for (Valoracion va : valors) {
            if (va.getCriterio().getCriterio().getCompetencia().getUnidad().getAsignatura().getId().equals(asig.getId())) {
                System.out.println("" + va.getCriterio().getCriterio().getDescripcion());
                valoras.add(va);
            }
        }
        return valoras;
    }

    public boolean existeAsignatura(List<ResultadosAsignatura> resultados, Asignatura a) {
        boolean existe = false;
        for (ResultadosAsignatura ra : resultados) {
            if (ra.getAsignatura().getId().equals(a.getId())) {
                existe = true;
            }
        }
        return existe;
    }

    public void agregarValoracionDimension(Dimension d, Valoracion v, Hashtable<Long, List<Valoracion>> valoraciones) {
        List<Valoracion> existentes = valoraciones.get(d.getId());

        if (existentes != null) {
            existentes.add(v);
            valoraciones.put(d.getId(), existentes);
        } else {
            existentes = new LinkedList();
            existentes.add(v);
            valoraciones.put(d.getId(), existentes);
        }

//        System.out.println("" + existentes + "\n");
    }

    public void prepararCriteriosParaEvaluar() {
        criteriosevaluacion = crievser.obtenerCriterioEvaluacionXAsignatura(evaluacion, asignaturaEvaluar);
        generarValoracionesXIntegrante();

    }

    public void obtenerValoracionesEvaluacion() {
        System.out.println("consultare las valoraciones de la evaluacion: " + evaluacion.getId());
        valoraciones = valser.obtenerValoracionesXEvaluacion(evaluacion);
        System.out.println("" + valoraciones.size());
    }

    public void valoracionesIntegrante(Integrante i) {
        for (Valoracion val : valoraciones) {

//            System.out.println("Asignatura: "+val.getCriterio().getCriterio().getCompetencia().getUnidad().getAsignatura().getId()+"   "+asignaturaEvaluar.getId());
            if (val.getIntegrante().getId().equals(i.getId()) && val.getCriterio().getCriterio().getCompetencia().getUnidad().getAsignatura().getId().equals(asignaturaEvaluar.getId())) {
                i.getValoracions().add(val);
            }
        }
    }

    public void generarValoracionesXIntegrante() {
        for (int i = 0; i < proyectoAula.getIntegrantes().size(); i++) {
            proyectoAula.getIntegrantes().get(i).setValoracions(new LinkedList());
            valoracionesIntegrante(proyectoAula.getIntegrantes().get(i));
            if (proyectoAula.getIntegrantes().get(i).getValoracions().size() > 0) {
                System.out.println("ya tiene valoraciones");
            } else {
                for (CriterioEvaluacion cev : criteriosevaluacion) {
                    Valoracion valoracion = new Valoracion();
                    valoracion.setCriterio(cev);
                    valoracion.setIntegrante(proyectoAula.getIntegrantes().get(i));
                    proyectoAula.getIntegrantes().get(i).getValoracions().add(valoracion);
                }
            }
        }
    }

    public void validarNotas() {

    }

    public void almacenarValoraciones() {
        for (int i = 0; i < proyectoAula.getIntegrantes().size(); i++) {
            for (Valoracion valoracion : proyectoAula.getIntegrantes().get(i).getValoracions()) {
                if (valoracion.getId() != null) {
                    valser.modificar(valoracion);
                } else {
                    valser.crear(valoracion);
                }

            }
        }
        obtenerValoracionesAsignatura(asignaturaEvaluar);
    }

    public boolean tieneValoraciones() {
        boolean tiene = false;
        if (valser.obtenerValoracionesXEvaluacion(evaluacion).size() > 0) {
            tiene = true;
        }
        return tiene;
    }

    public void prepararEvaluacion(Seccion s) {
        criteriosevaluacionParticulares = new LinkedList();
        criteriosevaluacionGlobales = new LinkedList();
        evaluacion = evaser.obtenerEvaluacionXSeccion(s);
        if (evaluacion.getId() != null) {
            List<CriterioEvaluacion> crits = crievser.obtenerCriterioEvaluacionXEvaluacion(evaluacion);
            clasificarcriterios(crits);
            setAsignaturasEvaluacion(new LinkedList());
            asignaturasExistentes(crits);
            criteriosNoExistentes(criteriosevaluacionParticulares);
            habilitarReprogramacion = tieneValoraciones();//verifica si hay valoraciones en una evaluacion
        } else {
            setAsignaturasEvaluacion(new LinkedList());
            setCriteriosevaluacion(new LinkedList());
        }
        obtenerDimensionesProgramaAndPeriodoSemestreNoGlobales();
        setIndTavEvaluacion(1);
    }

    public void asignaturasExistentes(List<CriterioEvaluacion> ce) {
        List<CriterioEvaluacion> criterios = new LinkedList();
        for (CriterioEvaluacion c : ce) {
            if (c.getCriterio().getTipo().equals("Global")) {
                if (!existeAsignaturaEvaluacion(c.getCriterio())) {
                    getAsignaturasEvaluacion().add(c.getCriterio().getCompetencia().getUnidad().getAsignatura());
                }
            }
        }

    }

    public boolean existeAsignaturaEvaluacion(Criterio c) {
        boolean existe = false;
        for (Asignatura a : getAsignaturasEvaluacion()) {
            if (a.getId().equals(c.getCompetencia().getUnidad().getAsignatura().getId())) {
                existe = true;
            }
        }
        return existe;
    }

    public void clasificarcriterios(List<CriterioEvaluacion> ce) {
//        List<CriterioEvaluacion> criterios = new LinkedList();
        setCriteriosevaluacion(new LinkedList());
        setCriteriosevaluacionGlobales(new LinkedList());
        setCriteriosevaluacionParticulares(new LinkedList());
        for (CriterioEvaluacion c : ce) {
            if (c.getCriterio().getTipo().equals("Especifico")) {
                getCriteriosevaluacionParticulares().add(c);
            } else {
                getCriteriosevaluacionGlobales().add(c);
            }
        }
    }

    public void criteriosNoExistentes(List<CriterioEvaluacion> ce) {
        List<Criterio> criterios = new LinkedList();
        for (CriterioEvaluacion c : ce) {
            criteriosSeccion.remove(c.getCriterio());
        }
    }

    public void obtenerDimensionesProgramaAndPeriodoSemestreNoGlobales() {
        dimencionesPeriodoNoGlobales = new LinkedList();
        for (Dimension d : dimencionesPeriodo) {
            if (!d.getTipo().equals("Global")) {
                dimencionesPeriodoNoGlobales.add(d);
            }
        }
    }

    public void obtenerDimensionesProgramaAndPeriodoSemestreGlobales() {
        dimencionesPeriodoGlobales = new LinkedList();
        for (Dimension d : dimencionesPeriodo) {
            if (d.getTipo().equals("Global")) {
                dimencionesPeriodoGlobales.add(d);
            }
        }
    }

    public void consultarCriteriosSeccion(Seccion s) {
        seccion = s;
        criteriosSeccion = criser.obtenerCriteriosXSeccion(s);
    }

    public void seleccionarSemestre(Semestre s) {
        semestre = s;
        dimenciones = s.getDimensions();
        if (dimenciones.size() > 0) {
            maxPor = 0;
        }
        mostPanelMod = true;
    }

    public void organizarDimensionesPorSemestre() {
        for (int i = 0; i < semestres.size(); i++) {
            semestres.get(i).setDimensions(new LinkedList());
            for (Dimension d : dimencionesPeriodo) {
                if (d.getSemestre().getId().equals(semestres.get(i).getId())) {
                    semestres.get(i).getDimensions().add(d);
                }
            }
        }
    }

    public boolean tieneDimensiones(Semestre sem) {
        boolean tiene = false;
        for (Dimension d : dimencionesPeriodo) {
            if (d.getSemestre().getId().equals(sem.getId())) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    public void agregarDimension() {
        if (!existeDimencion(dimencion)) {
            if (dimencion.getPorcentaje() > 0) {
                if (!dimencion.getTipo().trim().equals("")) {
                    dimenciones.add(dimencion);
                    maxPor = maxPor - dimencion.getPorcentaje();
                    limpiardimencion();
                } else {
                    FacesUtil.addErrorMessage("Debe seleccionar el tipo de dimension(Particular-Global)");
                }
            } else {
                FacesUtil.addErrorMessage("El porcentaje de la dimension debe ser superior a 0%");
            }
        }
    }

    public void quitarDimension(Dimension d) {
        maxPor = maxPor + d.getPorcentaje();
        for (int i = 0; i < dimenciones.size(); i++) {
            if (dimenciones.get(i).getNombre().equals(d.getNombre())) {
                dimenciones.remove(i);
                break;
            }
        }
    }

    public boolean habilitarAlmacenamientoDimenciones() {
        if (maxPor == 0) {
            return true;
        } else {
            if (maxPor < 0) {
                FacesUtil.addErrorMessage("La asignacion de Porcentajes supera el 100%");
            }
            return false;
        }
    }

    public boolean existeDimencion(Dimension d) {
        boolean e = false;
        if (dimenciones != null) {
            for (Dimension di : dimenciones) {
                if (di.getNombre().trim().equals(d.getNombre())) {
                    e = true;
                    break;
                }
            }
        } else {
            dimenciones = new LinkedList();
        }

        return e;
    }

    public void limpiardimencion() {
        Periodo per = dimencion.getPeriodo();
        ProgramaAcademico pra = dimencion.getPrograma();
        dimencion = new Dimension();
        dimencion.setPeriodo(per);
        dimencion.setPrograma(pra);
    }

    public void agregarSemestre(Semestre s) {
        if (!existeSemestre(s)) {
            semestresSeleccionados.add(s);
//            semestresAgregados = semestresAgregados + " - " + s.getDenominacion();
        }
    }

    public void quitarSemestre(Semestre s) {
        semestresSeleccionados.remove(s);
    }

    public boolean existeSemestre(Semestre s) {
        boolean e = false;
        if (semestresSeleccionados != null) {
            for (Semestre sem : semestresSeleccionados) {
                if (sem.getId().equals(s.getId())) {
                    e = true;
                }
            }
        } else {
            semestresSeleccionados = new LinkedList();
        }

        return e;
    }

    /**
     * @return the evaluacion
     */
    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    /**
     * @param evaluacion the evaluacion to set
     */
    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    /**
     * @return the criteriosevaluacion
     */
    public List<CriterioEvaluacion> getCriteriosevaluacion() {
        return criteriosevaluacion;
    }

    /**
     * @param criteriosevaluacion the criteriosevaluacion to set
     */
    public void setCriteriosevaluacion(List<CriterioEvaluacion> criteriosevaluacion) {
        this.criteriosevaluacion = criteriosevaluacion;
    }

    /**
     * @return the dimenciones
     */
    public List<Dimension> getDimenciones() {
        return dimenciones;
    }

    /**
     * @param dimenciones the dimenciones to set
     */
    public void setDimenciones(List<Dimension> dimenciones) {
        this.dimenciones = dimenciones;
    }

    /**
     * @return the evaluaciones
     */
    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    /**
     * @param evaluaciones the evaluaciones to set
     */
    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    /**
     * @return the dimencion
     */
    public Dimension getDimencion() {
        return dimencion;
    }

    /**
     * @param dimencion the dimencion to set
     */
    public void setDimencion(Dimension dimencion) {
        this.dimencion = dimencion;
    }

    /**
     * @return the semestres
     */
    public List<Semestre> getSemestres() {
        return semestres;
    }

    /**
     * @param semestres the semestres to set
     */
    public void setSemestres(List<Semestre> semestres) {
        this.semestres = semestres;
    }

    /**
     * @return the semestresAgregados
     */
    public String getSemestresAgregados() {
        return semestresAgregados;
    }

    /**
     * @param semestresAgregados the semestresAgregados to set
     */
    public void setSemestresAgregados(String semestresAgregados) {
        this.semestresAgregados = semestresAgregados;
    }

    /**
     * @return the maxPor
     */
    public int getMaxPor() {
        return maxPor;
    }

    /**
     * @param maxPor the maxPor to set
     */
    public void setMaxPor(int maxPor) {
        this.maxPor = maxPor;
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
     * @return the dimencionesPeriodo
     */
    public List<Dimension> getDimencionesPeriodo() {
        return dimencionesPeriodo;
    }

    /**
     * @param dimencionesPeriodo the dimencionesPeriodo to set
     */
    public void setDimencionesPeriodo(List<Dimension> dimencionesPeriodo) {
        this.dimencionesPeriodo = dimencionesPeriodo;
    }

    /**
     * @return the semestresSeleccionados
     */
    public List<Semestre> getSemestresSeleccionados() {
        return semestresSeleccionados;
    }

    /**
     * @param semestresSeleccionados the semestresSeleccionados to set
     */
    public void setSemestresSeleccionados(List<Semestre> semestresSeleccionados) {
        this.semestresSeleccionados = semestresSeleccionados;
    }

    /**
     * @return the semestre
     */
    public Semestre getSemestre() {
        return semestre;
    }

    /**
     * @param semestre the semestre to set
     */
    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    /**
     * @return the mostPanelMod
     */
    public boolean isMostPanelMod() {
        return mostPanelMod;
    }

    /**
     * @param mostPanelMod the mostPanelMod to set
     */
    public void setMostPanelMod(boolean mostPanelMod) {
        this.mostPanelMod = mostPanelMod;
    }

    /**
     * @return the criteriosSeccion
     */
    public List<Criterio> getCriteriosSeccion() {
        return criteriosSeccion;
    }

    /**
     * @param criteriosSeccion the criteriosSeccion to set
     */
    public void setCriteriosSeccion(List<Criterio> criteriosSeccion) {
        this.criteriosSeccion = criteriosSeccion;
    }

    /**
     * @return the asignaturasSeccion
     */
    public List<Asignatura> getAsignaturasSeccion() {
        return asignaturasSeccion;
    }

    /**
     * @param asignaturasSeccion the asignaturasSeccion to set
     */
    public void setAsignaturasSeccion(List<Asignatura> asignaturasSeccion) {
        this.asignaturasSeccion = asignaturasSeccion;
    }

    /**
     * @return the asignaturasEvaluacion
     */
    public List<Asignatura> getAsignaturasEvaluacion() {
        return asignaturasEvaluacion;
    }

    /**
     * @param asignaturasEvaluacion the asignaturasEvaluacion to set
     */
    public void setAsignaturasEvaluacion(List<Asignatura> asignaturasEvaluacion) {
        this.asignaturasEvaluacion = asignaturasEvaluacion;
    }

    /**
     * @return the dimencionesPeriodoNoGlobales
     */
    public List<Dimension> getDimencionesPeriodoNoGlobales() {
        return dimencionesPeriodoNoGlobales;
    }

    /**
     * @param dimencionesPeriodoNoGlobales the dimencionesPeriodoNoGlobales to
     * set
     */
    public void setDimencionesPeriodoNoGlobales(List<Dimension> dimencionesPeriodoNoGlobales) {
        this.dimencionesPeriodoNoGlobales = dimencionesPeriodoNoGlobales;
    }

    /**
     * @return the indTavEvaluacion
     */
    public int getIndTavEvaluacion() {
        return indTavEvaluacion;
    }

    /**
     * @param indTavEvaluacion the indTavEvaluacion to set
     */
    public void setIndTavEvaluacion(int indTavEvaluacion) {
        this.indTavEvaluacion = indTavEvaluacion;
    }

    /**
     * @return the dimencionesPeriodoGlobales
     */
    public List<Dimension> getDimencionesPeriodoGlobales() {
        return dimencionesPeriodoGlobales;
    }

    /**
     * @param dimencionesPeriodoGlobales the dimencionesPeriodoGlobales to set
     */
    public void setDimencionesPeriodoGlobales(List<Dimension> dimencionesPeriodoGlobales) {
        this.dimencionesPeriodoGlobales = dimencionesPeriodoGlobales;
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
     * @return the criteriosevaluacionGlobales
     */
    public List<CriterioEvaluacion> getCriteriosevaluacionGlobales() {
        return criteriosevaluacionGlobales;
    }

    /**
     * @param criteriosevaluacionGlobales the criteriosevaluacionGlobales to set
     */
    public void setCriteriosevaluacionGlobales(List<CriterioEvaluacion> criteriosevaluacionGlobales) {
        this.criteriosevaluacionGlobales = criteriosevaluacionGlobales;
    }

    /**
     * @return the criteriosevaluacionParticulares
     */
    public List<CriterioEvaluacion> getCriteriosevaluacionParticulares() {
        return criteriosevaluacionParticulares;
    }

    /**
     * @param criteriosevaluacionParticulares the
     * criteriosevaluacionParticulares to set
     */
    public void setCriteriosevaluacionParticulares(List<CriterioEvaluacion> criteriosevaluacionParticulares) {
        this.criteriosevaluacionParticulares = criteriosevaluacionParticulares;
    }

    /**
     * @return the proyectoAula
     */
    public Proyecto_Aula getProyectoAula() {
        return proyectoAula;
    }

    /**
     * @param proyectoAula the proyectoAula to set
     */
    public void setProyectoAula(Proyecto_Aula proyectoAula) {
        this.proyectoAula = proyectoAula;
    }

    /**
     * @return the asignaturaEvaluar
     */
    public Asignatura getAsignaturaEvaluar() {
        return asignaturaEvaluar;
    }

    /**
     * @param asignaturaEvaluar the asignaturaEvaluar to set
     */
    public void setAsignaturaEvaluar(Asignatura asignaturaEvaluar) {
        this.asignaturaEvaluar = asignaturaEvaluar;
    }

    /**
     * @return the habilitarReprogramacion
     */
    public boolean isHabilitarReprogramacion() {
        return habilitarReprogramacion;
    }

    /**
     * @param habilitarReprogramacion the habilitarReprogramacion to set
     */
    public void setHabilitarReprogramacion(boolean habilitarReprogramacion) {
        this.habilitarReprogramacion = habilitarReprogramacion;
    }

    /**
     * @return the valoraciones
     */
    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    /**
     * @param valoraciones the valoraciones to set
     */
    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    /**
     * @return the valoracionesAsignatura
     */
    public List<Valoracion> getValoracionesAsignatura() {
        return valoracionesAsignatura;
    }

    /**
     * @param valoracionesAsignatura the valoracionesAsignatura to set
     */
    public void setValoracionesAsignatura(List<Valoracion> valoracionesAsignatura) {
        this.valoracionesAsignatura = valoracionesAsignatura;
    }

}
