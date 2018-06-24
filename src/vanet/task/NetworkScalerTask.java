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
package vanet.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class NetworkScalerTask {
	public void parseFile(String path, double scale) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String output = path.replaceAll(".net", "_scaled.net");

			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			String read = "";
			while ((read = br.readLine()) != null) {
				if (read.indexOf("<net-offset>") != -1 || read.indexOf("<conv-boundary>") != -1
						|| read.indexOf("<orig-boundary>") != -1) {
					String temp = read.substring(read.indexOf(">") + 1, read.lastIndexOf("<"));

					String offset[] = temp.split(",");
					double o[] = new double[offset.length];
					for (int i = 0; i < offset.length; i++) {
						o[i] = Double.parseDouble(offset[i]) * scale;
						if (o[i] != 0)
							read = read.replace(offset[i], String.format("%.6f", o[i]));
					}

				} else if (read.indexOf("<lane ") != -1) {
					String temp = read.substring(read.indexOf("length="), read.lastIndexOf("</"));

					String len = temp.substring(temp.indexOf("\"") + 1);
					len = len.substring(0, len.indexOf("\""));
					double l = Double.parseDouble(len) * scale;
					read = read.replace("length=\"" + len, "length=\"" + String.format("%.2f", l));

					temp = temp.substring(temp.indexOf('>') + 1);
					String pair[] = temp.split(" ");
					for (int i = 0; i < pair.length; i++) {
						try {
							String vertex[] = pair[i].split(",");
							double v[] = new double[vertex.length];

							for (int j = 0; j < v.length; j++) {
								v[j] = Double.parseDouble(vertex[j]) * scale;
							}
							String newstr = String.format("%.2f", v[0]) + "," + String.format("%.2f", v[1]);
							read = read.replace(pair[i], newstr);
						} catch (NumberFormatException e) {
							// e.printStackTrace();
						}
					}

				} else if (read.indexOf("<junction ") != -1) {
					String temp = read.substring(read.indexOf("x="), read.lastIndexOf("\">"));
					String sx = temp.substring(temp.indexOf("\"") + 1, temp.indexOf("\" "));
					String sy = temp.substring(temp.indexOf("y=\"") + 3);
					double x = Double.parseDouble(sx) * scale;
					double y = Double.parseDouble(sy) * scale;
					String newstr = "x=\"" + String.format("%.2f", x) + "\" y=\"" + String.format("%.2f", y);
					read = read.replace(temp, newstr);

				} else if (read.indexOf("<shape>") != -1) {
					String temp = read.substring(read.indexOf("<shape>") + 7, read.lastIndexOf("</shape>"));
					String pair[] = temp.split(" ");
					for (int i = 0; i < pair.length; i++) {
						try {
							String vertex[] = pair[i].split(",");
							double v[] = new double[vertex.length];

							for (int j = 0; j < v.length; j++) {
								v[j] = Double.parseDouble(vertex[j]) * scale;
							}
							String newstr = String.format("%.2f", v[0]) + "," + String.format("%.2f", v[1]);
							read = read.replace(pair[i], newstr);
						} catch (NumberFormatException e) {
							// e.printStackTrace();
						}
					}
				}

				bw.write(read + "\n");
				bw.flush();
			}
			br.close();
			bw.close();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Cannot read file:" + path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
