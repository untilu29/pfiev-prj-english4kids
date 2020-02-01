package com.e4kids.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

	Context mcontext;
	public Network(Context mcontext) {
		this.mcontext = mcontext;
	}
	public  boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
