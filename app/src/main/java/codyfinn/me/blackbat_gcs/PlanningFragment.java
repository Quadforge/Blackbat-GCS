package codyfinn.me.blackbat_gcs;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlanningFragment extends Fragment{

    private static final String TAG = "blackbat-gcs";
    PlanningFragment.OnPlanningFragmentInteractionListener mListener;
    ArrayList<Marker> markerList = new ArrayList<Marker>();

    MapView mapView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_planning, container, false);

        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        if(MapsInitializer.initialize(getActivity()) != ConnectionResult.SUCCESS){

        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 10);
        map.animateCamera(cameraUpdate);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(mListener.onWaypointToggled()){
                    DialogFragment newFragment = new ConfirmDeleteWaypointsDialogFragment();
                    newFragment.show(getFragmentManager(), "Confirm waypoint deletion");
                }
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if (mListener.onWaypointToggled()) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(String.valueOf(markerList.size() + 1)));
                    markerList.add(marker);
                    Log.i(TAG, markerList.toString());
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            int index;

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mListener.onWaypointToggled()) {
                    index = markerList.indexOf(marker);
                    markerList.remove(index);
                    marker.remove();
                    markerList.trimToSize();
                    for (int i = index; i < markerList.size(); i++){
                        markerList.get(i).setTitle(String.valueOf(i+1));
                    }
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mListener = (PlanningFragment.OnPlanningFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GetWaypointToggler");
        }

    }

    public interface OnPlanningFragmentInteractionListener {
        public boolean onWaypointToggled();
    }
}
