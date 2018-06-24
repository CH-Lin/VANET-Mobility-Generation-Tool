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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

import vanet.utils.Config;
import vanet.utils.Lane;
import vanet.utils.Network;
import vanet.utils.Vehicle;
import vanet.utils.TravelingNodes;

public class MapPainter implements Painter<Object> {
	private Color fillColor = new Color(128, 192, 255, 128);
	private Color frameColor = new Color(0, 0, 255, 128);

	private SelectionAdapter adapter;
	private JXMapViewer viewer;
	private Network network;
	private TravelingNodes nodes;

	private double scaler = 1.0;

	/**
	 * @param adapter
	 *            the selection adapter
	 */
	public MapPainter(SelectionAdapter adapter, JXMapViewer viewer) {
		this.adapter = adapter;
		this.viewer = viewer;
	}

	public void setScaler(double s) {
		this.scaler = s;
		// System.out.println(s);
	}

	public void setNetwork(Network net) {
		this.network = net;
		viewer.repaint();
	}

	public void setNodes(TravelingNodes nodes) {
		this.nodes = nodes;
		viewer.repaint();
	}

	private double reMapping(double y) {

		double center = network.getConvBoundary().y2 / 2;
		if (y > center) {
			y -= 2 * (y - center);
		} else if (y < center) {
			y += 2 * (center - y);
		}

		return y;
	}

	@Override
	public void paint(Graphics2D g, Object t, int width, int height) {
		Rectangle rc = adapter.getRectangle();

		if (rc != null) {
			g.setColor(frameColor);
			g.draw(rc);
			g.setColor(fillColor);
			g.fill(rc);
		}

		Config c = new Config();
		int errx, erry, basicZoom = 2;
		try {
			errx = Integer.parseInt(c.getOffsetX());
			erry = Integer.parseInt(c.getOffsetY());
		} catch (NumberFormatException | NullPointerException e) {
			errx = 0;
			erry = 0;
		}

		if (nodes != null && network.hasOrigBoundary()) {

			Stroke s = g.getStroke();

			g.setStroke(new BasicStroke(2));

			g.setColor(new Color(254, 0, 254));

			Rectangle viewportBounds = viewer.getViewportBounds();

			g.translate(-viewportBounds.getX(), -viewportBounds.getY());

			Point2D basicPoint_zoom_1 = viewer.getTileFactory()
					.geoToPixel(new GeoPosition(network.getOrigBoundary().y2, network.getOrigBoundary().x1), basicZoom);

			Vehicle n[] = nodes.getNodes();
			for (int i = 0; i < n.length; i++) {

				double y = reMapping(n[i].getY());

				Point2D.Double new_from_1 = new Point2D.Double(basicPoint_zoom_1.getX() + n[i].getX() * scaler + errx,
						basicPoint_zoom_1.getY() + y * scaler + erry);

				GeoPosition new_from = viewer.getTileFactory().pixelToGeo(new_from_1, basicZoom);

				Point2D new_from_view = viewer.getTileFactory().geoToPixel(new_from, viewer.getZoom());

				g.drawOval((int) new_from_view.getX(), (int) new_from_view.getY(), 5, 5);
			}
			g.translate(viewportBounds.getX(), viewportBounds.getY());

			g.setStroke(s);
		}

		if (network != null && network.hasOrigBoundary()) {

			g.setColor(Color.blue);

			Rectangle viewportBounds = viewer.getViewportBounds();

			g.translate(-viewportBounds.getX(), -viewportBounds.getY());

			Point2D basicPoint_zoom_1 = viewer.getTileFactory()
					.geoToPixel(new GeoPosition(network.getOrigBoundary().y2, network.getOrigBoundary().x1), basicZoom);

			for (Lane l : network.getLanes().values()) {
				g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255),
						(int) (Math.random() * 255)));
				for (int i = 0; i < l.getPointCount() - 1; i++) {

					double y = reMapping(l.getSegmentFromY(i));

					Point2D.Double new_from_1 = new Point2D.Double(
							basicPoint_zoom_1.getX() + l.getSegmentFromX(i) * scaler + errx,
							basicPoint_zoom_1.getY() + y * scaler + erry);

					y = reMapping(l.getSegmentToY(i));

					Point2D.Double new_to_1 = new Point2D.Double(
							basicPoint_zoom_1.getX() + l.getSegmentToX(i) * scaler + errx,
							basicPoint_zoom_1.getY() + y * scaler + erry);

					GeoPosition new_from = viewer.getTileFactory().pixelToGeo(new_from_1, basicZoom);

					GeoPosition new_to = viewer.getTileFactory().pixelToGeo(new_to_1, basicZoom);

					Point2D new_from_view = viewer.getTileFactory().geoToPixel(new_from, viewer.getZoom());

					Point2D new_to_view = viewer.getTileFactory().geoToPixel(new_to, viewer.getZoom());

					g.drawLine((int) new_from_view.getX(), (int) new_from_view.getY(), (int) new_to_view.getX(),
							(int) new_to_view.getY());
				}
			}

			g.translate(viewportBounds.getX(), viewportBounds.getY());
		}
	}
}