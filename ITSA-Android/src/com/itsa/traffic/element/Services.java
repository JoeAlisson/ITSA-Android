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
 * Update on: 27 Jan, 2015
 * 
 * @author Alisson Oliveira
 *
 */
public enum Services {
	NONE, CAR, EMERGENCY, WARNING, PUBLIC_TRANSPORT;
	
	
	public static Services getService(int i) {
		for (Services s : values()) {
			if(s.ordinal() == i)
				return s;
		}
		return NONE;
	}
}
