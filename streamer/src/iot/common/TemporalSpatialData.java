package iot.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class TemporalSpatialData extends Thread{

    public int port = StreamerConfig.get("stream-port");
    public String load_path;
    public PrintWriter out = null;
    public Socket socket = null;
    public ServerSocket listener = null;

    public abstract void initialize(String path);
    public abstract void loadFromFiles(String path);

    public void emit(Event e) {
	out.println(e.toString());
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
	this.loadFromFiles(load_path);
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
