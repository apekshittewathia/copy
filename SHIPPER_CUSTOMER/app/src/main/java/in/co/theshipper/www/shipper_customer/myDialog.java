package in.co.theshipper.www.shipper_customer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Apekshit on 02-07-2016.
 */
public class myDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b=new AlertDialog.Builder(getActivity());
        b.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog,null));
        Dialog d=b.create();
        return d;
    }

}
