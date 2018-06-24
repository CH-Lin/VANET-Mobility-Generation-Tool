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
import vanet.swing.MainFrame;
import vanet.task.RSUTask;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

public class RSUDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	private HashMap<String, TravelingNodes> scenario;
	private HashMap<String, Lane> lanes;

	public RSUDialog(MainFrame frame) {
		super(frame);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Do you want to deploy RUSs?");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblNewLabel);
	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 285, 102);
	}

	@Override
	protected void setInitialValue() {
	}

	@Override
	protected void storeValues() {
	}

	@Override
	protected void generate() throws Exception {
		new RSUTask(frame, scenario, lanes).execute();
		frame.startBar("Optimize RSUs...");
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
		this.scenario = scenario;
		this.lanes = lanes;
	}
}
