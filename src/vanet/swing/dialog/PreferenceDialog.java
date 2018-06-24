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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import vanet.swing.MainFrame;
import vanet.utils.Config;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;
import vanet.utils.io.FileSelectionTool;

public class PreferenceDialog extends AbstractDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField JT_SUMO;
	private JTextField JT_OUTPUT;
	private JSlider Jslider_XErr;
	private JSlider Jslider_YErr;

	public PreferenceDialog(MainFrame owner) {
		super(owner);
	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 380, 218);
		JLabel JL_SUMO = new JLabel("SUMO:");
		JL_SUMO.setBounds(10, 10, 47, 15);
		contentPanel.add(JL_SUMO);

		JT_SUMO = new JTextField();
		JT_SUMO.setBounds(67, 7, 253, 21);
		contentPanel.add(JT_SUMO);
		JT_SUMO.setColumns(10);

		JButton JB_SUMO = new JButton("...");
		JB_SUMO.setOpaque(false);
		JB_SUMO.setActionCommand("selsumo");
		JB_SUMO.addActionListener(this);
		JB_SUMO.setBounds(330, 6, 23, 23);
		contentPanel.add(JB_SUMO);

		JLabel JL_OUTPUT = new JLabel("OUTPUT:");
		JL_OUTPUT.setBounds(10, 45, 47, 15);
		contentPanel.add(JL_OUTPUT);

		JT_OUTPUT = new JTextField();
		JT_OUTPUT.setBounds(67, 42, 253, 21);
		contentPanel.add(JT_OUTPUT);
		JT_OUTPUT.setColumns(10);

		JButton JB_OUTPUT = new JButton("...");
		JB_OUTPUT.setOpaque(false);
		JB_OUTPUT.setActionCommand("seloutput");
		JB_OUTPUT.addActionListener(this);
		JB_OUTPUT.setBounds(330, 41, 23, 23);
		contentPanel.add(JB_OUTPUT);

		JLabel JL_XErr = new JLabel("X Error:");
		JL_XErr.setBounds(10, 80, 46, 15);
		contentPanel.add(JL_XErr);

		JLabel JL_YErr = new JLabel("Y Error:");
		JL_YErr.setBounds(10, 115, 46, 15);
		contentPanel.add(JL_YErr);

		final JLabel JL_XErrInfo = new JLabel("");
		JL_XErrInfo.setBounds(67, 80, 50, 15);
		contentPanel.add(JL_XErrInfo);

		final JLabel JL_YErrInfo = new JLabel("");
		JL_YErrInfo.setBounds(67, 115, 50, 15);
		contentPanel.add(JL_YErrInfo);

		Jslider_XErr = new JSlider();
		Jslider_XErr.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JL_XErrInfo.setText("" + Jslider_XErr.getValue());
			}
		});
		Jslider_XErr.setOpaque(false);
		Jslider_XErr.setBounds(127, 76, 226, 23);
		contentPanel.add(Jslider_XErr);

		Jslider_YErr = new JSlider();
		Jslider_YErr.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JL_YErrInfo.setText("" + Jslider_YErr.getValue());
			}
		});
		Jslider_YErr.setOpaque(false);
		Jslider_YErr.setBounds(127, 111, 226, 23);
		contentPanel.add(Jslider_YErr);
	}

	@Override
	protected void setInitialValue() {
		Config c = new Config();
		JT_SUMO.setText(c.getSUMOPath());
		JT_OUTPUT.setText(c.getOutputPath());
		try {
			Jslider_XErr.setValue(Integer.parseInt(c.getOffsetX()));
		} catch (NumberFormatException e) {
			Jslider_XErr.setValue(10);
		}
		try {
			Jslider_YErr.setValue(Integer.parseInt(c.getOffsetY()));
		} catch (NumberFormatException e) {
			Jslider_YErr.setValue(10);
		}
	}

	@Override
	protected void storeValues() {
		Config c = new Config();
		c.setSUMOPath(JT_SUMO.getText());
		c.setOutputPath(JT_OUTPUT.getText());
		c.setOffsetY(Jslider_YErr.getValue() + "");
		c.setOffsetX(Jslider_XErr.getValue() + "");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("selsumo")) {
			String path = FileSelectionTool.openDir(JT_SUMO.getText());
			if (path != null)
				JT_SUMO.setText(path);
		} else if (e.getActionCommand().equalsIgnoreCase("seloutput")) {
			String path = FileSelectionTool.openDir(JT_OUTPUT.getText());
			if (path != null)
				JT_OUTPUT.setText(path);
		}
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
	}

	@Override
	protected void generate() throws Exception {
	}

}
