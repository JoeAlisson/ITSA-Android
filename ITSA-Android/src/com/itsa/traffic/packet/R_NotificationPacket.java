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
import com.itsa.conn.packet.AbstractReadablePacket;
import com.itsa.traffic.R;
import com.itsa.traffic.element.Services;
import com.itsa.traffic.handler.TrafficManager;

/**
 * 
 * Created on: Mar 06, 2015.
 * 
 * @author Alisson Oliveira
 *
 */
public class R_NotificationPacket extends AbstractReadablePacket<TrafficManager> {
	public static final int OPCODE = 0xFF;
	protected int id;
	protected int service;
	protected String serviceContext;
	protected String data;
	
	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#read(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 * 
	 */
	@Override
	public void read(Connection conn, ByteBuffer buf) {
		id = buf.getInt();
		service = buf.getInt();
		serviceContext = readString(buf);
		data = readString(buf);
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#process(com.itsa.conn.Connection, com.itsa.conn.Manager)
	 * 
	 */
	@Override
	public void process(Connection conn, TrafficManager manager) {
		Context ctx = manager.getContext();
		NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(ctx).
				setSmallIcon(R.drawable.abc_ic_menu_share_mtrl_alpha).
				setContentTitle(Services.getService(service).name() + ": " + serviceContext).setContentText(data);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(ctx, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		     
		notBuilder.setContentIntent(resultPendingIntent);
		NotificationManager nmn = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification not = notBuilder.build();
		not.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		nmn.notify(id, not);
	}

}
