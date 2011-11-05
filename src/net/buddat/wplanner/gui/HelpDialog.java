package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import net.buddat.wplanner.WPlanner;
import net.buddat.wplanner.util.Constants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelpDialog extends JDialog {

	private static final long serialVersionUID = 7966467669864633240L;
	
	private final JPanel contentPanel = new JPanel();

	public HelpDialog() {
		setTitle(Constants.PROGRAM_NAME + " Help");
		setBounds(0, 0, 460, 550);
		setLocationRelativeTo(WPlanner.getMainWindow());
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblWplannerHelpFile = new JLabel(Constants.PROGRAM_NAME + " Help File");
				panel.add(lblWplannerHelpFile);
			}
		}
		{
			JTextArea txtrHelpfile = new JTextArea();
			txtrHelpfile.setFont(new Font("Lucida Console", Font.PLAIN, 13));
			txtrHelpfile.setEditable(false);
			
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResource(Constants.GUI_HELP).openStream()));
				String total = "";
				String line;
				while ((line = br.readLine()) != null) {
					total += line + "\r\n";
				}
				
				txtrHelpfile.setText(total);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			txtrHelpfile.setCaretPosition(0);
			
			JScrollPane scrollHelp = new JScrollPane(txtrHelpfile, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			contentPanel.add(scrollHelp, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						HelpDialog.this.dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setVisible(true);
	}

}
