package src;

public class Task implements Runnable{

    private Circuit circuit;
    private double[] inputValues;
    private int[] outputs;
    private boolean finished = false;
    
    Task(Circuit circuit, double[] inputValues) {
        this.circuit = circuit;
        this.inputValues = inputValues;
    }

    @Override
    public void run() {
        try{
            outputs = circuit.process(inputValues);
            finished = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This should return the results of the circuit.
     * WARNING: outputs may be null as the circuit has not finished processing.
     * I would recommend trying another task, or waiting for this one to finish.
     * @return results of the circuit
     */
    public int[] getResults() {
        return outputs;
    }

    public boolean isFinished() {
        return finished;
    }
}
