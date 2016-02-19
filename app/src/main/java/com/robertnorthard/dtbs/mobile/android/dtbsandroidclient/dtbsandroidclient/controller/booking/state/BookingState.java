package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state;

/**
 * An interface to represent a booking state and the potential operations.
 */
public interface BookingState {

    public void awaitTaxi();

    public void requestTaxi();

    public void pickupPassenger();

    public void dropOffPassenger();

    public void cancelBooking();

}
