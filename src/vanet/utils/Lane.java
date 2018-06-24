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

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Vector;

public class Lane {

	private String id, allow, speed, edge, from, to;

	private double length;

	private LinkedList<Point2D.Double> points;

	private Vector<String> neighbors;

	private Vector<String> lanes;

	public final static int INCREASE = 1;
	public final static int DECREASE = -1;

	public Lane(String id) {
		this.id = id;
		points = new LinkedList<Point2D.Double>();

		lanes = new Vector<String>();
	}

	public String getId() {
		return this.id;
	}

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
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

	public void setLength(String l) {
		this.length = Double.parseDouble(l);
	}

	public double getLength() {
		return this.length;
	}

	public double getLength(int segment) {
		return getSegmentFrom(segment).distance(getSegmentTo(segment));
	}

	public void setSpeed(String s) {
		this.speed = s;
	}

	public String getSpeed() {
		return this.speed;
	}

	public void addPoint(double x, double y) {
		points.add(new Point2D.Double(x, y));
	}

	public int getPointCount() {
		return points.size();
	}

	public Point2D getLineStart() {
		return points.getFirst();
	}

	public Point2D getLineEnd() {
		return points.getLast();
	}

	public double getLineStartX() {
		return points.getFirst().x;
	}

	public double getLineStartY() {
		return points.getFirst().y;
	}

	public double getLineEndX() {
		return points.getLast().x;
	}

	public double getLineEndY() {
		return points.getLast().y;
	}

	public Point2D getSegmentFrom(int i) {
		return points.get(i);
	}

	public Point2D getSegmentTo(int i) {
		return points.get(i + 1);
	}

	public double getSegmentFromX(int i) {
		return points.get(i).x;
	}

	public double getSegmentFromY(int i) {
		return points.get(i).y;
	}

	public double getSegmentToX(int i) {
		return points.get(i + 1).x;
	}

	public double getSegmentToY(int i) {
		return points.get(i + 1).y;
	}

	public double getAngleFromEndStart() {
		return Math.atan((this.getLineEndY() - this.getLineStartY()) / (this.getLineEndX() - this.getLineStartX()));
	}

	public Vector<String> getLanes() {
		return lanes;
	}

	public void setNeighbors(Vector<String> neighbors) {
		this.neighbors = neighbors;
	}

	public Vector<String> getNeighbors() {
		return neighbors;
	}

	public int getSegmentSize() {
		return points.size() - 1;
	}

	public boolean isTheLastSegment(int i) {
		if (i == 0 || i == getSegmentSize() - 1)
			return true;
		return false;
	}

	public String toString() {
		String s = "Lane ";
		s += this.getId() + "(" + this.getEdge() + ") => ";
		s += "\tFrom( " + this.getLineStartX() + ", ";
		s += "" + this.getLineStartY() + ") ";
		s += "\tTo( " + this.getLineEndX() + ", ";
		s += "" + this.getLineEndY() + ") ";
		s += "\tLength( " + this.getLength() + ") ";
		s += "\tAngle( " + this.getAngleFromEndStart() + ") ";
		s += "Neighbors: " + this.getNeighbors();
		return s;
	}

	public String output() {
		String s = "Lane ";
		s += this.getEdge() + " ";
		s += this.getLineStartX() + " ";
		s += this.getLineStartY() + " ";
		s += this.getLineEndX() + " ";
		s += this.getLineEndY() + " ";
		s += this.getLength() + " ";
		s += this.from + " ";
		s += this.to + " ";
		s += this.getAngleFromEndStart() + " ";
		s += "Lanes: " + this.getNeighbors();
		return s;
	}
}
