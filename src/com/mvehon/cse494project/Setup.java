package com.mvehon.cse494project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Setup extends Activity {
	Boolean firsttime=true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		final Context context = this;
		final SharedPreferences prefs = this.getSharedPreferences("com.mvehon.cse494project", Context.MODE_PRIVATE);
		Button submit = (Button) findViewById(R.id.submit);
		final EditText name1 = (EditText) findViewById(R.id.name1);
		final ToggleButton avatar = (ToggleButton) findViewById(R.id.avatar);		

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String firstname = name1.getText().toString();

				if (firstname.equals("")) {
					Toast.makeText(getBaseContext(), "Please enter a name",
							Toast.LENGTH_LONG).show();
				} else {
					prefs.edit().putString("name1", firstname).commit();
					MakeChallenges.initializeChallenges(context);
					prefs.edit().putBoolean("firsttime", false).commit();
					startActivity(new Intent(Setup.this, MainActivity.class));
					Setup.this.finish();
				}
			}
		});
	}
}
