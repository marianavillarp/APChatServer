package COMP1549_G4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

class MessageHandler extends Thread {
	
	/*
	 * Handles client's incoming messages from server
	 */
	
	Socket mySocket = null;
	JTextArea messageArea;
	public Scanner in;
	ClientGUI frame;
	ReadOnlineClients ocl;
	String user;
	Client client;
	
	public MessageHandler(Client client, ReadOnlineClients ocl)  {
		this.client = client;
		this.mySocket = client.socket;
		this.messageArea = client.clientFrame.messageArea;
		this.frame = client.clientFrame;
		this.ocl = ocl;
		
	}
	
	


	public void run() {
		try {
			
			in = new Scanner(mySocket.getInputStream());
            String str = in.nextLine();
            
            while (str != null ) {
            	
            	if (str.equals("#A")) {
            		/*
            		 * server special messages
            		 */
            		
            		/*
            		 * #A : add new client to onlineClients display
            		 */
            		
            		str = in.nextLine();
            		user = str;	//new user to add
                    str = in.nextLine();
                    ocl.model.addElement(user);
                    frame.onlineCDisplay.setModel(ocl.model); // update display
                    
            	} else {
            		if (str.equals("#R")){
            			/*
            			 * #R : remove specific client from onlineClients display
            			 */
            			str = in.nextLine();
                		user = str;
                        ocl.model.removeElement(user);
                        frame.onlineCDisplay.setModel(ocl.model); // update display
                        str = in.nextLine();
                        
            		}
            		
            		else if (str.equals("#INVALIDNAME")) {
            			
            			/*
            			 * #INVALIDNAME : server rejected username
            			 */

            			JOptionPane.showMessageDialog(frame, "ID is taken ", "Error", JOptionPane.ERROR_MESSAGE);
            			frame.setVisible(false); // closes window
            			mySocket.close(); // ends connection
            			str = null;
            			System.exit(0); 
;            			
            		}
            		
            		else if (str.equals("You are coordinator.")) {
            			/*
            			 *  set this client as coordinator
            			 */
            			client.setCoordinator();
            			messageArea.append(str + "\n");
            			str = in.nextLine();
            		}
            		else {
            			// Receive general messages
            			messageArea.append(str + "\n");
            			str = in.nextLine();
            			
            		}
            		
            	}
            }
            serverDisconnected();
		}
		catch (IOException ioe) {
			serverDisconnected();
		}
		catch (NoSuchElementException nsE) {
			serverDisconnected();
		}
	}
	
	public void serverDisconnected() {
		JOptionPane.showMessageDialog(frame, "Server disconnected");
		frame.setVisible(false);
		System.exit(0);
	}
	
}