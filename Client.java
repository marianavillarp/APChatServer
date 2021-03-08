package test_client;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

class HandleMsgs extends Thread {
	
	Socket mySocket = null;
	JTextArea messageArea;
	public Scanner in;
	ObjectInputStream inn;
	
	public HandleMsgs(Socket mySocket, JTextArea messageArea)  {
		this.mySocket = mySocket;
		this.messageArea = messageArea;
	}
	
	
	public void run() {
		try {
			in = new Scanner(mySocket.getInputStream());
            String str = in.nextLine();
            while (str != null) {
            	messageArea.append(str + "\n");
            	str = in.nextLine();
            }
		}
		catch (IOException ioe) {
		      ioe.printStackTrace();
		}
	}
}

class OnlineClients extends Thread{
	Socket mySocket;
	ClientGUI frame;
	InputStream in;
	
	public OnlineClients(Socket mySocket, ClientGUI frame) {
		this.mySocket = mySocket;
		this.frame = frame;
	}
	
	public void run() {
		try {
			in = mySocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
		    ArrayList<OnlineClient> online_clients = (ArrayList<OnlineClient>) ois.readObject();
		    DefaultListModel model = new DefaultListModel();
		    online_clients.forEach((client)-> model.addElement(client.getName()));
		    frame.onlineClients.setModel(model);
		    
		    
		}
		catch (IOException oie) {
			System.out.println("meh1");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("meh");
		}
	}
	
}

class Client {
	
	private PrintWriter out;
	private Socket socket;
	public String name;
	public int port;
	ClientGUI clientFrame; //after disconnect go back to login window
	
	public Client(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	public void contactServer(JTextArea messageArea, ClientGUI frame) throws IOException {
        socket = new Socket("127.0.0.1", port);
        OutputStream os = socket.getOutputStream();
        out = new PrintWriter(os, true) ;
        out.println(name);
        
        this.clientFrame = frame;
        OnlineClients ocl = new OnlineClients(socket,clientFrame);
        ocl.start();
        
        HandleMsgs hmsg = new HandleMsgs(socket,messageArea);
        hmsg.start();
	}
	
	public void sendMessage(JTextArea messageChat) {
		int inputLines = messageChat.getLineCount();
		try {
			for (int i = 0 ; i < inputLines; i++) {
				int start = messageChat.getLineStartOffset(i);
                int end = messageChat.getLineEndOffset(i);
                String str = messageChat.getText(start, end - start);
                out.println("[" + this.name + "] " + str);
                messageChat.setText("");
			}
		} 
		catch (BadLocationException ble) {
			System.out.println("no more messages");
		}
		
	}
	
	public String getUsername() {
		return name;
	}
	
	
}
