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
package vanet.swing.dialog;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTextField;

import vanet.swing.MainFrame;
import vanet.task.LocServerTask;
import vanet.utils.Config;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;

public class LocServerDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	private JTextField JT_NUM;
	private TravelingNodes nodes;
	private String output;

	public LocServerDialog(MainFrame owner) {
		super(owner);
	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 324, 110);
		{
			JLabel JL_NUM = new JLabel("Number of server:");
			JL_NUM.setBounds(10, 10, 90, 15);
			contentPanel.add(JL_NUM);
		}
		{
			JT_NUM = new JTextField();
			JT_NUM.setBounds(110, 7, 186, 21);
			contentPanel.add(JT_NUM);
			JT_NUM.setColumns(10);
		}
	}

	@Override
	protected void setInitialValue() {
		Config c = new Config();
		JT_NUM.setText(c.getNumOfLoc());
	}

	@Override
	protected void storeValues() {
		Config c = new Config();
		c.setNumOfLoc(JT_NUM.getText());
	}

	public void generate() {
		new LocServerTask(frame, output, JT_NUM.getText(), nodes).execute();
		frame.changeStatusBar("Generating Location Server...");
		frame.startBar("Generating Location Server...");
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
		nodes = scenario.get("-1");
		output = scenario_path.replaceAll(".tcl", "_loc.tcl");
	}

}
