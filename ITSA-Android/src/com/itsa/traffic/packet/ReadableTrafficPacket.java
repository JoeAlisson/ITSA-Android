/**
 * 
 */
package com.itsa.traffic.packet;

import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.conn.packet.AbstractReadablePacket;
import com.itsa.traffic.handler.TrafficManager;

/**
 * @author Alisson Oliveira
 *
 */
public abstract class ReadableTrafficPacket extends AbstractReadablePacket<AndroidBluetoothConnection, TrafficManager> {

}
