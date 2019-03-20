package iot.tools.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileBatchReader {

	public boolean eof = false;
	public ArrayList<String> lines = new ArrayList<String>();
	public static int batchLimit =100000;
	BufferedReader br;
	
	public FileBatchReader(String path) {
		try {
			br = new BufferedReader(new FileReader(path));
			nextBatch();
		} catch (FileNotFoundException e) {
			eof = true;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FileBatchReader(BufferedReader br) {
		this.br = br;
		nextBatch();
	}
	
	public String readAll() {
		String str = "";
		while(!eof) {
			for(String line:lines) {
				str += line;
			}
			nextBatch();
		}
		return str;
	}
	
	public void nextBatch() {
		lines.clear();
		String line;
		int linenum = 0;
		try {
			while((line=br.readLine())!=null) {
				//System.out.println(line);
				lines.add(line);
				if(++linenum>=batchLimit) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			eof = true;
		}
		if(linenum==0) {
			eof = true;
		}
	}
	
	public void closeFile() {
		try {
			br.close();
			eof = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
