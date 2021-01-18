package ea.ricardo.cal4kids.ui.main;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Comparator;

import ea.ricardo.cal4kids.ConsultasBD;
import ea.ricardo.cal4kids.Puntuacion;
import ea.ricardo.cal4kids.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private int mode;
    private TableLayout tblRank;
    private String nombreJug;

    public static PlaceholderFragment newInstance(int index, String nombreJug) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString("nombreJugador",nombreJug);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            this.nombreJug = getArguments().getString("nombreJugador");
            mode = index;
        }
        pageViewModel.setIndex(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        tblRank = view.findViewById(R.id.tblRank);
        ArrayList<Puntuacion> punt = new ArrayList<>();

        switch (mode){
            case 1:
                ConsultasBD bd = new ConsultasBD(getContext());
                punt = bd.rankFacil();
                break;
            case 2:
                ConsultasBD bd2 = new ConsultasBD(getContext());
                punt = bd2.rankNormal();
                break;
            case 3:
                ConsultasBD bd3 = new ConsultasBD(getContext());
                punt = bd3.rankExtremo();
                break;
        }
        punt.sort(new Comparator<Puntuacion>() {
            @Override
            public int compare(Puntuacion o1, Puntuacion o2) {
                if(o1.getPuntos()>o2.getPuntos()){
                    return -1;
                }else if(o1.getPuntos()<o2.getPuntos()){
                    return 1;
                }else{
                    return 0;
                }
            }
        });

        for(Puntuacion p : punt){
            TableRow tr = new TableRow(getContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;

            tr.setLayoutParams(lp);
            TextView txtNombreJug = new TextView(getContext());
            txtNombreJug.setText(p.getJugador());
            txtNombreJug.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
            txtNombreJug.setLayoutParams(lp);
            txtNombreJug.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView txtPuntos = new TextView(getContext());
            txtPuntos.setText("" + p.getPuntos());
            txtPuntos.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
            txtPuntos.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txtPuntos.setLayoutParams(lp);
            if(p.getJugador().equals(this.nombreJug)){
                txtNombreJug.setBackgroundColor(R.color.colorSecondary);
                txtPuntos.setBackgroundColor(R.color.colorSecondary);
            }

            tr.addView(txtNombreJug);
            tr.addView(txtPuntos);
            tblRank.addView(tr,tblRank.getChildCount());
        }
        return view;
    }
}