package COMP1549_G4;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class Client {
	
	public PrintWriter out;
	public Socket socket;
	private String name;
	public int port;
	public String ip;
	public ClientGUI clientFrame;
	public boolean isCoordinator = false;
	
	public Client(String name, String ip, int port) {
		this.name = name;
		this.port = port;
		this.ip = ip;
	}
	
	public void contactServer() throws IOException {
        /*
         * Contact server method
         */
		socket = new Socket(ip, port); 
		clientFrame = new ClientGUI(this);
        OutputStream os = socket.getOutputStream();
        out = new PrintWriter(os, true) ;
        out.println(this.getUsername());
        out.println(ip);
        
        /*
         * Receive online clients list
         */
        ReadOnlineClients ocl = new ReadOnlineClients(socket,clientFrame);
        ocl.start();

        /*
         * Thread to handle messages
         */
        MessageHandler hmsg = new MessageHandler(this, ocl);
        hmsg.start();
	}
	
	public void sendMessage(JTextArea messageChat, boolean pm, Object sUser) {
		/*
		 * Read message from ClientGUI and send it to the server
		 */
		
		int inputLines = messageChat.getLineCount();
		try {
			
			String message = messageChat.getText().trim();
            if(message.isEmpty()) {
                return;
            }
			for (int i = 0 ; i < inputLines; i++) {
				int start = messageChat.getLineStartOffset(i);
                int end = messageChat.getLineEndOffset(i);
                String str = messageChat.getText(start, end - start);
                
                if (pm == false) {
                	out.println("[" + this.name.toUpperCase() + " < everyone] " + str);
                    messageChat.setText("");
                } else if (pm==true && sUser.toString() != null) {
                	out.println("#PRIVATEMSG");
                	out.println(sUser.toString());
                	out.println("[" + this.name.toUpperCase() + " > " + sUser.toString().toUpperCase() +"] " + str);
                	messageChat.setText("");
                }
			}
		} 
		catch (BadLocationException ble) {
			System.out.println("No more messages");
		}
		
	}
	
	public String getUsername() {
		return name;
	}
	
	/*
	 * Coordinator methods
	 */
	public void setCoordinator() {
		isCoordinator = true;
		setCoordinatorFaculties();
		
	}
	
	private void setCoordinatorFaculties() {
		if (isCoordinator == true) {
			clientFrame.adminBtn.setVisible(true);
		}
	}
	
	public void removeUser(Object sUser) { // Allow the coordiator to remove users
		if (isCoordinator == true) {
			String sUsername = sUser.toString();
			out.println("#USEREMOVE");	// special code to server
			out.println(sUsername);
		}
	}

	/* 
	 * Disconnect from server
	 */
	public void endConnection() {
		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
