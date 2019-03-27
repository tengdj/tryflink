package iot.common;

import org.json.JSONObject;

public class Event {

	public long timestamp;
	public Point coordinate;
	public JSONObject features;
	public String geohash = null;
	
	
	public Event() {
		
	}
	public Event(String str) {
		JSONObject obj = new JSONObject(str);
		timestamp = obj.getLong("timestamp");
		coordinate = new Point(obj.getDouble("longitude"),obj.getDouble("latitude"));
		features = obj.getJSONObject("features");
		getGeoHash();
	}
	public JSONObject getFeatures() {
		return features;
	}
	
	public long getTime() {
		return timestamp;
	}
	
	public Point getLocation() {
		return coordinate;
	}
	
	public String getGeoHash() {
		if(geohash==null) {
			geohash = iot.tools.geohash.GeoHash.withCharacterPrecision(coordinate.latitude, coordinate.longitude, 12).toBase32();
		}
		return geohash;
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
	
	public String toString() {
		return toJson().toString();
	}
	public String toString(int dent) {
		return toJson().toString(dent);
	}
	
	public void print() {
		System.out.println(toString());
	};
	
	
}
