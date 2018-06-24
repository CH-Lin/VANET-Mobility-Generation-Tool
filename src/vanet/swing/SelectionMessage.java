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

import java.awt.Point;
import java.awt.Rectangle;

import org.jdesktop.swingx.mapviewer.GeoPosition;

public class SelectionMessage {
	private GeoPosition g1, g2;

	private Point releasePoint;

	private Rectangle rect;

	public SelectionMessage(GeoPosition g1, GeoPosition g2, Point r, Rectangle rect) {
		this.g1 = g1;
		this.g2 = g2;
		this.releasePoint = r;
		this.rect = rect;
	}

	public Point getReleasePoint() {
		return releasePoint;
	}

	public Point getStartPoint() {
		return rect.getLocation();
	}

	public double getWidth() {
		return rect.getWidth();
	}

	public double getHeight() {
		return rect.getHeight();
	}

	public GeoPosition getG1() {
		return g1;
	}

	public GeoPosition getG2() {
		return g2;
	}
}
