package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state;

import android.app.Activity;
import android.app.Fragment;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Booking;

/**
 * An interface to represent a booking state and the potential operations.
 */
public interface BookingState {

    public void awaitTaxi(Booking booking);

    public void requestTaxi();

    public void pickupPassenger();

    public void dropOffPassenger();

    public void cancelBooking();

}
