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

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.HashMap;

import javax.swing.JScrollBar;
import javax.swing.JLabel;

import vanet.swing.MainFrame;
import vanet.task.NetworkScalerTask;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;

public class ResizeDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	private JScrollBar scrollValue;
	private JLabel value;

	private String network_path = null;

	public ResizeDialog(MainFrame owner) {
		super(owner);
	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 450, 100);
		contentPanel.setLayout(null);

		value = new JLabel();
		value.setBounds(240, 11, 60, 15);
		value.setPreferredSize(new Dimension(60, 15));
		contentPanel.add(value);

		scrollValue = new JScrollBar();
		scrollValue.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (scrollValue != null)
					value.setText("" + (double) scrollValue.getValue() / 100);
			}
		});
		scrollValue.setBounds(10, 11, 220, 17);
		scrollValue.setOrientation(JScrollBar.HORIZONTAL);
		contentPanel.add(scrollValue);
		scrollValue.setOpaque(false);
		scrollValue.setPreferredSize(new Dimension(190, 17));
		scrollValue.setValue(30);
		scrollValue.setToolTipText("" + (double) scrollValue.getValue() / 100);

	}

	@Override
	protected void setInitialValue() {
	}

	@Override
	protected void storeValues() {
	}

	public void generate() {
		new NetworkScalerTask().parseFile(network_path, (double) scrollValue.getValue() / 100);
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
		this.network_path = network_path;
	}
}
