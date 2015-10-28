package com.citintech.simplenotes;

import com.citintech.simplenotes.data.NoteItem;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;

import java.util.Locale;

public class NoteEditorActivity extends Activity {

	private NoteItem note;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.citintech.simplenotes.R.layout.activity_note_editor);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = this.getIntent();
		note = new NoteItem();
		note.setKey(intent.getStringExtra("key"));
		note.setText(intent.getStringExtra("text"));
		
		EditText et = (EditText) findViewById(com.citintech.simplenotes.R.id.noteText);
		et.setText(note.getText());
		et.setSelection(note.getText().length());
	}

	private void saveAndFinish() {
		EditText et = (EditText) findViewById(com.citintech.simplenotes.R.id.noteText);
		String noteText = et.getText().toString();
		
		Intent intent = new Intent();
		intent.putExtra("key", note.getKey());
		intent.putExtra("text", noteText);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			saveAndFinish();
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		saveAndFinish();
	}
	
}
