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
import java.io.IOException;
import java.util.HashMap;

import javax.swing.SwingWorker;

import vanet.swing.MainFrame;
import vanet.utils.Vehicle;
import vanet.utils.TravelingNodes;

public class LocServerTask extends SwingWorker<Void, Void> {

	private MainFrame frame;

	private TravelingNodes nodes;
	private String output;
	private String num;

	public LocServerTask(MainFrame owner, String output, String num, TravelingNodes nodes) {
		this.frame = owner;
		this.num = num;
		this.nodes = nodes;
		this.output = output;
	}

	@Override
	protected Void doInBackground() throws Exception {
		frame.changeStatusBar("Generating Location Server...");

		Vehicle node[] = nodes.getNodes();
		HashMap<Integer, Vehicle> random = new HashMap<Integer, Vehicle>();

		int total = node.length, num;
		try {
			num = Integer.parseInt(this.num);
		} catch (NumberFormatException e) {
			num = 10;
		}

		for (int i = 0; i < num; i++) {
			int index = (int) (Math.random() * total);
			Vehicle tem = random.put(index, node[index]);
			if (tem != null) {
				i--;
			}
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));

			for (Vehicle n : random.values()) {
				StringBuffer o = new StringBuffer();
				o.append("$node_(" + total + ") set " + "X_ " + n.getX() + "\n"); // X
																					// register
				o.append("$node_(" + total + ") set " + "Y_ " + n.getY() + "\n"); // Y
																					// register
				o.append("$node_(" + total + ") set " + "Z_ 0.0000000\n"); // Z
				o.append("$ns_ at 0.00002 \"$ragent_(" + total + ") locserver\"");
				bw.write(o + "\n\n");
				bw.flush();
				total++;
			}
			bw.close();

			bw = new BufferedWriter(new FileWriter(output.replaceAll("_loc.tcl", "_loc_conf.tcl")));
			bw.write("set opt(loc_n) 10\n\n");
			bw.flush();
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public void done() {
		frame.stopBar("Done");
	}
}
