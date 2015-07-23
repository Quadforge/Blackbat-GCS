package codyfinn.me.blackbat_gcs;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.o3dr.android.client.interfaces.DroneListener;
import com.o3dr.android.client.interfaces.TowerListener;
import com.o3dr.services.android.lib.drone.connection.ConnectionResult;

public class FlightDataFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Blackbat:FlightData: ";
    private OnFlightDataFragmentInteractionListener mListener;
    private ToggleButton connectionToggleButton;
    private ToggleButton waypointToggleButton;

    public FlightDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_flight_data, container, false);
        connectionToggleButton = (ToggleButton) inflatedView.findViewById(R.id.connection_button);
        connectionToggleButton.setOnClickListener(this);
        waypointToggleButton = (ToggleButton) inflatedView.findViewById(R.id.waypoint_button);
        waypointToggleButton.setOnClickListener(this);
        return inflatedView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFlightDataFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.connection_button:
                connectionToggleButton.setChecked(mListener.ConnectButtonTap());
                Log.i(TAG, String.valueOf("value: " + mListener.ConnectButtonTap()));
                Log.i(TAG, "Connection button pressed");
                break;
            case R.id.waypoint_button:
                Log.i(TAG, "Waypoint button pressed");
                break;
        }
    }

    public interface OnFlightDataFragmentInteractionListener {
        boolean ConnectButtonTap();
    }

    public boolean getWaypointCheckedStatus(){
        return waypointToggleButton.isChecked();
    }
}
