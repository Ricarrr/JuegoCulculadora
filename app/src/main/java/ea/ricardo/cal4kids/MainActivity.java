package ea.ricardo.cal4kids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

//ICONOS DE ANIMALES DISEÑADOS POR Katemangostar / Freepik editado por Ricardo
//ICONO DE APLICACIÓN DISEÑADO POR catalyststuff / Freepik editado por Ricardo

public class MainActivity extends AppCompatActivity {
    private ArrayList<Puntuacion> historial = new ArrayList<Puntuacion>();
    private EditText txtNombre;
    private Button btJugar, btHistorial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.eTxtNombre);
        btJugar = findViewById(R.id.btJugar);
        btHistorial = findViewById(R.id.btHistorico);

        if(getIntent().getExtras() != null){
            txtNombre.setText(getIntent().getExtras().getString("nombreJugador"));
        }


        btJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(txtNombre.getText().toString())){
                    Intent intent = new Intent (v.getContext(), ModoJuego.class);
                    intent.putExtra("nombreJugador", txtNombre.getText().toString());
                    intent.putExtra("historial",historial);
                    startActivityForResult(intent, 0);
                }else{
                    Toast.makeText(getApplicationContext(),"Introduce tu nombre!", Toast.LENGTH_SHORT).show();
                    txtNombre.requestFocus();
                }
            }
        });

        btHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Ranking.class);
                intent.putExtra("nombreJugador", txtNombre.getText().toString());
                intent.putExtra("historial",historial);
                startActivityForResult(intent, 0);
            }
        });


    }




}