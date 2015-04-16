package com.mvehon.cse494project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//This handles the password entry and changing
public class Pw extends Activity{
	LinearLayout oldpw, confirm;
	EditText oldpwedit, edit1, edit2;
	SharedPreferences prefs;
	Button ok;
	AlertDialog.Builder builder;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pw);
		prefs = this.getSharedPreferences("com.mvehon.cse494project", Context.MODE_PRIVATE);
		oldpw = (LinearLayout) findViewById(R.id.oldpw);
		confirm = (LinearLayout) findViewById(R.id.confirm);
		ok = (Button) findViewById(R.id.ok);
		edit1 = (EditText)findViewById(R.id.edit1);
		edit2 = (EditText)findViewById(R.id.edit2);
		oldpwedit = (EditText) findViewById(R.id.oldpwedit);
		builder = new AlertDialog.Builder(this);
		
		if(!prefs.getBoolean("existingpassword", false) && !prefs.getString("password", "").equals("")){
			oldpw.setVisibility(View.GONE);
			confirm.setVisibility(View.GONE);
		}else if(!prefs.getBoolean("existingpassword", false)){
			oldpw.setVisibility(View.GONE);
		}
		else{TextView textView1 = (TextView)findViewById(R.id.textView1);
			textView1.setText("New Password:");
			prefs.edit().putBoolean("existingpassword", false).commit();
}
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(matching()){
				prefs.edit().putBoolean("loggedin", true).commit();
				startActivity(new Intent(Pw.this, MainActivity.class));
				Pw.this.finish();
				}
			}
		});

		
		}
	@Override
	public void onBackPressed() {
		super.finish();
		finish();
	}
	public boolean matching() {
		String field1="", field2 = "", field3 = "";
		field1 = edit1.getText().toString();
		field2 = edit2.getText().toString();
		if(oldpw.getVisibility()==View.VISIBLE){
			field3 = oldpwedit.getText().toString();
			if(!field3.equals(prefs.getString("password", "noonewillevermakethisapasswordIswearpootis"))){
				
				builder.setMessage("The old password is incorrect.")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										// FIRE ZE MISSILES!
									}
								});
				builder.create();
				builder.show();
			return false;}			
		}else if(oldpw.getVisibility()==View.GONE && confirm.getVisibility()==View.GONE){
			if(field1.equals(prefs.getString("password", "noonewillevermakethisapasswordIswearpootis"))){
				return true;
			}else {
				
				builder.setMessage("The password is incorrect.")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										// FIRE ZE MISSILES!
									}
								});
				builder.create();
				builder.show();
			return false;}
		}
		if(field1.equals("") || field2.equals("")){
			
			builder.setMessage("Please enter a password.")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// FIRE ZE MISSILES!
								}
							});
			builder.create();
			builder.show();
			return false;
		}else{
			if(!field1.equals(field2)){	builder.setMessage("The passwords do not match.")
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								// FIRE ZE MISSILES!
							}
						});
		builder.create();
		builder.show();}else{
			prefs.edit().putString("password", field1).commit();
			return true;}
		}
		return false;
	}
}
