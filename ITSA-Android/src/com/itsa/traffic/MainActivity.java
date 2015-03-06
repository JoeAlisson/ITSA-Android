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
import com.itsa.conn.Connection;
import com.itsa.conn.InetConnection;
import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.traffic.handler.TrafficManager;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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
	private boolean useBluetooth = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	@Override
	protected void onResume() {
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
	protected void onDestroy() {
		if(trafficManager != null)
			trafficManager.destroy();
		trafficManager = null;
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		/*NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(this).
				setSmallIcon(R.drawable.abc_ic_menu_share_mtrl_alpha).
				setContentTitle("Traffic Message").setContentText("Opening");
		//Intent resultIntent = new Intent(this, MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		//stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		//stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		     
		notBuilder.setContentIntent(resultPendingIntent);
		NotificationManager nmn = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification not = notBuilder.build();
		not.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		nmn.notify(1, not);*/
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			return true;
		case R.id.action_connect:
			new Thread(new Runnable() {
				public void run() {
					try {
						Connection con = null;
						if (useBluetooth) {
							con = new AndroidBluetoothConnection("14:2D:27:CD:A5:68", 1);
						} else {
							con = new InetConnection("192.168.0.103", 3736);
						}
						trafficManager.connectToOmnet(con);
					} catch (IOException e) {
						Toast.makeText(getApplicationContext(),
								"couldn't connect ", Toast.LENGTH_SHORT).show();
					}
				}
			}).start();

			break;
		case R.id.action_goto:
			final EditText input = new EditText(this);
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setMessage("Digite o Destino ('Rua x' ou 'Bairro y')").setTitle("Ir Para").setView(input).
			setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String text = input.getText().toString();
					List<String> ls = new ArrayList<String>();
					ls.add("ir para " + text);
					trafficManager.handleCommand(ls);
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
				}
			});
			AlertDialog d = b.create();
			d.show();
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
