package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.buddat.wplanner.WPlanner;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LabelDialog extends JDialog {

	private static final long serialVersionUID = -4620553759342852518L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtLabelinput;
	private JPanel previewPanel;

	public LabelDialog() {
		setBounds(0, 0, 300, 200);
		setLocationRelativeTo(WPlanner.getMainWindow());
		setModal(true);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblLabel = new JLabel("Label: ");
				panel.add(lblLabel);
			}
			{
				txtLabelinput = new JTextField();
				panel.add(txtLabelinput);
				txtLabelinput.setColumns(25);
			}
		}
		{
			JPanel panel_1 = new JPanel();
			contentPanel.add(panel_1, BorderLayout.CENTER);
			{
				JLabel lblCurrentColour = new JLabel("Current Colour:");
				panel_1.add(lblCurrentColour);
			}
			{
				previewPanel = new JPanel() {
					private static final long serialVersionUID = -8315693362583551064L;
					
					@Override
					public void paint(Graphics g) {
						super.paint(g);
						
						g.setColor(WPlanner.getMainWindow().getLabelColor());
						g.fillRect(0, 0, 50, 50);
					}
				};
				previewPanel.setPreferredSize(new Dimension(50, 50));
				panel_1.add(previewPanel);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			{
				JButton btnChooseTextColour = new JButton("Choose Text Colour...");
				btnChooseTextColour.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						WPlanner.getMainWindow().setLabelColor(JColorChooser.showDialog(LabelDialog.this, "Choose Colour", WPlanner.getMainWindow().getLabelColor()));
						
						updatePreview();
					}
				});
				panel.add(btnChooseTextColour);
			}
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
						LabelDialog.this.setVisible(false);
					}					
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		updatePreview();
		
		pack();
		setVisible(true);
	}
	
	public void updatePreview() {
		previewPanel.repaint();
	}
	
	public String getLabel() {
		return txtLabelinput.getText();
	}

}
