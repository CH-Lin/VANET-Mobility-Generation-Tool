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
package vanet.task;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.SwingWorker;

import vanet.swing.MainFrame;
import vanet.utils.Lane;
import vanet.utils.Vehicle;

public class AutonomousTask extends SwingWorker<Void, Void> {

	private int numberOfVehicle, maxSpeed, minSpeed, begin, end;
	private MainFrame frame;

	private HashMap<String, Lane> lanes;

	private LinkedList<Vehicle> vehicles;

	public AutonomousTask(MainFrame frame, int numberOfVehicle, int maxSpeed, int minSpeed, int begin, int end,
			HashMap<String, Lane> lanes) {
		this.frame = frame;
		this.numberOfVehicle = numberOfVehicle;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.begin = begin;
		this.end = end;
		this.lanes = lanes;
		this.vehicles = new LinkedList<Vehicle>();
	}

	@Override
	protected Void doInBackground() throws Exception {

		this.createVehicles();
		for (int i = begin; i < end; i++) {
			this.uodatePosition();
			this.environmentDetection();

		}

		return null;
	}

	private void createVehicles() {
		for (int i = 0; i < numberOfVehicle; i++) {
			Vehicle v = new Vehicle(i);

			while (true) {
				// initial position
				int index = (int) (Math.random() * lanes.values().size());
				Lane lane = lanes.values().toArray(new Lane[0])[index];
				int segment = (int) (Math.random() * lane.getSegmentSize());
				v.setSegmentIndex(segment);
				v.setX(lane.getSegmentFromX(segment));
				v.setY(lane.getSegmentFromY(segment));
				if (!lane.getLineStart().equals(lane.getLineEnd())) {
					v.setLane(lane);
					break;
				}
			}

			// initial direction
			if (Math.random() > 0.5)
				v.setDirectionXPositive(Vehicle.POSITIVE);
			else
				v.setDirectionXPositive(!Vehicle.POSITIVE);

			if (Math.random() > 0.5)
				v.setDirectionYPositive(Vehicle.POSITIVE);
			else
				v.setDirectionYPositive(!Vehicle.POSITIVE);

			double speed = minSpeed + Math.random() * (maxSpeed - minSpeed);
			v.setSpeed(speed);

			vehicles.add(v);
		}
	}

	private void uodatePosition() {

		for (Vehicle v : vehicles) {

			double speed = v.getSpeed(); // moving distance
			int segment = v.getSegmentIndex();
			double x = v.getX();
			double y = v.getY();

			Lane current = v.getLane();
			System.out.println(current.getId() + "\t" + v.getSegmentIndex() + "\t" + current.getAngleFromEndStart()
					+ "\t" + current.getSegmentFrom(v.getSegmentIndex()) + "\t"
					+ current.getSegmentTo(v.getSegmentIndex()));

			Point2D p1 = current.getSegmentFrom(segment);
			Point2D p2 = current.getSegmentTo(segment);

			double movingDistance = 0;

			if (p1.getX() < p2.getX()) { // X direction

			} else if (p1.getX() == p2.getX()) { // Y direction

			}
		}

	}

	public boolean idIntheRange() {
		return true;
	}

	public Lane findConnecttedLane(int x, int y, Lane current) {
		Lane next = null;

		return next;
	}

	private boolean transmissionRangeDetection(Vehicle v1, Vehicle v2) {
		Point p1 = new Point((int) v1.getX(), (int) v1.getY());
		Point p2 = new Point((int) v2.getX(), (int) v2.getY());
		if (p1.distance(p2) <= 50)
			return true;
		else
			return false;
	}

	private void environmentDetection() {

	}

	public void done() {
		frame.stopBar("Done");
	}
}
