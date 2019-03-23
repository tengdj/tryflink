package main;

import java.util.ArrayList;

import iot.common.Point;
import iot.data.aot.AOTData;
import iot.data.taxi.ChicagoMap;
import iot.data.taxi.TaxiData;
import iot.tools.gps.Map;
import iot.tools.gps.Street;
import iot.tools.utils.Util;

public class Main {
	
	public static void main(String args[]) {
//		GeoHash gh = GeoHash.fromGeohashString("tengdejun");
//		System.out.println(gh.getBoundingBoxCenterPoint().getLatitude()+" "+gh.getBoundingBoxCenterPoint().getLongitude());
//		System.out.println(gh.toBase32());
		
//		gh = GeoHash.withCharacterPrecision(40.084591, 116.031330, 12);
//		System.out.println(gh.toBase32());
//
		
		
//		AOTData dt = new AOTData("data/aotdata");
//		dt.loadFromFiles("data/aotdata/data.csv");
//		dt.loadFromFiles("data/aot/data.csv.gz");
		
		
//		ClimateData cd = new ClimateData("climate");
//		cd.loadFolder("climate/daily");
		
		ChicagoMap st = new ChicagoMap();
//		st.loadFromCsv("data/chicago/transportation.csv");
//		st.dumpTo("data/chicago/formated");
		st.loadFromFormatedData("data/chicago/formated");
		ArrayList<Street> nav = st.navigate(new Point(-87.62076287,41.89833179), new Point(-87.90303966,41.97907082));
    	Util.dumpToFile("chicago", Map.genGeoJson(nav).toString(1));
		
//		TaxiData td = new TaxiData("data/chicago/formated");
//		td.loadFromFiles("data/chicago/Taxi_Trips.csv");

		

//		System.out.println(Util.min_distance_point_to_segment(0,0, 2,0, 0, 4));
	}
	
}
