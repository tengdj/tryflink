package iot.data.aot;

import java.text.ParseException;
import java.util.HashMap;

import iot.common.Point;
import iot.common.TemporalSpatialData;
import iot.tools.utils.CompressedFileReader;
import iot.tools.utils.FileBatchReader;

public class AOTData extends TemporalSpatialData{
	
	HashMap<Long, Node> nodes = new HashMap<>();
	HashMap<String, Provenance> provenances = new HashMap<>();
	HashMap<String, Sensor> sensors = new HashMap<>();
	
	//initialize nodes, provenances and sensors
	public AOTData(String path) {
		this.initialize(path);
	}
	
	//emit formated data
	@Override
	public void loadFromFiles(String path) {
		
		//loading data
		boolean header = true;//skip header
		FileBatchReader.batchLimit = 200000;
		FileBatchReader reader = null;
		if(path.endsWith(".csv")) {
			reader = new FileBatchReader(path);
		}else if(path.endsWith(".gz")) {
			reader = new FileBatchReader(CompressedFileReader.getBufferedReaderForCompressedFile(path, "gz"));
		}else {
			System.err.println("unsupported file format");
		}
		while(!reader.eof) {
			for(String line:reader.lines) {
				if(header) {
					header = false;
					continue;
				}
				Thing d;
				try {
					d = new Thing(line.split(","));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				//validate the datum parsed
//				if(!sensors.containsKey(d.sensor+d.parameter)) {
//					System.out.println(d.sensor+" "+d.parameter+" does not exist");
//					System.out.println(line+"\n");
//				}
				//the node_id must be valid
				if(!nodes.containsKey(d.node_id)) {
					System.out.println("node "+d.node_id+" does not exist");
				}else {
					//assign the coordinate information to one "thing"
					Node n= nodes.get(d.node_id);
					d.coordinate = new Point(n.longitude,n.latitude);
					emit(d);
				}
			}
			reader.nextBatch();
		}
		reader.closeFile();
	}

	@Override
	public void initialize(String path) {
		String filepath;
		FileBatchReader reader;
		//loading data from provenance file
		filepath = path+"/provenance.csv";
		boolean header = true;
		reader = new FileBatchReader(filepath);
		while(!reader.eof) {
			for(String line:reader.lines) {
				if(header) {
					header = false;
					continue;
				}
				Provenance p;
				try {
					p = new Provenance(line.split(","));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				provenances.put(p.project_id,p);
			}
			reader.nextBatch();
		}
		reader.closeFile();
		
		//loading sensor
		filepath = path+"/sensors.csv";
		header = true;//skip header
		reader = new FileBatchReader(filepath);
		while(!reader.eof) {
			for(String line:reader.lines) {
				if(header) {
					header = false;
					continue;
				}
				Sensor s = new Sensor(line.split(","));
				sensors.put(s.sensor+s.parameter,s);
			}
			reader.nextBatch();
		}
		reader.closeFile();
		
		//loading nodes
		filepath = path+"/nodes.csv";
		header = true;//skip header
		reader = new FileBatchReader(filepath);
		while(!reader.eof) {
			for(String line:reader.lines) {
				if(header) {
					header = false;
					continue;
				}
				//contains a string with ","
				String newline = "";
				if(line.contains("\"")) {
					boolean instring = false;
					for(char ch:line.toCharArray()) {
						if(ch=='"') {
							if(instring) {
								instring = false;
							}else {
								instring = true;
							}
						}
						if(ch==',') {
							if(instring) {//replace comma in string to tab
								ch = '\t';
							}
						}
						newline += ch;
					}
				}else {
					newline = line;
				}				
				Node n;
				try {
					n = new Node(newline.split(","));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				nodes.put(n.node_id,n);
				
			}
			reader.nextBatch();
		}
		reader.closeFile();
	}
}
