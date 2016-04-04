package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.state;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.cache.AllBookings;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Booking;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.config.DtbsPreferences;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.utils.datamapper.DataMapper;

/**
 * Represents completed booking state
 */
public class BookingCompleteStateFragment extends Fragment implements BookingState {

    private TextView txtBookingCost;
    private Button btnConfirmBookingCompletition;

    private Booking activeBooking;

    private Fragment nextFragment;

    public BookingCompleteStateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_booking_complete_state, container, false);

        this.txtBookingCost = (TextView)v.findViewById(R.id.txt_booking_cost);
        this.btnConfirmBookingCompletition = (Button)v.findViewById(R.id.btn_confirm_booking_completion);

        if(getArguments().get("data") == null){
            this.activeBooking = AllBookings.getInstance().findItem(getArguments().getLong(DtbsPreferences.ACTIVE_BOOKING));
        }else{
            this.activeBooking = DataMapper.getInstance().readObject(getArguments().get("data").toString(), Booking.class);
        }

        this.txtBookingCost.setText("£" + String.valueOf(this.activeBooking.getCost()));

        this.btnConfirmBookingCompletition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeBooking();
            }
        });

        return v;
    }

    @Override
    public void awaitTaxi(Booking booking) {
        throw new IllegalStateException("Taxi already dispatched.");
    }

    @Override
    public void requestTaxi() {
        throw new IllegalStateException("Taxi already requested.");
    }

    @Override
    public void pickupPassenger() {
        throw new IllegalStateException("Passenger already picked up.");
    }

    @Override
    public void dropOffPassenger() {
        throw new IllegalStateException("Passenger already dropped off.");
    }

    @Override
    public void cancelBooking() {
        throw new IllegalStateException("Booking cancelled.");
    }

    public void completeBooking(){

        nextFragment = new RequestRideStateFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_map_state_frame, nextFragment)
                .addToBackStack(null)
                .commit();
    }
}
