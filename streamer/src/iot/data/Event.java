package iot.data;

import org.json.JSONObject;

public class Event {

	public long timestamp;
	public Point coordinate;
	public String id = "";
	
	public Event() {
		
	}
	public Event(String str) {
		JSONObject obj = new JSONObject(str);
		id = obj.getString("id");
		timestamp = obj.getLong("timestamp");
		coordinate = new Point(obj.getDouble("longitude"),obj.getDouble("latitude"));
	}
	
	public long getTime() {
		return timestamp;
	}
	
	public Point getLocation() {
		return coordinate;
	}

	public JSONObject toJson() {
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", id);
		jsonobj.put("timestamp", timestamp);
		jsonobj.put("longitude", coordinate.longitude);
		jsonobj.put("latitude", coordinate.latitude);
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
