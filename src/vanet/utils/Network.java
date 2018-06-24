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
package vanet.utils;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.HashMap;

public class Network {
	private HashMap<String, Lane> lanes;
	private Point.Double netOffset;
	private Line2D.Double convBoundary, origBoundary;

	public Network() {
		this.lanes = new HashMap<String, Lane>();
	}

	public void setNetOffset(double x, double y) {
		this.netOffset = new Point.Double(x, y);
	}

	public Point.Double getNetOffset() {
		return this.netOffset;
	}

	public boolean hasOrigBoundary() {
		if (origBoundary.x2 != 0 && origBoundary.y2 != 0)
			return true;
		else
			return false;
	}

	public void setConvBoundary(double x1, double y1, double x2, double y2) {
		this.convBoundary = new Line2D.Double(x1, y1, x2, y2);
	}

	public Line2D.Double getConvBoundary() {
		return this.convBoundary;
	}

	public void setOrigBoundary(double x1, double y1, double x2, double y2) {
		this.origBoundary = new Line2D.Double(x1, y1, x2, y2);
	}

	public Line2D.Double getOrigBoundary() {
		return this.origBoundary;
	}

	public Point.Double getCenter() {
		if (origBoundary != null)
			return new Point.Double((origBoundary.x1 + origBoundary.x2) / 2, (origBoundary.y1 + origBoundary.y2) / 2);
		else
			return null;
	}

	public void addLane(Lane l) {
		this.lanes.put(l.getId(), l);
	}

	public Lane getLane(String id) {
		return this.lanes.get(id);
	}

	public HashMap<String, Lane> getLanes() {
		return this.lanes;
	}
}
