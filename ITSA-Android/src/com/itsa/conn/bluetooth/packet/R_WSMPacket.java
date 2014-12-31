/**
 * 
 */
package com.itsa.conn.bluetooth.packet;

import java.nio.ByteBuffer;

import android.util.Log;

import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.traffic.handler.TrafficManager;

/**
 * @author Alisson Oliveira
 *
 */
public class R_WSMPacket extends ReadableTrafficPacket {

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
	private double senderPosX;
	private double senderPosY;
	private double senderPosZ;
	
	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#read(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void read(AndroidBluetoothConnection conn, ByteBuffer buf) {
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
		senderPosX = buf.getDouble();
		senderPosY = buf.getDouble();
		senderPosZ = buf.getDouble();

	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#process(com.itsa.conn.Connection, com.itsa.conn.Manager)
	 */
	@Override
	public void process(AndroidBluetoothConnection conn, TrafficManager manager) {
		Log.i("VANET", "WSMessage: "
				+ "\nname: " + name
				+ "\nkink: " + kind
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
				+ "\nposition: latitude x: " + senderPosX + " longitude y: " + senderPosY + " z: " +senderPosZ);
		//conn.getActivity().addCar(new Car(senderAddress, senderPosX, senderPosY));;
		
	}

}
