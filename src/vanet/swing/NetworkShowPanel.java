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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JLabel;

import vanet.utils.Lane;
import vanet.utils.Vehicle;
import vanet.utils.TravelingNodes;

public class NetworkShowPanel extends JLabel {

	private static final long serialVersionUID = 1L;

	private Point rectPoint1 = null, rectPoint2 = null, linePoint1 = null, linePoint2 = null;

	private Vehicle vehiclePoint1 = null, vehiclePoint2 = null;

	private HashMap<String, Lane> lanes = null;

	private TravelingNodes travelingNodes = null;

	private NetworkMapping mapping = null;

	public NetworkShowPanel(NetworkMapping mapping) {
		super();
		initialize();
		this.mapping = mapping;
	}

	private void initialize() {
		this.setSize(300, 200);
		this.setDoubleBuffered(true);
	}

	public void drawLine(Point p1, Point p2) {
		this.linePoint1 = p1;
		this.linePoint2 = p2;
		repaint();
	}

	public void drawRect(Point p1, Point p2) {
		this.rectPoint1 = p1;
		this.rectPoint2 = p2;
		repaint();
	}

	public void traceFrom(int id) {
		if (this.travelingNodes != null) {
			vehiclePoint1 = this.travelingNodes.getNode(id);
			repaint();
		}
	}

	public void traceTo(int id) {
		if (this.travelingNodes != null) {
			vehiclePoint2 = this.travelingNodes.getNode(id);
			repaint();
		}
	}

	public void setNodes(TravelingNodes nodes, double maxx, double maxy) {
		this.travelingNodes = nodes;
		repaint();
	}

	public void setLanes(HashMap<String, Lane> lanes, double maxx, double maxy) {
		this.lanes = lanes;
		repaint();
	}

	public void removeAll() {
		travelingNodes = null;
		rectPoint1 = null;
		rectPoint2 = null;
		linePoint1 = null;
		linePoint2 = null;
		vehiclePoint1 = null;
		vehiclePoint2 = null;
		repaint();
	}

	public String getNodeList() {
		String list = "";

		if (travelingNodes != null) {
			if (rectPoint1 != null && rectPoint2 != null) {
				double minx = mapping.projToMapX(Math.min((int) rectPoint1.getX(), (int) rectPoint2.getX()));
				double miny = mapping.projToMapY(Math.min((int) rectPoint1.getY(), (int) rectPoint2.getY()));
				double maxx = mapping.projToMapX(Math.max((int) rectPoint1.getX(), (int) rectPoint2.getX()));
				double maxy = mapping.projToMapY(Math.max((int) rectPoint1.getY(), (int) rectPoint2.getY()));

				int count = 0;
				Vehicle n[] = travelingNodes.getNodes();
				for (int i = 0; i < n.length; i++) {
					if (n[i].getX() > minx && n[i].getY() > miny && n[i].getX() < maxx && n[i].getY() < maxy) {
						list += n[i].getID() + " [" + n[i].getX() + " , " + n[i].getY() + " , " + n[i].getSpeed()
								+ "]\n";
						count++;
					}
				}
				list += "Total node number: " + count + "\n\n";
			}
		}
		return list;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// Color c = g2.getColor();

		g2.setColor(Color.gray);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		if (travelingNodes != null) {
			Vehicle n[] = travelingNodes.getNodes();
			for (int i = 0; i < n.length; i++) {
				g2.drawOval(mapping.projToRealX(n[i].getX()) - 2, mapping.projToRealY(n[i].getY()) - 2, 5, 5);
			}

		}

		if (vehiclePoint1 != null) {
			traceFrom(vehiclePoint1.getID());

			g2.setColor(Color.blue);
			g2.drawOval(mapping.projToRealX(vehiclePoint1.getX()) - 5, mapping.projToRealY(vehiclePoint1.getY()) - 5,
					10, 10);
		}

		if (vehiclePoint2 != null) {
			traceTo(vehiclePoint2.getID());
			g2.setColor(Color.red);
			g2.drawOval(mapping.projToRealX(vehiclePoint2.getX()) - 5, mapping.projToRealY(vehiclePoint2.getY()) - 5,
					10, 10);
		}

		if (rectPoint1 != null && rectPoint2 != null) {
			int x = Math.min((int) rectPoint1.getX(), (int) rectPoint2.getX());
			int y = Math.min((int) rectPoint1.getY(), (int) rectPoint2.getY());
			int width = Math.max((int) rectPoint1.getX(), (int) rectPoint2.getX()) - x;
			int height = Math.max((int) rectPoint1.getY(), (int) rectPoint2.getY()) - y;
			g2.drawRect(x, y, width, height);
		}
		g2.setColor(Color.red);
		if (linePoint1 != null) {
			g2.drawOval((int) linePoint1.getX() - 5, (int) linePoint1.getY() - 5, 10, 10);
		}
		if (linePoint2 != null) {
			g2.drawOval((int) linePoint2.getX() - 5, (int) linePoint2.getY() - 5, 10, 10);
		}
		if (linePoint1 != null && linePoint2 != null) {
			g2.drawLine((int) linePoint1.getX(), (int) linePoint1.getY(), (int) linePoint2.getX(),
					(int) linePoint2.getY());
		}
		g2.setColor(Color.LIGHT_GRAY);

		if (lanes != null) {
			for (Lane l : lanes.values()) {
				for (int i = 0; i < l.getPointCount() - 1; i++) {
					g2.drawLine(mapping.projToRealX(l.getSegmentFromX(i)), mapping.projToRealY(l.getSegmentFromY(i)),
							mapping.projToRealX(l.getSegmentToX(i)), mapping.projToRealY(l.getSegmentToY(i)));
				}
			}
		}
	}
}
