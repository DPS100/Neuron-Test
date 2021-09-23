package src.network;

public abstract class Trainer implements Manager {
    private Circuit[] circuits;
    private double[] fitness;
    public final int generationSize;
    public final int inputs;
    public final int[] layerSize;
	public final int outputs;
    private double mutationRate;
    private int generation;

    protected Trainer(int generationSize, int inputs, int[] layerSize, double mutationRate) {
        this.generationSize = generationSize;
        this.inputs = inputs;
        this.layerSize = layerSize;
		this.outputs = layerSize[layerSize.length - 1];
        this.mutationRate = mutationRate;
		this.generation = 0;
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
		System.out.println("Starting task " + threadName);
        Thread thread = new Thread(task, threadName);
        thread.start();
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
    public double[] readTask(Task task) {
        double[] results = new double[0]; // May not work - dynamic size error
        int attempt = 1;
        tryReadTask:
        while (attempt <= 10) {
            if(task.isFinished()) {
                results = task.getFitness();
                break tryReadTask;
            } else {
                if(attempt != 1) System.out.println("Attempt #" + attempt + " waiting for circuit " + task.circuitNames()[0] +  " to process failed.");
                try {
                    Thread.sleep(100l);
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
     * If it is the first generation of the best circuit is null, will randomly generate new circuits
     * @param bestCircuit Parent of new generation
     */
    private void createCircuits(Circuit bestCircuit) {
        circuits = new Circuit[generationSize];
        fitness = new double[generationSize];
        if(generation == 0 || bestCircuit == null) { // First generation
            for(int i = 0; i < generationSize; i++) {
                circuits[i] = new Circuit(inputs, layerSize, ("#" + i + ", gen" + generation + ", mutations: Original"));
            }
        } else {
            circuits[0] = bestCircuit;
            for (int i = 1; i < generationSize; i++) {
                // Copy best circuit, then mutate
                circuits[i] = bestCircuit.createMutatedChild(mutationRate, ("#" + i + ", gen" + generation + ", mutations:"));
                circuits[i].mutations = circuits[i].mutations + generation + " ";
                circuits[i].setID(circuits[i].toString() + circuits[i].mutations);
                // New random circuit
                // circuits[i] = new Circuit(inputs, layerSize, ("#" + i + ", gen" + generation + ", mutations:"));
            }
        }
    }

    /**
     * Processes each circuit through tasks, then determines the best performer
     * @return Circuit with highest fitness
     */
    private Circuit doGeneration() {
		fitness = populateFitness(createTasks(circuits));
        int bestIndex = 0;
        for(int i = 0; i < generationSize; i++) {
            if(fitness[i] > fitness[bestIndex]) {
                bestIndex = i;
            }
        }
        System.out.println("Best fitness: " + fitness[bestIndex] + " ID: "+ circuits[bestIndex].toString());
        return circuits[bestIndex];
    }

    public void sentinelLoop() {
        Circuit bestLastGen = null;
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
                createCircuits(bestLastGen);
                bestLastGen = doGeneration();
                writeCircuitToFile("Generation " + generation, bestLastGen);
                writeGenerationToCsv(new GenerationData(i, fitness, bestLastGen));
                generation++;
            }
        }
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