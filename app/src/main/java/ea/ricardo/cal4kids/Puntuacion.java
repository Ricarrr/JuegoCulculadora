package ea.ricardo.cal4kids;

import android.content.ContentValues;
import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class Puntuacion implements Serializable{
    private String jugador;
    private int puntos;
    private String modo;

    public Puntuacion(String jugador, int puntos, String modo) {
        this.jugador = jugador;
        this.puntos = puntos;
        this.modo = modo;
    }

    public Puntuacion() {
    }

    public String getJugador() {
        return jugador;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getModo() {
        return modo;
    }

    @Override
    public String toString() {
        return  jugador + " - " +  puntos + "->" + modo;
    }
}
