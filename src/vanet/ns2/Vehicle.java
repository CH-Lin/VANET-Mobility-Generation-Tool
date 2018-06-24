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

public class Vehicle {
	private Vector<String> routes = null;

	private String vehicleId = null;
	private String routeId = null;

	private String type = null;
	private int nsNode = -1;

	private double loopPosition = 0.0;
	private String loopLane = null;

	private boolean stop = false;
	private double stopPosition = 0.0;
	private int duration = 0;
	private String stopLane = null;

	public Vehicle() {
		routes = new Vector<String>();
	}

	public Vector<String> getRoutes() {
		return routes;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public int getNsNode() {
		return nsNode;
	}

	public String getRouteId() {
		return routeId;
	}

	public String getRouteString() {
		String route = "";
		for (String r : routes) {
			route += r + " ";
		}
		return route;
	}

	public boolean isStop() {
		return stop;
	}

	public double getLoopPosition() {
		return loopPosition;
	}

	public String getLoopLane() {
		return loopLane;
	}

	public String getStopLane() {
		return stopLane;
	}

	public double getStopPosition() {
		return stopPosition;
	}

	public int getDuration() {
		return duration;
	}

	public String getType() {
		return type;
	}

	public void setRoutes(Vector<String> routes) {
		this.routes = routes;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public void setNsNode(int nsNode) {
		this.nsNode = nsNode;
	}

	public void setStopPosition(double stopPosition) {
		this.stopPosition = stopPosition;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void setStopLane(String stopLane) {
		this.stopLane = stopLane;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLoopPosition(double loopPosition) {
		this.loopPosition = loopPosition;
	}

	public void setLoopLane(String loopLane) {
		this.loopLane = loopLane;
	}

	public void addRoutes(String newRoute) {
		// routes += "," + newRoute;
		routes.addElement(newRoute);
	}

	public String toString() {
		String s = new String();

		s += "\nVehicle " + this.getVehicleId();
		s += "(NS Node: " + this.getNsNode() + ") ";
		s += "\n\tPos: " + this.getLoopPosition();
		s += "\n\tLane: " + this.getLoopLane();
		// s += "\n\tDuration: " + this.getDuration();
		// s += "\n\tPosition: " + this.getStopPosition();
		// s += "\n\tRoutes: " + this.getRoutes();

		return s;
	}
}
