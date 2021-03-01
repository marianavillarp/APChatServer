package test_client;

import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

class HandleMsgs extends Thread {
	
	Socket mySocket = null;
	JTextArea messageArea;
	public Scanner in;
	
	public HandleMsgs(Socket mySocket, JTextArea messageArea) {
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

class Client {
	
	private PrintWriter out;
	private Socket socket;
	public String name;
	public int port;
	
	public Client(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	public void contactServer(JTextArea messageArea) {
		try {
            socket = new Socket("127.0.0.1", port);
            OutputStream os = socket.getOutputStream();
            out = new PrintWriter(os, true) ;
            out.println(name);
            
            HandleMsgs hmsg = new HandleMsgs(socket,messageArea);
            hmsg.start();
       }
        catch (IOException ioe) {
            messageArea.append("Server unavailable");
        }
	}
	
	public void sendMessage(JTextArea messageChat) {
		int inputLines = messageChat.getLineCount();
        try {
          for (int i = 0 ; i < inputLines; i++) {
                int start = messageChat.getLineStartOffset(i);
                int end = messageChat.getLineEndOffset(i);
                String str = messageChat.getText(start, end - start);
                out.println("[" + this.name + "] " + str);
          }
        }
        catch (BadLocationException ble) {
          ble.printStackTrace();
        }  
		
	}
	
	public String getUsername() {
		return name;
	}
	
}
