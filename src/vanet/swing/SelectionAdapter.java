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

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;

/**
 * Creates a selection rectangle based on mouse input Also triggers repaint
 * events in the viewer
 * 
 * @author Martin Steiger
 */
public class SelectionAdapter extends MouseAdapter {
	private boolean dragging;
	private JXMapViewer viewer;
	private TileFactory tileFactory;

	private Point2D startPos = new Point2D.Double();
	private Point2D endPos = new Point2D.Double();

	private SelectionMessage message;

	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	/**
	 * @param viewer
	 *            the jxmapviewer
	 */
	public SelectionAdapter(JXMapViewer viewer, TileFactory tileFactory) {
		this.viewer = viewer;
		this.tileFactory = tileFactory;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		viewer.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1)
			return;

		startPos.setLocation(e.getX(), e.getY());
		endPos.setLocation(e.getX(), e.getY());

		dragging = true;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!dragging)
			return;

		endPos.setLocation(e.getX(), e.getY());

		viewer.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!dragging)
			return;

		if (e.getButton() != MouseEvent.BUTTON1)
			return;

		// viewer.repaint();
		Rectangle rect = getRectangle();

		dragging = false;

		{
			SelectionMessage old_m = message;
			double x1 = Math.min(startPos.getX(), endPos.getX());
			double y1 = Math.min(startPos.getY(), endPos.getY());
			double x2 = Math.max(startPos.getX(), endPos.getX());
			double y2 = Math.max(startPos.getY(), endPos.getY());

			if (x1 != x2 && y1 != y2) {
				Insets insets = viewer.getInsets();
				int viewportHeight = viewer.getHeight() - insets.top - insets.bottom;
				int viewportWidth = viewer.getWidth() - insets.left - insets.right;
				Point2D old = viewer.getCenter();
				GeoPosition g1 = tileFactory.pixelToGeo(new Point2D.Double((old.getX() + x1 - (viewportWidth / 2)),
						(old.getY() + y2 - (viewportHeight / 2))), viewer.getZoom());
				GeoPosition g2 = tileFactory.pixelToGeo(new Point2D.Double((old.getX() + x2 - (viewportWidth / 2)),
						(old.getY() + y1 - (viewportHeight / 2))), viewer.getZoom());
				Point releasePoint = e.getPoint();
				message = new SelectionMessage(g1, g2, releasePoint, rect);
				changes.firePropertyChange("GEO", old_m, message);
			}
		}

	}

	/**
	 * @return the selection rectangle
	 */
	public Rectangle getRectangle() {
		if (dragging) {
			int x1 = (int) Math.min(startPos.getX(), endPos.getX());
			int y1 = (int) Math.min(startPos.getY(), endPos.getY());
			int x2 = (int) Math.max(startPos.getX(), endPos.getX());
			int y2 = (int) Math.max(startPos.getY(), endPos.getY());

			return new Rectangle(x1, y1, x2 - x1, y2 - y1);
		}

		return null;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
	}
}
