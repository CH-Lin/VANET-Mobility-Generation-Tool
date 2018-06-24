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

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import vanet.utils.Lane;

import java.io.*;
import java.util.*;

public class MobilityGenerator {

	private Hashtable<String, Lane> lanes;
	private Hashtable<Integer, NSMobilityNodePath> nodes;
	private Vector<String> vehiclesId;
	private Hashtable<String, Vehicle> vehicles;
	private Vector<TimestepRegister> lastVehicleReg;
	private Vector<NSMobilityNode> posVector;
	private Vector<NSMobilityNode> movVector;
	private double xMin = 0.0;
	private double yMin = 0.0;
	private double xBoundary = 0.0;
	private double yBoundary = 0.0;
	private int vehicleCounter = 0;

	public MobilityGenerator() {
		lanes = new Hashtable<String, Lane>();
		nodes = new Hashtable<Integer, NSMobilityNodePath>();
		vehiclesId = new Vector<String>();
		posVector = new Vector<NSMobilityNode>();
		movVector = new Vector<NSMobilityNode>();
		vehicles = new Hashtable<String, Vehicle>();
		lastVehicleReg = new Vector<TimestepRegister>(2);
	}

	public int getVehicleCounter() {
		return vehicleCounter;
	}

	public double getXBoundary() {
		return xBoundary;
	}

	public double getYBoundary() {
		return yBoundary;
	}

	private void parseDumpFile(String fileName) throws JDOMException, IOException {
		System.out.println("MobilityGenerator.parseOutputDocument(): " + fileName);

		SAXBuilder builder = new SAXBuilder();

		Document inputDocument = builder.build(new File(fileName));

		Element root = inputDocument.getRootElement();

		Iterator<Element> timesteps = root.getChildren("timestep").iterator();

		TimestepRegister ts;

		while (timesteps.hasNext()) {

			Element timestep = (Element) timesteps.next();

			String time = timestep.getAttributeValue("time");

			Iterator<Element> edges = timestep.getChildren("edge").iterator();

			while (edges.hasNext()) {
				Element edge = (Element) edges.next();
				Iterator<Element> lanes = edge.getChildren("lane").iterator();

				while (lanes.hasNext()) {
					Element lane = (Element) lanes.next();
					String laneID = lane.getAttributeValue("id");

					Iterator<Element> vehicles = lane.getChildren("vehicle").iterator();
					while (vehicles.hasNext()) {
						Element vehicle = (Element) vehicles.next();

						ts = new TimestepRegister();
						ts.setTimestepId(time);
						ts.setLaneId(laneID);

						ts.setVehicleId(vehicle.getAttributeValue("id"));
						ts.setVehiclePos(Double.parseDouble(vehicle.getAttributeValue("pos")));
						ts.setVehicleSpeed(vehicle.getAttributeValue("speed"));

						extractNodeData(ts);
					}

				}

			}
		}
	}

