package com.mvehon.cse494project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MakeChallenges {
	static List<Challenge> entries;

	public static void initializeChallenges(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("com.mvehon.cse494project", Context.MODE_PRIVATE);

		entries = new ArrayList<Challenge>();

		//The list of challenges to be added at first run
		entries.add(new Challenge("Watch a documentary", "Documentaries can be entertaining and insightful. Pick a documentary on something you don't know much about.", "Art"));
		entries.add(new Challenge("Go to an art museum", "Go find a local art museum and wander around.", "Art"));
		entries.add(new Challenge("Take a walk", "Go for a walk down the street or on a scenic trail, it doesn't matter where, just get movin.", "Exercise"));
		entries.add(new Challenge("Read a random Wikipedia article", "You have the world's knowledge literally at your fingertips, why not use it.", "Education"));
		entries.add(new Challenge("Call a friend", "See how they're doing. Taking a little time out of your day for other people can greatly help them.", "Social"));
		entries.add(new Challenge("Catch up on some paperwork", "Chances are you're putting off something, everyone does it. Get it out of the way now so you don't have to worry about it later.", "Productivity"));
		entries.add(new Challenge("Volunteer at an event", "Charities can always use an extra hand. You'll feel productive and help others.", "Charity"));
		entries.add(new Challenge("Do ten pushups", "If that's too easy for you, crank it up to as many as you can handle.", "Exercise"));
		entries.add(new Challenge("Play a retro game", "Dust off an old game you haven't touched in years and take in the nostalgia.", "Gaming"));
		entries.add(new Challenge("Clean a room of the house", "You know it needs to be done.", "Productivity"));

		try {
			// Save the list of entries to internal storage
			InternalStorage.writeObject(context, "allChallenges", entries);

		} catch (IOException e) {
			Log.e("ERR", e.getMessage());
		}


	
	}

}
