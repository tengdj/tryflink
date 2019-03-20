package iot.data.climate;

import java.text.ParseException;

import org.json.JSONObject;

import iot.common.Event;
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
		System.out.println("element:\t"+element);
		System.out.println("mflag:\t"+mflag);
		System.out.println("qflag:\t"+qflag);
		System.out.println("sflag:\t"+sflag);
		System.out.println("value:\t"+value);
		System.out.println("time:\t"+timestamp);
	}
	
	public Element(String stid,int year, int month, String element, int date, String data) {
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				valid = false;
			}


		}
		
		

		
	}

	@Override
	public JSONObject getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

}
