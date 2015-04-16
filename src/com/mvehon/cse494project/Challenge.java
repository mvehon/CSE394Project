package com.mvehon.cse494project;

import java.io.Serializable;

public class Challenge implements Serializable{
	public String title, description = "", datecompleted="", type="";
	public boolean checked, hasbeenpicked, hasbeencompleted = false;

	public Challenge(String t, String typ) {
		title = t;
		description = "";
		type = typ;
	}

	public Challenge(String t, String d, String typ) {
		title = t;
		description = d;
		type = typ;
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
