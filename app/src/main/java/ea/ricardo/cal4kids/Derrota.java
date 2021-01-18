package ea.ricardo.cal4kids;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Derrota extends AppCompatActivity {
    private String jugador;
    private int puntos = 0;
    private int modo;
    private int numVidas = 0;

    private Button btVolverAJugar, btCambiarModo, btMenuPpal;
    private TextView txtNombreJugador, txtPuntos;

    private void recuperarInfo(){
        jugador = getIntent().getExtras().getString("nombreJugador");
        puntos = getIntent().getExtras().getInt("puntos");
        numVidas = getIntent().getExtras().getInt("vidas");
        modo = getIntent().getExtras().getInt("modo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derrota);
        btVolverAJugar = findViewById(R.id.btVolverAJugar);
        btCambiarModo = findViewById(R.id.btCambiarModo);
        btMenuPpal = findViewById(R.id.btMenuPpal);
        txtNombreJugador = findViewById(R.id.txtNombreJugador);
        txtPuntos = findViewById(R.id.txtPuntos);

        recuperarInfo();

        txtNombreJugador.setText(jugador);
        txtPuntos.setText("" + puntos);

        btVolverAJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarHistorial(jugador,puntos,modo);
                if(modo == 3){
                    Intent intent = new Intent (v.getContext(), JuegoDificil.class);
                    intent.putExtra("nombreJugador", jugador);
                    intent.putExtra("modo",modo);
                    intent.putExtra("puntos",puntos);
                    startActivityForResult(intent, 0);
                }else{
                    Intent intent = new Intent (v.getContext(), Juego.class);
                    intent.putExtra("nombreJugador", jugador);
                    intent.putExtra("modo",modo);
                    intent.putExtra("puntos",puntos);
                    startActivityForResult(intent, 0);
                }
                finish();
            }
        });
        btCambiarModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarHistorial(jugador,puntos,modo);
                Intent intent = new Intent (v.getContext(), ModoJuego.class);
                intent.putExtra("nombreJugador", jugador);
                //intent.putExtra("puntos",puntos);
                startActivityForResult(intent, 0);
                finish();
            }
        });
        btMenuPpal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarHistorial(jugador,puntos,modo);
                Intent intent = new Intent (v.getContext(), MainActivity.class);
                intent.putExtra("nombreJugador", jugador);
                //intent.putExtra("puntos",puntos);
                startActivityForResult(intent, 0);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("nombreJugador", jugador);
        outState.putInt("puntos",puntos);
        outState.putInt("modo",modo);
        outState.putInt("vidas",numVidas);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        puntos = savedInstanceState.getInt("puntos");
        modo = savedInstanceState.getInt("modo");
        jugador = savedInstanceState.getString("nombreJugador");
        txtPuntos.setText("" + puntos);
    }

    private void agregarHistorial(String jugador, int puntos, int modo){
        String sModo = "";
        switch (modo){
            case 1:
                sModo = "FÁCIL";
                break;
            case 2:
                sModo = "NORMAL";
                break;
            case 3:
                sModo = "EXTREMO";
                break;
        }
        ConsultasBD cons = new ConsultasBD(getApplicationContext());
        cons.añade(jugador,puntos,sModo);
    }
}

