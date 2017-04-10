package com.mpavkovic.capacitivehomebutton.autostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mpavkovic.capacitivehomebutton.home_button.HomeButtonTouchService;

/**
 * Created by michael on 3/26/17.
 */

public class AutoStart extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent i = new Intent(context, HomeButtonTouchService.class);
		context.startService(i);
	}
}
