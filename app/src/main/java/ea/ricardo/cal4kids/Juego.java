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

public class Juego extends AppCompatActivity {
    private TextView txtNombreJugador, txtPuntos, txtOperacion, txtCorrecto, txtCorreccion;
    private EditText eTxtRespuesta;
    private Button btComprobar;
    private ImageView imgNum1, imgNum2, vida1, vida2, vida3;
    private long animDuration = 1600;
    private View viewOperacion;

    private int num1, num2, result;
    private int numVidas = 3;
    private int puntos = 0;
    //El modo 1 será fácil, 2 normal y 3 difícil
    private int modo;
    private String operacion;
    private String nombreJugador;

    @Override
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
        setContentView(R.layout.activity_juego);

        txtNombreJugador = findViewById(R.id.txtNombreJugador);
        txtPuntos = findViewById(R.id.txtPuntos);
        btComprobar = findViewById(R.id.btComprobar);
        eTxtRespuesta = findViewById(R.id.eTxtRespuesta);
        imgNum1 = findViewById(R.id.imgNum12);
        imgNum2 = findViewById(R.id.imgNum21);
        vida1 = findViewById(R.id.vida1);
        vida2 = findViewById(R.id.vida3);
        vida3 = findViewById(R.id.vida2);
        txtOperacion = findViewById(R.id.txtOperacion);
        txtCorrecto = findViewById(R.id.txtCorrecto);
        txtCorreccion = findViewById(R.id.txtCorreccion);
        viewOperacion = findViewById(R.id.viewOperacion);

        nombreJugador = getIntent().getExtras().getString("nombreJugador");
        modo = getIntent().getExtras().getInt("modo");
        crearOpreacion();
        renovarUI();

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
                Toast.makeText(getApplicationContext(),new String(num1 + operacion + num2).trim(),Toast.LENGTH_SHORT).show();
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
        outState.putInt("num1",num1);
        outState.putInt("num2",num2);
        outState.putInt("result",result);
        outState.putString("operacion",operacion);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        numVidas = savedInstanceState.getInt("vidas");
        nombreJugador = savedInstanceState.getString("nombreJugador");
        puntos = savedInstanceState.getInt("puntos");
        modo = savedInstanceState.getInt("modo");
        num1 = savedInstanceState.getInt("num1");
        num2 = savedInstanceState.getInt("num2");
        result = savedInstanceState.getInt("result");
        operacion = savedInstanceState.getString("operacion");
        renovarUI();
    }

    private void crearOpreacion(){
        if(numVidas <= 0){
            finPartida();
        }
        switch (modo) {
            case 1:
                //Para el modo fácil solo hay sumas hasta obtener 10 puntos o más, luego añade restas, para las restas no dejamos que el resultado sea negativo
                String[] op;
                if(puntos >=10){
                    op = new String[]{"+", "-"};
                }else{
                    op = new String[]{"+"};
                }
                operacion = op[(int) (Math.random() * op.length + 1) - 1];
                //Se eligen los números
                num1 = (int) (Math.random() * 9 + 1);
                num2 = (int) (Math.random() * 9 + 1);
                //Si es una resta Busca un numero para el segundo que sea menor o igual al num1, para que no haya negativos
                if (operacion.equals("-")) {
                    while (num1 < num2) {
                        num2 = (int) (Math.random() * 9 + 1);
                    }
                }
                break;
            case 2:
                //Para el modo normal hay sumas, restas, multiplicaciones y divisiones para las restas no dejamos que el resultado sea negativo
                // Y para las divisiones solo deja dividir entre un número que de un resultado exacto
                String[] op2;
                //Si tiene más de 10 puntos añade multiplicaciones y si tiene más de 30 añade divisiones
                if(puntos >= 10 && puntos < 30){
                    op2 = new String[]{"+", "-", "×"};
                }else if(puntos >= 30){
                    op2 = new String[]{"+", "-", "×", "÷"};
                }else{
                    op2 = new String[]{"+", "-"};
                }

                //Se elige la operación
                operacion = op2[(int) (Math.random() * op2.length + 1) - 1];
                //Se eligen los números
                num1 = (int) (Math.random() * 9 + 1);
                num2 = (int) (Math.random() * 9 + 1);
                //Si es una resta Busca un numero para el segundo que sea menor o igual al num1, para que no haya negativos
                if (operacion.equals("-")) {
                    while (num1 < num2) {
                        num2 = (int) (Math.random() * 9 + 1);
                    }
                }
                //Si es una división Busca un numero para el segundo para que el resultado de la division sea exacto
                if (operacion.equals("÷")) {
                    while (num1 % num2 != 0 && num2 != 0) {
                        num2 = (int) (Math.random() * 9 + 1);
                    }
                }
                break;
        }
        //Una vez elegidos los numeros se calcula el resultado
        this.calcularResult();
        //renovarUI();
    }

    private void calcularResult(){
        switch (operacion){
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "×":
                result = num1 * num2;
                break;
            case "÷":
                result = num1 / num2;
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
                animacionQuitarCorazon(vida2);
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
                break;
        }
    }

    private void pintarNumeros(){
        imgNum1.setImageDrawable(getDrawable(getResources().getIdentifier("n"+num1,"drawable",this.getPackageName())));
        imgNum2.setImageDrawable(getDrawable(getResources().getIdentifier("n"+num2,"drawable",this.getPackageName())));
    }

    private void sumarPunto(){
        int puntosInicial = puntos;
        //Si el resultado tiene más de dos cifras suma 2 puntos, sino suma 1
        puntos = String.valueOf(result).length()>=2?puntos + 2: puntos + 1;
        //Si la operación es una multiplicación o division suma 4 puntos
        if(operacion.equals("÷") || operacion.equals("×")){
            puntos = puntos + 3;
        }
        if(modo == 1){
            if(puntosInicial < 10 && puntos >=10){
               new SubirNivelDialog(getString(R.string.level1Easy)).show(getSupportFragmentManager(),"");
            }
        }else if(modo == 2){
            if(puntosInicial < 10 && puntos>=10){
                new SubirNivelDialog(getString(R.string.level1Normal)).show(getSupportFragmentManager(),"");
            }else if(puntosInicial >=10 && puntosInicial <30 && puntos>=30){
                new SubirNivelDialog(getString(R.string.level2Normal)).show(getSupportFragmentManager(),"");
            }
        }
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

    private void limpiarUI() {
        imgNum1.setImageDrawable(null);
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
        txtCorreccion.setText(num1 + " " + operacion + " " + num2 + " = " + result);
        limpiarUI();
        ObjectAnimator aninmator1;
        ObjectAnimator aninmator2;
        aninmator1 = ObjectAnimator.ofFloat(txtCorrecto,View.ALPHA,0.0f,1.0f);
        aninmator2 = ObjectAnimator.ofFloat(txtCorreccion,View.ALPHA,0.0f,1.0f);
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
