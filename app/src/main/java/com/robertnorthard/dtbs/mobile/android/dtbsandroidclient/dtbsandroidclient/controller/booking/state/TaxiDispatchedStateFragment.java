package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.cache.AllBookings;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Booking;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.config.DtbsPreferences;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.utils.datamapper.DataMapper;

/**
 * Represents a taxi dispatched booking state.
 */
public class TaxiDispatchedStateFragment extends Fragment implements BookingState{

    private Booking activeBooking;

    public TaxiDispatchedStateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activeBooking = AllBookings.getInstance().findItem(getArguments().getLong(DtbsPreferences.ACTIVE_BOOKING));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taxi_dispatched_state, container, false);
    }


    @Override
    public void awaitTaxi(Booking booking) {

    }

    @Override
    public void requestTaxi() {

    }

    @Override
    public void pickupPassenger() {

    }

    @Override
    public void dropOffPassenger() {

    }

    @Override
    public void cancelBooking() {

    }
}
