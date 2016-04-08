package codyfinn.me.blackbat_gcs;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Locale;

public class PlanningFragment extends Fragment implements MapEventsReceiver{

    private static final String TAG = "blackbat-gcs";
    PlanningFragment.OnPlanningFragmentInteractionListener mListener;
    ArrayList<GeoPoint> markerList = new ArrayList<>();
    ArrayList<Marker> markerIconList = new ArrayList<>();

    MapView mapView;
    MyLocationNewOverlay myLocationNewOverlay;
    IMapController mapController;
    ResourceProxy mResourceProxy;
    MapEventsOverlay mMapEventsOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_planning, container, false);

        mResourceProxy = new DefaultResourceProxyImpl(getActivity());
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.setMinZoomLevel(10);
        mapView.setMaxZoomLevel(20);
        mapController = mapView.getController();
        BingMapTileSource.retrieveBingKey(getActivity());
        String mLocale = Locale.getDefault().getDisplayName();
        BingMapTileSource bing = new BingMapTileSource(mLocale);
        bing.setStyle(BingMapTileSource.IMAGERYSET_AERIAL);
        mapView.setTileSource(bing);
        mapView.setMultiTouchControls(true);

        mMapEventsOverlay = new MapEventsOverlay(getActivity(), this);

        myLocationNewOverlay = new MyLocationNewOverlay(mapView.getContext(), mapView);
        myLocationNewOverlay.enableFollowLocation();
        mapView.getOverlays().add(this.myLocationNewOverlay);
        myLocationNewOverlay.enableMyLocation();

        mapController.setZoom(20);

        mapView.getOverlays().add(0, mMapEventsOverlay);

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

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        if(mListener.onWaypointToggled()) {
            markerList.add(p);
            Marker marker = new Marker(mapView);
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            markerIconList.add(marker);
            marker.setTitle(Integer.toString(markerIconList.size()));
            mapView.getOverlays().add(marker);
            mapView.invalidate();
            return false;
        }
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    public interface OnPlanningFragmentInteractionListener {
        public boolean onWaypointToggled();
    }

    public ArrayList<GeoPoint> getMarkerList(){
        return markerList;
    }
}
