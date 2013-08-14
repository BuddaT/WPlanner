package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.buddat.wplanner.WPlanner;
import net.buddat.wplanner.map.Map;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewDialog extends JDialog {

	private static final long serialVersionUID = -6528100707740327721L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	private JSpinner widthSpinner, heightSpinner;
	
	public NewDialog() {
		setTitle("New...");
		setBounds(0, 0, 351, 170);
		setLocationRelativeTo(WPlanner.getMainWindow());
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblMapName = new JLabel("Map Name: ");
				panel.add(lblMapName);
			}
			{
				textField = new JTextField();
				panel.add(textField);
				textField.setColumns(20);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.NORTH);
				panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				{
					JLabel lblWidth = new JLabel("Width: ");
					panel_1.add(lblWidth);
				}
				{
					widthSpinner = new JSpinner();
					widthSpinner.setModel(new SpinnerNumberModel(15, 1, 4096, 1));
					panel_1.add(widthSpinner);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.CENTER);
				{
					JLabel lblHeight = new JLabel("Height: ");
					panel_1.add(lblHeight);
				}
				{
					heightSpinner = new JSpinner();
					heightSpinner.setModel(new SpinnerNumberModel(15, 1, 4096, 1));
					panel_1.add(heightSpinner);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (textField.getText().equals(""))
							textField.setText("NewMap" + (int) (Math.random() * 50000));
						Map m = new Map(textField.getText(), (Integer) widthSpinner.getValue(), (Integer) heightSpinner.getValue());
						m.setMapName(WPlanner.getMapManager().addMap(m.getMapName(), m));
						
						WPlanner.getMainWindow().addMap(m);
						
						NewDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						NewDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setVisible(true);
	}

}
