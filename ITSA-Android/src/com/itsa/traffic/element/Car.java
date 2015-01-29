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
 * Updated on: Jan 27, 2015
 * 
 * @author Alisson Oliveira
 *
 */
public class Car extends TrafficObject {

	private boolean needUpdate;
	
	
	public Car(int id, double latitude, double longitude) {
		this(id,latitude,longitude,0,"");
	}

	public Car(int id, double latitude, double longitude, int service, String description) {
		super(id, latitude, longitude, service, description);
		this.needUpdate = true;
	}
	
	public boolean needUpdate() {
		return needUpdate;
	}
	
}
