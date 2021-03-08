package test_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;

import javax.swing.JTextArea;

class OnlineClient implements Serializable{
	
	private final String name;
	
	public OnlineClient(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

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
	    
	    while (client_msg != null) {
	    	messageChat.append(client_msg + "\n");
	    	server.broadcast_msg(client_msg);
	        client_msg = br.readLine();
	      }
	    }
	    catch (IOException ioe) {
	      System.out.println("Client Disconnected");
	      server.online_threads.remove(this);
	    }
	    messageChat.append("[" + client_name + "] disconnected\n");
	    //this.interrupt();
	    //try {
//			s.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	public ArrayList<ClientHandler> online_threads = new ArrayList<ClientHandler>();
	public ArrayList<OnlineClient> online_clients = new ArrayList<OnlineClient>();
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
	            updateOnlineClients();
	            ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
	            oos.writeObject(online_clients);
	            out = new PrintWriter(mySocket.getOutputStream(),true);
	            out.println("Connection Stablished!\n");
	            ClientHandler hc = new ClientHandler(mySocket, this, messageChat);
	            this.online_threads.add(hc);
	            hc.start();
	         }
		}
		catch (IOException e) {
			messageChat.append("Server closed...\n");
		}
	}
	
	void broadcast_msg(String msg) {
		for (ClientHandler client : online_threads) {
			client.send_msg(msg);
		}
	}
	public void updateOnlineClients() {
		online_clients.clear();
		for (ClientHandler client: online_threads) {
			online_clients.add(new OnlineClient(client.client_name));
		}
		online_clients.forEach((client)-> System.out.println(client.getName()));
	}
}

class Server {
	

	StartServerT serversocket;
	PrintWriter out;
	
	public void startServer(JTextArea messageChat) throws IOException {
		serversocket = new StartServerT(messageChat);
		serversocket.start();
	}
	
	public void removeUser(String name) {
		ArrayList<ClientHandler> online_clients = new ArrayList<ClientHandler>(serversocket.online_threads);
		for (ClientHandler client: online_clients) {
			if (client.client_name == name) {
				//ArrayUtils.removeElement(serversocket.online_clients, client);
			}
		}
		
	}
	public void closeServer(JTextArea messageChat) throws IOException {
		serversocket.server.close();
		serversocket.interrupt();
	}
	
}
