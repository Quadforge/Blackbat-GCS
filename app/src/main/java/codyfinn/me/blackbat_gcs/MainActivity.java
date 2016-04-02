package codyfinn.me.blackbat_gcs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.apis.drone.DroneStateApi;
import com.o3dr.android.client.interfaces.DroneListener;
import com.o3dr.android.client.interfaces.TowerListener;
import com.o3dr.services.android.lib.coordinate.LatLong;
import com.o3dr.services.android.lib.coordinate.LatLongAlt;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.connection.ConnectionParameter;
import com.o3dr.services.android.lib.drone.connection.ConnectionResult;
import com.o3dr.services.android.lib.drone.connection.ConnectionType;
import com.o3dr.services.android.lib.drone.mission.Mission;
import com.o3dr.services.android.lib.drone.mission.MissionItemType;
import com.o3dr.services.android.lib.drone.mission.item.MissionItem;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Waypoint;
import com.o3dr.services.android.lib.drone.property.State;
import com.o3dr.services.android.lib.drone.property.Type;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements PlanningFragment.OnPlanningFragmentInteractionListener,
                                                              ConfirmDeleteWaypointsDialogFragment.DeleteWaypointDialogListener,
                                                              FlightDataFragment.OnFragmentInteractionListener,
                                                              DroneListener,
                                                              TowerListener{

    //Dronekit
    Drone drone;
    private int droneType = Type.TYPE_UNKNOWN;
    ControlTower controlTower;
    private final Handler handler = new Handler();

    public static FragmentManager fragmentManager;
    FlightDataFragment flightDataFragment;
    PlanningFragment planningFragment;


    public final String TAG = "blackbat-gcs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drone = new Drone(getApplicationContext());
        controlTower = new ControlTower(getApplicationContext());

        fragmentManager = getFragmentManager();
        flightDataFragment = (FlightDataFragment) fragmentManager.findFragmentById(R.id.fragment_flight_data);
        planningFragment = (PlanningFragment) fragmentManager.findFragmentById(R.id.fragment_planning);
    }

    @Override
    protected void onStart(){
        super.onStart();
        controlTower.connect(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(this.drone.isConnected()){
            drone.disconnect();
            // set connection button to false
        }
        controlTower.unregisterDrone(drone);
        controlTower.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        planningFragment.markerList.clear();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Do nothing
    }

    @Override
    public void onDroneConnectionFailed(ConnectionResult connectionResult) {
        alertUser("Drone not connected");
    }

    @Override
    public void onDroneEvent(String event, Bundle extras) {
        switch (event){
            case AttributeEvent.STATE_CONNECTED:
                flightDataFragment.getConnectionToggleButton().setChecked(true);
                alertUser("DroneConnected");
                break;
            case AttributeEvent.STATE_CONNECTING:
                alertUser("Drone is connecting");
            case AttributeEvent.STATE_DISCONNECTED:
                flightDataFragment.getConnectionToggleButton().setChecked(false);
                alertUser("Drone Disconnected");
                flightDataFragment.getArmButton().setChecked(false);
                break;
            case AttributeEvent.STATE_ARMING:
                alertUser("Drone Armed");
                break;
            case AttributeEvent.STATE_UPDATED:
                State state = drone.getAttribute(AttributeType.STATE);
                flightDataFragment.getArmButton().setChecked(!state.isArmed());
        }
    }

    @Override
    public void onDroneServiceInterrupted(String s) {

    }

    @Override
    public void onTowerConnected() {
        controlTower.registerDrone(drone, handler);
        drone.registerDroneListener(this);
        alertUser("Connected to 3DR services");
    }

    @Override
    public void onTowerDisconnected() {
        alertUser("Disconnected from 3DR services");
    }

    //Fragment Listeners

    @Override
    public boolean onWaypointToggled() {
        return flightDataFragment.getWaypointCheckedStatus();
    }

    @Override
    public void connectButtonTap() {
       if(drone.isConnected()){
           drone.disconnect();
       }
       else{
           Bundle extraParams = new Bundle();
           extraParams.putInt(ConnectionType.EXTRA_USB_BAUD_RATE, 57600);
           ConnectionParameter connectionParams = new ConnectionParameter(ConnectionType.TYPE_USB, extraParams, null);
           drone.connect(connectionParams);
       }
    }

    @Override
    public void armButtonTap() {
        if(drone.isConnected()) {
            State currentState = drone.getAttribute(AttributeType.STATE);
            DroneStateApi.arm(drone, !currentState.isArmed());
        }
        else{
            alertUser("Drone is not connected so I cant arm");
            flightDataFragment.getArmButton().setChecked(false);
        }
    }

    protected void alertUser(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    Mission makeMission(ArrayList<Marker> markerList){
        Mission mission = new Mission();
        LatLng positon;
        LatLongAlt newLatLongAlt;
        Waypoint waypoint = new Waypoint();
        for(int i = 0; i < markerList.size() - 1; i++){
            positon = markerList.get(i).getPosition();
            newLatLongAlt = new LatLongAlt(positon.latitude, positon.longitude, 0);
            waypoint.setCoordinate(newLatLongAlt);
            mission.addMissionItem(waypoint);
        }
        return mission;
    }
}