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
package vanet.ns2;

import java.util.Vector;

public class NSMobilityNodePath {
	public Vector<NSMobilityNode> path = null;

	public NSMobilityNodePath() {
		path = new Vector<NSMobilityNode>();
	}

	public void add(NSMobilityNode node) {
		if (path.size() > 0 && (path.lastElement().edgeId.equals(node.edgeId))
				&& !node.getFrom().equalsIgnoreCase("X")) {
			path.lastElement().setX(node.getX());
			path.lastElement().setY(node.getY());
		} else
			path.add(node);
	}

	public int getID() {
		return path.get(0).getId();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (NSMobilityNode n : path) {
			sb.append(" ");
			sb.append(n.edgeId);
			sb.append(",");
			sb.append(n.getTimestep());
			sb.append(",");
			sb.append(n.getX());
			sb.append(",");
			sb.append(n.getY());
			sb.append(",");
			sb.append(n.getFrom());
			sb.append(",");
			sb.append(n.getTo());
		}
		return sb.toString();
	}
}
