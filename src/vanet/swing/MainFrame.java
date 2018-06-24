/*
Copyright (c) 2010 Che-Hung Lin

This file is part of VANET Mobility Generation Tool.

VANET Mobility Generation Tool is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

VANET Mobility Generation Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
VANET Mobility Generation Tool. If not, see <https://www.gnu.org/licenses/>.
*/
package vanet.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.input.CenterMapListener;
import org.jdesktop.swingx.input.PanKeyListener;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCursor;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.LocalResponseCache;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdom2.JDOMException;

import vanet.swing.dialog.AbstractDialog;
import vanet.swing.dialog.CbrDialog;
import vanet.swing.dialog.FlowDialog;
import vanet.swing.dialog.LocServerDialog;
import vanet.swing.dialog.PreferenceDialog;
import vanet.swing.dialog.RSUDialog;
import vanet.swing.dialog.ReportDialog;
import vanet.swing.dialog.ResizeDialog;
import vanet.swing.dialog.ScenarioDialog;
import vanet.swing.listeners.ConfigureListener;
import vanet.swing.listeners.SelectionPropertyListener;
import vanet.swing.table.TableValueModel;
import vanet.task.AutonomousTask;
import vanet.task.NetworkReIndexTask;
import vanet.utils.Config;
import vanet.utils.Network;
import vanet.utils.Vehicle;
import vanet.utils.TravelingNodes;
import vanet.utils.io.FileSelectionTool;
import vanet.utils.io.reader.NetworkReader;
import vanet.utils.io.reader.ScenarioReader;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import java.awt.FlowLayout;

