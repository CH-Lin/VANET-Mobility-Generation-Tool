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

import java.util.HashMap;

import javax.swing.SwingWorker;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.FixedBinaryGene;

import vanet.swing.MainFrame;
import vanet.task.ga.Level1FitnessFunction;
import vanet.utils.Lane;
import vanet.utils.TravelingNodes;

public class RSUTask extends SwingWorker<Void, Void> {

	private MainFrame frame;

	private HashMap<String, TravelingNodes> record;
	private HashMap<String, Lane> ls;

	public RSUTask(MainFrame owner, HashMap<String, TravelingNodes> record, HashMap<String, Lane> ls) {
		this.frame = owner;
		this.record = record;
		this.ls = ls;
	}

	@Override
	protected Void doInBackground() throws Exception {
		frame.changeStatusBar("Optimize RSUs...");
		level1();
		level2();
		return null;
	}

	private void level1() {
		try {
			Configuration conf = new DefaultConfiguration();
			FitnessFunction myFunc = new Level1FitnessFunction(record, ls);
			conf.setFitnessFunction(myFunc);

			Gene[] sampleGenes = new Gene[1];

			sampleGenes[0] = new FixedBinaryGene(conf, 100); // Quarters

			Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);

			conf.setSampleChromosome(sampleChromosome);

			conf.setPopulationSize(500);

			Genotype population = Genotype.randomInitialGenotype(conf);

			for (int i = 0; i < 10000; i++) {
				population.evolve();
				IChromosome bestSolutionSoFar = population.getFittestChromosome();
				Gene gene = bestSolutionSoFar.getGene(0);
				System.out.println(gene.toString());
			}

		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void level2() {

	}

	public void done() {
		frame.stopBar("Done");
		// frame.readRSUs(netfile.replaceAll(".net.xml", ".tcl"));
	}

}
