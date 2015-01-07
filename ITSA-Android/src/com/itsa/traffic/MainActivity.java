/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */
package com.itsa.traffic;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.itsa.traffic.handler.TrafficManager;

import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 07, 2015
 *
 */
public class MainActivity extends ActionBarActivity implements OnMapReadyCallback {

	private TrafficManager trafficManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		trafficManager = new TrafficManager(this);
	}
	
	@Override
	protected void onResume() {
		if(trafficManager.needsMapSetup()) {
			((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
		}
		trafficManager.resume();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		trafficManager.destroy();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			return true;
		case R.id.action_connect:
			try {
				trafficManager.connectToOmnet();
			} catch (IOException e) {
				Toast.makeText(this, "couldn't connect ", Toast.LENGTH_SHORT).show();;
			}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private static final int SPEECH_REQUEST_CODE = 0;

	// Create an intent that can start the Speech Recognizer activity
	private void displaySpeechRecognizer() {
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    // 	Start the activity, the intent will be populated with the speech text
	    startActivityForResult(intent, SPEECH_REQUEST_CODE);
	}

	// This callback is invoked when the Speech Recognizer returns.
	// This is where you process the intent and extract the speech text from the intent.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
	        List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	        String spokenText = results.get(0);
	        Log.i("Speech", spokenText);
	        // Do something with spokenText
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				displaySpeechRecognizer();
			}
		});
		trafficManager.setMap(map);
		
	}
}
