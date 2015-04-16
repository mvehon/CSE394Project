package com.mvehon.cse494project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//This adds a challenge to the list
public class AddChallenge extends Activity{
	
	EditText title, desc, cat;
	List<Challenge> allChallenges;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addchallenge);
		
		title = (EditText) findViewById(R.id.title);
		desc = (EditText) findViewById(R.id.desc);
		cat = (EditText) findViewById(R.id.cat);
		
		Button submit = (Button) findViewById(R.id.submit);
		
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (fieldsCompleted()) {
					allChallenges = new ArrayList<Challenge>();
					try {
						allChallenges = (List<Challenge>) InternalStorage
								.readObject(getBaseContext(), "allChallenges");
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					allChallenges
							.add(new Challenge(title.getText().toString(), desc
									.getText().toString(), cat.getText()
									.toString()));
					
					try {
						// Save the list of entries to internal storage
						InternalStorage.writeObject(getBaseContext(), "allChallenges", allChallenges);

					} catch (IOException e) {
						Log.e("ERR", e.getMessage());
					}
					
					showToast("Your challenge has been added to the list");
					AddChallenge.this.finish();
				}
			}
			
		});

	}
	
	public Boolean fieldsCompleted(){
		if(title.getText().length()==0){
			showToast("Please enter a title");
			return false;
		}
		else if(desc.getText().length()==0){
			showToast("Please enter a description");
			return false;
		}
		else if(cat.getText().length()==0){
			showToast("Please enter a category");
			return false;
		}
		return true;
	}
	public void showToast(String s){
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}
}
