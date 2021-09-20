package src.network.TrainingTypes;

import src.network.Circuit;

/**
 * For use when training a single network in an environment that will process multiple times
 */
public abstract class IndividualMultirun {
    protected int inputs;
    protected int outputs;
    IndividualMultirun(int inputs, int outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    protected abstract double getFitnessFromCircuit(Circuit circuit);
}
