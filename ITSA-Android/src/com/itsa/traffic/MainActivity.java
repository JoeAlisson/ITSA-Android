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

import com.google.android.gms.maps.SupportMapFragment;
import com.itsa.traffic.handler.TrafficManager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 02, 2015
 *
 */
public class MainActivity extends ActionBarActivity {

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
			trafficManager.initMap((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
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
}
