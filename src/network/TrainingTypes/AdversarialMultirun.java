package src.network.TrainingTypes;

import src.network.Circuit;
import src.network.Manager;

/**
 * For use when training multiple networks against each other in an environment that will process multiple times
 */
public abstract class AdversarialMultirun implements Manager {
    private int inputs;
    private int outputs;
    private int agents;
    protected AdversarialMultirun(int inputs, int outputs, int agents) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.agents = agents;
    }

    protected abstract double[] getFitnessFromCircuits(Circuit[] circuits);
}
