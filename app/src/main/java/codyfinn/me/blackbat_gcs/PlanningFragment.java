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

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.Marker;
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Locale;

public class PlanningFragment extends Fragment{

    private static final String TAG = "blackbat-gcs";
    PlanningFragment.OnPlanningFragmentInteractionListener mListener;
    ArrayList<Marker> markerList = new ArrayList<Marker>();

    MapView mapView;
    MyLocationNewOverlay myLocationNewOverlay;
    IMapController mapController;
    ResourceProxy mResourceProxy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_planning, container, false);

        mResourceProxy = new DefaultResourceProxyImpl(getActivity());
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapController = mapView.getController();
        BingMapTileSource.retrieveBingKey(getActivity());
        String mLocale = Locale.getDefault().getDisplayName();
        BingMapTileSource bing = new BingMapTileSource(mLocale);
        bing.setStyle(BingMapTileSource.IMAGERYSET_AERIAL);
        mapView.setTileSource(bing);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        this.myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), mapView, mResourceProxy);
        myLocationNewOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                mapController.animateTo(myLocationNewOverlay.getMyLocation());
            }
        });
        myLocationNewOverlay.disableMyLocation();
        myLocationNewOverlay.disableFollowLocation();
        mapView.getOverlays().add(this.myLocationNewOverlay);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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

    public ArrayList<Marker> getMarkerList(){
        return markerList;
    }
}
