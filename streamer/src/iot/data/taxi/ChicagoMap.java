package iot.data.taxi;

import org.json.JSONArray;
import org.json.JSONObject;

import iot.common.Point;
import iot.tools.gps.Map;
import iot.tools.gps.Street;
import iot.tools.utils.FileBatchReader;

public class ChicagoMap extends Map {

	public void loadFromJson(String path) {
		
		System.out.println("loading streets from Json file "+path);
		FileBatchReader fr = new FileBatchReader(path);
		String json_str = fr.readAll();
		fr.closeFile();
		JSONObject object = new JSONObject(json_str);
		JSONArray features = (JSONArray) object.get("features");
		long id = 0;
		for(int i=0;i<features.length();i++) {
			String coordinates = features.getJSONObject(i).getJSONObject("geometry").get("coordinates").toString();
			//System.out.println(coordinates);
			if(coordinates.contentEquals("[]")) {
				continue;
			}			
			coordinates = coordinates.replace("[[[", "");
			coordinates = coordinates.replace("]]]", "");
			String sts[] = coordinates.split("\\],\\[");
			
			//connect the head and tail of the list, ignore the middle points
			Point head = new Point(Double.parseDouble(sts[0].split(",")[0]),Double.parseDouble(sts[0].split(",")[1]));
			Point tail = new Point(Double.parseDouble(sts[sts.length-1].split(",")[0]),Double.parseDouble(sts[sts.length-1].split(",")[1]));
			streets.add(new Street(head,tail,id++));
			
//			System.out.println(sts[0]);
//			System.out.println(sts[sts.length-1]);
//			System.out.println();
//			Point former = new Point(Double.parseDouble(sts[0].split(",")[0]),Double.parseDouble(sts[0].split(",")[1]));
//			for(int j=1;j<sts.length;j++) {
//				Point cur = new Point(Double.parseDouble(sts[j].split(",")[0]),Double.parseDouble(sts[j].split(",")[1]));
//				Segment seg = new Segment(former,cur,id++);
//				former = cur;
//				//System.out.println(seg.start.longitude+" "+seg.start.latitude+"->"+seg.end.longitude+" "+seg.end.latitude);		
//				streets.add(seg);
//			}
		}
		connect_segments();
		System.out.println(streets.size()+" streets are loaded");
	}
}