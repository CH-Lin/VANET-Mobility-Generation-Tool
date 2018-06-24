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

import javax.swing.JLabel;
import javax.swing.JTextField;

import vanet.swing.MainFrame;
import vanet.utils.Config;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

public class CbrDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;
	private JTextField JT_NUMB;
	private JTextField JT_NUM;
	private JTextField JT_SIZE;
	private JTextField JT_INTERVAL;
	private JTextField JT_START;
	private JTextField JT_END;

	private int num = 0;
	private String path = null;

	public CbrDialog(MainFrame owner) {
		super(owner);

	}

	@Override
	protected void initialize() {
		setBounds(100, 100, 292, 264);
		{
			JLabel JL_NUMB = new JLabel("Number:");
			JL_NUMB.setBounds(10, 10, 46, 15);
			contentPanel.add(JL_NUMB);
		}
		{
			JT_NUMB = new JTextField();
			JT_NUMB.setBounds(66, 7, 198, 21);
			contentPanel.add(JT_NUMB);
			JT_NUMB.setColumns(10);
		}
		{
			JT_NUM = new JTextField();
			JT_NUM.setBounds(66, 38, 198, 21);
			contentPanel.add(JT_NUM);
			JT_NUM.setColumns(10);
		}
		{
			JT_SIZE = new JTextField();
			JT_SIZE.setBounds(66, 69, 198, 21);
			contentPanel.add(JT_SIZE);
			JT_SIZE.setColumns(10);
		}
		{
			JT_INTERVAL = new JTextField();
			JT_INTERVAL.setBounds(66, 100, 198, 21);
			contentPanel.add(JT_INTERVAL);
			JT_INTERVAL.setColumns(10);
		}
		{
			JT_START = new JTextField();
			JT_START.setBounds(66, 131, 198, 21);
			contentPanel.add(JT_START);
			JT_START.setColumns(10);
		}
		{
			JT_END = new JTextField();
			JT_END.setBounds(66, 163, 198, 21);
			contentPanel.add(JT_END);
			JT_END.setColumns(10);
		}
		{
			JLabel JL_PAIR = new JLabel("Pair:");
			JL_PAIR.setBounds(10, 41, 46, 15);
			contentPanel.add(JL_PAIR);
		}
		{
			JLabel JL_SIZE = new JLabel("Pkt Size:");
			JL_SIZE.setBounds(10, 72, 46, 15);
			contentPanel.add(JL_SIZE);
		}
		{
			JLabel JL_INTERVAL = new JLabel("Interval:");
			JL_INTERVAL.setBounds(10, 103, 46, 15);
			contentPanel.add(JL_INTERVAL);
		}
		{
			JLabel JL_START = new JLabel("Start:");
			JL_START.setBounds(10, 134, 46, 15);
			contentPanel.add(JL_START);
		}
		{
			JLabel JL_END = new JLabel("End:");
			JL_END.setBounds(10, 166, 46, 15);
			contentPanel.add(JL_END);
		}

	}

	@Override
	protected void setInitialValue() {
		Config c = new Config();
		JT_NUMB.setText(c.getNumOfCBR());
		JT_NUM.setText(c.getNumOfPair());
		JT_SIZE.setText(c.getPkSize());
		JT_INTERVAL.setText(c.getInterval());
		JT_START.setText(c.getCBRBegin());
		JT_END.setText(c.getCBREnd());
	}

	@Override
	protected void storeValues() {
		Config c = new Config();
		c.setNumOfCBR(JT_NUMB.getText());
		c.setNumOfPair(JT_NUM.getText());
		c.setPkSize(JT_SIZE.getText());
		c.setInterval(JT_INTERVAL.getText());
		c.setCBRBegin(JT_START.getText());
		c.setCBREnd(JT_END.getText());
	}

	@Override
	protected void generate() throws Exception {
		int total = Integer.parseInt(JT_NUMB.getText());

		for (int t = 1; t <= total; t++) {
			int pairs = Integer.parseInt(JT_NUM.getText());
			String line = System.getProperty("line.separator");
			BufferedWriter info = new BufferedWriter(new FileWriter(path.replaceAll(".tcl", "_" + t + ".tcl")));
			info.write("set opt(pktSize)\t\t" + JT_SIZE.getText() + line);
			info.write("set opt(interval)\t\t" + JT_INTERVAL.getText() + line);
			info.write("set opt(starCBR)\t\t" + JT_START.getText() + line);
			info.write("set opt(stopCBR)\t\t" + JT_END.getText() + line + line);
			info.write("# Upper layer agents/applications behavior" + line + line);
			HashMap<Integer, Integer> random = new HashMap<Integer, Integer>();
			for (int i = 0; i < pairs * 2; i++) {
				int max = (int) (Math.random() * num);
				Integer test = random.put(max, max);
				if (test != null) {
					i--;
				}
			}
			Integer values[] = random.values().toArray(new Integer[0]);
			for (int i = 1; i <= pairs; i++) {
				info.write("set null_(" + i + ") [new Agent/Null]" + line);
				info.write("$ns_ attach-agent $node_(" + values[2 * (i - 1)] + ") $null_(" + i + ")" + line + line);
				info.write("set udp_(" + i + ") [new Agent/UDP]" + line);
				info.write("$ns_ attach-agent $node_(" + values[1 + 2 * (i - 1)] + ") $udp_(" + i + ")" + line);
				info.write("$udp_(" + i + ") set fid_ " + i + line + line);
				info.write("set cbr_(" + i + ") [new Application/Traffic/CBR]" + line);
				info.write("$cbr_(" + i + ") set packetSize_ $opt(pktSize)" + line);
				info.write("$cbr_(" + i + ") set interval_ $opt(interval)" + line);
				info.write("$cbr_(" + i + ") set random_ 1" + line);
				info.write("#    $cbr_(" + i + ") set maxpkts_ 100" + line);
				info.write("$cbr_(" + i + ") attach-agent $udp_(" + i + ")" + line);
				info.write("$ns_ connect $udp_(" + i + ") $null_(" + i + ")" + line);
				info.write("$ns_ at $opt(starCBR) \"$cbr_(" + i + ") start\"" + line);
				info.write("$ns_ at $opt(stopCBR) \"$cbr_(" + i + ") stop\"" + line + line + line);
			}
			info.close();
		}
	}

	@Override
	public void set(HashMap<String, TravelingNodes> scenario, HashMap<String, Lane> lanes, String scenario_path,
			String network_path) {
		this.num = scenario.get("-1").getNodes().length;
		this.path = scenario_path.replaceAll(".tcl", "_cbr.tcl");
	}
}
