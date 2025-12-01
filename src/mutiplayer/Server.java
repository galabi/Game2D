package mutiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import MainPackage.Main;

public class Server implements Runnable{
	ServerSocket ss;
	BufferedReader in;
	PrintWriter out;
	String response = "start";
	boolean serverUP = false;
	int port = 1000;
	Queue<String> queue = new LinkedList<>();

	@Override
	public void run() {
	    serverUP = true;
	    
	    try {
	    	openServer();
	        
	        System.out.println("[server] Listening on port "+port);
	        Thread t1 = null;
	        while (true) {
	            Socket s = null;

	            try {
	                s = ss.accept();
	                System.out.println("[server] Client connected: " + s.getInetAddress());

	                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	                out = new PrintWriter(s.getOutputStream(), true);
	                
	                
	                t1 = new Thread(new ServerOut());
	                t1.start();
	                
	                sendToClient(Main.tilesManager.toString());
	                sendToClient(Main.player.toString());
	                sendToClient(Main.tilesManager.ObjectsToString());
	                sendToClient(Main.tilesManager.DropsToString());
	                response = in.readLine();

	                if ("test".equals(response)) {
	                    System.out.println("[server] Ping (test) received. Closing connection.");
	                    continue;
	                }

	                do {
	                    if (response != null) {
	                		serverClientHandler.responseHandeler(response);
	                		
	                    	if ("stop".equals(response)) {
	                            break;
	                        }

	                    }
	                    response = in.readLine();
	                } while (!"stop".equals(response));
                    Main.player2 = null;
	                System.out.println("[server] Client disconnected.");
	                
	            } catch (Exception e) {
	                if ("Connection reset".equals(e.getLocalizedMessage())) {
	                    Main.player2 = null;
	                } else {
	                    e.printStackTrace();
	                }
	            } finally {
	                try {
	                    if (in != null) in.close();
	                    if (out != null) out.close();
	                    if (s != null && !s.isClosed()) s.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }

	    } finally {
	        try {
	            if (ss != null && !ss.isClosed()) ss.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	private void openServer() {
		while(ss == null) {
			try {
				ss = new ServerSocket(port);
			}catch (Exception e) {
				port++;
				if(!e.getLocalizedMessage().equals("Address already in use")) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendToClient(String str) {
		queue.add(str);
	}
	
	public boolean isServerUp() {
		return serverUP;
	}
	
	private class ServerOut implements Runnable{
		
		@Override
		public void run() {
			while(true) {
				if(!queue.isEmpty()) {
					out.println(queue.poll());
				}
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
