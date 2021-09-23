package src.scenarios.tictactoe;

import src.network.*;

public class TicTrainer extends Trainer {

	public TicTrainer(int generationSize, int[] layerSize, double mutationRate) {
		super(generationSize, 9, layerSize, mutationRate);
	}

	protected Task[] createTasks(Circuit[] circuits) {
		
		if(circuits.length % 2 != 0) { // Odd
			System.out.println("Error: Odd number of circuits.");
			System.exit(1);
		}
		TicTask[] tasks = new TicTask[circuits.length / 2];
		// Populate & start tasks
		for(int i = 0; i < tasks.length; i++) {
			Circuit[] pair = new Circuit[2];
			pair[0] = circuits[i*2];
			pair[1] = circuits[i*2 + 1];
			tasks[i] = new TicTask(pair);
			// Start thread
			startCircuitTask(tasks[i], "TicGame " + i);
		}
		return tasks;
	}

	protected double[] populateFitness(Task[] tasks) {
		double[] fitness = new double[tasks.length * 2];
		int acc = 0;
		for(Task t : tasks) {
			double[] temp = readTask(t);
			fitness[acc] = temp[0];
			acc++;
			fitness[acc] = temp[1];
			acc++;
		}
		return fitness;
	}

	
}