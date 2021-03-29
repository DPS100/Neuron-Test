package src;

public abstract class Trainer implements Manager {
    private Circuit[] circuits;
    private double[][] circuitInputs;
    private double[] fitness;
    public final int generationSize;
    public final int inputs;
    public final int[] layerSize;
    private int generation = 0;
    private Task[] tasks;

    Trainer(int generationSize, int inputs, int[] layerSize) {
        this.generationSize = generationSize;
        this.inputs = inputs;
        this.layerSize = layerSize;
        circuitInputs = new double[generationSize][inputs];
        tasks = new Task[generationSize];
    }

    private void createCircuits() {
        circuits = new Circuit[generationSize];
        fitness = new double[generationSize];
        for (int i = 0; i < generationSize; i++) {
            circuits[i] = new Circuit(inputs, layerSize, ("#" + i + ", gen" + generation + ", mutations:"));
        }
    }

    private void createTasks() {
        for(int i = 0; i < generationSize; i++) {
            tasks[i] = startCircuitTask(circuits[i], circuitInputs[i], "Task #" + i);
        }
    }

    private Circuit doGeneration() {
        int maxTicks = 1;
        for(int tick = 0; tick < maxTicks; tick++) {
            createTasks();
            for(int i = 0; i < generationSize; i++) {
                fitness[i] = evaluateFitness(tasks[i], circuitInputs[i]);
            }
        }
        int bestIndex = 0;
        for(int i = 0; i < generationSize; i++) {
            if(fitness[i] > fitness[bestIndex]) {
                bestIndex = i;
            } if (fitness[i] == 0) {
                circuits[i].mutate(1);
                circuits[i].setID(circuits[i].toString() + generation + ',');
            } else {
                //circuits[i].mutate(1 / bestFitness);  // Mutation rate
            }
        }
        System.out.println("Best fitness: " + fitness[bestIndex] + " ID: "+ circuits[bestIndex].toString());
        return circuits[bestIndex];
    }

    /**
     * Given a task, rate how well the member circuit performed.
     * May or may not use the given inputs.
     * 
     * @param task Task that contains a circuit (May not be completed)
     * @param inputs
     * @return Given fitness-- higher is better
     * @see Manager.readTask
     */
    protected abstract double evaluateFitness(Task task, double[] inputs);

    /**
     * Define custom inputs for each circuit
     * 
     * @param circuitInputs Array which holds curcuit inputs which has yet to be filled
     */
    protected abstract void fillInputs(double[][] circuitInputs);

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
                fillInputs(circuitInputs);
                // FIXME reproduction/mutation step not defined
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

    /**
     * Attempt to get user to enter an integer. Will end the program on unknown error, 
     * but handle uncastable string by prompting user again.
     * 
     * @param text Question to prompt the user with
     * @return User answer in double form
     */
    public static double getDoubleFromUser(String text) {
        double value = 0.0;
        boolean valid = false;
        while(!valid) {
            try {
                value = Double.parseDouble(System.console().readLine(text));
                valid = true;
            } catch(NumberFormatException e) {
                System.out.println("Could not cast input to double. Please try again.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An unknown error occurred. Terminating program.");
                System.exit(1);
            }
        }
        return value;
    }

    public int getGeneration() {
        return generation;
    }
}