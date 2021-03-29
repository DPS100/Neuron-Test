package src;

public class Task implements Runnable{

    private Circuit circuit;
    private double[] inputValues;
    private double[] outputs;
    private boolean finished = false;
    private String circuitName;
    
    Task(Circuit circuit, double[] inputValues) {
        this.circuit = circuit;
        this.inputValues = inputValues;
        this.circuitName = circuit.toString();
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
    public double[] getResults() {
        return outputs;
    }

    public boolean isFinished() {
        return finished;
    }

    public String circuitName() {
        return circuitName;
    }
}
