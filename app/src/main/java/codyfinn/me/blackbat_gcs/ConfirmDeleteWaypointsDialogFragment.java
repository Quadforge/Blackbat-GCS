package codyfinn.me.blackbat_gcs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ConfirmDeleteWaypointsDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete All waypoints?").setPositiveButton("Confrim", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
               mListener.onDialogPositiveClick(ConfirmDeleteWaypointsDialogFragment.this);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    public interface DeleteWaypointDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    DeleteWaypointDialogListener mListener;

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (DeleteWaypointDialogListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement DeleteWaypointListener");
        }
    }
}
