package src.network;

import src.Main;

public abstract class Trainer implements Manager {
    private Circuit[] circuits;
    private double[] fitness;
    public final int generationSize;
    public final int inputs;
    public final int[] layerSize;
	public final int outputs;
    private double mutationRate;
    private double mutationChance;
    private int generation;

    protected Trainer(int generationSize, int inputs, int[] layerSize, double mutationRate, double mutationChance) {
        this.generationSize = generationSize;
        this.inputs = inputs;
        this.layerSize = layerSize;
		this.outputs = layerSize[layerSize.length - 1];
        this.mutationRate = mutationRate;
        this.mutationChance = mutationChance;
		this.generation = 0;

		circuits = new Circuit[generationSize];
		for(int j = 0; j < circuits.length; j++) {
			circuits[j] = new Circuit(inputs, layerSize, "#" + j + ", gen0");
			Main.debugLog("Created " + j);
		}

    }

	/**
	 * Creates and starts off a task array, and returns that started task array
	 * @param circuits Array of circuits that will be started in the tasks
	 * @return Started task array
	 * @see Trainer.startCircuitTask
	 */
    protected abstract Task[] createTasks(Circuit[] circuits);

	/**
     * Creates a task and starts it
     * @param task Task to start
	 * @param threadName Name of the thread where the task will run
     */
    public void startCircuitTask(Task task, String threadName) {
		// TODO running too many threads makes the program very slow, need to delegate
		// certain # of circuits per avalible CPU core
        if(Main.runMultiThreaded) {
            Thread thread = new Thread(task, threadName);
            thread.start();
        } else {
            task.run();
        }
        
    }

	/**
     * Given a task array, invoice the fitness using the tasks
     * 
     * @param task Task[] that contains a circuit[s] (May not be completed)
     * @return Given fitness array -- higher is better
     */
    protected abstract double[] populateFitness(Task[] tasks);

	/**
     * Attempt to read a task- has incompleted circuit protections
     * 
     * @param task Task that may or may not contain a completed circuit
     * @return Circuit fitness
     */
    public double readTask(Task task) {
        double results = 0;
        int attempt = 1;
        long sleepTimeMs = 1l;
        tryReadTask:
        while (attempt <= 10) {
            if(task.isFinished()) {
                results = task.getFitness();
                break tryReadTask;
            } else {
                if(attempt != 5) System.out.println("Attempt #" + attempt + " waiting for circuit " + task.getCircuit().toString() +  " to process failed.");
                try {
                    Thread.sleep(sleepTimeMs);
                    sleepTimeMs *= 2;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            attempt++;
        }
        if(attempt >= 11) {
            System.out.println("Circuit proccess failed.");
            System.exit(1);
        }
        return results;
    }

    /**
     * Creates a new generation of circuits based on a parent circuit.
     * @param bestCircuit Parent of new generation
     */
    private void createCircuits(Circuit bestCircuit) {
		Main.debugLog("Creating new generation from " + bestCircuit.toString());
        circuits[0] = bestCircuit;
		bestCircuit.setID("#0, gen" + (generation + 1));
        for (int i = 1; i < generationSize; i++) {
            circuits[i] = bestCircuit.createMutatedChild(mutationChance, mutationRate, ("#" + i + ", gen" + (generation + 1)));
            circuits[i].setID(circuits[i].toString());
        }
    }

	/** 
	 * Mutates every circuit with the exception of the first circuit.
	 */
	private void mutateCircuits(int bestIndex) {
		
		for(int i = 0; i < circuits.length; i++) {
			if(i != bestIndex) {
				circuits[i] = circuits[i].createMutatedChild(mutationChance, mutationRate, ("#" + i + ", gen" + generation));
			}
		}
	}

    /**
     * Processes each circuit through tasks, then determines the best performer
     * @return index with highest fitness
     */
    private int doGeneration() {
		// Bestlastgen is at position 0!
		// TODO ^^^
		double bestLastFitness;
		Main.debugLog("Best last at " + bestLastGen);
		if(bestLastGen == -1) { // Gen 0
			bestLastFitness = 0;
		} else {
			bestLastFitness = fitness[bestLastGen];
			Main.debugLog("Best last is " + bestLastFitness);
		}
		int bestIndex = 0;
		fitness = populateFitness(createTasks(circuits));

		for(int i = 0; i < generationSize; i++) {
			if(fitness[i] > fitness[bestIndex]) {
				bestIndex = i;
				Main.debugLog("Best fitness found:  " + fitness[i]);
			}
		}

		Main.debugLog("Bestlast fitness: " + bestLastFitness + " Found best fitness: " + fitness[bestIndex] + " at " + bestIndex);
		
		if(bestLastFitness < fitness[bestIndex]) { // This gen did better than last
			System.out.println("Best fitness: " + fitness[bestIndex] + " ID: "+ circuits[bestIndex].toString());
			createCircuits(circuits[bestIndex]);
			bestIndex = 0;
		} else { // Let current gen keep changing
			System.out.println("Fitness stagnant: " + fitness[bestIndex] + " ID: "+ circuits[bestIndex].toString());
			mutateCircuits(bestIndex);
		}
        return bestIndex;
    }

    public int sentinelLoop() {
        int bestLastGen = -1;
        watch:
        while(true) {
            int generations;
            try{
                generations = Integer.valueOf(System.console().readLine("Enter # of generations, or a non-number to quit: "));
            } catch(Exception e) {
                System.out.println("Simulation terminated.");
                break watch;
            }
            for(int i = 0; i < generations; i++) {
                System.out.println("Generation #" + generation);
                
                bestLastGen = doGeneration(bestLastGen);
				Main.debugLog("Best index returned as " + bestLastGen);
                writeCircuitToFile("Generation " + generation, circuits[bestLastGen]);
                writeGenerationToCsv(new GenerationData(getGeneration(), fitness, circuits[bestLastGen]));
                generation++;
            }
        }
        return getGeneration();
    }

    public int getGeneration() {
        return generation;
    }

	public Circuit[] getCircuits() {
		return circuits;
	}

	private double[] getFitness() {
		return fitness;
	}
}