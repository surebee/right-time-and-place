package com.citintech.simplenotes;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class TTSService extends Service implements TextToSpeech.OnInitListener {

    private String str;
    private TextToSpeech mTts = null;
    private static final String TAG="TTSService";
    private boolean ready = false;

    public class TTSBinder extends Binder {
        TTSService getService() {
            return TTSService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intend) {
        return mBinder;
    }

    private final IBinder mBinder = new TTSBinder();

    @Override
    public void onCreate() {
        mTts = new TextToSpeech(this, this);
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available.");
            } else {
                speakText("Hello buy groceries");
            }
        } else {
            Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }

    public void speakText(String str) {
        System.out.println("Comes in speakText" + str);
        mTts.speak(str,
                TextToSpeech.QUEUE_ADD,
                null);
    }

}