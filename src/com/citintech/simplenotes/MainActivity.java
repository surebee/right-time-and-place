package com.citintech.simplenotes;

import java.util.List;
import java.util.Locale;

import com.citintech.simplenotes.data.NoteItem;
import com.citintech.simplenotes.data.NotesDataSource;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.app.ListActivity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	private NotesDataSource datasource;
	private TextToSpeech t1;
	List<NoteItem> notesList;
    private TTSService mBoundService;
    private boolean mIsBound;
    private ServiceConnection mConnection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.citintech.simplenotes.R.layout.activity_main);
        System.out.println("Comes here before mBoundService init...");
		datasource = new NotesDataSource(this);
        ServiceConnection mConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName className, IBinder service) {
                mIsBound = true;
                mBoundService = ((TTSService.TTSBinder)service).getService();
                System.out.println("Comes here in mBoundService init...");
            }

            public void onServiceDisconnected(ComponentName className) {
                mIsBound = false;
                mBoundService = null;
            }
        };

        Intent mIntent = new Intent(this, TTSService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);

		refreshDisplay();
		
	}

	@Override
	public void onPause(){
		if(t1 !=null){
			t1.stop();
			t1.shutdown();
		}
		super.onPause();
	}

	private void refreshDisplay() {
		notesList = datasource.findAll();
		ArrayAdapter<NoteItem> adapter =
				new ArrayAdapter<NoteItem>(this, com.citintech.simplenotes.R.layout.list_item_layout, notesList);
		setListAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.citintech.simplenotes.R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == com.citintech.simplenotes.R.id.action_create) {
			createNote();
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void createNote() {
		NoteItem note = NoteItem.getNew();
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra("key", note.getKey());
		intent.putExtra("text", note.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		NoteItem note = notesList.get(position);
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra("key", note.getKey());
		String toSpeak = note.getText();
		intent.putExtra("text", toSpeak);

		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			NoteItem note = new NoteItem();
			note.setKey(data.getStringExtra("key"));
			note.setText(data.getStringExtra("text"));
			datasource.update(note);
			refreshDisplay();
		}
	}
	
}
