package codyfinn.me.blackbat_gcs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;

import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.interfaces.DroneListener;
import com.o3dr.android.client.interfaces.TowerListener;
import com.o3dr.services.android.lib.drone.connection.ConnectionParameter;
import com.o3dr.services.android.lib.drone.connection.ConnectionResult;
import com.o3dr.services.android.lib.drone.connection.ConnectionType;
import com.o3dr.services.android.lib.drone.property.Type;


public class MainActivity extends FragmentActivity implements FlightDataFragment.OnFlightDataFragmentInteractionListener,
                                                              PlanningFragment.OnPlanningFragmentInteractionListener,
                                                              ConfirmDeleteWaypointsDialogFragment.DeleteWaypointDialogListener,
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
        planningFragment.map.clear();
        planningFragment.markerList.clear();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Do nothing
    }

    @Override
    public void onDroneConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onDroneEvent(String s, Bundle bundle) {

    }

    @Override
    public void onDroneServiceInterrupted(String s) {

    }

    @Override
    public void onTowerConnected() {
        controlTower.registerDrone(drone, handler);
        drone.registerDroneListener(this);
    }

    @Override
    public void onTowerDisconnected() {

    }

    //Fragment Listeners

    @Override
    public boolean onWaypointToggled() {
        return flightDataFragment.getWaypointCheckedStatus();
    }

    @Override
    public boolean ConnectButtonTap() {
       if(drone.isConnected()){
           drone.disconnect();
       }
       else{
           Bundle extraParams = new Bundle();
           extraParams.putInt(ConnectionType.EXTRA_USB_BAUD_RATE, 57600);
           ConnectionParameter connectionParams = new ConnectionParameter(ConnectionType.TYPE_USB, extraParams, null);
           drone.connect(connectionParams);
       }
        return drone.isConnected();
    }
}
