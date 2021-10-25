package src.scenarios.maxOutput;

import src.network.*;

public class outputTrainer extends Trainer {

	public outputTrainer(int generationSize, int[] layerSize, double mutationRate, double mutationChance) {
		super(generationSize, 1, layerSize, mutationRate, mutationChance);
	}

	protected Task[] createTasks(Circuit[] circuits) {
		Task[] newTasks = new Task[circuits.length];
		for(int i = 0; i < circuits.length; i++) {
			newTasks[i] = new outputTask(circuits[i], this);
			startCircuitTask(newTasks[i], circuits[i].toString());
		}
		return newTasks;
	}

	protected double[] populateFitness(Task[] tasks) {
		double[] fitness = new double[tasks.length];
		for(int i = 0; i < tasks.length; i++) {
			fitness[i] = readTask(tasks[i]);
		}
		return fitness;
	}
}