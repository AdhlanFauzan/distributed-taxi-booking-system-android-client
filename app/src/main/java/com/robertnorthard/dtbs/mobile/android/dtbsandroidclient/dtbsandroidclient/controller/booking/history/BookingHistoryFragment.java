package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller.booking.history;

import android.os.Bundle;
import android.app.ListFragment;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Booking;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.BookingService;

import org.json.JSONException;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a collection of bookings.
 *
 * @author robertnorthard
 */
public class BookingHistoryFragment extends ListFragment {

    public BookingHistoryFragment() {
        /*
         * Mandatory empty constructor for the fragment manager to instantiate the
         * fragment (e.g. upon screen orientation changes).
         */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List<Booking> booking = null;
        try {
            booking = new BookingService().findAllBookings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // reverse list most recent first
        Collections.reverse(booking);

        BookingHistoryListAdapter bookingHistoryListAdapter
                = new BookingHistoryListAdapter(getActivity(), booking);

        setListAdapter(bookingHistoryListAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        getListView()
                .setEmptyView(getActivity()
                        .getLayoutInflater()
                        .inflate(R.layout.booking_history_list_empty_view, null, true));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
