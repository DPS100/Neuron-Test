package src.network.TrainingTypes;

import src.network.Circuit;

/**
 * For use when training a single network in an environment that will process a single time
 */
public abstract class IndividualSinglerun {
    protected int inputs;
    protected int outputs;
    IndividualSinglerun(int inputs, int outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    protected abstract double getFitnessFromCircuits(Circuit circuit);
}
