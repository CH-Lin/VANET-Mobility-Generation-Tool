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
import java.util.HashMap;

import javax.swing.JOptionPane;

public class NetworkReIndexTask {

	private static HashMap<String, String> lanes;
	private static HashMap<String, String> junction;
	private static int count_l;
	private static int count_j;

	public void parseFile(String path) {
		lanes = new HashMap<String, String>();
		junction = new HashMap<String, String>();
		count_l = 1;
		count_j = 1;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String output = path.replaceAll(".net", "_reindex.net");

			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			String read = "";
			while ((read = br.readLine()) != null) {
				if (read.indexOf("<edges") != -1) {
					String temp = read.substring(read.indexOf(">") + 1);
					temp = temp.substring(0, temp.indexOf("<"));

					String offset[] = temp.split(" ");
					for (int i = 0; i < offset.length; i++) {
						newLane(offset[i]);
						read = read.replace(offset[i], lanes.get(offset[i]));
					}

				} else if (read.indexOf("<edge ") != -1) {
					String temp = read.substring(read.indexOf("\"") + 1);
					temp = temp.substring(0, temp.indexOf("\""));
					newLane(temp);
					read = read.replace(temp, lanes.get(temp));

					if (read.indexOf("from") != -1) {
						String from = read.substring(read.indexOf("from"));
						from = from.substring(from.indexOf("\"") + 1);
						from = from.substring(0, from.indexOf("\""));
						newJunction(from);
						read = read.replaceAll("from=\"" + from, "from=\"" + junction.get(from));
					}

					if (read.indexOf("to") != -1) {
						String to = read.substring(read.indexOf("to"));
						to = to.substring(to.indexOf("\"") + 1);
						to = to.substring(0, to.indexOf("\""));
						newJunction(to);
						read = read.replaceAll("to=\"" + to, "to=\"" + junction.get(to));
					}

				} else if (read.indexOf("<lane ") != -1) {
					String temp = read.substring(read.indexOf("\"") + 1);
					temp = temp.substring(0, temp.indexOf("\""));
					temp = temp.substring(0, temp.lastIndexOf("_"));
					read = read.replace(temp, lanes.get(temp));
				} else if (read.indexOf("<cedge ") != -1) {
					String temp = read.substring(read.indexOf("\"") + 1);
					temp = temp.substring(0, temp.indexOf("\""));
					read = read.replace(temp, lanes.get(temp));
					temp = read.substring(read.indexOf(">") + 1);
					temp = temp.substring(0, temp.lastIndexOf("_"));
					if (lanes.get(temp) != null)
						read = read.replace(temp, lanes.get(temp));
				} else if (read.indexOf("<inclanes>") != -1) {
					String temp = read.substring(read.indexOf(">") + 1);
					temp = temp.substring(0, temp.indexOf("<"));

					String offset[] = temp.split(" ");
					for (int i = 0; i < offset.length; i++) {
						try {
							String t = offset[i].substring(0, offset[i].lastIndexOf("_"));
							read = read.replace(t, lanes.get(t));
						} catch (Exception e) {
						}
					}
				} else if (read.indexOf("<intlanes>") != -1) {
					String temp = read.substring(read.indexOf(">") + 1);
					temp = temp.substring(0, temp.indexOf("<"));

					String offset[] = temp.split(" ");
					for (int i = 0; i < offset.length; i++) {
						try {
							String t = offset[i].substring(0, offset[i].lastIndexOf("_"));
							read = read.replaceFirst(t, lanes.get(t));
						} catch (Exception e) {
						}
					}
				} else if (read.indexOf("<succ ") != -1) {
					String edge = read.substring(read.indexOf("\"") + 1);
					edge = edge.substring(0, edge.indexOf("\""));
					read = read.replace(edge, lanes.get(edge));

					if (read.indexOf("junction=") != -1) {
						String j = read.substring(read.indexOf("junction="));
						j = j.substring(j.indexOf("\"") + 1);
						j = j.substring(0, j.indexOf("\""));
						newJunction(j);
						read = read.replaceAll("junction=\"" + j, "junction=\"" + junction.get(j));
					}

				} else if (read.indexOf("<succlane ") != -1) {
					String l = read.substring(read.indexOf("\"") + 1);
					l = l.substring(0, l.indexOf("\""));
					if (l.indexOf("SUMO_NO_DESTINATION") == -1) {
						l = l.substring(0, l.lastIndexOf("_"));
						read = read.replace(l, lanes.get(l));
					}

					if (read.indexOf("via=\"") != -1) {
						String via = read.substring(read.indexOf("via=\"") + 1);
						via = via.substring(via.indexOf("\"") + 1);
						via = via.substring(0, via.indexOf("\""));
						via = via.substring(0, via.lastIndexOf("_"));
						read = read.replaceAll(via, lanes.get(via));
					}

					if (read.indexOf("tl=\"") != -1 && read.indexOf("tl=\"\"") == -1) {
						String tl = read.substring(read.indexOf("tl=\"") + 1);
						tl = tl.substring(tl.indexOf("\"") + 1);
						tl = tl.substring(0, tl.indexOf("\""));
						newJunction(tl);
						read = read.replaceAll("tl=\"" + tl, "tl=\"" + junction.get(tl));
					}
				} else if (read.indexOf("<key>") != -1) {
					String k = read.substring(read.indexOf(">") + 1, read.indexOf("</"));
					newJunction(k);
					read = read.replaceAll("<key>" + k, "<key>" + junction.get(k));
				} else if (read.indexOf("<junction ") != -1) {
					String j = read.substring(read.indexOf("id=\""));
					j = j.substring(j.indexOf("\"") + 1);
					j = j.substring(0, j.indexOf("\""));
					newJunction(j);
					read = read.replaceAll("id=\"" + j, "id=\"" + junction.get(j));
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

	private void newLane(String key) {
		if (!lanes.containsKey(key)) {
			if (key.startsWith(":")) {
				lanes.put(key, ":" + count_l);
				count_l++;
			} else if (key.startsWith("-")) {
				lanes.put(key, "-" + count_l);
				count_l++;
			} else {
				String tkey = "-" + key;
				String tvalue = lanes.get(tkey);
				if (tvalue != null)
					lanes.put(key, tvalue.replaceFirst("-", ""));
				else {
					lanes.put(key, "" + count_l);
					count_l++;
				}
			}
		}
	}

	private void newJunction(String key) {
		if (!junction.containsKey(key)) {
			junction.put(key, "" + count_j);
			count_j++;
		}
	}
}
