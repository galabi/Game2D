package mutiplayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import MainPackage.Main;

public class Client implements Runnable{
	Socket s;
	BufferedReader in;
	PrintWriter out;
	long time;
	Queue<String> queue = new LinkedList<>();
	int port;
	
	public Client() {}
	
	public Client(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
    	try {
    		Thread t1 = null;
        	s = new Socket("localhost",port);  
        	in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        	out = new PrintWriter(s.getOutputStream(),true);
            
        	
            t1 = new Thread(new ClientOut());
            t1.start();
            
        	
        	String response;
    		sendToServer(Main.player.toString());

        	do{                
        		response = in.readLine();
            	if(response != null) {
            		ServerClientHandler.responseHandeler(response);
            	}else {
            		response = "ok";
            	}
            }while(!response.equals("stop"));
            s.close();  
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void checkAvailablePorts(ServerSelectScreen serverSelect) {
        for (int port = 1000; port <= 2000; port++) {
            try (Socket socket = new Socket("localhost", port)) {
                System.out.println("[client] Port " + port + " is open.");
				PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
				tempOut.println("test");
                serverSelect.addIp(socket.getInetAddress()+"", port);
                socket.shutdownInput();
            } catch (Exception e) {
                if (!"Connection refused".equals(e.getLocalizedMessage())) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	public void sendToServer(String str) {
		queue.add(str);
	}
	
	private class ClientOut implements Runnable{
		
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
