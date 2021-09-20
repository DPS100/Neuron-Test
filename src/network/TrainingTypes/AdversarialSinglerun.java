package src.network.TrainingTypes;

import src.network.Circuit;

/**
 * For use when training multiple networks against each other in an environment that will process a single time
 */
public abstract class AdversarialSinglerun {
    protected int inputs;
    protected int outputs;
    protected int agents;
    AdversarialSinglerun(int inputs, int outputs, int agents) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.agents = agents;
    }

    protected abstract double[] getFitnessFromCircuits(Circuit[] circuits);
}
