package iot.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import iot.tools.utils.StreamerConfig;

public abstract class TemporalSpatialData extends Thread{
	
	public int port = StreamerConfig.getInt("stream-port");
	public String load_path = null;
	public PrintWriter out = null;
	public Socket socket = null;
	public ServerSocket listener = null;
	
	public abstract void initialize(String path);
	public abstract void loadFromFiles(String path);
	public void emit(Event e) {
		if(out!=null) {
			out.println(e.toString());
		}else {
			System.out.println(e.toString());
		}
	};
	
	public void setPath(String path) {
		this.load_path = path;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public void run() {
		try {
			listener = new ServerSocket(port);
			System.out.println("listening for connection at port "+port);
			socket = listener.accept();
			System.out.println("connected to "+socket.getInetAddress().getHostAddress());
			out =  new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		loadFromFiles(load_path);
		disconnect();
	}
	
	public void disconnect() {
		if(socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(listener!=null) {
			try {
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
