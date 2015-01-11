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
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.itsa.traffic.handler.TrafficManager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 09, 2015
 *
 */
public class MainActivity extends ActionBarActivity implements OnMapReadyCallback {

	private TrafficManager trafficManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Event", "On Create");
		setContentView(R.layout.activity_main);
		
	}
	
	@Override
	protected void onResume() {
		Log.i("Event", "On Resume");
		trafficManager = new TrafficManager(this);
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
	protected void onPause() {
		Log.i("Event", "OnPause");
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		Log.i("Event", "OnDestroy");
		trafficManager.destroy();
		trafficManager = null;
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
			break;
		case R.id.action_goto:
			List<String> ls = new ArrayList<String>();
			ls.add("ir para Samaritana");
			trafficManager.handleCommand(ls);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onMapReady(GoogleMap map) {
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				trafficManager.waitCommand(true);
			}
		});
		trafficManager.setMap(map);
		
	}
}
