package com.mpavkovic.capacitivehomebutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mpavkovic.capacitivehomebutton.home_button.HomeButtonTouchService;
import com.mpavkovic.capacitivehomebutton.util.Utils;

public class MainActivity extends AppCompatActivity
{


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!Utils.serviceRunning) {
			Intent intent = new Intent(this, HomeButtonTouchService.class);
			startService(intent);
		}
	}
}
