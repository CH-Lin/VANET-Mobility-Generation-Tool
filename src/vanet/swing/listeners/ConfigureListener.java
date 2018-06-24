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
package vanet.swing.listeners;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import vanet.utils.Config;

public class ConfigureListener implements TableModelListener {

	private TableModel tableModel;

	public ConfigureListener(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	@Override
	public void tableChanged(TableModelEvent tme) {
		if (tme.getType() == TableModelEvent.UPDATE) {

			String confName = (String) tableModel.getValueAt(tme.getFirstRow(), tme.getColumn() - 1);
			String confValue = (String) tableModel.getValueAt(tme.getFirstRow(), tme.getColumn());

			Config c = new Config();
			switch (confName) {
			case "Number Of Vehicle":
				c.setNumberOfAutonomous(confValue);
				break;

			case "Min Speed":
				c.setMinSpeed(confValue);
				break;

			case "Max Speed":
				c.setMaxSpeed(confValue);
				break;

			case "Start Time":
				c.setSimulationTimeBegin(confValue);
				break;

			case "End Time":
				c.setSimulationTimeEnd(confValue);
				break;
			}

		}

	}
}
