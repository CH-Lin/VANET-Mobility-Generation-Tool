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

import java.util.Vector;

public class Connection {

	private String from;

	private Vector<String> neighbors;

	public Connection(String from) {
		this.from = from;
		neighbors = new Vector<String>();
	}

	public String getEdgeID() {
		return this.from;
	}

	public void setNeighbors(Vector<String> neighbors) {
		this.neighbors = neighbors;
	}

	public Vector<String> getNeighbors() {
		return neighbors;
	}
}
