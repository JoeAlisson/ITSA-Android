package com.itsa.conn;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Set;

/**
 * Bluetooth RFComm Connection 
 * 
 * @author Alisson Oliveira
 *
 * @param <T> - The Class of Bluetooth Device.
 */
public abstract class BluetoothConnection<T> extends Connection {
	
	protected static ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;
	
	/**
	 * @return true if bluetooth device is available. False otherwise.
	 */
	public abstract boolean checkDeviceAvailability();
	
	/**
	 * @return true if bluetooth device is enabled. False otherwise
	 */
	public abstract boolean isEnabled();
	
	/**
	 * 
	 * @return a set of paired devices.
	 */
	public abstract Set<T> getPairedDevices();
	
	
	/**
	 * Connect to a Device.
	 * 
	 * @param mac - The MAC Address of Device.
	 * @throws IOException - if something goes wrong in connection.
	 */
	public abstract void connect(String mac) throws IOException;
	
	/**
	 * Connect to a Device.
	 * 
	 * @param device
	 * @throws IOException
	 */
	public abstract void connect(T device, int porta) throws IOException;
	
	/**
	 * 
	 * Start inquiring process.
	 */
	public abstract void discoverDevices();
	
	/**
	 * 
	 * Cancel inquiring process.
	 */
	public abstract void cancelDiscovery();
	
	
	/**
	 * 
	 * Get the Device's visible name
	 */
	public abstract String getName();
	
	
	
	

}
