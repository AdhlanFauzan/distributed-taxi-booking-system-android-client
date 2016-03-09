package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.history;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Booking;

import java.util.List;

/**
 * Booking history list custom adapter.
 *
 * @author robertnorthard
 */
public class BookingHistoryListAdapter extends ArrayAdapter<Booking> {

    private final Activity context;
    private final List<Booking> bookings;

    public BookingHistoryListAdapter(Activity context, List<Booking> bookings) {
        super(context, R.layout.booking_history_list, bookings);
        this.context=context;
        this.bookings = bookings;
    }

    @Override
    public View getView(int position,View view,ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.booking_history_list, null, true);

        TextView txtBookingId = (TextView)rowView.findViewById(R.id.txt_booking_id);
        TextView txtBookingState = (TextView)rowView.findViewById(R.id.txt_booking_state);
        TextView txtBookingCost = (TextView)rowView.findViewById(R.id.txt_booking_cost);

        Booking booking = this.bookings.get(position);

        txtBookingId.setText(String.valueOf(booking.getId()));
        txtBookingState.setText(booking.getState());
        txtBookingCost.setText("£" + String.valueOf(booking.getCost()));

        return rowView;
    }
}
