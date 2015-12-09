package com.citintech.simplenotes.data;

import android.annotation.SuppressLint;
import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteItem {

	private String key;
	private String text;
	private Location location;
	
	public static final String TEXT = "text";
	public static final String LOCATION = "location";
	public static final String KEY = "key";
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static NoteItem getNew() {
	
		Locale locale = new Locale("en_US");
		Locale.setDefault(locale);

		String pattern = "yyyy-MM-dd HH:mm:ss Z";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String key = formatter.format(new Date());

		Location targetLocation = new Location("");
		targetLocation.setLatitude(0.0d);
		targetLocation.setLongitude(0.0d);
		
		NoteItem note = new NoteItem();
		note.setKey(key);
		note.setText("");
		note.setLocation(targetLocation);
		return note;
		
	}
	
	@Override
	public String toString() {
		return this.getText();
	}
	
}
