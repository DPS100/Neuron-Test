package src;

public abstract class Trainer implements Manager {
    Circuit[] circuits;
    double[] fitness;
    final int size;
    final int outputs = 1;
    double bestFitness = 0;
    static int generation = 0;
    double[] circuitInputs;
    Task[] tasks;

    Trainer(int size) {
        this.size = size;
        circuitInputs = new double[]{1.0,1.0};
        tasks = new Task[size];
    }

    private void createCircuits() {
        circuits = new Circuit[size];
        fitness = new double[size];
        for (int i = 0; i < size; i++) {
            circuits[i] = new Circuit(circuitInputs.length, new int[]{outputs}, ("#" + i + ", gen" + generation + ", mutations:"));
        }
    }

    private void createTasks() {
        for(int i = 0; i < size; i++) {
            tasks[i] = startCircuitTask(circuits[i], circuitInputs, "Task #" + i);
        }
    }

    private Circuit doGeneration() {
        int maxTicks = 1;
        for(int tick = 0; tick < maxTicks; tick++) {
            createTasks();
            for(int i = 0; i < size; i++) {
                fitness[i] = evaluateFitness(tasks[i], circuitInputs);
            }
        }
        int bestIndex = 0;
        for(int i = 0; i < size; i++) {
            if(fitness[i] > fitness[bestIndex]) {
                bestIndex = i;
            } if (fitness[i] == 0) {
                circuits[i].mutate(1);
                circuits[i].setID(circuits[i].toString() + generation);
            } else {
                //circuits[i].mutate(1 / bestFitness);  // Mutation rate
            }
        }
        System.out.println("Best fitness: " + fitness[bestIndex] + " ID: "+ circuits[bestIndex].toString());
        return circuits[bestIndex];
    }

    /**
     * Attempt to read a task- has incompleted circuit protections
     * 
     * @param task Task that may or may not contain a completed circuit
     * @return Circuit outputs
     */
    protected double[] readTask(Task task) {
        double[] results = new double[outputs];
        int attempt = 1;
        tryReadTask:
        while (attempt <= 10) {
            if(task.isFinished()) {
                results = task.getResults();
                break tryReadTask;
            } else {
                if(attempt != 1) System.out.println("Attempt #" + attempt + " waiting for circuit to process failed.");
                try {
                    Thread.sleep(100l);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            attempt++;
        }
        return results;
    }

    /**
     * Given a task, rate how well the member circuit performed.
     * May or may not use the given inputs.
     * 
     * @param task Task that contains a circuit (May not be completed)
     * @param inputs
     * @return Given fitness-- higher = better
     * @see 
     */
    protected abstract double evaluateFitness(Task task, double[] inputs);

    protected void sentinelLoop() {
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
                createCircuits();
                if(i != 0) {
                    Circuit bestLastGen = readCircuitFromFile("Generation " + (generation - 1));
                    circuits[0] = bestLastGen;
                }
                Circuit toWrite = doGeneration();
                this.writeCircuitToFile("Generation " + generation, toWrite);
                generation++;
            }
        }
    }

    public int getGeneration() {
        return generation;
    }
}