package COMP1549_G4;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ServerGUI extends JFrame {

	Scanner in;
    PrintWriter out;
	
	private JPanel contentPane;
	public JTextArea serverScreen;
	public Server server;
	private boolean connected = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
					frame.setTitle("Server");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 599, 378);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(54, 56, 457, 260);
		contentPane.add(scrollPane);
		
		serverScreen = new JTextArea();
		serverScreen.setFont(new Font("Monospaced", Font.PLAIN, 12));
		serverScreen.setForeground(Color.WHITE);
		serverScreen.setBackground(Color.DARK_GRAY);
		scrollPane.setViewportView(serverScreen);
		serverScreen.setEditable(false);
		
		
		JButton btnConnect = new JButton("Start");
		btnConnect.setBounds(54, 10, 142, 25);
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!connected) {
					try {
						server = new Server(serverScreen);
						server.startServer();
						btnConnect.setText("Disconnect");
						connected = true;
					} catch (IOException ioe) {
					}
				} else {
					try {
						server.closeServer();
						btnConnect.setText("Start");
						connected = false;
					} catch (IOException e1) {
						System.out.println("Could not close the server");
					}
				}
			}
				
			
		});
		contentPane.setLayout(null);
		contentPane.add(btnConnect);
		
		JButton getDetailsBtn = new JButton("Server details");
		getDetailsBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getDetailsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(null,"Current Admin: " + server.getCoordinator() + "\n Server Port: " + server.getPort());
				}
				catch (NullPointerException ne) {
					JOptionPane.showMessageDialog(null, "Server currently disconnected");
				}
			}
		});
		getDetailsBtn.setBounds(375, 10, 136, 25);
		contentPane.add(getDetailsBtn);
	}
}

