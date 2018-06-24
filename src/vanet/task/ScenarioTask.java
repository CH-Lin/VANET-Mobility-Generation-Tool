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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.SwingWorker;

import vanet.ns2.*;
import vanet.swing.MainFrame;
import vanet.task.cmd.CommandProcessor;
import vanet.utils.Lane;

public class ScenarioTask extends SwingWorker<Void, Void> {

	private MobilityGenerator mobilityGenerator;

	private String exe;
	private String netfile, routefile;
	private int begin, end;
	private MainFrame frame;

	private HashMap<String, Lane> lanes;

	public ScenarioTask(MainFrame frame, String exe, String netfile, String routefile, int begin, int end,
			HashMap<String, Lane> lanes) {
		this.frame = frame;
		this.exe = exe;
		this.netfile = netfile;
		this.routefile = routefile;
		this.begin = begin;
		this.end = end;
		this.lanes = lanes;

		mobilityGenerator = new MobilityGenerator();
	}

	@Override
	protected Void doInBackground() throws Exception {

		frame.changeStatusBar("Do Simulation...");

		if (System.getProperty("os.name").startsWith("Windows")) {
			String command = "\"" + exe + "\"";
			command += " --net-file " + "\"" + netfile + "\"";
			command += " --route-files " + "\"" + routefile + "\"";
			command += " --netstate-dump " + "\"" + netfile.replaceAll(".net", ".dump") + "\"";
			command += " -b " + begin;
			command += " -e " + end;

			CommandProcessor.executeCommand(command);
		} else {
			String command = exe;
			command += " --net-file " + netfile;
			command += " --route-files " + routefile;
			command += " --netstate-dump " + netfile.replaceAll(".net", ".dump");
			command += " -b " + begin;
			command += " -e " + end;

			CommandProcessor.executeCommand(command);
		}

		frame.changeStatusBar("Generating TCL File...");

		if (mobilityGenerator.parseRoutesFile(routefile)) {

			mobilityGenerator.setLanes(new Hashtable<String, Lane>(lanes));

			if (mobilityGenerator.generateMobilityTraces(netfile.replaceAll(".net", ".dump"))) {
				System.out.println("Mobility Traces File (NS2 Input) was created succesfuly. Produced File: "
						+ netfile.replaceAll(".net.xml", ".tcl"));

				StringBuffer scenario = new StringBuffer();
				// Mobility Scenario
				scenario.append("Before runing ns2 do not forget to change the values of the");
				scenario.append("following variables in the ./mobility_traces/test.tcl file \n\n");
				scenario.append("set opt(nn)\t\t" + mobilityGenerator.getVehicleCounter() + "\t\t;# number of nodes\n");
				scenario.append("set opt(x)\t\t" + Math.ceil(mobilityGenerator.getXBoundary())
						+ "\t\t;# the width (x-axis) of the simulation\n");
				scenario.append("set opt(y)\t\t" + Math.ceil(mobilityGenerator.getYBoundary())
						+ "\t\t;# the width (x-axis) of the simulation\n");
				scenario.append("set opt(stop)\t\t" + end + "\t\t;# simulation end time\n");
				scenario.append("set opt(sc)\t\t" + netfile.replaceAll(".net.xml", ".tcl") + " \n");

				scenario.append("set opt(map)\t\t" + netfile.replaceAll(".net.xml", ".map") + " \n");
				String line = System.getProperty("line.separator");
				BufferedWriter info = new BufferedWriter(new FileWriter(netfile.replaceAll(".net.xml", ".txt")));
				info.write(scenario.toString().replaceAll("\n", line));
				info.close();

			} else {
				System.out.println("An error was produced while creating the NS2 Input File.");
			}
		}

		return null;
	}

	public void done() {
		frame.stopBar("Done");
		frame.readScenario(netfile.replaceAll(".net.xml", ".tcl"));
	}
}
