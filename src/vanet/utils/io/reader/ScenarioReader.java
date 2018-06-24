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
package vanet.utils.io.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import vanet.utils.Vehicle;
import vanet.utils.TravelingNodes;
import vanet.utils.io.AbstractReader;

public class ScenarioReader extends AbstractReader {

	public ScenarioReader(String path) {
		super(path);
	}

	private HashMap<String, TravelingNodes> scenario = null;

	public HashMap<String, TravelingNodes> getScenario() {
		return scenario;
	}

	public boolean readScenario() throws NumberFormatException, IOException {
		if (path == null)
			return false;

		scenario = new HashMap<String, TravelingNodes>();
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line;
		TravelingNodes nodes = new TravelingNodes(0);
		TravelingNodes n_pre = nodes;
		int now = -1, total = 0;

		scenario.put("" + now, nodes);

		while ((line = br.readLine()) != null) {
			Vehicle n;
			if (line.startsWith("$node_(")) {
				n = new Vehicle(Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(")"))));
				n.setX(Double.parseDouble(line.substring(line.indexOf("X") + 3)));
				line = br.readLine();
				n.setY(Double.parseDouble(line.substring(line.indexOf("Y") + 3)));
				line = br.readLine();
				total++;
			} else {
				int ttime = Integer
						.parseInt(line.substring(line.indexOf("at") + 3, line.indexOf("\"")).replaceAll(" ", ""));
				if (now != ttime) {
					if (nodes.getNodes().length != total && now != -1) {
						for (int i = 0; i < total; i++) {
							if (nodes.getNode(i) == null) {
								nodes.add(n_pre.getNode(i));
							}
						}
					}
					n_pre = nodes;
					nodes = new TravelingNodes(ttime);
					scenario.put("" + now, nodes);
					now = ttime;
				}

				n = new Vehicle(Integer.parseInt(line.substring(line.indexOf("_(") + 2, line.indexOf(")"))));

				line = line.substring(line.indexOf("setdest"));
				String data[] = line.split(" ");
				n.setX(Double.parseDouble(data[1]));
				n.setY(Double.parseDouble(data[2]));
				n.setSpeed(Double.parseDouble(data[3]));
				n.setEdge(data[4]);
				n.setFrom(data[5]);
				n.setTo(data[6]);
			}

			if (n.getX() > maxx) {
				maxx = n.getX();
			}
			if (n.getY() > maxy) {
				maxy = n.getY();
			}
			nodes.add(n);
		}
		br.close();

		if (nodes.getNodes().length != total && now != 0) {
			for (int i = 0; i < total; i++) {
				if (nodes.getNode(i) == null) {
					nodes.add(n_pre.getNode(i));
				}
			}
		}

		return true;
	}

}
