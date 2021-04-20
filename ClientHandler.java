package COMP1549_G4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Formatter;

import javax.swing.JTextArea;

public class ClientHandler extends Thread {
	
	/** One thread per client connected to the server, manages incoming, 
	 * outgoing msgs and connection ***/

	  public Socket s = null;
	  public JTextArea serverScreen; // server message display
	  public String username;	// current client username
	  public String userIp;	// current client IP
	  PrintWriter out;
	  InputStream is;
	  ServerConnection server;
	  Formatter fmt;	// for time stamps
	  public boolean isCoordinator = false;	// used to check if the current user has admin faculties
	  
	  /*
	   * Constructor
	   */
	  public ClientHandler(Socket s, ServerConnection server) {
	  this.s = s;
	  this.serverScreen = server.serverScreen;
	  this.server = server;
	}
	  
	public void run() {
		
	    serverScreen.append("[" + getTime() + "] " + "New connection: ");
	    try {
		    	
		    is = s.getInputStream();
		    BufferedReader br = new BufferedReader (new InputStreamReader(is));
		    out = new PrintWriter(s.getOutputStream(),true);
		    String client_msg = br.readLine();
		    
		 /*
		  * Identify owner of current thread
		  */
		    if (acceptName(client_msg)==true) {
		    	 setClientName(client_msg); 
		    	 send_msg("Connection Stablished!\n"); 
				    
				    /*
				     * Let the user know if they are the first client
				     */
				    if (server.onlineClients.size()==0) {
				    	out.println("You are the first client connected to the server!");
				    }
				    
				    checkCoordinator();	/* let the user know who is the current coordinator*/
				    
				    s.setSoTimeout(120000);	// time out after two minutes
				    
				    
				    client_msg = br.readLine();
				    userIp = client_msg;
				    
				    serverScreen.append(username.toUpperCase() + "\n" );
				    client_msg = username.toUpperCase() +" has joined with ip: "+ userIp;
				    server.updateConnections(username,false,false);
				    
				    
				    while (client_msg != null) {
				    	/*
				    	 *  loop until there are no more messages 
				    	 */
				    	if (isCoordinator == true && client_msg.equals("#USEREMOVE")) {
				    		/*
				    		 * remove specific user 
				    		 */
				    		client_msg = br.readLine();
				    		server.broadcastMsg("[" + client_msg.toUpperCase() + "] removed.");
				    		server.updateConnections(client_msg, true,true);
				    		client_msg = br.readLine();
				    	}
				    	/*
				    	 * Private message
				    	 */
				    	else if (client_msg.equals("#PRIVATEMSG")) {
					    		String sUser = br.readLine();
					    		client_msg = br.readLine();
					    		client_msg = "[" + getTime() + "] "+ client_msg ;
					    		server.sendPM(sUser,client_msg);
					    		send_msg(client_msg);
					    		serverScreen.append("[" + getTime() + "] PM messaged received\n");
					    		client_msg = br.readLine();
				    	}
					    else { /* Rest of messages*/
				    		serverScreen.append("[" + getTime() + "] "+client_msg + "\n");
					    	server.broadcastMsg("[" + getTime() + "] "+client_msg);
					        client_msg = br.readLine();
				    	}
				      }
					serverScreen.append("[" + getTime() + "] " + username.toUpperCase() + " disconnected.\n");
				    server.broadcastMsg("[" + username.toUpperCase() + "] disconnected at "+ getTime());
				    updateServer();
				    endConnection();

		    
		    } else {
		    	/*Connection denied*/
		    	serverScreen.append(" denied.\n");
			    endConnection();
		    }

		    
	    }
	    catch (IOException ioe) {
	      // close connection
		    try {
				endConnection();
				serverScreen.append("[" + getTime() + "] " + username.toUpperCase() + " has disconnected.\n");
			    server.broadcastMsg("[" + username.toUpperCase() + "] has disconnected at "+ getTime()+".");
			    updateServer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
	    		  
	    } catch (NullPointerException ne) {
	    	
	    }

	} // end of run()
	
	
	void setClientName(String name) {
		this.username = name;
	}

	void send_msg(String message){
		// send message to this client
		out.println(message);
	}
	
	public Formatter getTime() {
		/*
		 * current time hh:mm stamp format
		 */
		  fmt = new Formatter();
		  Calendar cal = Calendar.getInstance();
		  fmt = new Formatter();
		  fmt.format("%tl:%tM",cal,cal);
		  return fmt;
		
	}
	
	public void checkCoordinator(){
		if (isCoordinator == true) {
			send_msg("You are coordinator.");
		}
		else {
			send_msg("Current coordinator is: [" + server.getCurrentCoordinatorThread().username.toUpperCase() + "]");
		}
	}
	
	public boolean acceptName(String name) {
		for (ClientHandler client: server.currentConnections) {
	    	/*
	    	 * Check if the new client has an already registered name
	    	 */
	    	if (client.username != null && client.username.equals(name)) {
		    	try {
		    		send_msg("#INVALIDNAME");
				    server.currentConnections.remove(this); //delete connection from server
				    endConnection();
				    return false;
		    	}
		    	catch (IOException ioe) {
		    		System.out.println("Could not verify name");
		    		this.interrupt();
		    	}
	    	}
	    }
		return true;
	}
	
	private void updateServer() throws SocketException {
		/* 
		 * Update sere's current connections
		 */
	    server.updateConnections(username,true,false);
	    server.currentConnections.remove(this);
	    server.setCurrentAdmin();
	    isCoordinator= false;
	}
	
	public void endConnection() throws IOException {
		if (out != null && is != null) {
			is.close();
		    out.close();
		    s.close();
		}
	}

}
