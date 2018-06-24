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

import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTextField;

import vanet.swing.MainFrame;
import vanet.task.ScenarioTask;
import vanet.utils.Config;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;
import vanet.utils.io.FileSelectionTool;

public class ScenarioDialog extends AbstractDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField JT_Network;
	private JTextField JT_Route;
	private JTextField JTBegin;
	private JTextField JTEnd;

	private HashMap<String, Lane> lanes;

	public ScenarioDialog(MainFrame owner) {
		super(owner);
	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 381, 169);
		{
			JLabel JLNetworkFile = new JLabel("Network File:");
			JLNetworkFile.setBounds(10, 10, 63, 15);
			contentPanel.add(JLNetworkFile);
		}
		{
			JT_Network = new JTextField();
			JT_Network.setBounds(83, 7, 272, 21);
			JT_Network.setEditable(false);
			contentPanel.add(JT_Network);
			JT_Network.setColumns(10);
		}
		{
			JT_Route = new JTextField();
			JT_Route.setBounds(83, 38, 239, 21);
			contentPanel.add(JT_Route);
			JT_Route.setColumns(10);
		}
		{
			JLabel JLRouteFile = new JLabel("Route File:");
			JLRouteFile.setBounds(10, 41, 63, 15);
			contentPanel.add(JLRouteFile);
		}
		{
			JButton JB_Flow = new JButton("...");
			JB_Flow.addActionListener(this);
			JB_Flow.setOpaque(false);
			JB_Flow.setBounds(332, 37, 23, 23);
			JB_Flow.setActionCommand("SELFLOW");
			contentPanel.add(JB_Flow);
		}
		{
			JTBegin = new JTextField();
			JTBegin.setBounds(83, 69, 100, 21);
			contentPanel.add(JTBegin);
			JTBegin.setColumns(10);
		}
		{
			JLabel JLBegin = new JLabel("Begin:");
			JLBegin.setBounds(10, 72, 63, 15);
			contentPanel.add(JLBegin);
		}
		{
			JLabel JLEnd = new JLabel("End:");
			JLEnd.setBounds(193, 72, 46, 15);
			contentPanel.add(JLEnd);
		}
		{
			JTEnd = new JTextField();
			JTEnd.setBounds(255, 69, 100, 21);
			contentPanel.add(JTEnd);
			JTEnd.setColumns(10);
		}
	}

	@Override
	protected void setInitialValue() {
		Config c = new Config();
		JT_Network.setText(c.getNetworkPath());
		JT_Route.setText(c.getRoutePath());
		JTBegin.setText(c.getBegin());
		JTEnd.setText(c.getEnd());
	}

	@Override
	protected void storeValues() {
		Config c = new Config();
		c.setRoutePath(JT_Route.getText());
		c.setBegin(JTBegin.getText());
		c.setEnd(JTEnd.getText());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("SELFLOW")) {
			String path = FileSelectionTool.openDir(JT_Route.getText());
			if (path != null)
				JT_Route.setText(path);
		}
	}

	public void set(HashMap<String, Lane> lanes) {
		this.lanes = lanes;
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
		this.lanes = lanes;
	}

	@Override
	protected void generate() {
		String path = new Config().getSUMOPath() + System.getProperty("file.separator") + "sumo.exe";
		new ScenarioTask(frame, path, JT_Network.getText(), JT_Route.getText(), Integer.parseInt(JTBegin.getText()),
				Integer.parseInt(JTEnd.getText()), lanes).execute();

		frame.startBar("Generating TCL File...");
	}

}
