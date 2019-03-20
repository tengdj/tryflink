package iot.tools.gps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import iot.common.Point;
import iot.tools.utils.Util;

/*
 * class for the streets data 
 * 
 * */
public class Map {
	
	protected ArrayList<Street> streets = new ArrayList<Street>();
	
	
	public Map(){
		
	}
	
	/*
	 * compare each street pair to see if they connect with each other
	 * */
	protected void connect_segments() {
		System.out.println("connecting streets");
		for(int i=0;i<streets.size()-1;i++) {
			for(int j=i+1;j<streets.size();j++) {
				streets.get(i).touch(streets.get(j));
			}
		}		
	}
	
	public static String genGeoJson(ArrayList<Street> streets) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"type\":\"MultiLineString\",\"coordinates\":\n\t[\n");
		int count = 0;
		for(Street s:streets) {
			sb.append("\t");
			if(count++!=0) {
				sb.append(",");
			}
			sb.append("[["+s.start.longitude+","+s.start.latitude+"],["+s.end.longitude+","+s.end.latitude+"]]\n");
		}
		sb.append("\t]\n}\n");
		return sb.toString();
	}
	
	public String toString() {
		return Map.genGeoJson(streets);
	}
	public void print() {
		
		System.out.println(toString());

	}
	
	public void dumpTo(String path) {
		try {
			
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(path));
			stream.writeInt(streets.size());
			for(Street street:streets) {
				stream.writeLong(street.id);
				stream.writeDouble(street.start.longitude);
				stream.writeDouble(street.start.latitude);
				stream.writeDouble(street.end.longitude);
				stream.writeDouble(street.end.latitude);
				stream.writeInt(street.connected.size());
				for(Street s:street.connected) {
			        stream.writeLong(s.id);
				}

			}
			stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadFromFormatedData(String path) {
		
		System.out.println("loading from formated file "+path);
		HashMap<Long, Street> stmap = new HashMap<Long, Street>();
		try {
			DataInputStream stream = new DataInputStream(new FileInputStream(path));
			int size = stream.readInt();
			for(int i=0;i<size;i++) {
				Street s = new Street();
				s.id = stream.readLong();
				s.start = new Point(stream.readDouble(),stream.readDouble());
				s.end = new Point(stream.readDouble(),stream.readDouble());
				int connected_size = stream.readInt();
				for(int j=0;j<connected_size;j++) {
					s.connected_id.add(stream.readLong());
				}
				streets.add(s);
				stmap.put(s.id, s);
			}
			stream.close();
			
			//now map the id to object
			for(Street s:streets) {
				for(Long sid:s.connected_id) {
					s.connected.add(stmap.get(sid));
				}
			}
			System.out.println(size+" streets are loaded");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	double distPointToSegment(Point p, Street s){
		return Util.min_distance_point_to_segment(p.longitude, p.latitude, s.start.longitude, s.start.latitude, s.end.longitude, s.end.latitude);
	}
	
	
	public ArrayList<Street> nearest(Point target, int limit){
		ArrayList<Street> ret = new ArrayList<Street>();
		ArrayList<Double> dist = new ArrayList<Double>();
		double min = Double.MAX_VALUE;
		for(Street st:streets) {
			double d = this.distPointToSegment(target, st);
			if(dist.size()==0) {
				min = d;
				dist.add(d);
				ret.add(st);
				continue;
			}
			//the queue is full and the distance is bigger than or equal to the current minimum
			if(dist.size()>=limit&&d>=min) {
				continue;
			}
			//otherwise, insert current street into the return list, evict the tail
			int insert_into = 0;
			for(;insert_into<dist.size();insert_into++) {
				if(dist.get(insert_into)>=d) {
					ret.add(insert_into, st);
					dist.add(insert_into, d);
					break;
				}
			}
			
			if(ret.size()>limit) {
				ret.remove(limit);
				dist.remove(limit);
			}
			min = dist.get(dist.size()-1);
			
		}
		dist.clear();
		return ret;
	}
	
	public ArrayList<Street> navigate(Point origin, Point dest){
		
		ArrayList<Street> ret = new ArrayList<Street>();
		ArrayList<Street> originset = this.nearest(origin, 5);
		ArrayList<Street> destset = this.nearest(dest, 5);
		
		for(Street o:originset) {
			for(Street d:destset) {
				//initialize 
				for(Street s:streets) {
					s.father_from_origin = null;
				}
				Street s = o.breadthFirst(d.id);
				if(s!=null) {
					while(s.father_from_origin!=null) {
						ret.add(s);
						s = s.father_from_origin;
					}
					break;
				}
			}
			
			if(ret.size()>0) {
				break;
			}
		}
		
		
		
		return ret;
		
	}

}
