package net.buddat.wplanner.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ItemSelectionPanel extends JPanel {

	private static final long serialVersionUID = 2089576695712738873L;
	
	private JRadioButton selectionButton;
	
	public ItemSelectionPanel(MainWindow mainWindow, String name, ImageIcon img) {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		selectionButton = new JRadioButton(img);
		selectionButton.setToolTipText(name);
		selectionButton.setActionCommand(name);
		selectionButton.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
		
		String lblText = name;
		if (lblText.contains("."))
			lblText = lblText.substring(0, lblText.lastIndexOf("."));
		lblText = lblText.length() > 7 ? lblText.substring(0, 7) : lblText;
		JLabel lbl = new JLabel(lblText);
		
		add(selectionButton);
		add(lbl);
	}
	
	public JRadioButton getSelectionButton() {
		return selectionButton;
	}

}
