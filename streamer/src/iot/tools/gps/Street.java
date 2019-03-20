package iot.tools.gps;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import iot.common.Point;

/*
 * represents a segment with some features
 * 
 * */
public class Street {
	public Point start;
	public Point end;
	public long id;
	public ArrayList<Street> connected = new ArrayList<Street>();
	public ArrayList<Long> connected_id = new ArrayList<Long>();//internal use only, facilitate list for loading connection relations
	
	public Street father_from_origin;
	public double dist_from_origin;
	
	public void print() {
		System.out.print(id+"\t: ");
		System.out.print("[["+start.longitude+","+start.latitude+"],");
		System.out.println("["+end.longitude+","+end.latitude+"]]\t");
		System.out.print("\tconnect: ");
		
		for(Street s:connected) {
	        System.out.print(s.id+"\t");
		}
	    System.out.println();

	}
	
	public Street(Point start, Point end, long id) {
		this.start = start;
		this.end = end;
		this.id = id;
	}
	
	public Street() {
		// TODO Auto-generated constructor stub
	}

	public Point close(Street seg) {
		if(seg==null) {
			return null;
		}
		if(seg.start.close(start)||seg.start.close(end)) {
			return seg.start;
		}
		if(seg.end.close(end)||seg.end.close(start)) {
			return seg.end;
		}
		return null;
	}
	
	//whether the target segment interact with this one
	//if so, put it in the connected map
	public boolean touch(Street seg) {
		//if those two streets are connected, record the connection relationship
		//since one of the two streets is firstly added, it is for sure it is unique in others list
		if(close(seg)!=null) {
			connected.add(seg);
			seg.connected.add(this);
			return true;
		}
		return false;		
	}
	
	
	
	/*
	 * commit a breadth-first search start from this
	 * 
	 * */
	public Street breadthFirst(Long target_id) {
		
		if(this.id==target_id) {
			return this;
		}
		Queue<Street> queue = new LinkedList<>();
		queue.add(this);
		while(!queue.isEmpty()) {
			
			Street s = queue.poll();
			if(s.id == target_id) {//found
				return s;
			}
			for(Street sc:s.connected) {
				if(sc==this) {//skip current 
					continue;
				}
				if(sc.father_from_origin==null) {
					sc.father_from_origin = s;
					queue.add(sc);
				}
			}			
		}
		
		return null;//not found
		
		//not the origin
//		if(father != null) {
//			
//			//firstly visited
//			if(this.father_from_origin==null) {
//				this.father_from_origin = father;
//			}
//			Point p = this.father_from_origin.close(this.father_from_origin.father_from_origin);//where father is connected from the origin
//			//calculate the distance from origin if we follow this path, it is guaranteed that this.father_from_origin is not null
//			double new_distance = this.close(father).distance(p)+father.dist_from_origin;
//			double old_distance = this.close(this.father_from_origin).distance(p)+this.father_from_origin.dist_from_origin;
//			if(new_distance<old_distance) {//forward to new path, the old one is abandoned
//				this.father_from_origin = father;
//			}
//		}else {
//			this.father_from_origin = null;//has no father
//			this.dist_from_origin = 0;
//		}
//		
//		for(Street s:this.connected) {
//			Street ret = s.breadthFirst(this, target_id);
//			if(ret!=null) {
//				return ret;
//			}
//		}
//		return null;
	}
	
	
	
}
