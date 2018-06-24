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
package vanet.utils.io;

public abstract class AbstractReader {

	protected String path;
	protected double maxx;
	protected double maxy;

	protected AbstractReader(String path) {
		this.path = path;
		maxx = 0;
		maxy = 0;
	}

	public double getMaxX() {
		return maxx;
	}

	public double getMaxY() {
		return maxy;
	}

	public abstract boolean readScenario() throws Exception;
}
