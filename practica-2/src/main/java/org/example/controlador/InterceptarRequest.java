package org.example.controlador;
import  io.javalin.Javalin;
import org.example.util.BaseControler;
import org.example.models.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static io.javalin.apibuilder.ApiBuilder.*;
import java.util.ArrayList;
public class InterceptarRequest  extends  BaseControler{
    public InterceptarRequest(Javalin app){
        super(app);
    }
    @Override
    public void aplicarRutas(){
        app.routes(()->{
            path("/",()->{
                before(ctx->{
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(usuario== null && (ctx.path().startsWith("/manejadorUsuarios/")||ctx.path().startsWith("/posting/"))){
                        ctx.redirect("/login.html");
                    }
                    else if(usuario==null || !usuario.isAdministrador()){
                        if(ctx.path().startsWith("/manejadorUsuarios/")||ctx.path().startsWith("/registro")){
                            ctx.redirect("/noencontrado");
                        }
                    }
                });
                get("/",ctx->{
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    Map<String, Object> modelo = new HashMap<>();
                    if (usuario != null) {
                        modelo.put("tipo", "Logout");
                        modelo.put("sitio", "/logout");
                    } else {
                        modelo.put("tipo", "Login");
                        modelo.put("sitio", "/login");
                    }
                    List<Articulo> lista = Blogstorage.getInstancia().sortArticulosByFecha(Blogstorage.getInstancia().getArticuloArrayList());
                    modelo.put("lista", lista);
                    ctx.render("/publico/blog.html", modelo);
                });
                get("/blog.html",ctx->{
                    ctx.redirect("/");
                });
                get("/register",ctx->{
                    ctx.redirect("/register.html");
                });
                get("/login",ctx->{
                    ctx.redirect("/login.html");
                });

            });
        });
        app.routes(()->{
            path("/manejadorUsuarios/", ()->{
                get("/", ctx -> {
                    ctx.redirect("/manejadorUsuarios/listar");
                });

                get("/listar", ctx -> {
                    //tomando el parametro utl y validando el tipo.
                    List<Usuario> lista = Blogstorage.getInstancia().getUsuarioArrayList();
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Listado Usuarios");
                    modelo.put("lista", lista);
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/manejadorUsuarios/listar.html", modelo);
                });
                get("/crear", ctx -> {
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Creación de Usuario");
                    modelo.put("accion", "/manejadorUsuarios/crear");
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/manejadorUsuarios/crearEditarVisualizar.html", modelo);
                });

                post("/crear", ctx->{
                    boolean autor;
                    if(ctx.formParam("autor") == null){
                        autor=true;
                    }else{
                        autor=false;
                    }

                    boolean admin;
                    if(ctx.formParam("admin") == null){
                        admin=true;
                    }else{
                        admin=false;
                    }
                    Usuario temp = new Usuario(ctx.formParam("username"),ctx.formParam("user"),ctx.formParam("password"), admin, autor);
                    Blogstorage.getInstancia().insertarUsuario(temp);
                    ctx.redirect("/manejadorUsuarios");

                });
            });
        });

        app.post("/autenticar", ctxContext -> {
            //Obteniendo la informacion de la petion. Pendiente validar los parametros.
            String nombreUsuario = ctxContext.formParam("usuario");
            String password = ctxContext.formParam("password");
            //Autenticando el usuario para nuestro ejemplo siempre da una respuesta correcta.
            Usuario usuario = Blogstorage.getInstancia().autheticarUsuario(nombreUsuario, password);
            //agregando el usuario en la session... se puede validar si existe para solicitar el cambio.-
            ctxContext.sessionAttribute("usuario", usuario);
            //redireccionando la vista con autorizacion.
            ctxContext.redirect("/");

        });
        app.get("/crearusuario", ctxContext ->{
            boolean autor= false;
            if(ctxContext.queryParam("autor") == null){
                autor=false;
            }else{
                autor=true;
            }
            if(Blogstorage.getInstancia().buscarUsuarioByUserName(ctxContext.queryParam("username")) == null) {
                Usuario user = new Usuario(ctxContext.queryParam("username"), ctxContext.queryParam("user"), ctxContext.queryParam("password"), false, autor);

                Blogstorage.getInstancia().insertarUsuario(user);

                ctxContext.sessionAttribute("usuario", user);

                ctxContext.redirect("/");
            }else{
                ctxContext.redirect("/register");
            }
        });
        app.routes(()-> {
            path("/posting/", () -> {
                before(ctx->{
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(!usuario.isAutor()){
                        if ( ctx.path().startsWith("/posting/")) {
                            ctx.redirect("/notfound");
                        }
                    }
                });
                get("/", ctx -> {
                    ctx.redirect("/posting/listar");
                });

                get("/listar", ctx -> {
                    List<Articulo> lista = null;
                    Map<String, Object> modelo = new HashMap<>();
                    //tomando el parametro utl y validando el tipo.
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(Blogstorage.getInstancia().buscarUsuarioByUserName(usuario.getUsername()).isAdministrador()){
                        lista = Blogstorage.getInstancia().getArticuloArrayList();
                    }else if(Blogstorage.getInstancia().buscarUsuarioByUserName(usuario.getUsername()).isAutor()){

                        lista = Blogstorage.getInstancia().buscarArticulosByusername(usuario.getUsername());

                    }

                    modelo.put("titulo", "Listado Articulos");
                    modelo.put("lista", lista);
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/posting/listar.html", modelo);
                });

                get("/crear", ctx -> {
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Creación de Publicacion");
                    modelo.put("accion", "/posting/crear");
                    modelo.put("idart",Blogstorage.getInstancia().crearIdArticulo());
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/posting/crearEditarVisualizar.html", modelo);
                });

                post("/crear", ctx->{
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    String[] etiquetasArray = ctx.formParam("etiqueta").split(", ");
                    ArrayList<Etiqueta> etiquetas = new ArrayList<>();
                    for (int i = 0; i < etiquetasArray.length; i++) {
                        Etiqueta etiqueta = new Etiqueta((long) i, etiquetasArray[i]);
                        etiquetas.add(etiqueta);
                    }
                    Articulo temp = new Articulo( Long.parseLong(ctx.formParam("idart")),ctx.formParam("titulo"),ctx.formParam("cuerpo"),usuario);
                    temp.setListaEtiquetas(etiquetas);
                    Blogstorage.getInstancia().insertarArticulo(temp);
                    ctx.redirect("/posting");
                });
                get("/eliminar/{articuloid}", ctx->{
                    Blogstorage.getInstancia().eliminarArticuloById(ctx.pathParamAsClass("articuloid", long.class).get());
                    ctx.redirect("/posting");
                });
                get("/editar/{articuloid}", ctx->{
                    Articulo nuevo = Blogstorage.getInstancia().buscarArticuloById(ctx.pathParamAsClass("articuloid", long.class).get());
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Modificacion de Publicacion");
                    modelo.put("accion", "/posting/editar");
                    modelo.put("articulo",nuevo);
                    modelo.put("idart", ctx.pathParamAsClass("articuloid", long.class).get());
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/posting/crearEditarVisualizar.html", modelo);

                    //ctx.redirect("/posting");
                });
                post("/editar", ctx->{
                    String[] etiquetasArray = ctx.formParam("etiqueta").split(", ");
                    ArrayList<Etiqueta> etiquetas = new ArrayList<>();
                    for (int i = 0; i < etiquetasArray.length; i++) {
                        Etiqueta etiqueta = new Etiqueta((long) i, etiquetasArray[i]);
                        etiquetas.add(etiqueta);
                    }
                    System.out.println(Blogstorage.getInstancia().buscarArticuloById(Long.valueOf(ctx.formParam("idart"))).getAutor());
                    Articulo temp = new Articulo( Long.parseLong(ctx.formParam("idart")),ctx.formParam("titulo"),ctx.formParam("cuerpo"),Blogstorage.getInstancia().buscarArticuloById(Long.valueOf(ctx.formParam("idart"))).getAutor());
                    temp.setListaEtiquetas(etiquetas);
                    Blogstorage.getInstancia().actualizarArticulo(temp);
                    ctx.redirect("/posting");
                });
            });
        });
        app.routes(()->{
            path("/publicacion/",()->{
                get("/{articulo}", ctx->{

                    if(Blogstorage.getInstancia().buscarArticuloById(ctx.pathParamAsClass("articulo", Long.class).get()) != null){
                        Articulo temp = Blogstorage.getInstancia().buscarArticuloById(ctx.pathParamAsClass("articulo", Long.class).get());
                        Map<String, Object> modelo = new HashMap<>();
                        modelo.put("titulo", temp.getTitulo());
                        modelo.put("autor",temp.getAutor().getNombre());
                        modelo.put("fecha",temp.fechaFormateada());
                        modelo.put("cuerpo", temp.getCuerpo());
                        modelo.put("lista", temp.getListaComentarios());

                        Usuario usuario = ctx.sessionAttribute("usuario");
                        if(usuario!=null) {
                            modelo.put("tipo", "Logout");
                            modelo.put("sitio","/logout");
                        }else{
                            modelo.put("tipo", "Login");
                            modelo.put("sitio","/login");
                        }
                        if(usuario != null){
                            modelo.put("condicion", true);
                        }else{
                            modelo.put("condicion", false);
                        }
                        // ctx.render("/publico/post.html",modelo);
                        ctx.render("/publico/publicacion/publicacion.html",modelo);
                    }else{
                        ctx.redirect("/");
                    }
                });

                post("/{articulo}/comment", ctx ->{
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    Articulo temp = Blogstorage.getInstancia().buscarArticuloById(ctx.pathParamAsClass("articulo", Long.class).get());
                    Comentario newcoment = new Comentario(temp.generarCommentId(),ctx.formParam("texto"),usuario,temp);
                    Blogstorage.getInstancia().crearComentario(ctx.pathParamAsClass("articulo", Long.class).get(),newcoment);
                    ctx.redirect("/publicacion/"+ctx.pathParam("articulo"));
                });


            });
        });

        app.get("/logout", ctxContext -> {
            String id = ctxContext.req().getSession().getId();
            ctxContext.req().getSession().invalidate();
            ctxContext.redirect("/");

        });
    }
}
