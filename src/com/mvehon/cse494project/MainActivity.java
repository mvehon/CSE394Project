package com.mvehon.cse494project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.text.SpannableString;
import android.text.format.Time;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	static SharedPreferences prefs;
	String username;
	static LinearLayout levelbarinner, levelbarouter, levelbox;
	static TextView xpamount, levelnumber, leveltitle;
	static Time dt;
	static Boolean cancreatenew; 
	static LayoutInflater infl;
	static List<Challenge> allChallenges;
	static List<Integer> indices;
	static Context context;
	static int xp=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		prefs = this.getSharedPreferences("com.mvehon.cse494project",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		//If it is the first time the app has been opened, take the user to the profile creation
		if(prefs.getBoolean("firsttime", true)){
			startActivity(new Intent(MainActivity.this, Setup.class));
			MainActivity.this.finish();
		}
		//Check if the user requires a password
		else if(prefs.getBoolean("pwcheck", false) && !prefs.getBoolean("loggedin", false)){
			startActivity(new Intent(MainActivity.this, Pw.class));
			MainActivity.this.finish();}
		
		//Set the time
		dt = new Time();
		dt.setToNow();
		context = this;
		cancreatenew = prefs.getBoolean("cancreatenew", false);
		
		infl = getLayoutInflater();

		
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//Read in the challenges/selected ones
		allChallenges = new ArrayList<Challenge>();
		indices = new ArrayList<Integer>();		
		try {
			allChallenges = (List<Challenge>) InternalStorage.readObject(
					this, "allChallenges");
		} catch (ClassNotFoundException | IOException e) {
			allChallenges = new ArrayList<Challenge>();
			e.printStackTrace();
		}
		
		try {
			indices = (List<Integer>) InternalStorage.readObject(
					getBaseContext(), "indices");
		} catch (ClassNotFoundException | IOException e) {
			indices = new ArrayList<Integer>();		
			e.printStackTrace();
		}
		
		
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.advance) {
			//This doesn't actually advance a week, but decrements the previously set week 
			prefs.edit().putInt("taskweek", prefs.getInt("taskweek", 0)-1).commit();
			onPause();
			MainActivity.this.finish();
			System.exit(0);
			return true;
		}
		if (id == R.id.action_settings) {
			startActivity(new Intent(MainActivity.this, Settings.class));
			MainActivity.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
            //return 3;
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			if(position==0){
				return "Quests";
			}
			else{
				return "Progress";
			}
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = null;
			Bundle args = getArguments();
			int curView = args.getInt(ARG_SECTION_NUMBER);
			
			//This is for the "quests" page
			if (curView == 1) {
				v = inflater.inflate(R.layout.weekly, container, false);
				
				final Button completed = (Button) v
						.findViewById(R.id.completed);
				Button minus = (Button) v.findViewById(R.id.minus);
				Button plus = (Button) v.findViewById(R.id.plus);
				TextView dtrng = (TextView) v.findViewById(R.id.dtrng);

				//Set the top thing to the current week value
				dtrng.setText("Week #" + dt.getWeekNumber());

				//If it has been a week since last usage, pop this up
				if (checkWeek()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage(
							"It's a new week! Check the challenges you've completed and hit Complete to get new challenges.")
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
										}
									});
					builder.create();
					builder.show();
					cancreatenew = true;
				}

				final LinearLayout quest_ll = (LinearLayout) v
						.findViewById(R.id.quest_ll);

				// Add any previously selected challenges
				if (indices.size() > 0) {
					for (int i = 0; i < indices.size(); i++) {
						quest_ll.addView(inflater.inflate(R.layout.quest_stub,
								null));
						final LinearLayout quest_frag = (LinearLayout) quest_ll
								.findViewById(R.id.quest_frag);
						TextView name = (TextView) quest_frag
								.findViewById(R.id.name);
						TextView desc = (TextView) quest_frag
								.findViewById(R.id.desc);
						CheckBox check = (CheckBox) quest_frag
								.findViewById(R.id.check);
						check.setChecked(allChallenges.get(indices.get(i)).checked);
						check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								allChallenges.get(indices.get(quest_frag
										.getId() - 1)).checked = isChecked;
							}
						});
						name.setText(allChallenges.get(indices.get(i)).title);
						desc.setText(allChallenges.get(indices.get(i)).description);
						quest_frag.setId(quest_ll.getChildCount());
					}
				} else { //Otherwise add one
					quest_ll.addView(inflater
							.inflate(R.layout.quest_stub, null));
					final LinearLayout quest_frag = (LinearLayout) quest_ll
							.findViewById(R.id.quest_frag);
					Random rn = new Random();

					int index = 0;
					int tries = 0;
					if (allChallenges.size() > 0) {
						do {
							index = rn.nextInt(allChallenges.size());
							tries++;
						} while (!allChallenges.get(index).hasbeenpicked
								&& tries < 25);
						indices.add(index);
						TextView name = (TextView) quest_frag
								.findViewById(R.id.name);
						TextView desc = (TextView) quest_frag
								.findViewById(R.id.desc);
						CheckBox check = (CheckBox) quest_frag
								.findViewById(R.id.check);
						check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								allChallenges.get(indices.get(quest_frag
										.getId() - 1)).checked = isChecked;
							}
						});
						name.setText(allChallenges.get(index).title);
						desc.setText(allChallenges.get(index).description);
						quest_frag.setId(quest_ll.getChildCount());
					}
				}
				// End challenge area

				//Remove a challenge from the list
				minus.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (quest_ll.getChildCount() > 0) {
							indices.remove(quest_ll.getChildCount() - 1);
							quest_ll.removeViewAt(quest_ll.getChildCount() - 1);
						}
					}
				});
				
				//Add a challenge to the list
				plus.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						quest_ll.addView(infl
								.inflate(R.layout.quest_stub, null));
						final LinearLayout quest_frag = (LinearLayout) quest_ll
								.findViewById(R.id.quest_frag);
						Random rn = new Random();

						int index = 0;
						int tries = 0;
						if (allChallenges.size() > 0) {
							do {
								index = rn.nextInt(allChallenges.size());
								tries++;
							} while (!allChallenges.get(index).hasbeenpicked
									&& tries < 25);
							indices.add(index);
							TextView name = (TextView) quest_frag
									.findViewById(R.id.name);
							TextView desc = (TextView) quest_frag
									.findViewById(R.id.desc);
							CheckBox check = (CheckBox) quest_frag
									.findViewById(R.id.check);
							check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									allChallenges.get(indices.get(quest_frag
											.getId() - 1)).checked = isChecked;
								}
							});
							name.setText(allChallenges.get(index).title);
							desc.setText(allChallenges.get(index).description);
							quest_frag.setId(quest_ll.getChildCount());
						}
					}
				});

				//Complete button is only visible if it is a new week and the prior week's challenges have not been submitted
				if (!cancreatenew) {
					completed.setVisibility(View.GONE);
				} else {
					completed.setVisibility(View.VISIBLE);
				}
				
				
				completed.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						for (int i = 0; i < quest_ll.getChildCount(); i++) {
							LinearLayout tempfrag = (LinearLayout) quest_ll.findViewById(i + 1);
							CheckBox check = (CheckBox) tempfrag.findViewById(R.id.check);
							if (check.isChecked()) {
								xp += 10;
							}
						}
						wipeAllChecked();
						challengesComplete(xp);
						indices = new ArrayList<Integer>();
						quest_ll.removeAllViews();
						cancreatenew = false;
						completed.setVisibility(View.GONE);
						onPause();
					}
				});

				return v;
			} else {
				//This is for the "progress" page
				v = inflater.inflate(R.layout.level, container, false);
				levelbarouter = (LinearLayout) v
						.findViewById(R.id.levelbarouter);
				levelbox = (LinearLayout) v.findViewById(R.id.levelbox);
				levelbarinner = (LinearLayout) v
						.findViewById(R.id.levelbarinner);
				levelnumber = (TextView) v.findViewById(R.id.levelnumber);
				leveltitle = (TextView) v.findViewById(R.id.leveltitle);
				xpamount = (TextView) v.findViewById(R.id.xpamount);
				levelnumber.setText(prefs.getString("levelnumber", "0"));
				leveltitle.setText(prefs.getString("leveltitle", "TextView"));
				xpamount.setText(prefs.getString("xpamount", "0/25 xp"));
				xpamount.setTag(prefs.getString("xpamounttag", "0/25"));

				fixXpBarLength();
				setLevelTitle();

				return v;
			}
		}
	}

	
	//This method handles the appearance of the level bar
	public static void fixXpBarLength() {
		if (xpamount.getTag().equals("0/25")) {
			xpamount.setTag(prefs.getString("xpamounttag", "0/25"));
		}
		String xps = (String) xpamount.getTag();
		String[] parts = xps.split("/");
		int tempxp = Integer.parseInt(parts[0]);
		int xpbound = Integer.parseInt(parts[1]);

		int outerbarlength = levelbarouter.getWidth();
		if (outerbarlength == 0) {
			outerbarlength = prefs.getInt("levelbarouter", (int) 200f);
		}
		float ratio = (tempxp * 100) / xpbound;
		ratio = (outerbarlength * ratio) / 100;
		levelbarinner.setLayoutParams(new LayoutParams((int) ratio,
				LayoutParams.MATCH_PARENT));
	}
	
	//This method checks to see if it is a new week
	public static boolean checkWeek() {
		// If no week has been set, set to current
		if (prefs.getInt("taskweek", -1) == -1) {
			prefs.edit().putInt("taskweek", dt.getWeekNumber()).commit();
			prefs.edit().putInt("taskyear", dt.year).commit();
			return true;
		}
		// If stored week is lower than current week, update and return true
		else if (prefs.getInt("taskweek", -1) < dt.getWeekNumber()) {
			prefs.edit().putInt("taskweek", dt.getWeekNumber()).commit();
			return true;
		}
		// If stored week is higher than current but year is lower
		else if (prefs.getInt("taskweek", -1) > dt.getWeekNumber() && prefs.getInt("taskyear", -1) < dt.year) {
			prefs.edit().putInt("taskweek", dt.getWeekNumber()).commit();
			prefs.edit().putInt("taskyear", dt.year).commit();
			return true;
		}
		else{
			return false;
		}
	}
	
	//If paused, save data
	@Override
	protected void onPause() {
		super.onPause();
		prefs.edit().putBoolean("cancreatenew", cancreatenew).commit();
		try {
			InternalStorage.writeObject(getBaseContext(), "indices", indices);
			InternalStorage.writeObject(getBaseContext(), "allChallenges", allChallenges);
		} catch (IOException e) {
			Log.e("ERR", e.getMessage());
		}
	}
	
	//If destroyed, save data
	@Override
	protected void onDestroy() {
		super.onDestroy();
		prefs.edit().putBoolean("cancreatenew", cancreatenew).commit();
		prefs.edit().putBoolean("loggedin", false).commit();
		try {
			InternalStorage.writeObject(getBaseContext(), "indices", indices);
			InternalStorage.writeObject(getBaseContext(), "allChallenges", allChallenges);
		} catch (IOException e) {
			Log.e("ERR", e.getMessage());
		}
	}
	
	//Remove any 'checked' attributes
	public static void wipeAllChecked(){
		for(int i=0; i<allChallenges.size();i++){
			allChallenges.get(i).checked=false;
		}
	}
	
	//This handles the xp and leveling
	public static void challengesComplete(int exp) {
		String xps = (String) xpamount.getTag();
		String[] parts = xps.split("/");
		int tempxp = Integer.parseInt(parts[0]);
		int xpbound = Integer.parseInt(parts[1]);
		int derp = exp;
		exp += tempxp;
		if (exp >= xpbound) {

			exp = exp - xpbound;
			int herp = Integer.parseInt((String) levelnumber.getText()+"");
			herp += 1;
			levelnumber.setText(Integer.toString(herp));
			xpbound += 25;
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(
					"You have reached level " + herp).setPositiveButton(
					"Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// FIRE ZE MISSILES!
						}
					});
			builder.create();
			builder.show();

			builder.setMessage("You have gained " + (derp) + " xp!");
			builder.create();
			builder.show();
			int templevel = 0;
			if (herp % 5 == 0) {
				templevel = 1;
			}
		} else {
			if ((exp - tempxp) > 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("You have gained " + (exp - tempxp) + " xp!")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// FIRE ZE MISSILES!
									}
								});
				builder.create();
				builder.show();
			}
		}
		
		Log.d("xps", xps);
		Log.d("exp", Integer.toString(exp));
		Log.d("xpbound", Integer.toString(xpbound));
		
		xps = Integer.toString(exp) + "/" + Integer.toString(xpbound);
		xpamount.setText(xps + " xp");
		xpamount.setTag(xps);
		prefs.edit().putString("xpamounttag", xps).commit();

		fixXpBarLength();
		
		prefs.edit().putString("levelnumber", (String) levelnumber.getText()+"").commit();
		prefs.edit().putString("leveltitle", (String) leveltitle.getText()+"").commit();
		prefs.edit().putString("xpamount", (String) xpamount.getText()+"").commit();
		prefs.edit().putString("xpamounttag", (String) xpamount.getTag()).commit();
	}
	
	//This determines the level name
	public static void setLevelTitle(){
		int levelnum = Integer.parseInt((String) levelnumber.getText()+"");
		String title="";
		switch (levelnum){
		case 0:
			title = "Peasant";
			break;
		case 1:
			title="Baronet";
			break;
		case 2:
			title="Knight";
			break;
		case 3:
			title="Baron";
			break;
		case 4:
			title="Viscount";
			break;
		case 5:
			title="Earl";
			break;
		case 6:
			title="Count";
			break;
		case 7:
			title="Marquess";
			break;
		case 8:
			title="Duke";
			break;
		case 9:
			title="Prince";
			break;
		case 10:
			title="Queen";
			break;
		case 11:
			title="King";
			break;
		default:
			title="Emperor";
			break;
			
		}
		leveltitle.setText(title);
	}
}
