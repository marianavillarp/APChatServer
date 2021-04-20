package COMP1549_G4;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

abstract class ServerConnection extends Thread {
	/*
	 * Thread to handle the server ***/
	
	public JTextArea serverScreen;
	public ArrayList<ClientHandler> currentConnections = new ArrayList<ClientHandler>(); // All clients' threads connected to the server
	public ArrayList<OnlineClient> onlineClients = new ArrayList<OnlineClient>();	// Connected clients usernames
	public ServerSocket serverSocket;
	PrintWriter out;
	ObjectOutputStream oos;
	private ClientHandler hc;
	public ClientHandler currentCoordinator;
	int port;
	
	/*
	 * Constructor
	 */
	public ServerConnection(JTextArea serverScreen) {
		this.serverScreen = serverScreen;
		this.port = 7000;
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			serverScreen.append("Server running...\n");
			while (true) {
	            Socket mySocket = serverSocket.accept();	// accept connection - new user
	            onlineClientsList(mySocket);	// send list of online clients to new user
	            out = new PrintWriter(mySocket.getOutputStream(),true);
	            hc = new ClientHandler(mySocket, this);
	            this.currentConnections.add(hc);	// add new connection to server
	            setCurrentAdmin();	// method to set admin faculties
	            hc.start();
	         }
		}
		catch (IOException e) {

		}
	}
	
	/*
	 * output the online client list to Client class
	 */
	private void onlineClientsList(Socket mySocket) {

		onlineClients.clear();
		for (ClientHandler client: currentConnections) {
			onlineClients.add(new OnlineClient(client.username));
		}
		try {
			// object output stream to output onlineClients list to new user
			oos = new ObjectOutputStream(mySocket.getOutputStream());
			oos.writeObject(onlineClients);
		} catch (IOException e) {
			System.out.println("Error: Server can't output online clients list.");
		}
	}
	
	
	/*
	 *  Update currentConnections and onlineClients lists and output code for the client to update their list
	 */
	// fromAdmin : if the admin is the one trying to remove a client
	public void updateConnections(String cUsername, boolean remove, boolean fromCoordinator) throws SocketException {
		
		// currentConnections iterator in order to modify it simultaneously 
		Iterator<ClientHandler> iterator = currentConnections.iterator();
		
		while(iterator.hasNext()) {
			
			ClientHandler client = iterator.next();
					
			if(client.username != cUsername) {
				if (remove == false) {
					client.send_msg("#A");	// add user
					client.send_msg(cUsername);
					onlineClients.add(new OnlineClient(cUsername));
				} 
				if (remove == true){
					client.send_msg("#R");	// remove user
					client.send_msg(cUsername);
					onlineClients.remove(cUsername);
				}
			} 
			if (client.username.equals(cUsername) && remove == true && fromCoordinator == true) {
				try {
					client.is.close();
					client.out.close();
					client.s.close(); // close connection with selected user to remove
					iterator.remove();	// remove from server currentConnections
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	
		}
	}
	
	/*
	 * set server's current coordinator
	 */
	public void setCurrentAdmin() throws SocketException {
		
		/*
		 * first connection is coordinator
		 */
		if (currentCoordinator == null) {
			
			currentCoordinator = currentConnections.get(0);
			currentCoordinator.isCoordinator = true;
		}
		else {
			
			/*
			 * if all current clients have disconnected
			 */
			if (currentConnections.size()==0) {
				
				currentCoordinator = null;
			} 
			/*
			 * if current coordinator has disconnected, the next connection is coordinator
			 */
			else if (currentCoordinator != currentConnections.get(0)){

				currentCoordinator = currentConnections.get(0);
				currentCoordinator.isCoordinator = true;
				currentCoordinator.checkCoordinator();
			}
			
		}
	}
	
	public ClientHandler getCurrentCoordinatorThread() {
		return currentCoordinator;
	}
	
	// for each connected client, send message
	public void broadcastMsg(String msg) {
		for (ClientHandler client : currentConnections) {
			client.send_msg(msg);
		}
	}
	
	public void sendPM(String toUser, String msg) {
		for (ClientHandler client:currentConnections) {
			if (client.username.equals(toUser)) {
				client.send_msg(msg);
			}
		}
	}
}

