package iot.tools.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import iot.common.Event;
import iot.data.climate.ClimateData;
import iot.data.climate.Element;

public class ClimateDataTransposer extends ClimateData {
	
	// only those three fields need be stored in files
	// timestamp can be given by the key
	private class TinyElement{
		public String stationid;
		public String element;
		public double value;	
		public TinyElement(Element e) {
			this.stationid = e.stationid;
			this.element = e.element;
			this.value = e.value;
		}
	}
	
	// events happened in a time period
	HashMap<Long, ArrayList<TinyElement>> time_bin = new HashMap<>();
	// note that we should not use buffered writer, it will
	// take too many spaces 
	HashMap<Long, FileWriter> writers = null;
	String output_dir = null;
	Stack<String> file_list = null;
	long event_counter = 0;
	final long max_event_counter = 1000000;
	int thread_id = 0;
	int origin_stack_size = 0;
	// internal use only

	
	public ClimateDataTransposer(int thread_id, String meta_dir_path, String out_dir, Stack<String> file_list, HashMap<Long, FileWriter> writers) {
		super(meta_dir_path);
		output_dir = out_dir;
		this.file_list = file_list;
		this.thread_id = thread_id;
		this.origin_stack_size = file_list.size();
		this.writers = writers;
	}
	
	@Override
	public void finalize() {
		flushToFolder();
	}
	
	void flushToFolder(){
		if(event_counter==0||output_dir==null) {
			return;
		}
		
		// compose the output string 
		System.out.println("thread "+thread_id+" is flushing to "+output_dir);
		for (HashMap.Entry<Long,ArrayList<TinyElement>> entry : time_bin.entrySet()) {

			long timestamp = entry.getKey();
			ArrayList<TinyElement> elements = entry.getValue();
			String out_str = "";
			// generate the output string for this file
			for(TinyElement e:elements) {
				out_str += e.stationid+"|"+e.element+"|"+e.value+"\n";
			}
			elements.clear();
			
			FileWriter writer = null;
			// get the writter to the target file
			synchronized(writers) {
				// writer already been created
				if(writers.containsKey(timestamp)) {
					writer = writers.get(timestamp);
				}else {
					String filename = Paths.get(output_dir, Util.formatTimestamp("yyyy-MM-dd-hh-mm-ss", timestamp)+".evt").toString();
					File outfile = new File(filename);
					if(!outfile.exists()) {
						try {
							outfile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					try {
						writer = new FileWriter(outfile);
						writers.put(timestamp, writer);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			//append to the target file, protected by lock
			synchronized(writer) {
				try {
					writer.write(out_str);
					writer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		time_bin.clear();
		event_counter = 0;
	}
	
	@Override
	protected void emit(Event e) {
		//push event to the bin of the certain time
		ArrayList<TinyElement> list;
		if(!time_bin.containsKey(e.timestamp)) {
			list = new ArrayList<TinyElement>();
		}else {
			list = time_bin.get(e.timestamp);
		}
		Element elm = (Element)e;
		elm.value /= 10;
		TinyElement telm = new TinyElement(elm);
		list.add(telm);
		time_bin.put(e.timestamp, list);
		if(event_counter++>=max_event_counter) {
			flushToFolder();
		}
	}

	@Override
	public void run() {
		if(file_list == null) {
			System.err.println("file list is null, please specify before using");
			finalize();
		}
		while(!file_list.isEmpty()) {
			String path = null;
			synchronized(file_list) {
				if(!file_list.empty()) {
					path = file_list.pop();
					System.out.println("thread "+thread_id+" is processing "+path+" ("+file_list.size()+" remains)");
				}
			}
			if(path==null) {
				break;
			}
			loadFromFiles(path);
		}
		finalize();
	}
}
