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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vanet.swing.MainFrame;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;

public abstract class AbstractDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	protected MainFrame frame;
	protected JPanel contentPanel;

	public AbstractDialog(MainFrame owner) {
		super(owner, true);
		this.frame = owner;
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setOpaque(false);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							storeValues();
							setVisible(false);
							generate();
							dispose();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				okButton.setOpaque(false);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setOpaque(false);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		initialize();
		setInitialValue();
	}

	abstract protected void initialize();

	abstract protected void setInitialValue();

	abstract protected void storeValues();

	abstract protected void generate() throws Exception;

	abstract public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes,
			String scenario_path, String network_path);
}
