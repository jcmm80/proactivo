/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.servlet.ServletContext;

/**
 *
 * @author jcmm
 */
@Entity
public class Tutoria implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    private String estado;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaAsignacion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEjecucion;
    @ManyToOne
    private Proyecto_Aula proyecto;
    @ManyToOne
    private Integrante integrante;
    @ManyToOne
    private Asignatura asignatura;
    private String recomendaciones;
    private String compromisos;

    public Tutoria() {
    }

    public Tutoria(Long id, Date fechaSolicitud, String estado, Date fechaAsignacion, Date fechaEjecucion, Proyecto_Aula proyecto) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaEjecucion = fechaEjecucion;
        this.proyecto = proyecto;
    }

    public String imagenTutoria() {
        String img = "";
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getRealPath("/imagenInicial.jpeg").replace("imagenInicial.jpeg", "Imagenes\\Tutorias\\");
        //this.setId(Long.getLong("5"));
        String imagen = path + this.getId() + ".jpg";
        File f = new File(imagen);
        //System.out.println(""+f.toString()+" "+f.exists()+" "+f.length()+" ");
        if (f.length() > 0) {
            img = this.getId() + ".jpg";;
        } else {
            img = "inotutoria.jpg";
        }
        return img;
    }

//    public String mensajeCorrespondencia() {
//        String mensaje = "<h2>Notificaci&oacute;n del Proceso: "+this.estado+"</h2>\n"
//                + "<p>Fecha:"+this.fechaSolicitud+"</p>\n"
//                + "<p>Quien realiz&oacute; el proceso: <strong>"+this.asignatura.getProfesor().toString()+"</strong></p>\n"
//                + "<p>------------------------------------------------------------------</p>\n"
//                + "<p style=\"text-align: justify;\"><span style=\"color: #008000;\">Titulo del Proyecto: "+this.proyecto.getTitulo()+"</span></p>\n"
//                + "<p style=\"text-align: justify;\"><span style=\"color: #008000;\">Descripci&oacute;n: "+this.proyecto.getProblematica()+"</span></p>\n"
//                + "<p>------------------------------------------------------------------</p>\n"
//                + "<p><span style=\"color: #008000;\">Integrantes:</span></p>\n";                
//                for(Integrante i:this.proyecto.getIntegrantes()){
//                    mensaje=mensaje+ "<p><span style=\"color: #008000;\">"+i.getMatricula().getEstudiante().toString()+"</span></p>\n";
//                }
//        return mensaje;
//    }

    public boolean fueRealizada() {
        boolean valido = false;
        if (this.estado.equals("Realizada")) {
            valido = true;
        }
        return valido;
    }

    public boolean validarTutoria() {
        boolean valido = true;
        if (this.asignatura.getId() <= 0) {
            valido = false;
        }
        if (this.proyecto.getId() <= 0) {
            valido = false;
        }
//        if (this.integrante.getId() <= 0) { se quitó esta validacion para los casos en los que se crea una tutoria directamente por el profesor
//            valido = false;
//        }
        return valido;
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
        if (!(object instanceof Tutoria)) {
            return false;
        }
        Tutoria other = (Tutoria) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Tutoria[ id=" + getId() + " ]";
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
     * @return the fechaSolicitud
     */
    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    /**
     * @param fechaSolicitud the fechaSolicitud to set
     */
    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
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
     * @return the fechaAsignacion
     */
    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    /**
     * @param fechaAsignacion the fechaAsignacion to set
     */
    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    /**
     * @return the fechaEjecucion
     */
    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    /**
     * @param fechaEjecucion the fechaEjecucion to set
     */
    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
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
     * @return the recomendaciones
     */
    public String getRecomendaciones() {
        return recomendaciones;
    }

    /**
     * @param recomendaciones the recomendaciones to set
     */
    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    /**
     * @return the compromisos
     */
    public String getCompromisos() {
        return compromisos;
    }

    /**
     * @param compromisos the compromisos to set
     */
    public void setCompromisos(String compromisos) {
        this.compromisos = compromisos;
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
     * @return the asignatura
     */
    public Asignatura getAsignatura() {
        return asignatura;
    }

    /**
     * @param asignatura the asignatura to set
     */
    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

}
