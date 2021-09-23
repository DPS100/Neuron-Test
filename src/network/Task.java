package src.network;

public abstract class Task implements Runnable{

    protected Circuit[] circuits;
    private double[] fitness;
    private boolean finished = false;
    private String[] circuitNames;
    
    protected Task(Circuit[] circuits) {
        this.circuits = circuits;
		this.circuitNames = new String[circuits.length];
		for(int i = 0; i < circuits.length; i++) {
			circuitNames[i] = circuits[i].toString();
		}
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
    public double[] getFitness() {
        return calcFitness();
    }

	/**
	 * This will be called by @see Task.run()
	 * Circuits should process thier values and be graded accordingly here.
	*/
	protected abstract double[] calcFitness();

    public boolean isFinished() {
        return finished;
    }

    public String[] circuitNames() {
        return circuitNames;
    }

	public Circuit[] getCircuits() {
		return circuits;
	}
}