	private void extractNodeData(TimestepRegister ts) {

		Lane lane = ((Lane) lanes.get(ts.getLaneId()));

		double a = (lane.getLineEndY() - lane.getLineStartY())
				/ Math.sqrt(Math.pow((lane.getLineEndY() - lane.getLineStartY()), 2)
						+ Math.pow((lane.getLineEndX() - lane.getLineStartX()), 2));
		double b = (lane.getLineEndY() - lane.getLineStartY()) / (lane.getLineEndX() - lane.getLineStartX());

		double x = ((a * ts.getVehiclePos()) + (b * lane.getLineStartX())) / b;
		double y = (a * ts.getVehiclePos()) + lane.getLineStartY();

		if (lane.getLineStartY() == lane.getLineEndY()) {
			y = lane.getLineStartY();
			if (lane.getLineEndX() > lane.getLineStartX())
				x = lane.getLineStartX() + ts.getVehiclePos();
			else
				x = lane.getLineStartX() - ts.getVehiclePos();
		}
		if (lane.getLineStartX() == lane.getLineEndX()) {
			x = lane.getLineStartX();
			if (lane.getLineEndY() > lane.getLineStartY())
				y = lane.getLineStartY() + ts.getVehiclePos();
			else
				y = lane.getLineStartY() - ts.getVehiclePos();
		}

		int nsNode = -1;
		if (vehicles.get(ts.getVehicleId()) != null)
			nsNode = vehicles.get(ts.getVehicleId()).getNsNode();

		if (!vehiclesId.contains(ts.getVehicleId())) {
			if (nsNode > -1) {

				if (nsNode > vehiclesId.size() - 1) {
					int size = nsNode + 1;
					vehiclesId.setSize(size);
				}
				vehiclesId.setElementAt(ts.getVehicleId(), nsNode);
			} else {
				vehiclesId.setSize(vehicleCounter + 1);
				vehiclesId.setElementAt(ts.getVehicleId(), vehicleCounter);
				vehicleCounter = vehiclesId.size();
			}

			Lane l = lanes.get(ts.getLaneId());
			NSMobilityNode nodeInitial = new NSMobilityNode(-1, vehiclesId.indexOf(ts.getVehicleId()), x, y, 0.0,
					ts.getLaneId(), l.getEdge(), l.getFrom(), l.getTo());
			NSMobilityNode nodeInitial0 = new NSMobilityNode(0, vehiclesId.indexOf(ts.getVehicleId()), x, y, 0.0,
					ts.getLaneId(), l.getEdge(), l.getFrom(), l.getTo());
			posVector.addElement(nodeInitial);
			movVector.insertElementAt(nodeInitial0, 0);
		}

		nsNode = vehiclesId.indexOf(ts.getVehicleId());
		if (nsNode >= lastVehicleReg.size())
			lastVehicleReg.setSize(lastVehicleReg.size() + 1);
		lastVehicleReg.setElementAt(ts, nsNode);

		if (vehicles.containsKey(ts.getVehicleId())) {
			vehicles.get(ts.getVehicleId()).setNsNode(nsNode);
			vehicles.get(ts.getVehicleId()).setLoopLane(ts.getLaneId());
			vehicles.get(ts.getVehicleId()).setLoopPosition(ts.getVehiclePos());
		}

		int node = vehiclesId.indexOf(ts.getVehicleId());

		Lane l = lanes.get(ts.getLaneId());
		NSMobilityNode nodeMoving = new NSMobilityNode((int) Double.parseDouble(ts.getTimestepId()), node, x, y,
				Double.parseDouble(ts.getVehicleSpeed()), ts.getLaneId(), l.getEdge(), l.getFrom(), l.getTo());
		movVector.addElement(nodeMoving);

		if (x < xMin)
			xMin = x;
		if (y < yMin)
			yMin = y;
		if (x > xBoundary)
			xBoundary = x;
		if (y > yBoundary)
			yBoundary = y;
	}

	private void translatePositions() {
		if (xMin < 0.0 || yMin < 0.0) {

			for (NSMobilityNode node : posVector) {
				node.setX(node.getX() - (xMin - 10));
				node.setY(node.getY() - (yMin - 10));
			}

			for (NSMobilityNode node : movVector) {
				node.setX(node.getX() - (xMin - 10));
				node.setY(node.getY() - (yMin - 10));
			}
		}
	}

