/*
NEURON
Each neuron will consist of one axon that sends an action potential to the dendrites of connected neurons
and one or more dendrites that recieves action potentials from other neurons.
It will be set up and connected to other neurons through the manager.
*/

public class Neuron{

    private Dendrite[] dendrites;
    private Axon axon;
    
    /**
     * This is a interneuron, and needs sensory and motor neurons to function
     * @param neuronsRecievengFromMe
     * @param neuronsSendingToMe
     */
    Neuron(Neuron[] neuronsRecievengFromMe, int neuronsSendingToMe) {
        createDendrites(neuronsSendingToMe);
        axon = new Axon(connectAxon(neuronsRecievengFromMe, neuronsRecievengFromMe.length));
    }

    /**
     * This is a motor neuron, and can be made with no recievers
     * @param neuronsSendingToMe
     */
    Neuron(int neuronsSendingToMe) {
        createDendrites(neuronsSendingToMe);
        axon = new Axon();
    }
    
    /**
     * This is a sensory neuron, and can be made with no sending neurons
     * @param dendrite
     * @param neuronsRecievengFromMe
     */
    Neuron(Dendrite dendrite, Neuron[] neuronsRecievengFromMe) {
        dendrites = new Dendrite[]{dendrite};
        dendrite.setNeuron(this);
        dendrites[0].setNeuron(this);
        axon = new Axon(connectAxon(neuronsRecievengFromMe, neuronsRecievengFromMe.length));
    }

    /**
     * This is a standalone neuron, and should only be used as the singular neuron in a circuit
     * @param dendrite
     */
    Neuron(Dendrite dendrite) {
        dendrites = new Dendrite[]{dendrite};
        dendrites[0].setNeuron(this);
        axon = new Axon();
    }

    private Dendrite[] connectAxon(Neuron[] neurons, int neuronsRecievengFromMe) {
        Dendrite[] connectedDendrites = new Dendrite[neuronsRecievengFromMe];
        for(int i = 0; i < connectedDendrites.length; i++) {//finds next unassinged dendrite
            connectedDendrites[i] = neurons[i].getUnassignedDendrite();
        }
        return connectedDendrites;
    }

    private void createDendrites(int length) {
        dendrites = new Dendrite[length];
        for(int i = 0; i < length; i++) {
            dendrites[i] = new Dendrite();
            dendrites[i].setAssigned(false);
            dendrites[i].setNeuron(this);
        }
    }

    public Dendrite getUnassignedDendrite() {
        for(int i = 0; i < dendrites.length; i++) {
            if(!dendrites[i].getAssigned()) {
                dendrites[i].setAssigned(true);
                return dendrites[i];
            }
        }
        System.out.println("ERROR: No unassigned dendrites found");
        return new Dendrite();
    }

    private boolean readyToFire() {
        for(int i = 0; i < dendrites.length; i++) {
            if(!dendrites[i].getHasReceived()) {return false;}//if any dendrites have not recieved an action potential, return false
        }
        return true; //only runs when all dendrites have recieved action potential
    }

    private void resetDendrites() {//should be called after neuron has fired
        for(int i = 0; i < dendrites.length; i++) {
            dendrites[i].resetDendrite();
        }
    }

    public void update() {
        if(readyToFire()) {
            fire(calculateActionPotential());
        }
    }

    private double calculateActionPotential() {
        double sum = 0;
        for(int i = 0; i < dendrites.length; i++) {
            if(dendrites[i].getIsExcitatory()) {sum += dendrites[i].getActionPotential();}
            else {sum -= dendrites[i].getActionPotential();}
        }
        if(sum < 0) {sum = 0;}//neurons should only fire a positive value
        return sum / dendrites.length;
    }

    private void fire(double actionPotential) {
        //System.out.println(actionPotential);
        //System.out.println(this.toString());
        axon.sendActionPotential(actionPotential);
        resetDendrites();
    }

    public void setAxon(Axon axon) {
        this.axon = axon;
    }

    public Axon getAxon() {
        return axon;
    }

    public String toString() {
        return "Neuron:" +
        "\nNumber of Dendrites: " + dendrites.length +
        "\nDendrites:" + //TODO loop through all dendrites
        "\nAxon:" + axon +
        "\nReady to fire? " + readyToFire();
    }
}