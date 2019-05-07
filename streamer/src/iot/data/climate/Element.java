package iot.data.climate;

import java.text.ParseException;

import org.json.JSONObject;

import iot.common.Event;
import iot.common.Point;
import iot.tools.utils.Util;


/*
 * 
 * the basic event for climate data
 * 
 * */
public class Element extends Event{
	
	public String stationid;
	public boolean valid = false;
	public String element;
	public char mflag;
	public char qflag;
	public char sflag;
	public double value;
	
	@Override
	public void print() {
		System.out.println("station:\t"+stationid);
		System.out.println("coordinate:\t"+coordinate.latitude+","+coordinate.longitude);
		System.out.println("element:\t"+element);
		System.out.println("mflag:\t"+mflag);
		System.out.println("qflag:\t"+qflag);
		System.out.println("sflag:\t"+sflag);
		System.out.println("value:\t"+value);
		System.out.println("time:\t"+Util.formatTimestamp(timestamp));
	}
	
	public Element(String stid,int year, int month, int date, String element, Station st, String data) {
		
		value = Double.parseDouble(data.substring(0,5));
		if(value==-9999) {
			valid = false;
		}else {
			valid = true;
			this.element = element;
			stationid = stid;
			mflag = data.charAt(5);
			qflag = data.charAt(6);
			sflag = data.charAt(7);
			String time = String.format("%04d/%02d/%02d 00:00:00", year,month,date);
			try {
				timestamp = Util.getTimestamp(time);
			} catch (ParseException e) {
				e.printStackTrace();
				valid = false;
			}
			this.coordinate =  new Point(st.longitude, st.latitude);
			this.geohash = st.geohash;
			if(qflag!=' ') {
				// the value is fail the check
				valid = false;
			}
		}

	}

	@Override
	public JSONObject getFeatures() {
		JSONObject feature = super.getFeatures();
		feature.put("element", element);
		feature.put("stationid", stationid);
		feature.put("value", value);
		return feature;
	}

}
