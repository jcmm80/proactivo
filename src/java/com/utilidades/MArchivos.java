/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades;

import com.entity.Estudiante;
import com.entity.Matricula;
import com.entity.Periodo;
import com.entity.ProgramaAcademico;
import com.entity.Seccion;
import com.entity.Semestre;
import com.services.EstudianteServices;
import com.services.MatriculaServices;
import com.services.PeriodoServices;
import com.services.ProgramaAcademicoServices;
import com.services.SeccionServices;
import com.services.SemestreServices;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Jcmm
 */
public class MArchivos implements Serializable {

    private List<Estudiante> estudiantes = new LinkedList();
    private List<Semestre> semestres = new LinkedList();
    private List<Periodo> periodos = new LinkedList();
    private List<ProgramaAcademico> programas = new LinkedList();
    private List<Seccion> secciones = new LinkedList();
    private List<Matricula> matriculas = new LinkedList();

    List<Semestre> semestresAntiguos = new LinkedList();
    List<Periodo> periodosAntiguos = new LinkedList();
    List<ProgramaAcademico> programasAntiguos = new LinkedList();
    List<Seccion> seccionesAntiguos = new LinkedList();
    List<Estudiante> estudiantesAntiguos = new LinkedList();
    List<Matricula> matriculasAnteriores = new LinkedList();

    EstudianteServices es = new EstudianteServices();
    MatriculaServices ms = new MatriculaServices();
    PeriodoServices ps = new PeriodoServices();
    SemestreServices ss = new SemestreServices();
    ProgramaAcademicoServices pas = new ProgramaAcademicoServices();
    SeccionServices secs = new SeccionServices();

    public MArchivos() {
        obtenerdatosAnteriores();
    }

    public void obtenerdatosAnteriores() {
        semestresAntiguos = ss.consultarTodo(Semestre.class);
        periodosAntiguos = ps.consultarTodo(Periodo.class);
        programasAntiguos = pas.consultarTodo(ProgramaAcademico.class);
        seccionesAntiguos = secs.consultarTodo(Seccion.class);
    }

    public int ultimamatricula() {
        matriculasAnteriores = ms.consultarTodo(Matricula.class);
        return matriculasAnteriores.size();
    }

    public int ultimoconsec() {
        estudiantesAntiguos = es.consultarTodo(Estudiante.class);
        return estudiantesAntiguos.size();
    }

    public void almacenamiento() {
        if (periodos.size() > 0) {
            for (Periodo p : periodos) {
                ps.modificar(p);
            }
        }
        if (programas.size() > 0) {
            for (ProgramaAcademico pa : programas) {
                pas.modificar(pa);
            }
        }
        if (secciones.size() > 0) {
            for (Seccion s : secciones) {
                secs.modificar(s);
            }
        }
        if (estudiantes.size() > 0) {
            for (Estudiante e : estudiantes) {
                es.modificar(e);
                System.out.println("almacenado: " + e.toString());
            }
        }
        if (matriculas.size() > 0) {
            for (Matricula m : matriculas) {
                ms.modificar(m);
                System.out.println("Almacenada matricula para: " + m.getEstudiante().getId());
            }
        }
    }

