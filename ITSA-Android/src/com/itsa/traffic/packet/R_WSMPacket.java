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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.itsa.conn.Connection;
import com.itsa.conn.packet.AbstractReadablePacket;
import com.itsa.traffic.element.Car;
import com.itsa.traffic.handler.TrafficManager;

/**
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 03, 2015
 *
 */
public class R_WSMPacket extends AbstractReadablePacket<TrafficManager> {

	final public static short OPCODE = 0x01;
	private String name;
	private short kind;
	private int version;
	private int securityType;
	private int channel;
	private int dataRate;
	private int priority;
	private int psid;
	private String psc;
	private int wsmLength;
	private String wsmData;
	private int senderAddress;
	private int recipientAddress;
	private int serial;
	private double longitude;
	private double latitude;
	private double altitude;
	
	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#read(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void read(Connection conn, ByteBuffer buf) {
		name = readString(buf);
		kind = buf.getShort();
		version = buf.getInt();
		securityType = buf.getInt();
		channel = buf.getInt();
		dataRate = buf.getInt();
		priority = buf.getInt();
		psid = buf.getInt();
		psc = readString(buf);
		wsmLength = buf.getInt();
		wsmData = readString(buf);
		senderAddress = buf.getInt();
		recipientAddress = buf.getInt();
		serial = buf.getInt();
	    // Sender Pos
		longitude = buf.getDouble();
		latitude = buf.getDouble();
		altitude = buf.getDouble();

	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#process(com.itsa.conn.Connection, com.itsa.conn.Manager)
	 */
	@Override
	public void process(Connection conn, final TrafficManager manager) {
		Log.i("VANET", "WSMessage: "
				+ "\nname: " + name
				+ "\nkind " + kind
				+ "\nversion: " + version
				+ "\nsercurityType: " + securityType
				+ "\nchannel: " + channel
				+ "\ndataRate: "+ dataRate
				+ "\npriority: "+ priority
				+ "\npsid: " + psid
				+ "\npsc: " + psc
				+ "\nwsmLength: " + wsmLength
				+ "\nwsmData: " + wsmData
				+ "\nsenderAddres: " + senderAddress
				+ "\nrecipientAddress: " + recipientAddress
				+ "\nserial: " + serial
				+ "\nposition: latitude x: " + longitude + " longitude y: " + latitude + " z: " +altitude);
		manager.addCar(new Car(senderAddress, latitude, longitude));
		(new Handler(Looper.getMainLooper())).post(new Runnable() {
			
			@Override
			public void run() {
				manager.updateTraffic();
			}
		});
		
	}

}
