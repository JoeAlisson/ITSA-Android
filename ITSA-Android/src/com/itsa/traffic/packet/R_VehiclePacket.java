/**
 * 
 */
package com.itsa.traffic.packet;

import java.nio.ByteBuffer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.traffic.element.Car;
import com.itsa.traffic.handler.TrafficManager;

/**
 * Created on: Jan 27, 2015
 * 
 * @author Alisson Oliveira
 *
 */
public class R_VehiclePacket extends ReadableTrafficPacket {
	
	public static final short OPCODE = 0x03;
	
	protected int id;
	protected int service;
	protected String serviceContext;
	protected double longitude;
	protected double latitude;
	protected double altitude;
	
	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#read(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void read(AndroidBluetoothConnection conn, ByteBuffer buf) {
		id = buf.getInt();
		service = buf.getInt();
		serviceContext = readString(buf);
		longitude = buf.getDouble();
		latitude = buf.getDouble();
		altitude = buf.getDouble();
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#process(com.itsa.conn.Connection, com.itsa.conn.Manager)
	 */
	@Override
	public void process(AndroidBluetoothConnection conn, final TrafficManager manager) {
		Log.i("Vehicle", "service " + service);
		manager.addCar(new Car(id, latitude, longitude, service, serviceContext));
		(new Handler(Looper.getMainLooper())).post(new Runnable() {
			
			@Override
			public void run() {
				manager.updateTraffic();
			}
		});

	}

}
