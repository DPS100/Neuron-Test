package src;

public class Trainer implements Manager {
    Circuit[] circuits;
    double[] scores;
    final int size;
    final int outputs = 1;
    int generation = 0;
    double[] circuitInputs;
    Task[] tasks;

    Trainer() {
        size = getGenerationSize();
        createCircuits();
        createTasks();
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
        circuitInputs = new double[] {1};
        for (int i = 0; i < size; i++) {
            circuits[i] = new Circuit(circuitInputs.length, new int[]{outputs});
        }
    }

    private void createTasks() {
        tasks = new Task[size];
        for(int i = 0; i < size; i++) {
            int attempt = 1;
            tryCreateCircuit:
            while(attempt <= 10) {
                try{
                    tasks[i] = startCircuitTask(circuits[i], circuitInputs, "Task #" + i);
                    break tryCreateCircuit;
                } catch (Exception e) {
                    System.out.println("Attempt #" + attempt + " waiting for circuit to be created failed");
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
        }
    }

    public void doGeneration() {
        int[][] results = new int[size][];
        createTasks();
        for(int i = 0; i < this.size; i++) {
            int attempt = 1;
            tryReadTask:
            while (attempt <= 10) {
                try {
                    results[i] = tasks[i].getResults();
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
        }
        evaluateFitness();
        generation++;
    }

    private void evaluateFitness() {
        //Put fitness function here
    }

    public void sentinelLoop() {
        watch:
        while(true) {
            System.out.println("Generation #" + generation);
            String input = System.console().readLine("Press y to continue, or anything else to stop: ");
            if(input.equals("y")) {
                doGeneration();
            } else {
                break watch;
            }
        }
    }

    public static void main(String[] args) {
        Trainer trainer = new Trainer();
        trainer.sentinelLoop();
    }
}