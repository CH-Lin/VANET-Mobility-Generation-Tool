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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JCheckBox;

import vanet.utils.Lane;
import vanet.utils.TravelingNodes;

public class NetworkPanel extends JPanel implements AdjustmentListener, ActionListener, MouseListener,
		MouseMotionListener, ItemListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;

	private JPopupMenu myJPopupMenu = new JPopupMenu();
	private JPanel JP_LOC = null;
	private JPanel JPTime = null;
	private JPanel panel_4 = null;
	private JLabel JLScreenScaler = null;
	private JLabel JL_STATUE = null;
	private JTextArea JTFinfo = null;
	private JMenuItem jMenuItemFind = null;
	private JCheckBox JCPlay = null;
	private JScrollBar scalerBar = null;
	private JScrollBar JSTime = null;

	private NetworkShowPanel JPNetworkShow = null;

	private Point linePoint, areaPoint;
	private Thread play = null;
	private HashMap<String, TravelingNodes> record = null;

	private int time = -1;
	private double maxx, maxy;
	private boolean click = true, firstClick = true, leftDrag = false, rightDrag = false, playing = false;

	private NetworkMapping m = new NetworkMapping();

	public NetworkPanel() {
		super();
		setBackground(Color.WHITE);
		initialize();
	}

	private void initialize() {
		myJPopupMenu.add(getJMenuFind());
		setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		setOpaque(false);
		setLayout(new BorderLayout());
		add(getJP_LOC(), BorderLayout.NORTH);
		add(getJPScreen(), BorderLayout.CENTER);
		JL_STATUE = new JLabel();
		add(JL_STATUE, BorderLayout.SOUTH);

		JL_STATUE.setBorder(new LineBorder(Color.LIGHT_GRAY));
		JL_STATUE.setText("Location: [ 0 , 0 ] \tTime: " + JSTime.getValue() + " sec.");
	}

	private NetworkShowPanel getJPScreen() {
		if (JPNetworkShow == null) {
			JPNetworkShow = new NetworkShowPanel(m);
			JPNetworkShow.setLayout(null);
			JPNetworkShow.addMouseListener(this);
			JPNetworkShow.addMouseMotionListener(this);
			JPNetworkShow.addMouseWheelListener(this);
		}
		return JPNetworkShow;
	}

	private JMenuItem getJMenuFind() {
		if (jMenuItemFind == null) {
			jMenuItemFind = new JMenuItem("Find");
			jMenuItemFind.addActionListener(this);
		}
		return jMenuItemFind;
	}

	private JScrollBar getScalerBar() {
		if (scalerBar == null) {
			scalerBar = new JScrollBar();
			scalerBar.setMinimum(0);
			scalerBar.setMaximum(510);
			scalerBar.setOpaque(false);
			scalerBar.setOrientation(JScrollBar.HORIZONTAL);
			scalerBar.setPreferredSize(new Dimension(190, 17));
			scalerBar.setValue(100);
			scalerBar.addAdjustmentListener(this);
		}
		return scalerBar;
	}

	private JPanel getPanel_4() {
		if (panel_4 == null) {
			panel_4 = new JPanel();
			panel_4.setOpaque(false);
			panel_4.setLayout(new BorderLayout(0, 0));
			panel_4.add(getScalerBar());
			JLScreenScaler = new JLabel("100%");
			JLScreenScaler.setPreferredSize(new Dimension(30, 15));
			panel_4.add(JLScreenScaler, BorderLayout.EAST);
		}
		return panel_4;
	}

	private JScrollBar getJSTime() {
		if (JSTime == null) {
			JSTime = new JScrollBar();
			JSTime.setOrientation(JScrollBar.HORIZONTAL);
			JSTime.setOpaque(false);
			JSTime.setMaximum(0);
			JSTime.setMinimum(0);
			JSTime.addAdjustmentListener(this);
		}
		return JSTime;
	}

	private JPanel getJPTime() {
		if (JPTime == null) {
			JPTime = new JPanel();
			JPTime.setOpaque(false);
			JPTime.setLayout(new BorderLayout());
			JPTime.add(getJSTime(), BorderLayout.CENTER);
			JPTime.add(getJCPlay(), BorderLayout.WEST);
		}
		return JPTime;
	}

	private JPanel getJP_LOC() {
		if (JP_LOC == null) {
			JP_LOC = new JPanel();
			JP_LOC.setOpaque(false);
			JP_LOC.setLayout(new BorderLayout(0, 0));
			JP_LOC.add(getPanel_4(), BorderLayout.EAST);
			JP_LOC.add(getJPTime(), BorderLayout.CENTER);
		}
		return JP_LOC;
	}

	private JCheckBox getJCPlay() {
		if (JCPlay == null) {
			JCPlay = new JCheckBox();
			JCPlay.setOpaque(false);
			JCPlay.setContentAreaFilled(false);
			JCPlay.addItemListener(this);
		}
		return JCPlay;
	}

	public void setJTextArea(JTextArea text) {
		this.JTFinfo = text;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		click = true;
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			if (firstClick) {
				linePoint = new Point(arg0.getPoint());
				firstClick = false;
				JPNetworkShow.drawLine(linePoint, null);
			} else {
				firstClick = true;
				JTFinfo.append("Distance: "
						+ linePoint.distance(arg0.getPoint()) / ((double) scalerBar.getValue() / 100) + "\n");
				JPNetworkShow.drawLine(linePoint, arg0.getPoint());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		areaPoint = new Point(arg0.getPoint());
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			leftDrag = true;
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			rightDrag = true;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		String temp = "Location: [ " + m.projToMapX(arg0.getX()) + " , " + m.projToMapY(arg0.getY()) + " ]";
		temp += " \tTime: " + JSTime.getValue() + " sec.";
		JL_STATUE.setText(temp);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (!click && leftDrag && arg0.getButton() == MouseEvent.BUTTON1) {
			try {
				JTFinfo.append("Area: [ (" + m.projToMapX(areaPoint.getX()) + "," + m.projToMapY(areaPoint.getY())
						+ ") , (" + m.projToMapX(arg0.getX()) + "," + m.projToMapY(arg0.getY()) + ") ]\n");

				JTFinfo.append(JPNetworkShow.getNodeList());
			} catch (NullPointerException e) {
			}
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			firstClick = true;
			linePoint = areaPoint = null;

			JPNetworkShow.drawLine(linePoint, areaPoint);
			JPNetworkShow.drawRect(linePoint, areaPoint);
			// myJPopupMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
		}
		rightDrag = leftDrag = false;
		click = true;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		click = false;
		if (leftDrag) {
			JPNetworkShow.drawRect(areaPoint, arg0.getPoint());
		} else if (rightDrag) {
			m.adjOffset(arg0.getPoint().getX() - areaPoint.getX(), arg0.getPoint().getY() - areaPoint.getY());
			areaPoint = new Point(arg0.getPoint());
			JPNetworkShow.repaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jMenuItemFind) {
			trace();
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource() == scalerBar) {
			m.setScale((double) scalerBar.getValue() / 100);
			JPNetworkShow.repaint();
			JLScreenScaler.setText(scalerBar.getValue() + "%");
		} else if (e.getSource() == JSTime) {
			if (record != null) {
				time = JSTime.getValue();
				JPNetworkShow.setNodes(record.get("" + JSTime.getValue()), maxx, maxy);

				JL_STATUE.setText("Location: [ 0 , 0 ]" + " \tTime: " + JSTime.getValue() + " sec.");
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == JCPlay && JCPlay.isSelected() && play == null) {
			play = new Thread() {
				public void run() {
					// System.out.println("Play");

					if (record != null) {
						playing = true;
						while (time < JSTime.getMaximum() && playing) {

							JPNetworkShow.setNodes(record.get("" + JSTime.getValue()), maxx, maxy);

							time++;
							JSTime.setValue(time);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
							}
						}
						play = null;
						JCPlay.setSelected(false);
						JSTime.setValue(-1);
						JPNetworkShow.setNodes(record.get("-1"), maxx, maxy);
					}
				};
			};
			play.start();
		} else {
			if (play != null) {
				playing = false;
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scalerBar.setValue(scalerBar.getValue() + 10 * e.getWheelRotation());
	}

	public void setNet(HashMap<String, Lane> lanes, double maxx, double maxy) {
		scalerBar.setValue(100);
		JPNetworkShow.setLanes(lanes, maxx, maxy);
	}

	public void setScenario(HashMap<String, TravelingNodes> record, double maxx, double maxy) {
		scalerBar.setValue(100);
		this.record = record;
		this.maxx = maxx;
		this.maxy = maxy;
		JPNetworkShow.setNodes(record.get("-1"), maxx, maxy);

		JSTime.setMaximum(record.size() - 2);
		JSTime.setMinimum(-1);
		JSTime.setValue(-1);
	}

	public void trace() {
		try {
			int from = Integer.parseInt(JOptionPane.showInputDialog("From (Blue)"));
			JPNetworkShow.traceFrom(from);
		} catch (NumberFormatException e) {
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
		try {
			int to = Integer.parseInt(JOptionPane.showInputDialog("To (Red)"));
			JPNetworkShow.traceTo(to);
		} catch (NumberFormatException e) {
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}

	public void removeAll() {
		JPNetworkShow.removeAll();
	}

}
