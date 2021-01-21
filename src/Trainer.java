package src;

public class Trainer implements Manager {
    Circuit[] circuits;
    double[] fitness;
    final int size;
    final int outputs = 10;
    double bestFitness = 0;
    int generation = 0;
    double[] circuitInputs;
    Task[] tasks;

    Trainer() {
        size = getGenerationSize();
        circuitInputs = new double[]{1.0,1.0};
        createCircuits();
        tasks = new Task[size];
    }

    private int getGenerationSize() {
        int generationSize;
        while(true) {
            try{
                generationSize = Integer.valueOf(System.console().readLine("Enter the generation size: "));
                break;
            } catch(Exception e) {
                System.out.println("Invalid entry");
            }
        }
        return generationSize;
    }

    private void createCircuits() {
        circuits = new Circuit[size];
        fitness = new double[size];
        for (int i = 0; i < size; i++) {
            circuits[i] = new Circuit(circuitInputs.length, new int[]{10, outputs});
        }
    }

    private void createTasks() {
        for(int i = 0; i < size; i++) {
            tasks[i] = startCircuitTask(circuits[i], circuitInputs, "Task #" + i);
        }
    }

    public Circuit doGeneration() {
        int maxTicks = 1;
        for(int tick = 0; tick < maxTicks; tick++) {
            createTasks();
            for(int i = 0; i < size; i++) {
                fitness[i] = evaluateFitness(tasks[i], circuitInputs);
            }
        }
        int bestIndex = 0;
        for(int i = 0; i < size; i++) {
            if(fitness[i] < fitness[bestIndex]) {
                bestIndex = i;
            } if (fitness[i] == 0) {
                circuits[i].mutate(1);
            } else {
                circuits[i].mutate(1 / bestFitness);  // Mutation rate
            }
        }
        System.out.println("Best fitness: " + fitness[bestIndex]);
        return circuits[bestIndex];
    }

    private int[] readTask(Task task) {
        int[] results = new int[outputs];
        int attempt = 1;
        tryReadTask:
        while (attempt <= 10) {
            if(task.isFinished()) {
                results = task.getResults();
                break tryReadTask;
            } else {
                System.out.println("Attempt #" + attempt + " waiting for circuit to process failed.");
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

    private double evaluateFitness(Task task, double[] inputs) {
        double sum = 0;
        int[] results = readTask(task);
        for(int i = 0; i < results.length; i++) { // Temporary code to see if circuits evolve
            sum += results[i];
        }
        return sum;
    }

    public void sentinelLoop() {
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
                Circuit toWrite = doGeneration();
                this.writeCircuitToFile("Generation " + generation, toWrite);
                generation++;
            }
        }
    }

    public static void main(String[] args) {
        Trainer trainer = new Trainer();
        trainer.sentinelLoop();
    }
}