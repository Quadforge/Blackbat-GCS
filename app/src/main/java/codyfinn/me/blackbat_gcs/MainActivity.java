package codyfinn.me.blackbat_gcs;

import android.app.FragmentManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;


public class MainActivity extends FragmentActivity implements FlightDataFragment.OnFragmentInteractionListener, PlanningFragment.OnFragmentInteractionListener{

    public static FragmentManager fragmentManager;
    ToggleButton waypointButton;
    FlightDataFragment flightDataFragment;

    public final String TAG = "blackbat-gcs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        flightDataFragment = (FlightDataFragment) fragmentManager.findFragmentById(R.id.fragment_flight_data);
        waypointButton = flightDataFragment.getWaypointToggleButton();
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
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public boolean onWaypointToggled() {
        Log.i(TAG, "the toggle button state is: " + waypointButton.isChecked());
        return waypointButton.isChecked();
    }
}
