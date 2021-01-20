package src;

public class Trainer implements Manager {
    Circuit[] circuits;
    double[] fitness;
    final int size;
    final int outputs = 1;
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
            circuits[i] = new Circuit(circuitInputs.length, new int[]{11, outputs});
        }
    }

    private void createTasks() {
        for(int i = 0; i < size; i++) {
            tasks[i] = startCircuitTask(circuits[i], circuitInputs, "Task #" + i);
        }
    }

    public void doGeneration() {
        createTasks();
        for(int i = 0; i < size; i++) {
            fitness[i] = evaluateFitness(readTask(tasks[i]), circuitInputs);
            circuits[i].mutate(fitness[i]);
        }
        
        generation++;
    }

    private int[] readTask(Task task) {
        int[] results = new int[outputs];
        int attempt = 1;
        tryReadTask:
        while (attempt <= 10) {
            try {
                results = task.getResults();
                break tryReadTask;
            } catch (Exception e) {
                System.out.println("Attempt #" + attempt + " waiting for circuit to process failed.");
                e.printStackTrace();
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } finally {
                attempt++;
            }
        }
        return results;
    }

    private double evaluateFitness(int[] results, double[] inputs) {
        double sum = 0;
        for(int i = 0; i < results.length; i++) { // Temporary code to see if circuits evolve
            sum += results[i];
        }
        System.out.println("Fitness is " + sum);
        return 1 - sum; // Mutation rate
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
                doGeneration();
            }
        }
    }

    public static void main(String[] args) {
        Trainer trainer = new Trainer();
        trainer.sentinelLoop();
    }
}