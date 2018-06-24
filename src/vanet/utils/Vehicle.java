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

public class Vehicle {

	public static final boolean POSITIVE = true;

	private int id;
	private double x;
	private double y;
	private double speed;
	private String edge = null;
	private String from = null;
	private String to = null;

	private Lane lane = null;
	private int segmentIndex = 0;

	private boolean directionXPositive;
	private boolean directionYPositive;

	public Vehicle(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

	public String getEdge() {
		return this.edge;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom() {
		return this.from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return this.to;
	}

	public Lane getLane() {
		return lane;
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}

	public int getSegmentIndex() {
		return segmentIndex;
	}

	public void setSegmentIndex(int segmentIndex) {
		this.segmentIndex = segmentIndex;
	}

	public boolean isDirectionXPositive() {
		return directionXPositive;
	}

	public void setDirectionXPositive(boolean directionXPositive) {
		this.directionXPositive = directionXPositive;
	}

	public boolean isDirectionYPositive() {
		return directionYPositive;
	}

	public void setDirectionYPositive(boolean directionYPositive) {
		this.directionYPositive = directionYPositive;
	}

}
