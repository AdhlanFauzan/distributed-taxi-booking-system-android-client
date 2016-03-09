package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.tasks;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.cache.AllBookings;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state.AwaitingTaxiStateFragment;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state.PassengerPickedupStateFragment;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state.RequestRideStateFragment;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state.TaxiDispatchedStateFragment;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.passenger.PassengerMapFragment;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Booking;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.BookingService;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.config.DtbsPreferences;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.utils.datamapper.DataMapper;

import org.json.JSONException;

import java.io.IOException;

/**
 * Async task to check if user has any active bookings.
 *
 * @author robertnorthard
 */
public class CheckActiveBookingAsyncTask extends AsyncTask<Void, Void, Booking>{

    // TAG used for logging.
    private static final String TAG = CheckActiveBookingAsyncTask.class.getName();

    private Exception exception = null;
    private BookingService bookingService;
    private PassengerMapFragment controller;

    public CheckActiveBookingAsyncTask(PassengerMapFragment controller){
        this.bookingService = new BookingService();
        this.controller = controller;
    }

    /**
     * Tasks back task for finding active bookings.
     *
     * @param params task parameters.
     * @return active booking, else null if no active booking.
     */
    @Override
    protected Booking doInBackground(Void... params) {

        Booking activeBooking = null;

        try {
            activeBooking = this.bookingService.findActiveBooking();

        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return activeBooking;
    }

    /**
     * Handle to manage result of background task.
     *
     * @param result result of background task.
     */
    @Override
    protected void onPostExecute(final Booking result) {

        if(result != null) {

            AllBookings.getInstance().addItem(result);

            Fragment fragment = null;
            Bundle data = new Bundle();
            data.putLong(DtbsPreferences.ACTIVE_BOOKING, result.getId());

            switch(result.getState()) {

                case "AWAITING_TAXI":
                    fragment = new AwaitingTaxiStateFragment();
                    break;
                case "TAXI_DISPATCHED":
                    fragment = new TaxiDispatchedStateFragment();
                    break;
                case "PASSENGER_PICKED_UP":
                    fragment = new PassengerPickedupStateFragment();
                    break;
            }

            if(fragment != null){
                fragment.setArguments(data);
                this.controller.setBookingState(fragment);
            }
        }else{
            this.controller.setBookingState(new RequestRideStateFragment());
        }
    }
}
