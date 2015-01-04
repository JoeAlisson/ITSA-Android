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
package com.itsa.traffic.element;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Update on: Jan 04, 2015
 *
 */
public abstract class TrafficObject {

	protected Position pos;
	protected ObjectType type = ObjectType.DUMMY;
	protected int id;
	private String title;
	private String description;
	private long lastModified;

	public TrafficObject(int id, double latitude, double longitude, String title, String description) {
		this.id = id;
		this.pos = new Position(latitude, longitude);
		this.title = title;
		this.description = description;
		lastModified = System.currentTimeMillis();
	}
	
	public Position getPosition() {
		return pos;
	}
	
	public abstract boolean needUpdate();
	
	public void updatePosition(double latitude, double longitude) {
		this.pos.setLatitude(latitude);
		this.pos.setLongitude(longitude);
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public long getLastModified() {
		return lastModified;
	}


}
