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

import javax.swing.SwingWorker;

import vanet.swing.MainFrame;
import vanet.task.cmd.CommandProcessor;
import vanet.utils.Lane;

public class FlowTask extends SwingWorker<Void, Void> {

	private String exe;
	private String netfile;
	private HashMap<String, Lane> ls;
	private int num, begin, end, max;

	private MainFrame frame;

	public FlowTask(MainFrame owner, String exe, String netfile, HashMap<String, Lane> ls, int num, int begin, int end,
			int max) {
		this.frame = owner;
		this.exe = exe;
		this.netfile = netfile;
		this.ls = ls;
		this.num = num;
		this.begin = begin;
		this.end = end;
		this.max = max;
	}

	@Override
	protected Void doInBackground() throws Exception {

		frame.changeStatusBar("Generating Flow File...");

		int size = ls.size();
		if (size > 0) {
			String flowpath = netfile.replaceAll(".net", ".flows");
			BufferedWriter bw = new BufferedWriter(new FileWriter(flowpath));
			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<flowdefs>\n");

			Lane lanes[] = ls.values().toArray(new Lane[0]);

			for (int i = 0; i < num; i++) {
				int choose = (int) (Math.random() * size);
				int choose2 = choose;
				while (choose2 == choose) {
					choose2 = (int) (Math.random() * size);
				}

				String from = lanes[choose].getEdge();
				String to = lanes[choose2].getEdge();

				if (from.startsWith(":") || to.startsWith(":")) {
					i--;
					continue;
				}

				bw.write("  <flow id=\"R" + i + "\" from=\"" + from + "\" to=\"" + to + "\" begin=\"" + begin
						+ "\" end=\"" + end + "\" no=\"" + (10 + (int) (Math.random() * (max - 10))) + "\" />\n");

			}
			bw.write("</flowdefs>\n");
			bw.close();

			if (System.getProperty("os.name").startsWith("Windows")) {
				String command = "\"" + exe + "\"";
				command += " -f " + "\"" + flowpath + "\"";
				command += " -n " + "\"" + netfile + "\"";
				command += " -o " + "\"" + netfile.replaceAll(".net", ".rou") + "\"";
				command += " -b " + begin;
				command += " -e " + end;

				CommandProcessor.executeCommand(command);
			} else {
				String command = exe;
				command += " -f " + flowpath;
				command += " -n " + netfile;
				command += " -o " + netfile.replaceAll(".net", ".rou");
				command += " -b " + begin;
				command += " -e " + end;

				CommandProcessor.executeCommand(command);
			}
		}
		return null;
	}

	public void done() {
		frame.stopBar("Done");
	}
}
