package test_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JTextArea;

class ClientHandler extends Thread {

	  Socket s = null;
	  JTextArea messageChat;
	  String client_name;
	  PrintWriter out;
	  StartServerT server;
	  
	  public ClientHandler(Socket s, StartServerT server, JTextArea messageChat) {
	  this.s = s;
	  this.messageChat = messageChat;
	  this.server = server;
	}
	public void run() {
	    messageChat.append(new Date().toString() + " New client ");
	    try {
	    InputStream is = s.getInputStream();
	    BufferedReader br = new BufferedReader (new InputStreamReader(is));
	    out = new PrintWriter(s.getOutputStream(),true);
	    
	    String client_msg = br.readLine();
	    set_client_name(client_msg);
	    
	    messageChat.append("[" + client_name + "]\n" );
	    client_msg = "[" + client_name + "] has joined";
//	    broadcast_msg(client_msg);
//	    client_msg = br.readLine();
	    
	    while (client_msg != null) {
	    	messageChat.append(client_msg + "\n");
	    	server.broadcast_msg(client_msg);
	        client_msg = br.readLine();
	      }
	    }
	    catch (IOException ioe) {
	      ioe.printStackTrace();
	    }
	    messageChat.append("[" + client_name + "] Disconnected\n");
	} // end of run()
	
	void set_client_name(String name) {
		this.client_name = name;
	}
	
	void send_msg(String message) {
		out.println(message);
	}
	}


class StartServerT extends Thread {
	private JTextArea messageChat;
	private boolean end;
	public Set<ClientHandler> online_clients = new HashSet<>();
	ServerSocket server;
	PrintWriter out;
	
	public StartServerT(JTextArea messageChat) {
		this.messageChat = messageChat;
	}
	
	public void run() {
		try {
			server = new ServerSocket(7000);
			messageChat.append("Server running...\n");
			while (true) {
	            Socket mySocket = server.accept();
	            out = new PrintWriter(mySocket.getOutputStream(),true);
	            out.println("Connection Stablished!\n");
	            ClientHandler hc = new ClientHandler(mySocket, this, messageChat);
	            this.online_clients.add(hc);
	            hc.start();
	         }
		}
		catch (IOException e) {
			messageChat.append("Server closed...\n");
		}
	}
	
	void broadcast_msg(String msg) {
		for (ClientHandler client : online_clients) {
			client.send_msg(msg);
		}
	}
	
}

class Server {
	

	StartServerT serversocket;
	PrintWriter out;
	
	public void startServer(JTextArea messageChat) throws IOException {
		serversocket = new StartServerT(messageChat);
		serversocket.start();
	}
	
	public void sendMessage(String message, Client client) {
		
	}
	public void closeServer(JTextArea messageChat) throws IOException {
		serversocket.server.close();
		serversocket.interrupt();
		
		
	}
}
