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

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import vanet.swing.MainFrame;
import vanet.utils.Lane;
import vanet.utils.Vehicle;
import vanet.utils.TravelingNodes;

import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ReportDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> comboBox_start, comboBox_end;

	private HashMap<String, TravelingNodes> scenario;
	private String output;

	public ReportDialog(MainFrame owner) {
		super(owner);
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblTime = new JLabel("Output the report from");
		contentPanel.add(lblTime);

		comboBox_start = new JComboBox<String>();
		comboBox_start.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				int index = comboBox_start.getSelectedIndex();
				if (index != -1 && comboBox_end != null)
					try {
						comboBox_end.setSelectedIndex(index + 5);
					} catch (IllegalArgumentException e) {
						comboBox_end.setSelectedIndex(comboBox_end.getItemCount() - 1);
					}
			}
		});
		comboBox_start.setOpaque(false);
		contentPanel.add(comboBox_start);

		JLabel lblSecond = new JLabel("second to");
		contentPanel.add(lblSecond);

		comboBox_end = new JComboBox<String>();
		comboBox_end.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				int start = comboBox_start.getSelectedIndex();
				int end = comboBox_end.getSelectedIndex();
				if (end < start) {
					try {
						comboBox_end.setSelectedIndex(start + 5);
					} catch (IllegalArgumentException e) {
						comboBox_end.setSelectedIndex(comboBox_end.getItemCount() - 1);
					}
				}
			}
		});
		contentPanel.add(comboBox_end);

		JLabel lblSecond_1 = new JLabel("second");
		contentPanel.add(lblSecond_1);
	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 324, 111);
	}

	@Override
	protected void setInitialValue() {
	}

	@Override
	protected void storeValues() {
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
		this.scenario = scenario;
		this.output = scenario_path.replaceAll(".tcl", "_report_");

		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		DefaultComboBoxModel<String> model_start = (DefaultComboBoxModel<String>) comboBox_start.getModel();
		DefaultComboBoxModel<String> model_end = (DefaultComboBoxModel<String>) comboBox_end.getModel();
		for (String s : scenario.keySet()) {
			int value = Integer.parseInt(s);
			if (value < min)
				min = value;
			if (value > max)
				max = value;
		}
		for (int i = min; i <= max; i++) {
			model_start.addElement("" + i);
			model_end.addElement("" + i);
		}
		try {
			comboBox_end.setSelectedIndex(comboBox_start.getSelectedIndex() + 5);
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	protected void generate() throws IOException {

		int start = 0;
		int end = 0;
		try {
			start = Integer.parseInt(comboBox_start.getSelectedItem().toString());
			end = Integer.parseInt(comboBox_end.getSelectedItem().toString());
		} catch (NullPointerException e) {
			start = -1;
		}

		for (int value = start; value <= end; value++) {
			TravelingNodes nodes = scenario.get("" + value);

			if (nodes != null) {

				HashMap<String, ArrayList<Vehicle>> edges = new HashMap<String, ArrayList<Vehicle>>();

				for (Vehicle n : nodes.getNodes()) {

					String edge = getEdge(n.getEdge());

					ArrayList<Vehicle> list = edges.get(n.getEdge());
					if (list == null) {
						list = new ArrayList<Vehicle>();
					}
					list.add(n);
					edges.put(edge, list);
				}

				BufferedWriter bfw = new BufferedWriter(new FileWriter(output + value + ".csv"));

				int edge = 1, node = 1, all = 1;
				bfw.write("Count,Edge,Number,Id,X,Y,Speed");
				bfw.newLine();
				for (ArrayList<Vehicle> list : edges.values()) {
					for (Vehicle n : list) {
						bfw.write(edge + ", E:" + getEdge(n.getEdge()) + "," + node + "," + n.getID() + "," + n.getX()
								+ "," + n.getY() + "," + n.getSpeed());
						bfw.newLine();
						node++;
						all++;
					}
					bfw.newLine();
					node = 1;
					edge++;
				}
				bfw.write(all);
				bfw.newLine();
				bfw.flush();
				bfw.close();
			}
		}
	}

	private String getEdge(String data) {
		String edge = data;
		if (edge.startsWith("-")) {
			edge = edge.substring(1);
		}
		if (edge.startsWith(":")) {
			edge = edge.substring(1);
		}
		if (edge.indexOf("#") != -1) {
			edge = edge.substring(0, edge.indexOf("#"));
		}
		if (edge.indexOf("_") != -1) {
			edge = edge.substring(0, edge.indexOf("_"));
		}
		return edge;
	}
}
