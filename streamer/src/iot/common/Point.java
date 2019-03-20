package iot.common;

public class Point {
	public double longitude;
	public double latitude;
	
	public Point(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public boolean close(Point p) {
		return longitude==p.longitude&&latitude==p.latitude;
	}
	
	public double distance(Point p) {
		if(p==null) {
			return 0;
		}
		return Math.sqrt((longitude-p.longitude)*(longitude-p.longitude)+(latitude-p.latitude)*(latitude-p.latitude));
	}
}
