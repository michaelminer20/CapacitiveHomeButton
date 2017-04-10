package com.mpavkovic.capacitivehomebutton.home_button;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mpavkovic.capacitivehomebutton.util.Utils;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

/**
 * Service that listens for home button taps using Samsung's Pass SDK
 */

public class HomeButtonTouchService extends Service
{
	SpassFingerprint fingerprint = null;
	Thread restarter = null;
	boolean stopped = false;

	private SpassFingerprint.IdentifyListener listener = new SpassFingerprint.IdentifyListener()
	{
		@Override
		public void onFinished(int i)
		{

		}

		@Override
		public void onReady()
		{

		}

		@Override
		public void onStarted()
		{
			Log.i("Fingerprint Info", "Home button was tapped!");

			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(startMain);
		}

		@Override
		public void onCompleted()
		{
			Log.i("Fingerprint Info", "Completed");

			stopped = true;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Utils.serviceRunning = true;

		Toast.makeText(this, "Listening for home button taps!", Toast.LENGTH_LONG).show();

		final Spass spass = new Spass();
		try {
			spass.initialize(this);
		} catch (SsdkUnsupportedException e) {
			Log.e("Fingerprint Error", "Fingerprint recognition not supported on this device.");
		}

		boolean isFeatureEnabled = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);

		if (isFeatureEnabled)
		{
			fingerprint = new SpassFingerprint(this);
			try
			{
				fingerprint.startIdentify(listener);
			}
			catch (IllegalStateException e) {
				Utils.serviceRunning = false;
			}
			restarter = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						System.out.println(stopped);
						try {
							Thread.sleep(1000);
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (stopped && spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT)) {
							stopped = false;
							fingerprint.startIdentify(listener);
						}
					}
				}
			});
			restarter.start();
		}
		else
			Log.e("Fingerprint Error", "Fingerprint recognition not supported on this device.");
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "HomeButtonTouchService stopped", Toast.LENGTH_LONG).show();

		if (restarter != null)
			restarter.interrupt();

		Utils.serviceRunning = false;
	}
}
