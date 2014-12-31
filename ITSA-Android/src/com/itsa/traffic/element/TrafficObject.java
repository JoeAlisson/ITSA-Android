package com.itsa.traffic.element;

public abstract class TrafficObject {

	protected Position pos;
	protected ObjectType type = ObjectType.DUMMY;
	protected int id;
	private String title;
	private String description;

	public TrafficObject(int id, double latitude, double longitude, String title, String description) {
		this.id = id;
		this.pos = new Position(latitude, longitude);
		this.title = title;
		this.description = description;
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

}
