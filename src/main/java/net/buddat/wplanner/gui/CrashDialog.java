package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import net.buddat.wplanner.util.Constants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrashDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 8950391398380474231L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextArea txtErrorMsg = new JTextArea();

	public CrashDialog(String error, JComponent parent) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setBackground(Color.RED);
		setBounds(0, 0, 300, 200);
		setLocationRelativeTo(parent);
		setModal(true);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.RED);
		contentPanel.setBorder(new EmptyBorder(2, 5, 2, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		{
			txtErrorMsg.setWrapStyleWord(true);
			txtErrorMsg.setFont(new Font("Monospaced", Font.PLAIN, 11));
			txtErrorMsg.setForeground(Color.BLACK);
			txtErrorMsg.setBackground(Color.WHITE);
			txtErrorMsg.setEditable(false);
			txtErrorMsg.setLineWrap(true);
			//txtErrorMsg.
			txtErrorMsg.setText(error);
			contentPanel.add(txtErrorMsg);
		}
		
		{
			JLabel lblError = new JLabel(Constants.PROGRAM_NAME + " has stopped working.");
			lblError.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblError.setForeground(Color.WHITE);
			contentPanel.add(lblError, BorderLayout.NORTH);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.RED);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("Okay");
				okButton.setBackground(Color.WHITE);
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setVisible(true);
	}
	
	public void setErrorMessage(String errorMessage) {
		txtErrorMsg.setText(errorMessage);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("OK")) {
			System.exit(1);
		}
	}

}
