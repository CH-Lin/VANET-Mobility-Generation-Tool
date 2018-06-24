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
package vanet.utils;

import java.io.File;
import java.io.IOException;

import org.ini4j.Wini;

public class Config {

	private final static String set = "setting";

	Wini ini;

	public Config() {
		String filename = "";
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0)
			filename = "config.ini";
		else if (OS.indexOf("mac") >= 0)
			filename = "config_mac.ini";

		try {
			ini = new Wini(new File(filename));
		} catch (IOException e1) {
			File f = new File(filename);
			try {
				f.createNewFile();
				ini = new Wini(new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getValue(String key) {
		return ini.get(set, key);
	}

	private String getValue(String key, String defaultValue) {
		return getValue(key) != null ? getValue(key) : defaultValue;
	}

	private void store(String key, String value) {
		try {
			ini.put(set, key, value);
			ini.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getOffsetX() {
		return getValue("errx");
	}

	public void setOffsetX(String x) {
		store("errx", x);
	}

	public String getOffsetY() {
		return getValue("erry");
	}

	public void setOffsetY(String y) {
		store("erry", y);
	}

	public String getNetworkPath() {
		return getValue("net");
	}

	public void setNetworkPath(String path) {
		store("net", path);
	}

	public String getFlowPath() {
		return getValue("flow");
	}

	public void setFlowPath(String path) {
		store("flow", path);
	}

	public String getRoutePath() {
		return getValue("route");
	}

	public void setRoutePath(String path) {
		store("route", path);
	}

	public String getScenarioPath() {
		return getValue("scenario");
	}

	public void setScenarioPath(String path) {
		store("scenario", path);
	}

	public String getSUMOPath() {
		return getValue("sumopath");
	}

	public void setSUMOPath(String path) {
		store("sumopath", path);
	}

	public String getOutputPath() {
		return getValue("outputpath");
	}

	public void setOutputPath(String path) {
		store("outputpath", path);
	}

	public String getNumOfFlows() {
		return getValue("numofflow");
	}

	public void setNumOfFlows(String n) {
		store("numofflow", n);
	}

	public String getMaxNoNode() {
		return getValue("maxnonode");
	}

	public void setMaxNoNode(String n) {
		store("maxnonode", n);
	}

	public String getBegin() {
		return getValue("begin");
	}

	public void setBegin(String n) {
		store("begin", n);
	}

	public String getEnd() {
		return getValue("end");
	}

	public void setEnd(String n) {
		store("end", n);
	}

	public String getNumOfCBR() {
		return getValue("numofcbr");
	}

	public void setNumOfCBR(String n) {
		store("numofcbr", n);
	}

	public String getNumOfPair() {
		return getValue("numofpair");
	}

	public void setNumOfPair(String n) {
		store("numofpair", n);
	}

	public String getPkSize() {
		return getValue("pksize");
	}

	public void setPkSize(String n) {
		store("pksize", n);
	}

	public String getInterval() {
		return getValue("interval");
	}

	public void setInterval(String n) {
		store("interval", n);
	}

	public String getCBRBegin() {
		return getValue("cbrbegin");
	}

	public void setCBRBegin(String n) {
		store("cbrbegin", n);
	}

	public String getCBREnd() {
		return getValue("cbrend");
	}

	public void setCBREnd(String n) {
		store("cbrend", n);
	}

	public String getNumOfLoc() {
		return getValue("NumOfLoc");
	}

	public void setNumOfLoc(String n) {
		store("NumOfLoc", n);
	}

	public int getSliderValue() {
		return Integer.parseInt(getValue("slider") != null ? getValue("slider") : "0");
	}

	public void setSliderValue(int n) {
		store("slider", "" + n);
	}

	public String getNumberOfAutonomous() {
		return getValue("autonomous", "100");
	}

	public void setNumberOfAutonomous(String n) {
		store("autonomous", n);
	}

	public String getMinSpeed() {
		return getValue("minspeed", "40");
	}

	public void setMinSpeed(String n) {
		store("minspeed", n);
	}

	public String getMaxSpeed() {
		return getValue("maxspeed", "140");
	}

	public void setMaxSpeed(String n) {
		store("maxspeed", n);
	}

	public String getSimulationTimeBegin() {
		return getValue("simbegin", "0");
	}

	public void setSimulationTimeBegin(String n) {
		store("simbegin", n);
	}

	public String getSimulationTimeEnd() {
		return getValue("simend", "2000");
	}

	public void setSimulationTimeEnd(String n) {
		store("simend", n);
	}
}