public class MainFrame extends JFrame implements AdjustmentListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel topLayer = null;
	private JPanel openStreetMapPanel;
	private JTabbedPane mainTab = null;
	private JTextField JTScenario = null;
	private JMenuBar menuBar = null;
	private JProgressBar progress = null;
	private JScrollBar timeSlider = null;
	private JSlider offsetSlider = null;

	private JDialog progressDialog = null;

	private JXMapViewer mapviewer = null;
	private NetworkPanel networkShowPanel = null;
	private MapPainter mappainter = null;

	private JScrollPane JPCenterInfo;
	private JTextArea JTInfo;

	private ScenarioReader scenario_reader = null;
	private NetworkReader network_reader = null;

	private String networkPath = null;

	public MainFrame() {
		super();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Config c = new Config();
				c.setSliderValue(offsetSlider.getValue());
			}
		});
		setBackground(Color.WHITE);
		initialize();

		progress = new JProgressBar();
		progressDialog = new JDialog(this, Dialog.DEFAULT_MODALITY_TYPE);
		progressDialog.setSize(250, 40);
		progressDialog.setUndecorated(true);
		progressDialog.setEnabled(false);
		progressDialog.getContentPane().add(progress);
	}

	private void initialize() {
		this.setJMenuBar(getMenu());
		this.setContentPane(getTopLayer());
		this.setTitle("Vehiclar Ad-hoc Network Simulation Tool");
		this.pack();
	}

	private JMenuBar getMenu() {
		if (menuBar == null) {
			menuBar = new JMenuBar();

			JMenu mnNewMenu = new JMenu("File");

			JMenuItem mntmExit = new JMenuItem("Exit");
			mntmExit.addActionListener(this);
			mntmExit.setActionCommand("Exit");

			JMenuItem mntmOpen = new JMenuItem("Open");
			mntmOpen.setActionCommand("Open");
			mntmOpen.addActionListener(this);

			JMenuItem mntmSet = new JMenuItem("Prefereces");
			mntmSet.setActionCommand("Prefereces");
			mntmSet.addActionListener(this);

			mnNewMenu.add(mntmOpen);
			mnNewMenu.add(mntmSet);
			mnNewMenu.add(mntmExit);

			menuBar.add(mnNewMenu);
		}
		return menuBar;
	}

	private JPanel getTopLayer() {
		if (topLayer == null) {
			topLayer = new JPanel();
			topLayer.setOpaque(false);
			topLayer.setLayout(new BorderLayout());
			topLayer.add(getMainTab(), BorderLayout.CENTER);
		}
		return topLayer;
	}

	private NetworkPanel getNetworkPanel() {
		if (networkShowPanel == null) {
			networkShowPanel = new NetworkPanel();
			networkShowPanel.setPreferredSize(new Dimension(923, 560));
		}
		return networkShowPanel;
	}

	private JScrollPane getJPCenterInfo() {
		if (JPCenterInfo == null) {
			JPCenterInfo = new JScrollPane();
			JTInfo = new JTextArea();
			JTInfo.setColumns(40);
			JPCenterInfo.setViewportView(JTInfo);
		}
		return JPCenterInfo;
	}

	private TableValueModel getAutonomousTableModel() {
		Config c = new Config();
		TableValueModel model = new TableValueModel(
				new Object[][] { { "Number Of Vehicle", new Integer(c.getNumberOfAutonomous()) },
						{ "Min Speed", new Integer(c.getMinSpeed()) }, { "Max Speed", new Integer(c.getMaxSpeed()) },
						{ "Start Time", new Integer(c.getSimulationTimeBegin()) },
						{ "End Time", new Integer(c.getSimulationTimeEnd()) }, },
				new String[] { "", "" });
		model.addTableModelListener(new ConfigureListener(model));
		return model;

	}

	private JTabbedPane getMainTab() {
		if (mainTab == null) {
			mainTab = new JTabbedPane(JTabbedPane.TOP);
			JPanel mapNetworkPanel = new JPanel();
			mainTab.addTab("OpenStreetMap", null, getOpenStreetMapPanel(), null);
			mainTab.addTab("Map Network Viewer", null, mapNetworkPanel, null);

			JTabbedPane functionTab = new JTabbedPane(JTabbedPane.TOP);

			JPanel autonomousPanel = new JPanel();
			functionTab.addTab("Autonomous Driving", null, autonomousPanel, null);
			autonomousPanel.setLayout(new BorderLayout(0, 0));

			JTable autonomousTable = new JTable(5, 2);
			autonomousTable.setGridColor(Color.LIGHT_GRAY);
			autonomousTable.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
			autonomousTable.setModel(getAutonomousTableModel());
			autonomousPanel.add(autonomousTable, BorderLayout.NORTH);

			JPanel autonomousSubPanel = new JPanel();
			autonomousPanel.add(autonomousSubPanel, BorderLayout.CENTER);
			autonomousSubPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

			JButton btnAutonomous = new JButton("Autonomous");
			autonomousSubPanel.add(btnAutonomous);
			btnAutonomous.setActionCommand("Autonomous");
			btnAutonomous.setOpaque(false);
			btnAutonomous.addActionListener(this);

			JPanel JP_NS2 = new JPanel();
			functionTab.addTab("NS2 Scenario", null, JP_NS2, null);
			JP_NS2.setLayout(null);

			JButton JB_RFLOW = new JButton("Flow");
			JB_RFLOW.setBounds(10, 48, 100, 22);
			JP_NS2.add(JB_RFLOW);
			JB_RFLOW.setActionCommand("Flow");
			JB_RFLOW.setOpaque(false);
			JB_RFLOW.addActionListener(this);
			JB_RFLOW.setPreferredSize(new Dimension(100, 22));

			JButton JB_SUMONS2 = new JButton("Scenario");
			JB_SUMONS2.setBounds(10, 80, 100, 22);
			JP_NS2.add(JB_SUMONS2);
			JB_SUMONS2.setActionCommand("Scenario");
			JB_SUMONS2.setOpaque(false);
			JB_SUMONS2.addActionListener(this);
			JB_SUMONS2.setPreferredSize(new Dimension(100, 22));

			JButton JB_CBR = new JButton("CBR");
			JB_CBR.setBounds(10, 112, 100, 22);
			JP_NS2.add(JB_CBR);
			JB_CBR.setActionCommand("CBR");
			JB_CBR.setOpaque(false);
			JB_CBR.addActionListener(this);
			JB_CBR.setPreferredSize(new Dimension(100, 22));

			JButton JB_LOCSERVER = new JButton("Loc Server");
			JB_LOCSERVER.setBounds(10, 144, 100, 22);
			JP_NS2.add(JB_LOCSERVER);
			JB_LOCSERVER.setActionCommand("Loc Server");
			JB_LOCSERVER.setOpaque(false);
			JB_LOCSERVER.addActionListener(this);
			JB_LOCSERVER.setPreferredSize(new Dimension(100, 22));

			JButton JB_RSUs = new JButton("RSUs");
			JB_RSUs.setBounds(10, 176, 100, 22);
			JP_NS2.add(JB_RSUs);
			JB_RSUs.setActionCommand("RSUs");
			JB_RSUs.setOpaque(false);
			JB_RSUs.addActionListener(this);
			JB_RSUs.setPreferredSize(new Dimension(100, 22));

			JButton JBReport = new JButton("Report");
			JBReport.setBounds(10, 208, 100, 22);
			JP_NS2.add(JBReport);
			JBReport.setActionCommand("Report");
			JBReport.setOpaque(false);
			JBReport.addActionListener(this);
			JBReport.setPreferredSize(new Dimension(100, 22));

			JLabel JLScenario = new JLabel("Scenario");
			JLScenario.setBounds(10, 10, 49, 15);
			JP_NS2.add(JLScenario);
			JTScenario = new JTextField(25);
			JTScenario.setBounds(59, 7, 149, 21);
			JP_NS2.add(JTScenario);

			JButton JBopenScenarioFile = new JButton("Open");
			JBopenScenarioFile.setBounds(218, 6, 58, 22);
			JP_NS2.add(JBopenScenarioFile);
			JBopenScenarioFile.setActionCommand("open scenario");
			JBopenScenarioFile.setOpaque(false);
			JBopenScenarioFile.setPreferredSize(new Dimension(64, 22));
			JBopenScenarioFile.addActionListener(this);

			mapNetworkPanel.setLayout(new BorderLayout(0, 0));
			mapNetworkPanel.add(getNetworkPanel(), BorderLayout.CENTER);
			mapNetworkPanel.add(functionTab, BorderLayout.EAST);

			JPanel JP_MapTool = new JPanel();
			functionTab.addTab("Map Tools", null, JP_MapTool, null);
			JP_MapTool.setLayout(null);

			JButton JBScaler = new JButton("Resize");
			JBScaler.setBounds(10, 10, 80, 22);
			JP_MapTool.add(JBScaler);
			JBScaler.setActionCommand("Resize");
			JBScaler.setOpaque(false);
			JBScaler.addActionListener(this);
			JBScaler.setPreferredSize(new Dimension(80, 22));

			JButton JBNetworkReIndex = new JButton("Reindex");
			JBNetworkReIndex.setBounds(10, 42, 80, 22);
			JP_MapTool.add(JBNetworkReIndex);
			JBNetworkReIndex.setActionCommand("Reindex");
			JBNetworkReIndex.setOpaque(false);
			JBNetworkReIndex.addActionListener(this);
			JBNetworkReIndex.setPreferredSize(new Dimension(80, 22));
			functionTab.addTab("Info", null, getJPCenterInfo(), null);
			networkShowPanel.setJTextArea(JTInfo);
		}
		return mainTab;
	}

	private JPanel getOpenStreetMapPanel() {
		if (openStreetMapPanel == null) {
			openStreetMapPanel = new JPanel();
			openStreetMapPanel.setLayout(new BorderLayout(0, 0));
			openStreetMapPanel.add(getJXMapKit(), BorderLayout.CENTER);
			openStreetMapPanel.add(getTimeSlider(), BorderLayout.WEST);
		}
		return openStreetMapPanel;
	}

	private JScrollBar getTimeSlider() {
		if (timeSlider == null) {
			timeSlider = new JScrollBar();
			timeSlider.setOpaque(false);
			timeSlider.setMaximum(0);
			timeSlider.setMinimum(0);
			timeSlider.addAdjustmentListener(this);
		}
		return timeSlider;
	}

	private JXMapViewer getJXMapKit() {
		if (mapviewer == null) {
			TileFactoryInfo info = new OSMTileFactoryInfo();
			DefaultTileFactory tileFactory = new DefaultTileFactory(info);
			tileFactory.setThreadPoolSize(8);

			// Setup local file cache
			File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
			LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

			// Setup JXMapViewer
			mapviewer = new JXMapViewer();
			mapviewer.setTileFactory(tileFactory);

			// Add interactions
			MouseInputListener mia = new PanMouseInputListener(mapviewer);
			mapviewer.addMouseListener(mia);
			mapviewer.addMouseMotionListener(mia);
			mapviewer.addMouseListener(new CenterMapListener(mapviewer));
			mapviewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapviewer));
			mapviewer.addKeyListener(new PanKeyListener(mapviewer));

			// Add a selection painter
			SelectionAdapter sa = new SelectionAdapter(mapviewer, tileFactory);
			mappainter = new MapPainter(sa, mapviewer);
			sa.addPropertyChangeListener(new SelectionPropertyListener(this, mapviewer));
			mapviewer.addMouseListener(sa);
			mapviewer.addMouseMotionListener(sa);
			mapviewer.setOverlayPainter(mappainter);
			mapviewer.setLayout(null);

			offsetSlider = new JSlider();
			offsetSlider.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					mapviewer.repaint();
				}
			});
			offsetSlider.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					offsetSlider.setBackground(new Color(255, 255, 255, 20));
					mapviewer.repaint();
				}

				@Override
				public void focusGained(FocusEvent e) {
					offsetSlider.setBackground(new Color(255, 255, 255, 160));
					mapviewer.repaint();
				}
			});
			offsetSlider.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					offsetSlider.setBackground(new Color(255, 255, 255, 20));
					mapviewer.repaint();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					offsetSlider.setBackground(new Color(255, 255, 255, 160));
					mapviewer.repaint();
				}
			});

			offsetSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					mapviewer.repaint();
					mappainter.setScaler(1 + (float) offsetSlider.getValue() / 100);
				}
			});
			offsetSlider.setBackground(new Color(255, 255, 255, 20));
			offsetSlider.setBounds(10, 10, 200, 23);
			offsetSlider.setMaximum(100);
			offsetSlider.setMinimum(0);
			offsetSlider.setValue(0);

			mapviewer.add(offsetSlider);

			setGeoLocation(50.11, 8.68);
		}
		return mapviewer;
	}

	private void centerDialog(Dialog dialog) {
		dialog.setLocation((this.getLocation().x + this.getWidth() / 2 - dialog.getWidth() / 2),
				(this.getLocation().y + this.getHeight() / 2 - dialog.getHeight() / 2));

		dialog.setVisible(true);
	}

	private void createDialog(AbstractDialog dialog) {
		try {
			dialog.set(scenario_reader.getScenario(), network_reader.getRoadNetwork().getLanes(), JTScenario.getText(),
					networkPath);
			centerDialog(dialog);
		} catch (NullPointerException e) {
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource() == timeSlider) {
			if (scenario_reader != null && scenario_reader.getScenario() != null) {
				mappainter.setNodes(scenario_reader.getScenario().get("" + timeSlider.getValue()));
			}
		}
	}

	public void startBar(String a) {
		progress.setStringPainted(true);
		progress.setString(a);
		progress.setFont(new Font("Verdana", Font.BOLD, 14));
		progress.setIndeterminate(true);
		progress.setUI(new BasicProgressBarUI() {
			protected Color getSelectionBackground() {
				return Color.blue;
			}

			protected Color getSelectionForeground() {
				return Color.white;
			}
		});

		centerDialog(progressDialog);
	}

	public void changeStatusBar(String message) {
		if (progressDialog.isVisible())
			progress.setString(message);

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

	}

	public void stopBar(String message) {
		progress.setString(message);
		progress.setIndeterminate(false);
		progressDialog.setVisible(false);
		setCursor(null);
	}

	public void setInitialValue() {

		final Config c = new Config();
		networkPath = c.getNetworkPath();
		JTScenario.setText(c.getScenarioPath());
		offsetSlider.setValue(c.getSliderValue());
		mappainter.setScaler(1 + (float) c.getSliderValue() / 100);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
				changeStatusBar("Reading Network File...");
				readNetFile(c.getNetworkPath());
				changeStatusBar("Reading Scenario File...");
				readScenarioFile(c.getScenarioPath());
				stopBar("Done");
			}
		}.start();

		startBar("Reading Network File...");
	}

	private void setGeoLocation(double x, double y) {
		mapviewer.setZoom(7);
		mapviewer.setAddressLocation(new GeoPosition(x, y));
	}

	private void openNetworkFile() {
		networkShowPanel.removeAll();
		String extension[] = { "XML", "xml" };
		readNetwork(FileSelectionTool.openFile(networkPath, "XML", extension));
	}

	private void openScenarioFile() {
		networkShowPanel.removeAll();
		String extension[] = { "TCL", "tcl" };
		readScenario(FileSelectionTool.openFile(JTScenario.getText(), "TCL", extension));
	}

	public void readNetwork(final String path) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
				changeStatusBar("Reading Network File...");
				readNetFile(path);
				stopBar("Done");
			}
		}.start();

		startBar("Reading Network File...");
	}

	private boolean readNetFile(String path) {

		if (path != null) {
			networkPath = path;
			new Config().setNetworkPath(path);
		} else
			return false;
		try {
			network_reader = new NetworkReader(path);
			if (network_reader.readScenario()) {
				setNetwork();

				String tcl = path.replaceAll(".net.xml", ".tcl");
				File f = new File(tcl);
				if (f.exists() && scenario_reader == null) {
					JTScenario.setText(tcl);
					scenario_reader = new ScenarioReader(tcl);
					if (scenario_reader.readScenario()) {
						setScenario();
						JTScenario.setText(tcl);
					}
				}

				if (network_reader.getRoadNetwork().hasOrigBoundary()) {
					mainTab.setSelectedIndex(0);
				} else {
					mainTab.setSelectedIndex(1);
				}
			}

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
		} catch (FileNotFoundException e) {
			return false;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void readScenario(final String path) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
				changeStatusBar("Reading Scenario File...");
				readScenarioFile(path);
				stopBar("Done");
			}
		}.start();

		startBar("Reading Scenario File...");
	}

	private boolean readScenarioFile(String path) {
		if (path != null) {
			JTScenario.setText(path);
			new Config().setScenarioPath(path);
		} else
			return false;
		try {
			scenario_reader = new ScenarioReader(path);
			if (scenario_reader.readScenario()) {
				setScenario();

				String net = path.replaceAll(".tcl", ".net.xml");
				File f = new File(net);
				if (f.exists() && network_reader == null) {
					networkPath = net;
					network_reader = new NetworkReader(net);
					if (network_reader.readScenario()) {
						setNetwork();
					}
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			return false;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (StringIndexOutOfBoundsException e) {
		}
		return true;
	}

	private void setNetwork() {
		networkShowPanel.setNet(network_reader.getRoadNetwork().getLanes(), network_reader.getMaxX(),
				network_reader.getMaxY());
		mappainter.setNetwork(network_reader.getRoadNetwork());
		Double p = network_reader.getRoadNetwork().getCenter();
		if (p != null)
			setGeoLocation(p.y, p.x);
	}

	private void setScenario() {
		networkShowPanel.setScenario(scenario_reader.getScenario(), scenario_reader.getMaxX(),
				scenario_reader.getMaxY());
		mappainter.setNodes(scenario_reader.getScenario().get("" + -1));
		timeSlider.setMaximum(scenario_reader.getScenario().size() - 2);
		timeSlider.setMinimum(-1);
		timeSlider.setValue(-1);
	}

	private float getScaler() {
		return 1 + (float) offsetSlider.getValue() / 100;
	}

	public void showInfo(SelectionMessage m) {
		TravelingNodes nodes = scenario_reader.getScenario().get("" + timeSlider.getValue());
		Network network = network_reader.getRoadNetwork();

		if (nodes == null)
			return;

		if (network.hasOrigBoundary()) {

			Config c = new Config();
			int errx, erry, basicZoom = 2;
			try {
				errx = Integer.parseInt(c.getOffsetX());
				erry = Integer.parseInt(c.getOffsetY());
			} catch (NumberFormatException e) {
				errx = 0;
				erry = 0;
			}

			Rectangle viewportBounds = mapviewer.getViewportBounds();

			Point2D basicPoint_zoom_1 = mapviewer.getTileFactory()
					.geoToPixel(new GeoPosition(network.getOrigBoundary().y2, network.getOrigBoundary().x1), basicZoom);

			double minx = Math.min(m.getStartPoint().getX(), m.getReleasePoint().getX());
			double miny = Math.min(m.getStartPoint().getY(), m.getReleasePoint().getY());
			double maxx = Math.max(m.getStartPoint().getX(), m.getReleasePoint().getX());
			double maxy = Math.max(m.getStartPoint().getY(), m.getReleasePoint().getY());

			JTInfo.append("Area [" + minx + "," + miny + "] [" + maxx + "," + maxy + "]\n");

			int count = 0;
			for (Vehicle n : nodes.getNodes()) {
				double y = reMapping(n.getY());

				Point2D.Double new_from_1 = new Point2D.Double(basicPoint_zoom_1.getX() + n.getX() * getScaler() + errx,
						basicPoint_zoom_1.getY() + y * getScaler() + erry);

				GeoPosition new_from = mapviewer.getTileFactory().pixelToGeo(new_from_1, basicZoom);

				Point2D new_from_view = mapviewer.getTileFactory().geoToPixel(new_from, mapviewer.getZoom());

				double x = new_from_view.getX() - viewportBounds.getX();
				double new_y = new_from_view.getY() - viewportBounds.getY();

				if (x > minx && new_y > miny && x < maxx && new_y < maxy) {
					JTInfo.append(n.getID() + " [" + x + " , " + new_y + " , " + n.getSpeed() + "]\n");
					count++;
				}
			}
			JTInfo.append("Total node number: " + count + "\n\n");
		}
	}

	private double reMapping(double y) {

		double center = network_reader.getRoadNetwork().getConvBoundary().y2 / 2;
		if (y > center) {
			y -= 2 * (y - center);
		} else if (y < center) {
			y += 2 * (center - y);
		}

		return y;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Prefereces":
			PreferenceDialog p = new PreferenceDialog(this);
			centerDialog(p);
			mapviewer.repaint();
			break;
		case "Exit":
			System.exit(0);
			break;
		case "open scenario":
			openScenarioFile();
			break;
		case "open network":
		case "Open":
			openNetworkFile();
			break;
		case "CBR":
			createDialog(new CbrDialog(this));
			break;
		case "Flow":
			createDialog(new FlowDialog(this));
			break;
		case "Scenario":
			createDialog(new ScenarioDialog(this));
			break;
		case "Loc Server":
			createDialog(new LocServerDialog(this));
			break;
		case "Resize":
			createDialog(new ResizeDialog(this));
			break;
		case "RSUs":
			createDialog(new RSUDialog(this));
			break;
		case "Report":
			createDialog(new ReportDialog(this));
			break;
		case "Reindex":
			new NetworkReIndexTask().parseFile(networkPath);
			break;
		case "Autonomous":
			Config c = new Config();
			new AutonomousTask(this, Integer.parseInt(c.getNumberOfAutonomous()), Integer.parseInt(c.getMaxSpeed()),
					Integer.parseInt(c.getMinSpeed()), Integer.parseInt(c.getSimulationTimeBegin()),
					Integer.parseInt(c.getSimulationTimeEnd()), network_reader.getRoadNetwork().getLanes()).execute();

			changeStatusBar("Autonomous Driving Simulation...");
			startBar("Autonomous Driving Simulation...");

			break;
		}
	}

}
