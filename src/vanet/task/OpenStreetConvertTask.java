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

import vanet.swing.MainFrame;
import vanet.task.cmd.CommandProcessor;
import vanet.utils.Config;

public class OpenStreetConvertTask extends SwingWorker<Void, Void> {

	private MainFrame frame;
	private String path, output;

	public OpenStreetConvertTask(MainFrame frame, String path, String output) {
		this.frame = frame;
		this.path = path;
		this.output = output;
	}

	@Override
	protected Void doInBackground() throws Exception {

		frame.changeStatusBar("Convert Map File...");
		Config c = new Config();

		// netconvert --osm-files map.osm.xml -o map.net.xml
		if (System.getProperty("os.name").startsWith("Windows")) {
			String command = "\"" + c.getSUMOPath() + System.getProperty("file.separator") + "netconvert.exe" + "\"";
			command += " --osm-files " + "\"" + path + "\"";
			command += " -o " + "\"" + output + "\"";
			command += " --remove-edges.isolated";
			CommandProcessor.executeCommand(command);
		} else {
			String command = c.getSUMOPath() + System.getProperty("file.separator") + "netconvert";
			command += " --osm-files " + path;
			command += " -o " + output;

			CommandProcessor.executeCommand(command);
		}

		return null;
	}

	public void done() {
		frame.stopBar("Done");
	}
}