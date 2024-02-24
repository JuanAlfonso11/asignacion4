package org.example.util;
import io.javalin.Javalin;

public abstract class BaseControler {
    protected Javalin app;

    public BaseControler(Javalin app){
        this.app = app;
    }
    abstract public void aplicarRutas();

}
