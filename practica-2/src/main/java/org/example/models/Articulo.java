package org.example.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Articulo {
    private long id;
    private String titulo;
    private String cuerpo;
    private Usuario autor;
    private Date fecha;
    private ArrayList<Comentario> listaComentarios;
    private ArrayList<Etiqueta> listaEtiquetas;

    public Articulo(long id, String titulo, String cuerpo, Usuario autor) {
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = new Date();
        this.listaComentarios = new ArrayList<>();
        this.listaEtiquetas = new ArrayList<>();
    }
    public long generarCommentId(){
        long temp = 0;
        if(listaComentarios.isEmpty()){
            return 0;
        }else{
            for (Comentario coment: listaComentarios) {
                temp++;
            }
        }
        return temp;
    }
    public void agregarComentario(Comentario coment){
        listaComentarios.add(coment);
    }
    public String getCuerpo70(){
        if (cuerpo.length() <= 70) {
            return cuerpo;
        } else {
            return cuerpo.substring(0, 70);
        }
    }
    public String fechaFormateada(){
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formateador.format(fecha);
        return fechaFormateada;
    }
    public void addComentario(Comentario co){
        listaComentarios.add(co);
    }
    public String showEtiquetas() {
        if (listaEtiquetas.size() == 1) {
            return listaEtiquetas.get(0).getEtiqueta();
        } else {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < listaEtiquetas.size(); i++) {
                if (i > 0) {
                    str.append(", ");
                }
                str.append(listaEtiquetas.get(i).getEtiqueta());
            }
            return str.toString();
        }
    }
    public void addEtiqueta(Etiqueta et){
        listaEtiquetas.add(et);
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(ArrayList<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public ArrayList<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(ArrayList<Etiqueta> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }
}