package ea.ricardo.cal4kids;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class SubirNivelDialog extends DialogFragment {

    private String msg;
    public SubirNivelDialog(String msg){
        this.msg = msg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                }).setTitle(getString(R.string.levelUp));
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
