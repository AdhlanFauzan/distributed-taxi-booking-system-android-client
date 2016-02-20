package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;

/**
 * A controller to handle user preferences.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