	private boolean writeNS2InputFile(String nsInputFile) {
		boolean ns2InputCreated = true;
		System.out.println("MobilityGenerator.writeNS2InputFile(): NS2 Scenario Boundaries are (" + xBoundary + ","
				+ yBoundary + ")");
		translatePositions();

		try {
			StringTokenizer st = new StringTokenizer(nsInputFile, "\"");
			// Create the buffer for the output file
			BufferedWriter ns2Input = new BufferedWriter(new FileWriter(st.nextToken()));

			// Write each entry from the .pos vector to the NS2 Input file
			for (NSMobilityNode node : posVector) {
				ns2Input.write(node.toString());
				ns2Input.newLine();
			}
			// Write each entry from the .mov vector to the NS2 Input file
			for (NSMobilityNode node : movVector) {
				ns2Input.write(node.toString());
				ns2Input.newLine();

				NSMobilityNodePath mnode = null;
				if (nodes.containsKey(node.getId())) {
					mnode = nodes.get(node.getId());
				} else {
					mnode = new NSMobilityNodePath();
					nodes.put(node.getId(), mnode);
				}
				mnode.add(node);
			}
			ns2Input.flush();
			ns2Input.close();

			String sbf = nsInputFile.replaceAll(".tcl", ".map");

			BufferedWriter ns2MapInput = new BufferedWriter(new FileWriter(sbf.toString()));
			System.out.println("MobilityGenerator.writeNS2InputFile(): Write map file " + sbf.toString());

			double length = 0;
			HashMap<String, String> map = new HashMap<String, String>();
			for (Lane l : lanes.values()) {
				length += l.getLength();
				if (!l.getFrom().equalsIgnoreCase("-"))
					map.put(l.getFrom(), l.getFrom());
				if (!l.getTo().equalsIgnoreCase("-"))
					map.put(l.getTo(), l.getTo());
			}
			ns2MapInput.write("JUNCTION " + map.size());
			ns2MapInput.newLine();
			ns2MapInput.write("LENGTH " + length);
			ns2MapInput.newLine();
			double density = nodes.size() / (length / 100);
			ns2MapInput.write("DENSITY " + density);
			ns2MapInput.newLine();

			for (Lane l : lanes.values()) {
				ns2MapInput.write(l.output());
				ns2MapInput.newLine();
			}

			for (NSMobilityNodePath mnode : nodes.values()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Node ");
				sb.append(mnode.getID());
				sb.append(" Path information:");
				sb.append(mnode);
				ns2MapInput.write(sb.toString());
				ns2MapInput.newLine();
			}

			ns2MapInput.flush();
			ns2MapInput.close();

			ns2InputCreated = true;
		} catch (IOException ioe) {
			ns2InputCreated = false;
		}
		return ns2InputCreated;
	}

	public boolean generateMobilityTraces(String dumpFile) {
		try {
			parseDumpFile(dumpFile);
			return writeNS2InputFile(dumpFile.replaceAll(".dump.xml", ".tcl"));
		} catch (JDOMException jde) {
			jde.printStackTrace();
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return false;
	}

	public void setLanes(Hashtable<String, Lane> lanes) {
		this.lanes = lanes;
	}

	public boolean parseRoutesFile(String rouFile) throws JDOMException, IOException {
		boolean valid = true;

		SAXBuilder builder = new SAXBuilder();
		Document inputDocument = builder.build(new File(rouFile));

		Element root = inputDocument.getRootElement();

		Iterator<Element> i = root.getChildren("vehicle").iterator();
		Vehicle vehicle;

		while (i.hasNext()) {
			Element e = (Element) i.next();
			String vehicleId = e.getAttributeValue("id");

			if (!vehicles.containsKey(vehicleId)) {

				vehicle = new Vehicle();
				vehicle.setVehicleId(vehicleId);
				vehicle.setType(e.getAttributeValue("type"));

			} else {

				vehicle = vehicles.get(vehicleId);
				vehicle.getRoutes().removeAllElements();
			}

			String route = new String();

			if (e.getChild("route") != null)
				route = e.getChild("route").getAttributeValue("edges");

			StringTokenizer st = new StringTokenizer(route);
			while (st.hasMoreTokens()) {
				vehicle.addRoutes(st.nextToken());
			}

			if (!vehicles.containsKey(vehicleId))
				vehicles.put(vehicle.getVehicleId(), vehicle);
		}

		return valid;
	}
}