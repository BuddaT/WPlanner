package net.buddat.wplanner.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

import net.buddat.wplanner.WPlanner;

import java.awt.SystemColor;

public class ClosableTab extends JPanel {

	private static final long serialVersionUID = 1800597890396968272L;

	public ClosableTab(final MainWindow main, final String title) {
		setBorder(new EmptyBorder(2, 0, 0, 0));
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitle = new JLabel(title + "   ");
		add(lblTitle, BorderLayout.WEST);
		
		JButton btnClose = new JButton("X");
		btnClose.setBackground(SystemColor.menu);
		btnClose.setFont(new Font("Vrinda", Font.PLAIN, 15));
		btnClose.setToolTipText("Close Tab\r\n");
		btnClose.setPreferredSize(new Dimension(15, 12));
		btnClose.setMargin(new Insets(0, 0, 0, 0));
		
		btnClose.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
            	WPlanner.getMapManager().removeMap(title);
            }
            
        });
		add(btnClose, BorderLayout.EAST);
	}

}
