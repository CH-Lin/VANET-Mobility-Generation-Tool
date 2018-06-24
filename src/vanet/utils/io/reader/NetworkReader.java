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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import vanet.utils.Connection;
import vanet.utils.Lane;
import vanet.utils.Network;
import vanet.utils.io.AbstractReader;

public class NetworkReader extends AbstractReader {

	public NetworkReader(String path) {
		super(path);
	}

	private Network roadNetwork = null;

	public Network getRoadNetwork() {
		return this.roadNetwork;
	}

	private void getLocationInfo(Element e) {
		if (e.getChild("location") != null) {
			e = e.getChild("location");

			String temp = e.getAttributeValue("netOffset");
			String xy[] = temp.split(",");

			roadNetwork.setNetOffset(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));

			temp = e.getAttributeValue("convBoundary");
			String boundary[] = temp.split(",");

			roadNetwork.setConvBoundary(Double.parseDouble(boundary[0]), Double.parseDouble(boundary[1]),
					Double.parseDouble(boundary[2]), Double.parseDouble(boundary[3]));

			temp = e.getAttributeValue("origBoundary");
			boundary = temp.split(",");

			roadNetwork.setOrigBoundary(Double.parseDouble(boundary[0]), Double.parseDouble(boundary[1]),
					Double.parseDouble(boundary[2]), Double.parseDouble(boundary[3]));

		} else {
			Element tempE = e.getChild("net-offset");
			if (tempE != null) {
				String temp = tempE.getValue();
				String xy[] = temp.split(",");
				roadNetwork.setNetOffset(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
			}
			tempE = null;

			tempE = e.getChild("conv-boundary");
			if (tempE != null) {
				String temp = tempE.getValue();
				String boundary[] = temp.split(",");
				roadNetwork.setConvBoundary(Double.parseDouble(boundary[0]), Double.parseDouble(boundary[1]),
						Double.parseDouble(boundary[2]), Double.parseDouble(boundary[3]));
			}
			tempE = null;

			tempE = e.getChild("orig-boundary");
			if (tempE != null) {
				String temp = tempE.getValue();
				String boundary[] = temp.split(",");
				roadNetwork.setOrigBoundary(Double.parseDouble(boundary[0]), Double.parseDouble(boundary[1]),
						Double.parseDouble(boundary[2]), Double.parseDouble(boundary[3]));
			}
		}
	}

	private void getContent(Element edge, Element element) {
		Iterator<Element> iterator2 = element.getChildren("lane").iterator();
		while (iterator2.hasNext()) {
			Element lane = (Element) iterator2.next();
			Lane l = new Lane(lane.getAttributeValue("id"));

			l.setEdge(edge.getAttributeValue("id"));

			l.setAllow(edge.getAttributeValue("allow") != null ? edge.getAttributeValue("allow") : "X");

			l.setFrom(edge.getAttributeValue("from") != null ? edge.getAttributeValue("from") : "X");

			l.setTo(edge.getAttributeValue("to") != null ? edge.getAttributeValue("to") : "X");

			l.setLength(lane.getAttributeValue("length") != null ? lane.getAttributeValue("length") : "X");

			l.setSpeed(lane.getAttributeValue("maxspeed") != null ? lane.getAttributeValue("maxspeed") : "X");

			l.setSpeed(lane.getAttributeValue("speed") != null ? lane.getAttributeValue("speed") : "X");

			String line = lane.getAttributeValue("shape") != null ? lane.getAttributeValue("shape") : lane.getText();

			StringTokenizer st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				String t = st.nextToken();
				String[] result = t.split(",");

				double x = Double.parseDouble(result[0]);
				double y = Double.parseDouble(result[1]);
				l.addPoint(x, y);
				maxx = Math.max(x, maxx);
				maxy = Math.max(y, maxy);
			}

			roadNetwork.addLane(l);
		}
	}

	private void getLanes(Element root) {
		getLocationInfo(root);

		Iterator<Element> edges = root.getChildren("edge").iterator();
		while (edges.hasNext()) {
			Element edge = (Element) edges.next();
			if (edge.getChildren("lanes").size() > 0) {
				Iterator<Element> lanes = edge.getChildren("lanes").iterator();
				while (lanes.hasNext()) {
					getContent(edge, (Element) lanes.next());
				}
			} else {
				getContent(edge, edge);
			}
		}
	}

	private HashMap<String, Connection> connections_map;

	private void connect(Element root) {

		connections_map = new HashMap<String, Connection>();

		Iterator<Element> conn = root.getChildren("connection").iterator();
		while (conn.hasNext()) {
			Element connection = (Element) conn.next();

			if (connection.getAttributeValue("from") != null) {

				Connection e = null;
				if (connections_map.get(connection.getAttributeValue("from")) == null) {
					e = new Connection(connection.getAttributeValue("from"));
				} else {
					e = connections_map.get(connection.getAttributeValue("from"));
				}

				e.getNeighbors().add(connection.getAttributeValue("to"));
				connections_map.put(connection.getAttributeValue("from"), e);

			}

		}

		for (Lane l : roadNetwork.getLanes().values()) {
			if (connections_map.get(l.getEdge()) != null) {
				Vector<String> neighbors = connections_map.get(l.getEdge()).getNeighbors();
				l.setNeighbors(neighbors);
			}
		}
	}

	public boolean readScenario() throws JDOMException, NumberFormatException, IOException {
		if (path == null)
			return false;

		roadNetwork = new Network();

		SAXBuilder sax_builder = new SAXBuilder();

		Element root = sax_builder.build(path).getRootElement();

		getLanes(root);
		connect(root);

		return true;
	}
}
