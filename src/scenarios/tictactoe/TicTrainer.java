package src.scenarios.tictactoe;

import src.Main;
import src.network.*;

public class TicTrainer extends Trainer {

	private Circuit opponent;

	public TicTrainer(int generationSize, int[] layerSize, double mutationRate, double mutationChance) {
		super(generationSize, 9, layerSize, mutationRate, mutationChance);
		opponent = getCircuits()[0];
	}

	protected Task[] createTasks(Circuit[] circuits) {
		
		TicTask[] tasks = new TicTask[circuits.length];
		// Populate & start tasks
		for(int i = 0; i < tasks.length; i++) {
			tasks[i] = new TicTask(circuits[i], this, opponent);
			// Start thread
			startCircuitTask(tasks[i], "TicGame " + i);
		}
		return tasks;
	}

	protected double[] populateFitness(Task[] tasks) {
		double[] fitness = new double[tasks.length];
		double best = -1;
		int bestIndex = -1;
		for(int i = 0; i < fitness.length; i++) {
			fitness[i] = readTask(tasks[i]);
			if(tasks[i].getFitness() > best) {
				best = tasks[i].getFitness();
				bestIndex = i;
			}
		}

		opponent = getCircuits()[bestIndex];

		return fitness;
	}
}