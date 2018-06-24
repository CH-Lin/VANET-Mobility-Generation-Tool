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

public class TimestepRegister {
	private String timestepId;
	private String laneId;
	private String vehicleId;

	private String vehicleSpeed;
	private double vehiclePos;

	public TimestepRegister() {
	}

	public void setTimestepId(String timestepId) {
		this.timestepId = timestepId;
	}

	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public void setVehiclePos(double vehiclePos) {
		this.vehiclePos = vehiclePos;
	}

	public void setVehicleSpeed(String vehicleSpeed) {
		this.vehicleSpeed = vehicleSpeed;
	}

	public String getTimestepId() {
		return timestepId;
	}

	public String getLaneId() {
		return laneId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public double getVehiclePos() {
		return vehiclePos;
	}

	public String getVehicleSpeed() {
		return vehicleSpeed;
	}

	public String toString() {
		String s = "\nTimestep ";
		s += this.getTimestepId() + ": ";
		s += "Lane => " + this.getLaneId() + ", ";
		s += "Vehicle => " + this.getVehicleId();
		s += " ( " + this.getVehiclePos();
		s += ", " + this.getVehicleSpeed() + ")";
		return s;
	}
}
