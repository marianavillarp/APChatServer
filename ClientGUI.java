package COMP1549_G4;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientGUI extends JFrame {

	Scanner in;
    PrintWriter out;
    
	JPanel contentPane;
	private JTextField portText;
	private JTextField IpText;
	private JTextArea inputMsg;
	JList<OnlineClient> onlineCDisplay;
	JTextArea messageArea;
	JButton btnSendMsg;
	JButton btnDisconnect;
	JButton adminBtn;
	Client c;

	/*
	 * Listener for user key commands:
	 * 	Enter sends message
	 * 	Ctrl+C exits program
	 */
	public class inputKeys implements KeyListener{
		JTextArea inputMsg;
		
		public inputKeys(JTextArea inputMsg) {
			this.inputMsg = inputMsg;
		}
		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				e.consume();
				sendMsg();
			}
			if ((e.isControlDown()) && e.getKeyCode() == KeyEvent.VK_C) {
				endConnection();
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}
		
	}
	/**
	 * Create the client GUI Frame.
	 */
	public ClientGUI(Client c) {
		
		this.c = c;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 659, 575);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTextField userText = new JTextField();
		userText.setDisabledTextColor(Color.BLACK);
		userText.setSelectedTextColor(Color.BLACK);
		userText.setSelectionColor(Color.GRAY);
		userText.setEnabled(false);
		userText.setEditable(false);
		userText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		userText.setText(c.getUsername());
		userText.setBounds(121, 26, 122, 22);
		contentPane.add(userText);
		
		portText = new JTextField();
		portText.setDisabledTextColor(Color.BLACK);
		portText.setSelectedTextColor(Color.BLACK);
		portText.setSelectionColor(Color.GRAY);
		portText.setEnabled(false);
		portText.setEditable(false);
		portText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portText.setText("7000");
		portText.setBounds(121, 71, 129, 25);
		contentPane.add(portText);
		portText.setColumns(10);
		
		IpText = new JTextField();
		IpText.setDisabledTextColor(Color.BLACK);
		IpText.setSelectedTextColor(Color.BLACK);
		IpText.setSelectionColor(Color.GRAY);
		IpText.setEnabled(false);
		IpText.setFont(new Font("Tahoma", Font.PLAIN, 15));
		IpText.setEditable(false);
		IpText.setText("127.0.0.1");
		IpText.setBounds(302, 26, 129, 22);
		contentPane.add(IpText);
		IpText.setColumns(10);
		
		JButton pmBtn = new JButton("Send private message"); // Option to allow user to send private message instead of global
		pmBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pmBtn.setBounds(456, 446, 179, 25);
		pmBtn.setEnabled(false);
		pmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pm) {
				
				Object x = onlineCDisplay.getSelectedValue(); // Select user to private message with
				privateMessage(x);
			}
		});
		contentPane.add(pmBtn);
		
		onlineCDisplay = new JList<OnlineClient>();
		onlineCDisplay.setBounds(456, 120, 179, 280);
		contentPane.add(onlineCDisplay);
		onlineCDisplay.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==1) {
					pmBtn.setEnabled(true);
					adminBtn.setEnabled(true);
				}
			}
		});
		
		adminBtn = new JButton("Remove user"); // Remove user button only visible to current coordinator
		adminBtn.setVisible(false);
		adminBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		adminBtn.setBounds(456, 410, 179, 22);
		adminBtn.setEnabled(false);
		adminBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (e.getActionCommand().equals("Remove user")) {; // Confirmation box to remove user from the multi-chat program
				
					int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this user?", "Remove user confirmation",JOptionPane.YES_NO_OPTION);
					
					if (confirmation == JOptionPane.YES_OPTION) {
						
						Object x = onlineCDisplay.getSelectedValue();
						removeUser(x); // Remove selected user from the program
					}
				}
				
			}
		});
		contentPane.add(adminBtn);
		
		JLabel portLabel = new JLabel("Port");
		portLabel.setForeground(Color.WHITE);
		portLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		portLabel.setBounds(53, 64, 39, 36);
		contentPane.add(portLabel);
		
		JLabel idLabel = new JLabel("User ID");
		idLabel.setForeground(Color.WHITE);
		idLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		idLabel.setBounds(53, 18, 83, 36);
		contentPane.add(idLabel);
		
		JLabel ipLabel = new JLabel("IP");
		ipLabel.setForeground(Color.WHITE);
		ipLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		ipLabel.setBounds(274, 18, 30, 36);
		contentPane.add(ipLabel);
		
		inputMsg = new JTextArea();
		inputMsg.setBounds(53, 481, 378, 22);
		contentPane.add(inputMsg);
		inputMsg.addKeyListener(new inputKeys(inputMsg));
		inputMsg.setColumns(10);
		
		btnSendMsg = new JButton("Broadcast");
		btnSendMsg.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSendMsg.setBounds(456, 481, 179, 22);
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
		messageArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		messageArea.setForeground(Color.WHITE);
		scrollPane.setViewportView(messageArea);
		messageArea.setBackground(Color.DARK_GRAY);
		messageArea.setEditable(false);
		
		btnDisconnect = new JButton("Disconnect\r\n");
		btnDisconnect.setForeground(Color.RED);
		btnDisconnect.setBackground(Color.WHITE);
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endConnection();;
			}
		});
		btnDisconnect.setBounds(456, 36, 179, 22);
		contentPane.add(btnDisconnect);
		
		JTextArea txtrOnlineUsers = new JTextArea();
		txtrOnlineUsers.setFont(new Font("Monospaced", Font.BOLD, 18));
		txtrOnlineUsers.setText("Online clients");
		txtrOnlineUsers.setForeground(Color.WHITE);
		txtrOnlineUsers.setBackground(Color.BLACK);
		txtrOnlineUsers.setBounds(466, 85, 156, 28);
		contentPane.add(txtrOnlineUsers);
		
		
		
	}
	
	
	public void sendMsg() {
		c.sendMessage(inputMsg,false,null);
	}
	
	public void endConnection() {
		c.endConnection();
	}
	
	public void removeUser(Object sUser) {
		c.removeUser(sUser);
	}
	
	public void privateMessage(Object sUser) {
		c.sendMessage(inputMsg, true, sUser);
	}
}
