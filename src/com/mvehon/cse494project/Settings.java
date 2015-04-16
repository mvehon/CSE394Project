package com.mvehon.cse494project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;


//This has the settings
public class Settings extends Activity{

	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		prefs = this.getSharedPreferences("com.mvehon.cse494project",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		CheckBox pwcheck = (CheckBox) findViewById(R.id.pwcheck);
		Button pwchange = (Button) findViewById(R.id.pwchange);
		Button challengeedit = (Button) findViewById(R.id.challengeedit);
		Button reset = (Button) findViewById(R.id.reset);

		pwcheck.setChecked(prefs.getBoolean("pwcheck", false));

		if (pwcheck.isChecked()) {
			pwchange.setVisibility(View.VISIBLE);
		} else {
			pwchange.setVisibility(View.GONE);
		}

		pwcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				//If the checkbox is ticked and there is no stored password, take to the password creation page
				prefs.edit().putBoolean("pwcheck", isChecked).commit();
				if (isChecked && prefs.getString("password", "").equals("")) {
					startActivity(new Intent(Settings.this, Pw.class));
					Settings.this.finish();
				}

			}
		});

		//Take to the password change page
		pwchange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				prefs.edit().putBoolean("existingpassword", true).commit();
				startActivity(new Intent(Settings.this, Pw.class));
				Settings.this.finish();
			}
		});

		//Take to the challenge creation page
		challengeedit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Settings.this, AddChallenge.class));
			}
		});

		//Reset all user data
		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					List<Challenge> temptame = new ArrayList<Challenge>();
					InternalStorage.writeObject(getBaseContext(),
							"allChallenges", temptame);
					InternalStorage.writeObject(getBaseContext(), "indices",
							temptame);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SharedPreferences.Editor editor = prefs.edit();
				editor.clear();
				editor.commit();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(Settings.this, MainActivity.class));
		Settings.this.finish();
	}
}
