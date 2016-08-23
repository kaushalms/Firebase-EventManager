package com.example.kaushalmandayam.eventmanager;

/**
 * Created by Kaushal.Mandayam on 8/22/2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private Context context;

    public ConnectionDetector(Context _context) {
        this.context = _context;
    }

    public boolean isConnectedToInternet() {

        ConnectivityManager mConnectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mConnectivity != null) {
            NetworkInfo[] info = mConnectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}