    public void cargarArhivoEstudiante(String archivo) {

        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(archivo);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;

            Estudiante estudiante;
            Semestre semestre;
            Periodo periodo;
            ProgramaAcademico programa;
            Seccion seccion;
            Matricula matricula;

            // Leer el archivo linea por linea
            int c = ultimoconsec();
            int um = ultimamatricula();
            while ((strLinea = buffer.readLine()) != null) {
                estudiante = new Estudiante();
                matricula = new Matricula();
                seccion = new Seccion();
                programa = new ProgramaAcademico();
                periodo = new Periodo();
                semestre = new Semestre();
                // Imprimimos la línea por pantalla
                StringTokenizer stn = new StringTokenizer(strLinea, "\t");
                estudiante.setId(Long.parseLong("" + c));
                estudiante.setDireccion(stn.nextToken());
                estudiante.setEmail(stn.nextToken());
                estudiante.setGenero(stn.nextToken());
                estudiante.setIdentificacion(stn.nextToken());
                estudiante.setLogin(stn.nextToken());
                estudiante.setPassword(stn.nextToken());
                estudiante.setPrimerApellido(stn.nextToken());
                estudiante.setPrimerNombre(stn.nextToken());
                estudiante.setSegundoApellido(stn.nextToken());
                estudiante.setSegundoNombre(stn.nextToken());
                estudiante.setTelefono(stn.nextToken());
                estudiante.setTipo_ide(stn.nextToken());
                estudiante.setTipo("Estudiante");
                matricula.setEstudiante(estudiante);
                matricula.setEstado(stn.nextToken());
//                System.out.println(""+matricula.getEstado());
                matricula.setFecha(convertirFecha(stn.nextToken()));
                matricula.setEstadoPA("Libre");
                seccion.setDenominacion(stn.nextToken());
                semestre.setId(Long.parseLong(stn.nextToken()));
                programa.setId(Long.parseLong(stn.nextToken()));
                seccion.setId(Long.parseLong(stn.nextToken()));
                periodo.setAnio(Integer.parseInt(stn.nextToken()));
                periodo.setFechaFinal(convertirFecha(stn.nextToken()));
                periodo.setFechaInicial(convertirFecha(stn.nextToken()));
                periodo.setNumero(Integer.parseInt(stn.nextToken()));
                periodo.setId(Long.parseLong(stn.nextToken()));
                programa.setNombre(stn.nextToken());
                semestre.setDenominacion(stn.nextToken());
                programa.setNombreCompleto(programa.getNombre());

                seccion.setSemestre(semestre);
                seccion.setPrograma(programa);
                seccion.setPeriodo(periodo);
                matricula.setSeccion(seccion);
                matricula.setId(Long.parseLong("" + um));

                if (!existeSemestre(semestre)) {
                    getSemestres().add(semestre);
                }
                if (!existePrograma(programa)) {
                    getProgramas().add(programa);
                    programasAntiguos.add(programa);
                }
                if (!existePeriodo(periodo)) {
                    getPeriodos().add(periodo);
                    periodosAntiguos.add(periodo);
                }
                if (!existeSeccion(seccion)) {
                    getSecciones().add(seccion);
                    seccionesAntiguos.add(seccion);
                }
                if (!existeEstudiante(estudiante)) {
                    getEstudiantes().add(estudiante);
                    estudiantesAntiguos.add(estudiante);
                }
                if (!existeMatricula(matricula)) {
                    getMatriculas().add(matricula);
                    matriculasAnteriores.add(matricula);
                }
                c++;
            }
            // Cerramos el archivo
            entrada.close();
        } catch (Exception e) { //Catch de excepciones
            e.printStackTrace();
            System.err.println("Ocurrio un error: " + e.getMessage());
        }

    }

    public boolean existeMatricula(Matricula mat) {
        boolean existe = false;
        for (Matricula m : matriculasAnteriores) {
            if (m.getSeccion().equals(mat.getSeccion()) && m.getEstudiante().equals(mat.getEstudiante())) {
                matriculasAnteriores.add(mat);
                existe = true;
                break;
            }
        }
        return existe;
    }

    public boolean existeEstudiante(Estudiante es) {
        boolean existe = false;
        for (Estudiante e : estudiantesAntiguos) {
            if (e.getIdentificacion().equals(es.getIdentificacion())) {
                estudiantesAntiguos.add(es);
                existe = true;
                break;
            }
        }
        return existe;
    }

    public boolean existeSeccion(Seccion se) {
        boolean existe = false;
        for (Seccion s : seccionesAntiguos) {
            if (s.getId().equals(se.getId())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public boolean existePeriodo(Periodo pe) {
        boolean existe = false;
        for (Periodo p : periodosAntiguos) {
            if (p.getId().equals(pe.getId())) {
                periodosAntiguos.add(pe);
                existe = true;
                break;
            }
        }
        return existe;
    }

    public boolean existePrograma(ProgramaAcademico pro) {
        boolean existe = false;
        for (ProgramaAcademico p : programasAntiguos) {
            if (p.getId().equals(pro.getId())) {
                programasAntiguos.add(pro);
                existe = true;
                break;
            }
        }
        return existe;
    }

    public boolean existeSemestre(Semestre sem) {
        boolean existe = false;
        for (Semestre s : semestresAntiguos) {
            if (s.getId().equals(sem.getId())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public Date convertirFecha(String fech) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = formatoDelTexto.parse(fech);

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return fecha;

    }

    public void crearLicencia(String ruta, Licencias licencias) {
        try {
            // Crea un FileOutputStream para escribir en el archivo
            FileOutputStream fileOutputStream = new FileOutputStream(ruta);

            // Crea un ObjectOutputStream para escribir el objeto en el archivo
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Escribe el objeto en el archivo
            objectOutputStream.writeObject(licencias);

            // Cierra el ObjectOutputStream
            objectOutputStream.close();

            System.out.println("Objeto guardado en el archivo correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @return the periodos
     */
    public List<Periodo> getPeriodos() {
        return periodos;
    }

    /**
     * @param periodos the periodos to set
     */
    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    /**
     * @return the programas
     */
    public List<ProgramaAcademico> getProgramas() {
        return programas;
    }

    /**
     * @param programas the programas to set
     */
    public void setProgramas(List<ProgramaAcademico> programas) {
        this.programas = programas;
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
     * @return the matriculas
     */
    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    /**
     * @param matriculas the matriculas to set
     */
    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

}
