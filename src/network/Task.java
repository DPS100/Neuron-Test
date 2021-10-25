package src.network;

public abstract class Task implements Runnable{

    protected Circuit circuit;
    private double fitness;
    private boolean finished = false;
    private Trainer myTrainer;
    
    protected Task(Circuit circuit, Trainer myTrainer) {
        this.circuit = circuit;
        this.myTrainer = myTrainer;
    }

    @Override
    public void run() {
		fitness = calcFitness();
		finished = true;
    }

    /**
     * This should return the fitness of the circuits.
     * WARNING: outputs may be null as the circuit has not finished processing.
     * I would recommend trying another task, or waiting for this one to finish.
     * @return fitness of the circuit (MAY BE NULL)
     */
    public double getFitness() {
        return fitness;
    }

	/**
	 * This will be called by @see Task.run()
	 * Circuits should process thier values and be graded accordingly here.
	*/
	protected abstract double calcFitness();

    public boolean isFinished() {
        return finished;
    }

	public Circuit getCircuit() {
		return circuit;
	}
}
