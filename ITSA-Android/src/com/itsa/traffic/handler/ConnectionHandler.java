package com.itsa.traffic.handler;

import java.io.IOException;

import android.util.Log;

import com.itsa.conn.PacketListener;
import com.itsa.conn.PacketReader;
import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.conn.bluetooth.packet.R_WSMPacket;
import com.itsa.conn.bluetooth.packet.W_InitPacket;
import com.itsa.conn.bluetooth.packet.W_PositionUpdate;
import com.itsa.conn.packet.ReadablePacket;
import com.itsa.traffic.element.Position;

public class ConnectionHandler extends PacketReader<AndroidBluetoothConnection> implements PacketListener<AndroidBluetoothConnection> {

	private TrafficManager manager;
	private int external_id;
	
	public ConnectionHandler(TrafficManager trafficManager) {
		super(new AndroidBluetoothConnection());
		this.manager = trafficManager;
		setPacketListener(this);
	}

	public void connect() throws IOException {
		connectTo("14:2D:27:CD:A5:68");
	}
	
	public void connectTo(String address) throws IOException {
		con.connect(address);
		(new Thread(this)).start();
		con.sendPacket(new W_InitPacket(manager));
	}

	@Override
	public void processPacket(ReadablePacket<AndroidBluetoothConnection> packet) {
		packet.process(con);
	}

	@Override
	protected ReadablePacket<AndroidBluetoothConnection> createPacket(short opcode) {
		Log.i("Connection Handler", "Opcode received " + Integer.toHexString(opcode));
		ReadablePacket<AndroidBluetoothConnection> packet = null;
		switch (opcode) {
		case R_WSMPacket.OPCODE:
			packet = new R_WSMPacket();
			break;
		default:
			Log.e("Connection Handler", "No handler to opcode received " + Integer.toHexString(opcode));
			break;
		}
		return packet;
	}
	
	
	public void sendUpdatePosition(Position currentPosition) {
		if(con.isConnected())
			try {
				con.sendPacket(new W_PositionUpdate(currentPosition));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void setId(int id) {
		external_id = id;		
	}
	
	public int getId() {
		return external_id;
	}

}
