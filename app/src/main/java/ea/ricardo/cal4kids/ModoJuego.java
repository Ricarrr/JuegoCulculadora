package ea.ricardo.cal4kids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ModoJuego extends AppCompatActivity {
    private Button btFacil, btNormal, btDificil, btHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_juego);
        btFacil = findViewById(R.id.btFacil);
        btNormal = findViewById(R.id.btNormal);
        btDificil = findViewById(R.id.btDificil);

        btFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Juego.class);
                intent.putExtra("nombreJugador", getIntent().getExtras().getString("nombreJugador"));
                intent.putExtra("modo", 1);
                intent.putExtra("historial",getIntent().getExtras().getSerializable("historial"));
                startActivityForResult(intent, 0);
            }
        });

        btNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Juego.class);
                intent.putExtra("nombreJugador", getIntent().getExtras().getString("nombreJugador"));
                intent.putExtra("modo", 2);
                intent.putExtra("historial",getIntent().getExtras().getSerializable("historial"));
                startActivityForResult(intent, 0);
            }
        });

        btDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), JuegoDificil.class);
                intent.putExtra("nombreJugador", getIntent().getExtras().getString("nombreJugador"));
                intent.putExtra("modo", 3);
                intent.putExtra("historial",getIntent().getExtras().getSerializable("historial"));
                startActivityForResult(intent, 0);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        getMenuInflater().inflate(R.menu.menu_return, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itmInfo:
                Intent intent = new Intent (getApplicationContext(), InfoActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.itmHome:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}