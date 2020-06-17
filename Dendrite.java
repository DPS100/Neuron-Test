/*
Each dendrite will be created by a neuron
A seperate neuron will assign a axon to apply any action potential
*/
public class Dendrite {

    private boolean isExcitatory;
    private boolean hasRecieved;
    private boolean isAssigned;
    private double actionPotential;
    private Neuron neuron;

    Dendrite() {
        setExcitatory(true);
        setAssigned(false);
    }

    public void setActionPotential(double actionPotential) {
        this.actionPotential = actionPotential;
        hasRecieved = true;
        neuron.update();
    }

    public void setExcitatory(boolean isExcitatory) {
        this.isExcitatory = isExcitatory;
    }

    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public void setHasRecieved(boolean hasRecieved) {
        this.hasRecieved = hasRecieved;
    }

    public void setNeuron(Neuron neuron) {
        this.neuron = neuron;
    }

    public Neuron getNeuron() {
        return neuron;
    }

    public boolean getAssigned() {
        return isAssigned;
    }

    public double getActionPotential() {
        return actionPotential;
    }

    public boolean getIsExcitatory() {
        return isExcitatory;
    }

    public boolean getHasReceived() {
        return hasRecieved;
    }

    public void resetDendrite() {
        hasRecieved = false;
    }

    public String toString() {
        return "Dendrite: " +
        "\nIs assigned: " + isAssigned +
        "\nIs excitatory: " + isExcitatory +
        "\nHas recieved: " + hasRecieved +
        "\nAction potential: " + actionPotential;
    }
}