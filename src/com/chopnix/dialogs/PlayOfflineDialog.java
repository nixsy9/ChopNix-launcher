package com.chopnix.dialogs;

import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import net.ftb.gui.LaunchFrame;
import net.ftb.locale.I18N;

import com.chopnix.data.ModPack;
import com.chopnix.data.Settings;

@SuppressWarnings("serial")
public class PlayOfflineDialog extends JDialog {
	private JTextArea text;
	private JButton play;
	private JButton abort;

	public PlayOfflineDialog(String cause, final String username) {
		super(LaunchFrame.getInstance(), true);
		setupGui();

		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LaunchFrame.getInstance().launchMinecraft(Settings.getSettings().getInstallPath() + File.separator + ModPack.getSelectedPack().getDir()
						+ File.separator + "minecraft", username, "offlinemods");
			}
		});

		abort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}

	public void setupGui() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/image/logo_ftb.png")));
		setTitle("Could not log in");
		setResizable(false);

		Container panel = getContentPane();
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);

		text = new JTextArea(I18N.getLocaleString("PLAYOFFLINE_WANNA"));
		text.setEditable(false);
		text.setHighlighter(null);
		text.setBorder(BorderFactory.createEmptyBorder());
		play = new JButton(I18N.getLocaleString("MAIN_YES"));
		abort = new JButton(I18N.getLocaleString("MAIN_NO"));

		panel.add(text);
		panel.add(abort);
		panel.add(play);

		Spring hSpring;
		Spring columnWidth;

		hSpring = Spring.constant(10);

		layout.putConstraint(SpringLayout.WEST, text, hSpring, SpringLayout.WEST, panel);

		columnWidth = Spring.width(play);
		columnWidth = Spring.sum(columnWidth, Spring.constant(10));
		columnWidth = Spring.sum(columnWidth, Spring.width(abort));
		columnWidth = Spring.max(columnWidth, Spring.width(text));

		hSpring = Spring.sum(hSpring, columnWidth);
		hSpring = Spring.sum(hSpring, Spring.constant(10));

		layout.putConstraint(SpringLayout.EAST, panel, hSpring, SpringLayout.WEST, panel);

		layout.putConstraint(SpringLayout.EAST, play, -5, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.WEST, abort, 5, SpringLayout.HORIZONTAL_CENTER, panel);

		Spring vSpring;
		Spring rowHeight;

		vSpring = Spring.constant(10);

		layout.putConstraint(SpringLayout.NORTH, text, vSpring, SpringLayout.NORTH, panel);

		vSpring = Spring.sum(vSpring, Spring.height(text));
		vSpring = Spring.sum(vSpring, Spring.constant(10));

		layout.putConstraint(SpringLayout.NORTH, play,  vSpring, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.NORTH, abort, vSpring, SpringLayout.NORTH, panel);

		rowHeight = Spring.height(play);
		rowHeight = Spring.max(rowHeight, Spring.height(abort));

		vSpring = Spring.sum(vSpring, rowHeight);
		vSpring = Spring.sum(vSpring, Spring.constant(10));

		layout.putConstraint(SpringLayout.SOUTH, panel, vSpring, SpringLayout.NORTH, panel);

		pack();
		setLocationRelativeTo(getOwner());
	}
}
