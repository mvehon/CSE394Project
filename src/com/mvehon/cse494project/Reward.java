package com.mvehon.cse494project;

import java.io.Serializable;

public class Reward implements Serializable{
	public String title = "", description = "", datecompleted="";
	public boolean hasbeenpicked, hasbeencompleted = false;
	
	public Reward(String t) {
		title = t;
		description = "";
	}

	public Reward(String t, String d) {
		title = t;
		description = d;
	}
	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}

	public boolean getPickHistory() {
		return hasbeenpicked;
	}

	public boolean getCompletion() {
		return hasbeencompleted;
	}

	public void setTitle(String a) {
		title = a;
	}

	public void setDescription(String a) {
		description = a;
	}

	public void setCompletion(Boolean a) {
		hasbeencompleted = a;
	}

	public void setPickHistory(Boolean a) {
		hasbeenpicked = a;
	}
	
	public void setDateCompleted(String a) {
		datecompleted = a;
	}
	public String getDateCompleted() {
		return datecompleted;
	}

}
