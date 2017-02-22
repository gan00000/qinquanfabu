package com.egame.cn.ads;

import com.efun.ads.callback.GAListener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GAAdvert implements GAListener {

	private static final String TAG = "efun";
	@Override
	public void GAonReceive(Context context, Intent intent) {
		Log.i(TAG, "GAonReceive...");
		/*
		 * InMobi
		 */
		if(intent.getAction().equals("com.android.vending.INSTALL_REFERRER")){
		//	new IMAdTrackerReceiver().onReceive(context, intent);
			//Log.i("efun", "Inmobi广播已发送");
		}
	}

}
