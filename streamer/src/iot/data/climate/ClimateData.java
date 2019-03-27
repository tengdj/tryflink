package iot.data.climate;

import java.io.File;
import java.util.HashMap;

import iot.common.TemporalSpatialData;
import iot.tools.utils.FileBatchReader;

public class ClimateData extends TemporalSpatialData{
	
	HashMap<String,iot.data.climate.State> states = new HashMap<>();
	HashMap<String,Station> stations = new HashMap<>();

	//Initialization, load the states and stations information
	public ClimateData(String path) {
		initialize(path);
	}
	
	@Override
	public void loadFromFiles(String path) {
		File file = new File(path);
		if(!file.exists()) {
			System.out.println(path+" does not exist");
			return;
		}
		if(file.isFile()){
			//process only the file for US stations
			if(file.getName().startsWith("US")) {
				//loading data
				boolean header = false;//no header
				FileBatchReader.batchLimit = 200000;
				FileBatchReader reader = new FileBatchReader(path);
				int count = 0;
				while(!reader.eof) {
					for(String line:reader.lines) {
						if(header) {
							header = false;
							continue;
						}
						String stid = line.substring(0,11);
						if(!stations.containsKey(stid)) {//do not fit in any station
							continue;
						}
						int year = Integer.parseInt(line.substring(11,15));
						int month = Integer.parseInt(line.substring(15,17));
						String element = line.substring(17,21);
						//traverse all 31 days in one month, each one takes 8 characters
						for(int i=0;i<31;i++) {
							Element e = new Element(stid,year,month,element,i+1,line.substring(21+i*8,29+i*8));
							if(e.valid) {
								emit(e);
								count++;
							}

						}
					}
					reader.nextBatch();
					System.out.println("processed "+count);
				}
				reader.closeFile();
			}
		}else {//is a folder
			for(File f:file.listFiles()) {
				loadFromFiles(f.getAbsolutePath());
			}
		}
	}

	@Override
	public void initialize(String path) {
		// TODO Auto-generated method stub
		FileBatchReader reader;
		boolean header;
		//loading states
		header = false;
		reader = new FileBatchReader(path+"/states.txt");
		while(!reader.eof) {
			for(String line:reader.lines) {
				if(header) {
					header = false;
					continue;
				}
				iot.data.climate.State s = new iot.data.climate.State(line.split(" "));
				states.put(s.abbriev,s);
			}
			reader.nextBatch();
		}
		reader.closeFile();
		
		
		//loading stations
		header = false;
		reader = new FileBatchReader(path+"/stations.txt");
		while(!reader.eof) {
			for(String line:reader.lines) {
				if(header) {
					header = false;
					continue;
				}
				if(line.startsWith("US")) {//we only need state
					String st = line.substring(38,40);
					if(!st.contentEquals("  ")&&states.containsKey(st))// a valid US state
					{
						Station s = new Station(line);
						stations.put(s.ID, s);
					}
				}
				
			}
			reader.nextBatch();
		}
		reader.closeFile();
		System.out.println(states.size()+" states and "+stations.size()+" stations are loaded");
	}

}
