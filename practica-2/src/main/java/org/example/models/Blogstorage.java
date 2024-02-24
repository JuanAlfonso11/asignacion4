package org.example.models;

import java.util.*;

public class Blogstorage {
    private static Blogstorage instancia;

    private ArrayList<Usuario> usuarioArrayList;
    private ArrayList<Articulo> articuloArrayList;
    public Blogstorage() {
        super();
        this.articuloArrayList = new ArrayList<>();
        this.usuarioArrayList = new ArrayList<>();
        crearAdmin();
        crearPost();
    }

    public void actualizarArticulo(Articulo articuloActualizado) {
        for (int i = 0; i < articuloArrayList.size(); i++) {
            Articulo articulo = articuloArrayList.get(i);
            articuloActualizado.setListaComentarios(articulo.getListaComentarios());
            if (articulo.getId() == articuloActualizado.getId()) {
                articuloArrayList.set(i, articuloActualizado);
                break;
            }
        }
    }

    public void eliminarArticuloById(Long id){
        Iterator<Articulo> iterator = articuloArrayList.iterator();
        while (iterator.hasNext()) {
            Articulo articulo = iterator.next();
            if (articulo.getId() == id) {
                iterator.remove();
            }
        }
    }
    public long crearIdArticulo(){
        long temp =0;
        if(articuloArrayList.isEmpty()){
            return temp;
        }else{
            for (Articulo i: articuloArrayList) {
                temp++;
            }
        }
        return temp;
    }
    public static Blogstorage getInstancia(){
        if(instancia==null){
            instancia = new Blogstorage();
        }
        return instancia;
    }

    public void crearComentario(Long artId, Comentario coment){
        for (Articulo i: articuloArrayList) {
            if(artId == i.getId()){
                i.agregarComentario(coment);
            }
        }

    }

    public ArrayList<Articulo> sortArticulosByFecha(ArrayList<Articulo> articulos) {
        Collections.sort(articulos, new Comparator<Articulo>() {
            public int compare(Articulo articulo1, Articulo articulo2) {
                Date fecha1 = articulo1.getFecha();
                Date fecha2 = articulo2.getFecha();
                return fecha2.compareTo(fecha1);
            }
        });
        return articulos;
    }


    public Usuario autheticarUsuario(String usuario, String password){
        for (Usuario i: usuarioArrayList) {
            if(i.getUsername().equals(usuario) && i.getPassword().equals(password)){
                return i;
            }
        }
        return null;
    }

    public void crearAdmin(){
        Usuario admin = new Usuario("admin", "admin", "admin",true, true);
        usuarioArrayList.add(admin);
    }
    public void crearPost(){
        Usuario user = buscarUsuarioByUserName("admin");
        Articulo nuevoart = new Articulo(0,"Fire and ice","Ooh, you're givin' me the fever tonight\n" +
                "I don't want to give in, I'd be playin' with fire\n" +
                "You forget I've seen you work before\n" +
                "Take 'em straight to the top, leave 'em cryin' for more\n" +
                "I've seen you burn 'em before\n" +
                "Fire and ice\n" +
                "You come on like a flame, then you turn a cold shoulder\n" +
                "Fire and ice\n" +
                "I want to give you my love, but you'll just take a little piece of my heart\n" +
                "You'll just tear it apart\n" +
                "Movin' in for the kill tonight\n" +
                "You got every advantage when they put out the lights\n" +
                "It's not so pretty when it fades away\n" +
                "'Cause it's just an illusion in this passion play\n" +
                "I've seen you burn 'em before\n" +
                "Fire and ice\n" +
                "You come on like a flame, then you turn a cold shoulder\n" +
                "Fire and ice\n" +
                "I want to give you my love, but you'll just take a little piece of my heart\n" +
                "So you think you got it all figured out\n" +
                "You're an expert in the field, without a doubt\n" +
                "But I know your methods inside and out\n" +
                "And I won't be takin' in by fire and ice\n" +
                "Fire and ice\n" +
                "You come on like a flame, then you turn a cold shoulder\n" +
                "Fire and ice\n" +
                "I want to give you my love, but you'll just take a little piece of my heart\n" +
                "You come on like a flame, then you turn a cold shoulder\n" +
                "Fire and ice\n" +
                "I want to give you my love, but you'll just take a little piece of my heart\n" +
                "You come on like a flame, then you turn a cold shoulder, fire and ice\n" +
                "You come on like a flame, then you turn a cold shoulder, fire and ice",user);
        Etiqueta nuevaetiqueta = new Etiqueta(1,"Cancion");
        nuevoart.addEtiqueta(nuevaetiqueta);
        Comentario newcoment = new Comentario(0,"La cancion mas dificil de guitar hero",user,nuevoart);
        nuevoart.agregarComentario(newcoment);
        articuloArrayList.add(nuevoart);

    }

    public  Articulo buscarArticuloById(Long id){
        for (Articulo i: articuloArrayList) {
            if(i.getId() == id){
                return i;
            }
        }
        return null;
    }
    public ArrayList<Articulo> buscarArticulosByusername(String username){
        ArrayList<Articulo> articulos = new ArrayList<>();
        for (Articulo i: articuloArrayList) {
            if(i.getAutor().getUsername().equals(username)){
                articulos.add(i);
            }
        }
        return articulos;
    }
    public Usuario buscarUsuarioByUserName( String username){
        for (Usuario user: usuarioArrayList) {
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
    public void insertarUsuario(Usuario user){
        usuarioArrayList.add(user);
    }
    public void eliminarUsuario(Usuario user){
        usuarioArrayList.remove(user);
    }
    public void insertarArticulo(Articulo art){
        articuloArrayList.add(art);
    }
    public void eliminarArticulo(Articulo art){
        articuloArrayList.remove(art);
    }
    public ArrayList<Usuario> getUsuarioArrayList() {
        return usuarioArrayList;
    }

    public void setUsuarioArrayList(ArrayList<Usuario> usuarioArrayList) {
        this.usuarioArrayList = usuarioArrayList;
    }

    public ArrayList<Articulo> getArticuloArrayList() {
        return articuloArrayList;
    }

    public void setArticuloArrayList(ArrayList<Articulo> articuloArrayList) {
        this.articuloArrayList = articuloArrayList;
    }

}
