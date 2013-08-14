package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.JLabel;

import net.buddat.wplanner.WPlanner;

public class ColorDialog extends JDialog {

	private static final long serialVersionUID = -1391772574411994862L;
	
	private final JPanel contentPanel = new JPanel();
	
	private JPanel previewPanel;
	private JSlider opacitySlider;
	private Color plainColor, finalColor = WPlanner.getMainWindow().getOverlayColor();

	public ColorDialog() {
		setBounds(0, 0, 450, 300);
		setLocationRelativeTo(WPlanner.getMainWindow());
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		plainColor = new Color(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue());
		
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.WEST);
			{
				JLabel lblPreview = new JLabel("Preview:");
				panel.add(lblPreview);
			}
			{
				previewPanel = new JPanel() {
					private static final long serialVersionUID = -69215911360286108L;

					@Override
					public void paint(Graphics g) {
						super.paint(g);
						
						g.setColor(finalColor);
						g.fillRect(0, 0, 150, 150);
					}
				};
				previewPanel.setPreferredSize(new Dimension(150, 150));
				panel.add(previewPanel);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.EAST);
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton btnChooseColour = new JButton("Choose Colour...");
				btnChooseColour.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						plainColor = JColorChooser.showDialog(ColorDialog.this, "Choose Colour", plainColor);
						
						updatePreview();
					}
				});
				panel.add(btnChooseColour);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			{
				JLabel lblOpacity = new JLabel("Opacity: ");
				panel.add(lblOpacity);
			}
			{
				opacitySlider = new JSlider();
				opacitySlider.setMaximum(255);
				opacitySlider.setValue(finalColor.getAlpha());
				opacitySlider.setPaintTicks(true);
				opacitySlider.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent arg0) {
						updatePreview();
					}
				});
				panel.add(opacitySlider);
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
						WPlanner.getMainWindow().setOverlayColor(finalColor);
						
						ColorDialog.this.dispose();
					}					
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						ColorDialog.this.dispose();
					}					
				});
				buttonPane.add(cancelButton);
			}
		}
		
		updatePreview();

		pack();
		setVisible(true);
	}
	
	public void updatePreview() {
		finalColor = new Color(plainColor.getRed(), plainColor.getGreen(), plainColor.getBlue(), opacitySlider.getValue());
		
		previewPanel.repaint();
	}

}
