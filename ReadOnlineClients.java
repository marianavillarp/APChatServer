package COMP1549_G4;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

class ReadOnlineClients extends Thread{
	
	/*
	 * Reads onlineClients list from server and display it on client's frame
	 */
	
	Socket mySocket;
	ClientGUI frame;
	InputStream in;
	DefaultListModel model;
	
	// Constructor
	public ReadOnlineClients(Socket mySocket, ClientGUI frame) {
		this.mySocket = mySocket;
		this.frame = frame;
	}
	
	public void run() {
		
		try {
			
			in = mySocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
		    ArrayList<OnlineClient> online_clients = (ArrayList<OnlineClient>) ois.readObject(); // read list from server
		    model = new DefaultListModel();
		    online_clients.forEach((client)-> model.addElement(client.getName())); // add list elements to model
		    frame.onlineCDisplay.setModel(model); // display model
		     
		}
		catch (IOException oie) {
			System.out.println("Could not read online clients list");
			
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found");
		}
	}
	
}