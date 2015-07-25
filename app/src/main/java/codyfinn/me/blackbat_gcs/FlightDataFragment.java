package codyfinn.me.blackbat_gcs;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;


public class FlightDataFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "Blackbat:FlightData: ";
    private ToggleButton connectionToggleButton;
    private ToggleButton waypointToggleButton;
    private ToggleButton armButton;

    private OnFragmentInteractionListener mListener;

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
        armButton = (ToggleButton) inflatedView.findViewById(R.id.arm_button);
        armButton.setOnClickListener(this);
        return inflatedView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connection_button:
                mListener.connectButtonTap();
                Log.i(TAG, "Connection button pressed");
                break;
            case R.id.waypoint_button:
                Log.i(TAG, "Waypoint button pressed");
                break;
            case R.id.arm_button:
                mListener.armButtonTap();
                Log.i(TAG, "armed button pressed");
                break;
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mListener = (FlightDataFragment.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GetWaypointToggler");
        }

    }

    public boolean getWaypointCheckedStatus(){

        return waypointToggleButton.isChecked();
    }

    public ToggleButton getConnectionToggleButton(){
        return connectionToggleButton;
    }

    public ToggleButton getArmButton(){
        return armButton;
    }

    public interface OnFragmentInteractionListener{
        void connectButtonTap();
        void armButtonTap();
    }
}
