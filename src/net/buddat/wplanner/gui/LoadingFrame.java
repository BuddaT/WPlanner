package net.buddat.wplanner.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.GridLayout;

public class LoadingFrame extends JFrame {

	private static final long serialVersionUID = -7637593272209527155L;
	
	private JPanel contentPane;
	private JProgressBar progressBar = new JProgressBar();
	private JLabel statusLabel = new JLabel("Loading...");

	public LoadingFrame() {
		setResizable(false);
		setTitle("Loading...please wait.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 200, 50);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		statusLabel.setForeground(Color.WHITE);
		contentPane.add(statusLabel);

		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.GREEN);
		contentPane.add(progressBar);
	}
	
	public void setMessage(String msg) {
		statusLabel.setText("Loading..." + msg);
		progressBar.setToolTipText(msg);
	}

	public void setProgress(int newProgress) {
		progressBar.setValue(newProgress);
	}
	
	public void update(String newMessage, int newProgress) {
		setMessage(newMessage);
		setProgress(newProgress);
	}
}
