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

import javax.swing.SwingWorker;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import vanet.swing.MainFrame;
import vanet.utils.WebFileDownload;

public class OpenStreetDLTask extends SwingWorker<Void, Void> {

	private MainFrame frame;

	private String path;
	private GeoPosition g1, g2;

	public OpenStreetDLTask(MainFrame frame, String path, GeoPosition g1, GeoPosition g2) {

		this.frame = frame;
		this.path = path;
		this.g1 = g1;
		this.g2 = g2;
	}

	@Override
	protected Void doInBackground() throws Exception {

		frame.changeStatusBar("Getting Map File...");

		WebFileDownload dl = new WebFileDownload();

		String link = "http://www.overpass-api.de/api/xapi?map?bbox=";
		link += g1.getLongitude() + ",";
		link += g1.getLatitude() + ",";
		link += g2.getLongitude() + ",";
		link += g2.getLatitude();

		try {
			dl.DLFile(link, path, WebFileDownload.overwrite);
		} catch (Exception e) {
			System.out.println("Process error while downloading map");
			throw e;
		}

		return null;
	}

	public void done() {
		frame.stopBar("Done");
	}
}