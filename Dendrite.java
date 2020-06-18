/*
Each dendrite will be created by the manager to connect neurons
A seperate neuron will assign a axon to apply any action potential
*/
public class Dendrite {

    private boolean isExcitatory;
    private boolean hasRecieved;
    private double actionPotential;
    private Neuron neuron;
    private double resistance;
    private String name;

    Dendrite(Neuron neuron, String name) {
        setNeuron(neuron);
        this.name = neuron.getName() + " Dendrite #" + name;
        isExcitatory = true;
        resistance = 0;
    }

    public void setActionPotential(double actionPotential) {
        this.actionPotential = actionPotential - resistance;
        if(!isExcitatory) {this.actionPotential *= -1;}
        hasRecieved = true;
        neuron.update();
    }

    public void setExcitatory(boolean isExcitatory) {
        this.isExcitatory = isExcitatory;
    }

    public void setHasRecieved(boolean hasRecieved) {
        this.hasRecieved = hasRecieved;
    }

    public void setNeuron(Neuron neuron) {
        this.neuron = neuron;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public Neuron getNeuron() {
        return neuron;
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

    public String getName() {
        return name;
    }

    public String toString() {
        return "Dendrite: " +
        "\nIs excitatory: " + isExcitatory +
        "\nHas recieved: " + hasRecieved +
        "\nAction potential: " + actionPotential;
    }
}