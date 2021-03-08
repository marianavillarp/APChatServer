package test_client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Label;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import javax.swing.DropMode;
import java.awt.Panel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class ClientGUI extends JFrame {

	Scanner in;
    PrintWriter out;
    
	JPanel contentPane;
	private JTextField textField;
	private JTextField portText;
	private JTextField IpText;
	private JTextArea inputMsg;
	JList onlineClients;
	JTextArea messageArea;
	Client c;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientGUI frame = new ClientGUI(c);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	public class inputKeys implements KeyListener{
		JTextArea inputMsg;
		
		public inputKeys(JTextArea inputMsg) {
			this.inputMsg = inputMsg;
		}
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				e.consume();
				sendMsg();
			}
			if ((e.isControlDown()) && e.getKeyCode() == KeyEvent.VK_C) {
				System.exit(0);
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	/**
	 * Create the frame.
	 */
	public ClientGUI(Client c) {
		
		this.c = c;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 575);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		JTextField userText = new JTextField();
		userText.setEnabled(false);
		userText.setEditable(false);
		userText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		userText.setText(c.getUsername());
		userText.setBounds(142, 75, 101, 22);
		contentPane.add(userText);
		
		portText = new JTextField();
		portText.setEnabled(false);
		portText.setEditable(false);
		portText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portText.setText("7000");
		portText.setBounds(302, 74, 129, 25);
		contentPane.add(portText);
		portText.setColumns(10);
		
		IpText = new JTextField();
		IpText.setEnabled(false);
		IpText.setFont(new Font("Tahoma", Font.PLAIN, 15));
		IpText.setEditable(false);
		IpText.setText("127.0.0.1");
		IpText.setBounds(302, 26, 129, 22);
		contentPane.add(IpText);
		IpText.setColumns(10);
		
		onlineClients = new JList();
		onlineClients.setBounds(474, 158, 138, 280);
		contentPane.add(onlineClients);
		
		JLabel lblNewLabel = new JLabel("Port");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel.setBounds(253, 73, 39, 36);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("User ID");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel_1.setBounds(53, 67, 83, 36);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("IP");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel_2.setBounds(260, 18, 30, 36);
		contentPane.add(lblNewLabel_2);
		
		inputMsg = new JTextArea();
		inputMsg.setBounds(53, 476, 378, 22);
		contentPane.add(inputMsg);
		inputMsg.addKeyListener(new inputKeys(inputMsg));
		inputMsg.setColumns(10);
		
		JButton btnSendMsg = new JButton("Send");
		btnSendMsg.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSendMsg.setBounds(455, 473, 125, 25);
		btnSendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		contentPane.add(btnSendMsg);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 119, 378, 347);
		contentPane.add(scrollPane);
		
		messageArea = new JTextArea();
		messageArea.setForeground(Color.WHITE);
		scrollPane.setViewportView(messageArea);
		messageArea.setBackground(Color.DARK_GRAY);
		messageArea.setEditable(false);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnDisconnect.setBounds(458, 26, 156, 36);
		contentPane.add(btnDisconnect);
		
		JTextArea txtrOnlineUsers = new JTextArea();
		txtrOnlineUsers.setFont(new Font("Monospaced", Font.BOLD, 20));
		txtrOnlineUsers.setText("online users");
		txtrOnlineUsers.setForeground(Color.WHITE);
		txtrOnlineUsers.setBackground(Color.BLACK);
		txtrOnlineUsers.setBounds(464, 119, 162, 41);
		contentPane.add(txtrOnlineUsers);
		
	}
	
	
	public void sendMsg() {
		c.sendMessage(inputMsg);
	}
	
	public void endConnection() {
		
	}
}
