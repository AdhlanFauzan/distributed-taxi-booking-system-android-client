package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.passenger;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state.RequestRideStateFragment;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.formater.time.HourMinutesSecondsFormatter;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.formater.time.TimeFormatter;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.cache.AllTaxis;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Taxi;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.config.DtbsPreferences;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.gps.GpsLocationListener;

import java.util.Observable;
import java.util.Observer;

/**
 * Controller for managing a passengers interaction with the map.
 * Handles all operations related to updating of taxis and a user's location.
 *
 * @author robertnorthard
 */
public class PassengerMapFragment extends Fragment implements Observer {

    // TAG used for logging.
    private static final String TAG = PassengerMapFragment.class.getName();

    /**
     * Constants for UI settings.
     */
    private static final int CAMERA_ZOOM_LEVEL = 14;
    private static final int CAMERA_ANIMATE_DURATION = 2000;

    private AllTaxis allTaxis;

    private MapView mapView;
    private GoogleMap map;
    private EditText pickupLocation;
    private TextView waitTime;
    private Button btnBookTaxi;

    private TimeFormatter timeFormatter;

    /**
     * Constructor for class passenger map fragment.
     */
    public PassengerMapFragment() {
        this.timeFormatter = new HourMinutesSecondsFormatter();
    }

    /**
     * The system calls this method when creating the fragment.
     * Initialises essential components of the fragment
     * @param savedInstanceState state to restore.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.allTaxis = AllTaxis.getInstance();

        // start GPS service
        Intent gpsService = new Intent(getActivity(), GpsLocationListener.class);
        getActivity().startService(gpsService);

        // Subscribe to event broadcasters
        this.allTaxis.addObserver(this);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(DtbsPreferences.BOOKING_EVENTS_TOPIC));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mLocationReceiver,
                new IntentFilter(DtbsPreferences.LOCATION_EVENT));
    }

    /**
     * The system calls this callback when it's time for the
     * fragment to draw its user interface for the first time.
     * To draw a UI for your fragment, you must return a
     * View component from this method that is the root of your fragment's layout.
     * You can return null if the fragment does not provide a UI.
     *
     * @param inflater inflate layout.
     * @param container a collection of views.
     * @param savedInstanceState saved data.
     * @return the inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        pickupLocation = (EditText) v.findViewById(R.id.txt_pickup_location);
        waitTime = (TextView)v.findViewById(R.id.view_wait_time);
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this.getActivity());
        this.initialiseMap();

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_map_state_frame, new RequestRideStateFragment(), "RequestRideFragment");
        ft.commit();

        return v;
    }

    /**
     * The system calls this method as the first indication that the
     * user is leaving the fragment.
     */
    @Override
    public void onPause(){
        super.onPause();
        AllTaxis.getInstance().deleteObserver(this);
        this.unregisterLocationBroadcastManager();
    }

    /**
     * Invoked when fragment becomes active.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        AllTaxis.getInstance().addObserver(this);
    }

    /**
     * called to do final clean up of the fragment's
     * state but Not guaranteed to be called by the Android platform
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Invoked when the system is running low on memory.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Initialise map view with user's current location.
     */
    public void initialiseMap(){
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setMyLocationEnabled(true);
        map.setBuildingsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * Unregister from local broadcast manager.
     */
    private void unregisterLocationBroadcastManager(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    /**
     * Invoked by model/services to inform the view that there has been an update.
     *
     * @param observable callback.
     * @param data the update event.
     */
    @Override
    public void update(Observable observable, Object data){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                redrawGoogleMap();

                /*
                Update user's average taxi wait time.
                Throws illegal state exception if no taxis available, to prevent divide by 0 exception.
                */
                try {
                    updateWaitTime(allTaxis.getAverageWaitTimeInSeconds());
                } catch (IllegalStateException ex) {
                    Log.i(TAG, ex.getMessage());
                }
            }
        });
    }

    /**
     * Redraw google maps with taxis.
     * Invoked on taxi location update.
     */
    private void redrawGoogleMap(){

        map.clear();

        for (Taxi t : allTaxis.findAll()) {
            LatLng location = new LatLng(t.getLocation().getLatitude(), t.getLocation().getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.position(location);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_taxi_icon));
            map.addMarker(options);
        }
    }

    /**
     * Update wait time.
     *
     * @param time time in seconds.
     */
    private void updateWaitTime(int time){
        waitTime.setText(
                timeFormatter.format(time));
    }

    /**
     * Update address.
     * Display "Address not found" if address is null.
     *
     * @param address new address.
     */
    private void updateAddress(String address){
        if(address == null){
            pickupLocation.setText("Address not found.");
        }else{
            pickupLocation.setText(address);
        }
    }

    /**
     * Move cameras to specified latitude and longitude.
     *
     * @param latitude latitude to move camera to.
     * @param longitude longitude to move camera to.
     */
    private void moveCamera(double latitude, double longitude){

        // move camera to user's new location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(
                        latitude,
                        longitude),
                CAMERA_ZOOM_LEVEL));

        map.animateCamera(
                CameraUpdateFactory
                        .zoomTo(CAMERA_ZOOM_LEVEL),
                CAMERA_ANIMATE_DURATION,
                null);
    }

    // Broadcaster receiver to deal with user location changes.
    private BroadcastReceiver mLocationReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // update user's address.
            updateAddress(intent.getStringExtra("address"));

            // move camera to user's new location
            moveCamera(intent.getDoubleExtra("latitude", 0),
                    intent.getDoubleExtra("longitude", 0));
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    };
}
