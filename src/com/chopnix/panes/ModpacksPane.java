package com.chopnix.panes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.chopnix.data.ModPack;
import com.chopnix.data.events.ModPackListener;
import com.chopnix.dialogs.FilterDialog;

import net.ftb.gui.LaunchFrame;
import net.ftb.locale.I18N;
import net.ftb.locale.I18N.Locale;

public class ModpacksPane extends JPanel implements ILauncherPane,
		ModPackListener {
	private static final long serialVersionUID = 1L;

	private static JPanel packs;
	public static ArrayList<JPanel> packPanels;
	private static JScrollPane packsScroll;
	private static JLabel splash;

	private static JLabel typeLbl;
	private JButton filter;
	private static int selectedPack = 0;
	private static boolean modPacksAdded = false;
	private static HashMap<Integer, ModPack> currentPacks = new HashMap<Integer, ModPack>();
	private final ModpacksPane instance = this;
	private static JTextArea packInfo;

	// private JLabel loadingImage;
	public static String type = "Client", origin = "all";
	public static boolean loaded = false;

	public ModpacksPane() {
		super();
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		this.setOpaque(false);

		splash = new JLabel();
		splash.setBounds(432, 17, 389, 183);
		splash.setOpaque(false);
		this.add(splash);

		packPanels = new ArrayList<JPanel>();

		packs = new JPanel();
		packs.setLayout(null);
		packs.setOpaque(true);

		// stub for a real wait message
		final JPanel p = new JPanel();
		p.setBounds(0, 0, 420, 55);
		p.setLayout(null);

		filter = new JButton(I18N.getLocaleString("FILTER_SETTINGS"));
		filter.setBounds(5, 5, 105, 25);
		filter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loaded) {
					FilterDialog filterDia = new FilterDialog(instance);
					filterDia.setVisible(false);
				}
			}
		});
		// add(filter);

		typeLbl = new JLabel("<html><body><strong>Filter:</strong> " + type
				+ " / " + origin + "</body></html>");
		typeLbl.setBounds(115, 5, 175, 25);
		typeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		// add(typeLbl);

		JTextArea filler = new JTextArea(
				I18N.getLocaleString("MODS_WAIT_WHILE_LOADING"));
		filler.setBorder(null);
		filler.setEditable(true);
		filler.setForeground(Color.white);
		filler.setBounds(58, 6, 378, 42);
		// filler.setBackground(new Color(77, 77, 77, 0));
		// p.add(loadingImage);
		p.add(filler);
		packs.add(p);

		packsScroll = new JScrollPane();
		packsScroll.setBounds(10, 5, 420, 305);
		packsScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		packsScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		packsScroll.setWheelScrollingEnabled(true);
		packsScroll.setOpaque(true);
		packsScroll.setViewportView(packs);
		add(packsScroll);

		packInfo = new JTextArea();
		packInfo.setEditable(false);
		packInfo.setWrapStyleWord(true);
		packInfo.setLineWrap(true);
		packInfo.setBounds(420, 210, 410, 90);
		// packInfo.setBackground(UIManager.getColor("control").darker().darker());
		add(packInfo);

		JScrollPane infoScroll = new JScrollPane();
		infoScroll.setBounds(432, 210, 392, 90);
		infoScroll.setBackground(new Color(0, 0, 0, 64));
		infoScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		infoScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		infoScroll.setWheelScrollingEnabled(true);
		infoScroll.setViewportView(packInfo);
		infoScroll.setOpaque(true);
		add(infoScroll);
	}

	@Override
	public void onVisible() {
	}

	/*
	 * GUI Code to add a modpack to the selection
	 */
	public static void addPack(ModPack pack) {
		if (!modPacksAdded) {
			modPacksAdded = true;
			packs.removeAll();
		}

		final int packIndex = packPanels.size();
		System.out.println("Adding pack " + getModNum());
		final JPanel p = new JPanel();
		p.setBounds(0, (packIndex * 55), 420, 55);
		p.setLayout(null);
		JLabel logo = new JLabel(new ImageIcon(pack.getLogo()));
		logo.setBounds(6, 6, 42, 42);
		logo.setVisible(true);
		String info = "";
		if (pack.getInfo().length() > 60) {
			info = pack.getInfo().substring(0, 59) + "...";
		} else {
			info = pack.getInfo();
		}
		JTextArea filler = new JTextArea(pack.getName() + " : "
				+ pack.getAuthor() + "\n" + info);
		filler.setBorder(null);
		filler.setEditable(false);
		filler.setOpaque(true);
		filler.setForeground(Color.white);
		filler.setBounds(58, 6, 378, 42);
		filler.setBackground(new Color(255, 255, 255, 0));
		MouseListener lin = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedPack = packIndex;
				updatePacks();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
		};
		p.addMouseListener(lin);
		filler.addMouseListener(lin);
		logo.addMouseListener(lin);
		p.add(filler);
		p.add(logo);
		packPanels.add(p);
		packs.add(p);
		if (origin.equalsIgnoreCase("all")) {
			packs.setMinimumSize(new Dimension(420, (ModPack.getPackArray()
					.size()) * 55));
			packs.setPreferredSize(new Dimension(420, (ModPack.getPackArray()
					.size()) * 55));
		} else {
			packs.setMinimumSize(new Dimension(420, (currentPacks.size()) * 55));
			packs.setPreferredSize(new Dimension(420,
					(currentPacks.size()) * 55));
		}
		packsScroll.revalidate();
	}

	@Override
	public void onModPackAdded(ModPack pack) {
		if (isPackVisible(pack)) {
			addPack(pack);
			updatePacks();
		}
	}

	private static void sortPacks() {
		packPanels.clear();
		packs.removeAll();
		currentPacks.clear();
		packs.setMinimumSize(new Dimension(420, 0));
		packs.setPreferredSize(new Dimension(420, 0));
		packs.setLayout(null);
		packs.setOpaque(true);
		int counter = 0;
		selectedPack = 0;

		for (ModPack pack : ModPack.getPackArray()) {
			if (isPackVisible(pack)) {
				addPack(pack);
				currentPacks.put(counter, pack);
				counter++;
			}
		}

		updatePacks();
	}

	public static void searchPacks(String search) {
		System.out.println("Searching Packs for : " + search);
		packPanels.clear();
		packs.removeAll();
		currentPacks.clear();
		packs.setMinimumSize(new Dimension(420, 0));
		packs.setPreferredSize(new java.awt.Dimension(387, 286));
		packs.setLayout(null);
		packs.setOpaque(true);
		int counter = 0;
		selectedPack = 0;
		for (ModPack pack : ModPack.getPackArray()) {
			if (pack.getName().equalsIgnoreCase(search)
					|| pack.getAuthor().equalsIgnoreCase(search)) {
				addPack(pack);
				currentPacks.put(counter, pack);
				counter++;
			}
		}
		updatePacks();
	}

	private static void updatePacks() {
		for (int i = 0; i < packPanels.size(); i++) {
			if (selectedPack == i) {
				packPanels.get(i).setBackground(
						UIManager.getColor("control").darker().darker());
				splash.setIcon(new ImageIcon(ModPack.getPack(getIndex())
						.getImage()));
				packPanels.get(i).setCursor(
						Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				packInfo.setText(ModPack.getPack(getIndex()).getInfo());
			} else {
				packPanels.get(i).setBackground(UIManager.getColor("control"));
				packPanels.get(i).setCursor(
						Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
		packsScroll.revalidate();
	}

	public int getSelectedModIndex() {
		return modPacksAdded ? getIndex() : -1;
	}

	public static void updateFilter() {
		typeLbl.setText("<html><body><strong>Filter:</strong> " + type + " / "
				+ origin + "</body></html>");
		sortPacks();
		LaunchFrame.getInstance().updateFooter();
	}

	private static int getIndex() {
		if (currentPacks.size() > 0) {
			if (currentPacks.size() != ModPack.getPackArray().size()) {
				if (!origin.equalsIgnoreCase("all")) {
					return currentPacks.get(selectedPack).getIndex();
				}
			}
		}
		return selectedPack;
	}

	private static int getModNum() {
		if (currentPacks.size() > 0) {
			if (!origin.equalsIgnoreCase("all")) {
				return currentPacks.get((packPanels.size() - 1)).getIndex();
			}
		}
		return packPanels.size();
	}

	public void updateLocale() {
		filter.setText(I18N.getLocaleString("FILTER_SETTINGS"));
		if (I18N.currentLocale == Locale.deDE) {
			typeLbl.setBounds(115, 5, 165, 25);
		} else {
			typeLbl.setBounds(115, 5, 175, 25);
		}
	}

	private static boolean isPackVisible(ModPack pack) {

		if (origin.equalsIgnoreCase("all")) {
			return true;
		} else if (origin.equalsIgnoreCase("chopnix")
				&& pack.getAuthor().equalsIgnoreCase("chopnix")) {
			return true;
		} else if (origin.equalsIgnoreCase("ftb")
				&& pack.getAuthor().equalsIgnoreCase("the ftb team")) {
			return true;
		} else if (origin.equalsIgnoreCase("3rd Party")
				&& !pack.getAuthor().equalsIgnoreCase("chopnix")) {
			return true;
		}

		return false;
	}

	@Override
	public void onVisible(String[] args) {
		// TODO Auto-generated method stub

	}
}