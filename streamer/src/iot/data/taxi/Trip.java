package iot.data.taxi;

import iot.common.Point;
import iot.tools.utils.Util;

public class Trip {

	public long start_time;
	public long end_time;
	Point start_location;
	Point end_location;
	public double trip_length;
	public Trip(String cols[]) throws Exception {
		
		start_time = Util.getTimestamp("MM/dd/yyyy hh:mm:ss a", cols[2]);
		end_time = Util.getTimestamp("MM/dd/yyyy hh:mm:ss aa",cols[3]);
		trip_length = Double.parseDouble(cols[14]);
		start_location = new Point(Double.parseDouble(cols[18]),Double.parseDouble(cols[17]));
		end_location = new Point(Double.parseDouble(cols[21]),Double.parseDouble(cols[20]));

	}
	
	public void print() {
		System.out.println((end_time-start_time)/1000+"\t"+trip_length+"\t"+start_location.longitude+","+start_location.latitude+"\t"+ end_location.longitude+","+end_location.latitude);
	}
	
}
