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
package vanet.swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jdesktop.swingx.JXMapViewer;

import vanet.swing.MainFrame;
import vanet.swing.SelectionMessage;
import vanet.task.OpenStreetConvertTask;
import vanet.task.OpenStreetDLTask;
import vanet.utils.Config;
import vanet.utils.io.FileSelectionTool;

public class SelectionPropertyListener implements PropertyChangeListener {

	private MainFrame frame;
	private JXMapViewer viewer;

	public SelectionPropertyListener(MainFrame frame, JXMapViewer viewer) {
		this.frame = frame;
		this.viewer = viewer;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		final SelectionMessage m = (SelectionMessage) evt.getNewValue();

		frame.showInfo(m);

		JPopupMenu myJPopupMenu = new JPopupMenu();
		JMenuItem jMenuItemDL = new JMenuItem("Download");
		jMenuItemDL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewer.repaint();
				readOpenStreetMap(m);
			}
		});
		myJPopupMenu.add(jMenuItemDL);

		myJPopupMenu.show(viewer, (int) m.getReleasePoint().getX(), (int) m.getReleasePoint().getY());
	}

	private void readOpenStreetMap(SelectionMessage m) {

		String path = FileSelectionTool.saveFile(new Config().getNetworkPath(), "XML", "xml");

		if (path != null) {

			if (path.indexOf(".xml") == -1)
				path = path.concat(".xml");

			try {
				new OpenStreetDLTask(frame, path, m.getG1(), m.getG2()).execute();
				frame.startBar("Download Map File...");

				String output = path.replaceAll(".xml", ".net.xml");

				new OpenStreetConvertTask(frame, path, output).execute();
				frame.startBar("Convert Map File...");

				frame.readNetwork(output);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
