package com.itsa.traffic.element;


public class Car extends TrafficObject {

	private boolean needUpdate;
	
	
	public Car(int id, double latitude, double longitude) {
		this(id,latitude,longitude,"Car: " + id, "Commom Car");
	}

	public Car(int id, double latitude, double longitude, String title, String description) {
		super(id, latitude, longitude, title, description);
		this.needUpdate = true;
	}
	
	public boolean needUpdate() {
		return needUpdate;
	}
	
}
