package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.buddat.wplanner.WPlanner;

public class ResizeDialog extends JDialog {

	private static final long serialVersionUID = -8469409415651556665L;
	
	private final JPanel contentPanel = new JPanel();
	private JPanel previewPanel;
	
	private int northAdj = 0, eastAdj = 0, southAdj = 0, westAdj = 0;
	private boolean accepted = false;
	
	public ResizeDialog(String mapName, final int initW, final int initH) {
		setTitle("Resize " + mapName);
		
		setBounds(0, 0, 438, 314);
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
				JLabel lblNorth = new JLabel("North: ");
				panel.add(lblNorth);
			}
			{
				JSpinner northSpinner = new JSpinner();
				northSpinner.setModel(new SpinnerNumberModel(0, -(initH / 2), 4096, 1));
				northSpinner.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						northAdj = (Integer) ((JSpinner) e.getSource()).getValue();
						previewPanel.repaint();
					}
				});
				panel.add(northSpinner);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			{
				JLabel lblSouth = new JLabel("South: ");
				panel.add(lblSouth);
			}
			{
				JSpinner spinner = new JSpinner();
				spinner.setModel(new SpinnerNumberModel(0, -(initH / 2), 4096, 1));
				spinner.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						southAdj = (Integer) ((JSpinner) e.getSource()).getValue();
						previewPanel.repaint();
					}
				});
				panel.add(spinner);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.WEST);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
				flowLayout.setVgap(70);
				panel.add(panel_1, BorderLayout.EAST);
				{
					JLabel lblWest = new JLabel("West: ");
					panel_1.add(lblWest);
				}
				{
					JSpinner spinner = new JSpinner();
					spinner.setModel(new SpinnerNumberModel(0, -(initW / 2), 4096, 1));
					spinner.addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent e) {
							westAdj = (Integer) ((JSpinner) e.getSource()).getValue();
							previewPanel.repaint();
						}
					});
					panel_1.add(spinner);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.EAST);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
				flowLayout.setVgap(70);
				panel.add(panel_1, BorderLayout.WEST);
				{
					JLabel lblEast = new JLabel("East: ");
					panel_1.add(lblEast);
				}
				{
					JSpinner spinner = new JSpinner();
					spinner.setModel(new SpinnerNumberModel(0, -(initW / 2), 4096, 1));
					spinner.addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent e) {
							eastAdj = (Integer) ((JSpinner) e.getSource()).getValue();
							previewPanel.repaint();
						}
					});
					panel_1.add(spinner);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(160, 160));
			contentPanel.add(panel, BorderLayout.CENTER);
			{
				previewPanel = new JPanel() {
					private static final long serialVersionUID = 8044822007565570911L;
					
					@Override
					public void paint(Graphics g) {
						super.paint(g);
						
						int xMax = 160, yMax = 160;
						
						int newW = initW + westAdj + eastAdj;
						int newH = initH + northAdj + southAdj;
						
						float tSizeW = xMax / (newW > initW ? newW : initW);
						float tSizeH = yMax / (newH > initH ? newH : initH);
						float tSize = (tSizeW < tSizeH ? tSizeW : tSizeH);
						
						int newX = (int) ((xMax - (newW * tSize)) / 2);
						int newY = (int) ((yMax - (newH * tSize)) / 2);
						
						int initX = (int) (newX + (westAdj * tSize));
						int initY = (int) (newY + (northAdj * tSize));
						
						if (initX < 0) {
							newX -= initX;
							initX -= initX;
						}
						if (initY < 0) {
							newY -= initY;
							initY -= initY;
						}
						
						if (((int) tSize) <= 0) {
							g.setColor(Color.BLACK);
							g.drawString("Preview Unavailable", 20, 50);
							return;
						}
						
						g.setColor(Color.GREEN);
						g.fillRect(initX, initY, (int) (initW * tSize), (int) (initH * tSize));
						
						g.setColor(new Color(255, 0, 0, 100));
						g.fillRect(newX, newY, (int) (newW * tSize), (int) (newH * tSize));
					}
				};
				previewPanel.setPreferredSize(new Dimension(160, 160));
				panel.add(previewPanel);
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
						accepted = true;
						setVisible(false);
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
						setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		
		setVisible(true);
	}
	
	public int[] getAdjustments() {
		if (accepted)
			return new int[] { northAdj, eastAdj, southAdj, westAdj };
		else
			return null;
	}

}
