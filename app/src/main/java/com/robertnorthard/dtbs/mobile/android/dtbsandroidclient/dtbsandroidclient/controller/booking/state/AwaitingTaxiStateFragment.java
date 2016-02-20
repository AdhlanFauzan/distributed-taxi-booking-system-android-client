package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.cache.AllBookings;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Booking;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.BookingService;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.config.DtbsPreferences;

/**
 * Represents an awaiting taxi booking state.
 */
public class AwaitingTaxiStateFragment extends Fragment implements BookingState {

    private Button btnCancelBooking;
    private Booking activeBooking;

    public AwaitingTaxiStateFragment() {
        // fragments require empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_awaiting_taxi_state, container, false);

        this.btnCancelBooking = (Button)v.findViewById(R.id.btn_cancel_booking);
        this.activeBooking = AllBookings.getInstance().findItem(getArguments().getLong(DtbsPreferences.ACTIVE_BOOKING));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(DtbsPreferences.BOOKING_EVENTS_TOPIC));

        return v;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");

            Fragment fragment = new TaxiDispatchedStateFragment();
            fragment.setArguments(getArguments());

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_map_state_frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    };

    @Override
    public void awaitTaxi(Booking booking) {
        throw new IllegalStateException("Already awaiting taxi.");
    }

    @Override
    public void requestTaxi() {
        throw new IllegalStateException("Taxi already requested.");
    }

    @Override
    public void pickupPassenger() {
        throw new IllegalStateException("Awaiting taxi allocation.");
    }

    @Override
    public void dropOffPassenger() {
        throw new IllegalStateException("Awaiting taxi allocation.");
    }

    @Override
    public void cancelBooking() {

    }
}
