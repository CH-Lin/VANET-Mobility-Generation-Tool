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
package vanet.swing;

public class NetworkMapping {

	private double scale = 1;

	private double offsetX, offsetY;

	public void setScale(double scale) {
		this.scale = scale;
	}

	public void adjOffset(double offsetX, double offsetY) {
		this.offsetX += offsetX;
		this.offsetY += offsetY;
	}

	public int projToMapX(double x) {
		return (int) ((x - offsetX) / scale);
	}

	public int projToMapY(double y) {
		return (int) ((y - offsetY) / scale);
	}

	public int projToRealX(double x) {
		return (int) (x * scale + offsetX);
	}

	public int projToRealY(double y) {
		return (int) (y * scale + offsetY);
	}

	public double getScale() {
		return scale;
	}
}
