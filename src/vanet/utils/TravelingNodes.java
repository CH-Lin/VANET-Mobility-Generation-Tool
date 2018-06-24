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

import java.util.HashMap;

public class TravelingNodes {
	private int time;
	private HashMap<String, Vehicle> record = new HashMap<String, Vehicle>();

	public TravelingNodes(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public Vehicle getNode(int id) {
		return record.get("" + id);
	}

	public void add(Vehicle node) {
		record.put("" + node.getID(), node);
	}

	public Vehicle[] getNodes() {
		return record.values().toArray(new Vehicle[0]);
	}
}
