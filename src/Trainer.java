package src;

import java.io.File;

public class Trainer implements Manager {
    Circuit[] circuits;
    final int size;
    static double[] circuitInputs;
    static Task[] tasks;

    Trainer(int size) {
        this.size = size;
        circuits = new Circuit[size];
        circuitInputs = new double[] {1, 1};
        for (int i = 0; i < size; i++) {
            circuits[i] = new Circuit(circuitInputs.length, new int[]{1,1});
        }

        tasks = new Task[size];
        for(int i = 0; i < size; i++) {
            int attempt = 1;
            while(attempt <= 10) {
                try{
                    tasks[i] = startCircuitTask(circuits[i], circuitInputs, "Task #" + i);
                    attempt = 11;
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
        new Trainer(10000);
        for(int i = 0; i < 10000; i++) {
            int attempt = 1;
            while (attempt <= 10) {
                try {
                    for (int j = 0; j < tasks[i].getResults().length; j++) {
                        System.out.println(tasks[i].getResults()[j]);
                    }
                    attempt = 11;
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
