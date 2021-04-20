package COMP1549_G4;

import java.io.IOException;

import javax.swing.JTextArea;


public class Server extends ServerConnection {
	

	public Server(JTextArea serverScreen) {
		super(serverScreen);
	}
	
	/*
	 * Start server
	 */
	public void startServer() throws IOException {
		this.start();
	}
	
	public String getCoordinator() {
		ClientHandler coordinatorThread = this.getCurrentCoordinatorThread();
		
		if (coordinatorThread == null) {
			return "No connected clients";
		} else {
			return coordinatorThread.username;
		}
	}
	
	/*
	 * Get this server's current port
	 */
	public String getPort() {
		Integer serverPort = this.serverSocket.getLocalPort();
		return serverPort.toString();
	}
	
	/*
	 * Close server and its connections
	 */
	public void closeServer() throws IOException {
		
		if (out != null) {
			for (ClientHandler c: this.currentConnections) {
				if (c.out != null) {
					c.out.println();
					c.endConnection();
					this.out.close();
				}
			}
		}
		this.serverSocket.close();
		serverScreen.append("Server closed...\n");
		this.interrupt();
	}
	
	
}
