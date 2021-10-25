package src.scenarios.maxOutput;

import src.network.*;

public class outputTask extends Task {
	public outputTask(Circuit c, Trainer t) {
		super(c, t);
	}

	protected double calcFitness() {
		double[] output = getCircuit().process(new double[]{1});
		double sum = 0;
		for(double d : output) {
			sum += d;
		}
		return sum;
	}
}