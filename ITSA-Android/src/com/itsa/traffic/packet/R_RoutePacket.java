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
package com.itsa.traffic.packet;

import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.itsa.conn.Connection;
import com.itsa.traffic.element.Car;
import com.itsa.traffic.element.Position;
import com.itsa.traffic.handler.TrafficManager;

/**
 * Created on: Jan 18, 2015.
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 27, 2015
 *
 */
public class R_RoutePacket extends R_VehiclePacket {
	
	public static final int OPCODE = 0x04;
	String route;
	
	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#read(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void read(Connection conn, ByteBuffer buf) {
		super.read(conn, buf);
		route = readString(buf);
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#process(com.itsa.conn.Connection, com.itsa.conn.Manager)
	 */
	@Override
	public void process(Connection conn, final TrafficManager manager) {
		manager.addCar(new Car(id, latitude, longitude, service, serviceContext));
		(new Handler(Looper.getMainLooper())).post(new Runnable() {
			@Override
			public void run() {
				manager.updateTraffic();
			}
		});

		StringTokenizer st = new StringTokenizer(route, ",");
		while(st.hasMoreTokens()) {
			if(st.nextToken().equalsIgnoreCase(manager.getRequestedDestination())) {
				Log.i("Route", "Destination found in this route");
				int dist = (int) manager.getCurrentPosition().distanceTo(new Position(latitude, longitude));
				manager.report("O ônibus " + id + " que passará no seu destino está a " + dist + " metros." );
				break;
			}
		}

	}

}
