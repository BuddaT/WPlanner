package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import net.buddat.wplanner.WPlanner;
import net.buddat.wplanner.util.Constants;
import java.awt.geom.Rectangle2D;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -3465233766949369612L;
	
	private ImageIcon logoImage = null;
	
	AboutDialog() {
		super();
		
		setTitle("About " + Constants.PROGRAM_NAME);
		setSize(350, 220);
		setResizable(false);
		setLocationRelativeTo(WPlanner.getMainWindow());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		logoImage = WPlanner.getMainWindow().getResources().getGuiImage(Constants.GUI_LOGO);
		
		repaint();
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		FontMetrics fm = g.getFontMetrics();
		String[] aboutStrings = { Constants.PROGRAM_NAME + " - v" + Constants.VERSION_MAJOR + "." + Constants.VERSION_MIDI + "."
				+ Constants.VERSION_MINOR, "For use with planning maps for Wurm Online", "", "Created by " + Constants.AUTHOR, Constants.AUTHOR_WEBSITE };
		
		int count = 0;
		
		g.drawImage(logoImage.getImage(), (getWidth() / 2) - (logoImage.getImage().getWidth(null) / 2), 40, null);
		
		for (String s : aboutStrings) {
			Rectangle2D rect = fm.getStringBounds(s, g);
			int textHeight = (int)(rect.getHeight()); 
			int textWidth  = (int)(rect.getWidth());
			
			g.drawString(s, (getWidth() / 2) - (textWidth / 2), 120 + (count++ * textHeight));
		}
	}
	
}
