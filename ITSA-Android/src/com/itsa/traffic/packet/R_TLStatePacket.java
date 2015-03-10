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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.itsa.conn.Connection;
import com.itsa.traffic.R;
import com.itsa.traffic.element.Services;
import com.itsa.traffic.handler.TrafficManager;

/**
 * Created on: Jan 18, 2015.
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 27, 2015
 *
 */
public class R_TLStatePacket extends R_VehiclePacket {
	
	public static final int OPCODE = 0x05;
	String state;
	int nextSwitch;
	String extra = "";
	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#read(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void read(Connection conn, ByteBuffer buf) {
		super.read(conn, buf);
		state = getState((char) buf.get());
		nextSwitch = buf.getInt() / 1000;
		
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#process(com.itsa.conn.Connection, com.itsa.conn.Manager)
	 */
	@Override
	public void process(Connection conn, final TrafficManager manager) {
		Context ctx = manager.getContext();
		NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(ctx).
				setSmallIcon(R.drawable.abc_ic_menu_share_mtrl_alpha).
				setContentTitle(Services.getService(service).name() + ": " + serviceContext + state).setContentText("Sinal "+ state + ". " + extra + "\n Sinal mudará de estado em " + nextSwitch + "segundos");

		PendingIntent resultPendingIntent = PendingIntent.getActivity(ctx, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		     
		notBuilder.setContentIntent(resultPendingIntent);
		NotificationManager nmn = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification not = notBuilder.build();
		not.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		nmn.notify(id, not);
		manager.report("Sinal " + state+".");
		if(!extra.equalsIgnoreCase("")) {
			manager.report(extra);
		}
		manager.report("Sinal mudará de estado em " + nextSwitch + "segundos.");
	} 
	
	/*
	 * rRgGyYoO
	 */
	private String getState(char c) {
		String rs = "";
		switch (c) {
		case 'r':
		case 'R':
			rs = "vermelho";
			break;
		case 'y':
		case 'Y':
			rs = "amarelo";
			break;
		case 'g':
			extra = "Cuidado reduza velocidade o sinal vai fechar.";
		case 'G':
			rs = "verde";
			break;
		default:
			extra = "cuidado sinal desligado.";
			rs = "desligado";
		}
		
		return rs;
				
				
	}

}
