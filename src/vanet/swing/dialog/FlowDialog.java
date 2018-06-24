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

import javax.swing.JLabel;
import javax.swing.JTextField;

import vanet.swing.MainFrame;
import vanet.task.FlowTask;
import vanet.utils.Config;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;

import java.util.HashMap;

public class FlowDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;
	private JTextField JT_NUM;
	private JTextField JT_BEGIN;
	private JTextField JT_END;
	private JTextField JT_MAX;

	private HashMap<String, Lane> lanes;
	private String network_path = null;

	public FlowDialog(MainFrame frame) {
		super(frame);
	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 324, 206);
		{
			JLabel JL_NUM = new JLabel("Number of flow:");
			JL_NUM.setBounds(10, 10, 78, 15);
			contentPanel.add(JL_NUM);
		}
		{
			JLabel JL_Begin = new JLabel("Begin:");
			JL_Begin.setBounds(10, 41, 78, 15);
			contentPanel.add(JL_Begin);
		}
		{
			JLabel JL_End = new JLabel("End:");
			JL_End.setBounds(10, 72, 78, 15);
			contentPanel.add(JL_End);
		}
		{
			JLabel JL_MAX = new JLabel("Max nodes no.:");
			JL_MAX.setBounds(10, 103, 78, 15);
			contentPanel.add(JL_MAX);
		}
		{
			JT_NUM = new JTextField();
			JT_NUM.setBounds(98, 7, 198, 21);
			contentPanel.add(JT_NUM);
			JT_NUM.setColumns(10);
		}
		{
			JT_BEGIN = new JTextField();
			JT_BEGIN.setBounds(98, 38, 198, 21);
			contentPanel.add(JT_BEGIN);
			JT_BEGIN.setColumns(10);
		}
		{
			JT_END = new JTextField();
			JT_END.setBounds(98, 69, 198, 21);
			contentPanel.add(JT_END);
			JT_END.setColumns(10);
		}
		{
			JT_MAX = new JTextField();
			JT_MAX.setBounds(98, 100, 198, 21);
			contentPanel.add(JT_MAX);
			JT_MAX.setColumns(10);
		}

	}

	@Override
	protected void setInitialValue() {
		Config c = new Config();
		JT_NUM.setText(c.getNumOfFlows());
		JT_MAX.setText(c.getMaxNoNode());
		JT_BEGIN.setText(c.getBegin());
		JT_END.setText(c.getEnd());
	}

	@Override
	protected void storeValues() {
		Config c = new Config();
		c.setBegin(JT_BEGIN.getText());
		c.setEnd(JT_END.getText());
		c.setMaxNoNode(JT_MAX.getText());
		c.setNumOfFlows(JT_NUM.getText());
	}

	@Override
	protected void generate() {
		try {
			String path = new Config().getSUMOPath() + System.getProperty("file.separator") + "duarouter.exe";
			new FlowTask(frame, path, network_path, lanes, Integer.parseInt(JT_NUM.getText()),
					Integer.parseInt(JT_BEGIN.getText()), Integer.parseInt(JT_END.getText()),
					Integer.parseInt(JT_MAX.getText())).execute();
			frame.startBar("Generating Flow File...");

			Config c = new Config();
			c.setFlowPath(network_path.replaceAll(".net", ".flows"));
			c.setRoutePath(network_path.replaceAll(".net", ".rou"));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
		this.network_path = network_path;
		this.lanes = lanes;
	}

}
