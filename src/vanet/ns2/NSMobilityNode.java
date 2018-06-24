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

import java.text.DecimalFormat;

public class NSMobilityNode {
	private int id;
	private int timestep;
	private double x;
	private double y;
	private double speed;
	private String color;
	public String laneId;
	public String edgeId;
	public String from;
	public String to;

	public NSMobilityNode() {
		this.color = ColorCode.BLACK;
	}

	public NSMobilityNode(int timestep, int id, double x, double y, double speed, String laneId, String edgeId,
			String from, String to) {
		this.timestep = timestep;
		this.id = id;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.color = ColorCode.BLACK;
		this.laneId = laneId;
		this.edgeId = edgeId;
		this.from = from;
		this.to = to;
		// this.laneId = laneId;
		// this.laneId = laneId.replaceAll("_0", "").replaceAll(":", "")
		// .replaceAll("_", ".").replaceAll("#", ".");
		// if (this.laneId.indexOf('.') != this.laneId.lastIndexOf('.')) {
		// String temp = this.laneId
		// .substring(this.laneId.lastIndexOf(".") + 1);
		// this.laneId = this.laneId
		// .substring(0, this.laneId.lastIndexOf("."));
		// this.laneId += temp;
		// }
	}

	public int getTimestep() {
		return timestep;
	}

	public int getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getFormattedX() {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(x);
	}

	public String getFormattedY() {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(y);
	}

	public double getSpeed() {
		return speed;
	}

	public String getFormattedSpeed() {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(speed);
	}

	public String getColor() {
		return color;
	}

	public void setTimestep(int timestep) {
		this.timestep = timestep;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		StringBuffer output = new StringBuffer();
		if (this.getTimestep() == -1) {
			output.append("$node_(" + this.getId() + ") set " + "X_ " + this.getFormattedX() + "\n"); // X register
			output.append("$node_(" + this.getId() + ") set " + "Y_ " + this.getFormattedY() + "\n"); // Y register
			output.append("$node_(" + this.getId() + ") set " + "Z_ 0.0000000");
			// Z register (this is always 0. In vehicular networks, movement is
			// in 2D)
		} else {
			output.append("$ns_ at ");
			output.append(this.getTimestep());
			output.append(" \"$node_(");
			output.append(this.getId());
			output.append(") setdest-path ");
			output.append(this.getX() + " ");
			output.append(this.getY() + " ");
			output.append(this.getSpeed() + " ");
			// output.append(this.laneId + " ");
			output.append(this.edgeId + " ");
			output.append(this.getFrom() + " ");
			output.append(this.getTo() + "\"");
		}

		/*
		 * output.append("Node(" + this.getId() + "): " ); output.append(" at " +
		 * getTimestep()); output.append(" is " + getX()); output.append("," + getY());
		 * output.append(" color: " + getColor());
		 */

		return output.toString();
	}

}
