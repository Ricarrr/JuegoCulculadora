package ea.ricardo.cal4kids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class JuegoDificil extends AppCompatActivity {
    private TextView txtNombreJugador, txtPuntos, txtOperacion, txtCorrecto, txtCorreccion;
    private EditText eTxtRespuesta;
    private Button btComprobar;
    private ImageView imgNum11, imgNum12, imgNum21, imgNum22, imgNum2, vida1, vida2, vida3;
    private long animDuration = 1600;
    private View viewOperacion;

    private int num11, num12, num21, num22, result, num01, num02;
    private int numVidas = 1;
    private int puntos = 0;
    //El modo 1 será fácil, 2 normal y 3 difícil
    private int modo;
    private String operacion;
    private String nombreJugador;

    protected void onRestart() {
        super.onRestart();
        if(numVidas <= 0){
            Intent intent = new Intent (getApplicationContext(), ModoJuego.class);
            intent.putExtra("nombreJugador", nombreJugador);
            startActivityForResult(intent, 0);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_dificil);

        txtNombreJugador = findViewById(R.id.txtNombreJugador);
        txtPuntos = findViewById(R.id.txtPuntos);
        btComprobar = findViewById(R.id.btComprobar);
        eTxtRespuesta = findViewById(R.id.eTxtRespuesta);
        imgNum11 = findViewById(R.id.imgNum11);
        imgNum12 = findViewById(R.id.imgNum12);
        imgNum21 = findViewById(R.id.imgNum21);
        imgNum22 = findViewById(R.id.imgNum22);
        imgNum2 = findViewById(R.id.imgNum2);
        vida1 = findViewById(R.id.vida1);
        vida2 = findViewById(R.id.vida2);
        vida3 = findViewById(R.id.vida3);
        txtOperacion = findViewById(R.id.txtOperacion);
        txtCorrecto = findViewById(R.id.txtCorrecto);
        txtCorreccion = findViewById(R.id.txtCorreccion);
        viewOperacion = findViewById(R.id.viewOperacion);

        nombreJugador = getIntent().getExtras().getString("nombreJugador");
        modo = getIntent().getExtras().getInt("modo");
        crearOpreacion();

        btComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String respuesta = eTxtRespuesta.getText().toString();
                if(!respuesta.isEmpty()){
                    if(respuesta.equals(String.valueOf(result))){
                        sumarPunto();
                        animacion(true);
                    }else{
                        restarVida();
                        animacion(false);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Primero indica tu respuesta!", Toast.LENGTH_SHORT).show();
                    eTxtRespuesta.requestFocus();
                }
            }
        });

        viewOperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),new String(num01 + operacion + num02).trim(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("nombreJugador", txtNombreJugador.getText().toString());
        outState.putInt("puntos",puntos);
        outState.putInt("modo",modo);
        outState.putInt("vidas",numVidas);
        outState.putInt("num1",num01);
        outState.putInt("num2",num02);
        outState.putInt("result",result);
        outState.putString("operacion",operacion);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        puntos = savedInstanceState.getInt("puntos");
        modo = savedInstanceState.getInt("modo");
        numVidas = savedInstanceState.getInt("vidas");
        operacion = savedInstanceState.getString("operacion");
        num01 = savedInstanceState.getInt("num1");
        num02 = savedInstanceState.getInt("num2");
        num11 = Integer.parseInt(String.valueOf(num01).substring(0,1));
        num12 = Integer.parseInt(String.valueOf(num01).substring(1,2));

        if(operacion.equals("×") || operacion.equals("÷")){
            num21 = 0;
            num22 = Integer.parseInt(String.valueOf(num02).substring(0,1));
        }else{
            num21 = Integer.parseInt(String.valueOf(num02).substring(0,1));
            num22 = Integer.parseInt(String.valueOf(num02).substring(1,2));
        }

        result = savedInstanceState.getInt("result");

        nombreJugador = savedInstanceState.getString("nombreJugador");

        renovarUI();
    }

    private void crearOpreacion(){
        if(numVidas <= 0){
            finPartida();
        }
        //Para el modo difícil hay sumas, restas, multiplicaciones y divisiones, las restas si pueden ser negativo
        String[] op2 = new String[]{"+","-","×","÷"};
        //Se elige entre suma y resta
        operacion = op2[(int) (Math.random()*op2.length +1)-1];

        //Se elige el primer numero
        num11 = (int) (Math.random()*9 +1);
        num12 = (int) (Math.random()*9 +1);
        num01 = Integer.parseInt("" + num11 + num12);

        //Si es multiplicación el segundo número solo será de una cifra
        if(operacion.equals("×")){
            num21 = 0;
            num22 = (int) (Math.random()*9 +1);
            num02 = Integer.parseInt("" + num22);
        }else if(operacion.equals("÷")){
            num21 = 0;
            num22 = (int) (Math.random()*9 +1);
            num02 = num22;

            //Busca un numero hasta que el resultado de la division sea exacto
            while(num01%num02 != 0 && num02!=0){
                num22 = (int) (Math.random()*9 +1);
                num02 = num22;
            }
        }else{
            num21 = (int) (Math.random()*9 +1);
            num22 = (int) (Math.random()*9 +1);
            num02 = Integer.parseInt("" + num21 + num22);
        }

        //Una vez elegidos los numeros se calcula el resultado, se limpia el editText y se escriben los números, el operador y pintan las imágenes
        this.calcularResult();

        //Actualizamos la interfaz
        renovarUI();
    }

    private void calcularResult(){
        switch (operacion){
            case "+":
                result = num01 + num02;
                break;
            case "-":
                result = num01 - num02;
                break;
            case "×":
                result = num01 * num02;
                break;
            case "÷":
                result = num01 / num02;
                break;
        }
    }

    private void restarVida(){
        numVidas--;
    }

    private void pintarCorazonesVida(){
        switch (numVidas){
            case 1:
                vida1.setImageResource(R.drawable.vida);
                vida2.setImageDrawable(null);
                vida3.setImageDrawable(null);
                break;
            case 2:
                vida1.setImageResource(R.drawable.vida);
                vida2.setImageResource(R.drawable.vida);
                animacionQuitarCorazon(vida3);
                break;
            case 3:
                vida1.setImageResource(R.drawable.vida);
                vida2.setImageResource(R.drawable.vida);
                vida3.setImageResource(R.drawable.vida);
                break;
            default:
                animacionQuitarCorazon(vida1);
                vida2.setImageDrawable(null);
                vida3.setImageDrawable(null);
                break;
        }
    }

    private void pintarNumeros(){
        imgNum11.setImageDrawable(getDrawable(getResources().getIdentifier("n"+num11,"drawable",this.getPackageName())));
        imgNum12.setImageDrawable(getDrawable(getResources().getIdentifier("n"+num12,"drawable",this.getPackageName())));

        if(operacion.equals("×") || operacion.equals("÷")){
            imgNum21.setImageDrawable(null);
            imgNum22.setImageDrawable(null);
            imgNum2.setImageDrawable(getDrawable(getResources().getIdentifier("n"+num02,"drawable",this.getPackageName())));
        }else{
            imgNum2.setImageDrawable(null);
            imgNum21.setImageDrawable(getDrawable(getResources().getIdentifier("n"+num21,"drawable",this.getPackageName())));
            imgNum22.setImageDrawable(getDrawable(getResources().getIdentifier("n"+num22,"drawable",this.getPackageName())));
        }
    }

    private void sumarPunto(){
        //Si el resultado tiene más de tres cifras suma 2 puntos, sino suma 1
        puntos = String.valueOf(result).length()>=3?puntos + 2: puntos + 1;
        puntos = String.valueOf(result).contains("-")?puntos + 1: puntos;
        //Si la operación es una multiplicación o division suma 4 puntos
        if(operacion.equals("÷") || operacion.equals("×")){
            puntos = puntos + 3;
        }
        txtPuntos.setText("" +puntos);
    }

    private void finPartida(){
        Intent intent = new Intent (this.getApplicationContext(), Derrota.class);
        intent.putExtra("nombreJugador", txtNombreJugador.getText().toString());
        intent.putExtra("puntos",puntos);
        intent.putExtra("modo",modo);
        intent.putExtra("vidas",numVidas);
        startActivityForResult(intent, 0);
        finish();
    }

    private void renovarUI(){
        eTxtRespuesta.setText("");
        txtOperacion.setText(operacion);
        txtPuntos.setText("" + puntos);
        txtNombreJugador.setText(nombreJugador);
        pintarNumeros();
        pintarCorazonesVida();
    }

    private void limpiarUI(){
        imgNum11.setImageDrawable(null);
        imgNum12.setImageDrawable(null);
        imgNum21.setImageDrawable(null);
        imgNum22.setImageDrawable(null);
        imgNum2.setImageDrawable(null);

        txtOperacion.setText("");
    }

    private void animacionQuitarCorazon(ImageView cora){
        ObjectAnimator aninmator1;
        ObjectAnimator aninmator2;
        aninmator1 = ObjectAnimator.ofFloat(cora,ImageView.SCALE_Y,cora.getScaleY(),0f);
        aninmator2 = ObjectAnimator.ofFloat(cora,ImageView.SCALE_X,cora.getScaleX(),0f);
        aninmator1.setDuration(animDuration);
        aninmator2.setDuration(animDuration);
        aninmator1.start();
        aninmator2.start();
        aninmator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cora.setImageDrawable(null);
            }
        });
    }

    private void animacion(boolean acierto){
        renovarUI();
        btComprobar.setEnabled(false);
        txtCorrecto.setText(acierto?getString(R.string.correctCAPS):getString(R.string.errorCAPS));
        txtCorrecto.setTextColor(acierto? Color.parseColor("#558B2F"): Color.parseColor("#F42A1B"));
        txtCorreccion.setTextColor(acierto? Color.parseColor("#558B2F"): Color.parseColor("#F42A1B"));
        txtCorreccion.setText(num01 + " " + operacion + " " + num02 + " = " + result);
        limpiarUI();
        ObjectAnimator aninmator1;
        ObjectAnimator aninmator2;
        aninmator1 = ObjectAnimator.ofFloat(txtCorrecto,View.ALPHA,0.0f,3.0f);
        aninmator2 = ObjectAnimator.ofFloat(txtCorreccion,View.ALPHA,0.0f,3.0f);
        aninmator1.setDuration(animDuration);
        aninmator2.setDuration(animDuration);
        aninmator1.start();
        aninmator2.start();
        aninmator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                txtCorrecto.setText("");
                txtCorreccion.setText("");
                btComprobar.setEnabled(true);
                crearOpreacion();
                renovarUI();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_return, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itmHome:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}