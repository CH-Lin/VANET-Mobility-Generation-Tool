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
package vanet.utils.io;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelectionTool {

	private static JFileChooser getFileChooser(String defaultPath, String description, String... extensions) {
		JFileChooser chooser = null;
		if (defaultPath == null || defaultPath.lastIndexOf(System.getProperty("file.separator")) == -1) {
			chooser = new JFileChooser();
		} else
			chooser = new JFileChooser(
					defaultPath.substring(0, defaultPath.lastIndexOf(System.getProperty("file.separator"))));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
		return chooser;
	}

	public static String openFile(String defaultPath, String description, String... extensions) {
		JFileChooser chooser = getFileChooser(defaultPath, description, extensions);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	public static String saveFile(String defaultPath, String description, String... extensions) {
		JFileChooser chooser = getFileChooser(defaultPath, description, extensions);

		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	private static JFileChooser getDirChooser(String defaultPath) {
		JFileChooser chooser = null;
		if (defaultPath == null) {
			chooser = new JFileChooser();
		} else {
			if (defaultPath.lastIndexOf(System.getProperty("file.separator")) > -1)
				chooser = new JFileChooser(
						defaultPath.substring(0, defaultPath.lastIndexOf(System.getProperty("file.separator"))));
			else
				chooser = new JFileChooser(defaultPath);
		}
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		return chooser;
	}

	public static String openDir(String defaultPath) {
		JFileChooser chooser = getDirChooser(defaultPath);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	public static String saveDir(String defaultPath) {
		JFileChooser chooser = getDirChooser(defaultPath);

		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
}
