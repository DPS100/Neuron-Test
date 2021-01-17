package src;

public class Trainer implements Manager {
    Circuit[] circuits;
    final int size;
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
                generationSize = Integer.valueOf(System.console().readLine());
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
            circuits[i] = new Circuit(circuitInputs.length, new int[]{1});
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

    public static void main(String[] args) {
        Trainer trainer = new Trainer();
        for(int i = 0; i < trainer.size; i++) {
            int attempt = 1;
            tryReadTask:
            while (attempt <= 10) {
                try {
                    for (int j = 0; j < trainer.tasks[i].getResults().length; j++) {
                        System.out.println(trainer.tasks[i].getResults()[j]);
                    }
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
    }
}
