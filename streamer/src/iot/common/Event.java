package iot.common;

import org.json.JSONObject;

public abstract class Event {

	public long timestamp;
	public Point coordinate;
	
	public abstract JSONObject getFeatures();
	public long getTime() {
		return timestamp;
	}
	public Point getLocation() {
		return coordinate;
	}
	
	public JSONObject toJson() {
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("timestamp", timestamp);
		jsonobj.put("longitude", coordinate.longitude);
		jsonobj.put("latitude", coordinate.latitude);
		jsonobj.put("geohash", iot.tools.geohash.GeoHash.withCharacterPrecision(coordinate.latitude, coordinate.longitude, 12).toBase32());
		jsonobj.put("features", getFeatures());
		return jsonobj;
	}
	
	public abstract void print();
	
	
}
