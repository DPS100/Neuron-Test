/*
NEURON
Each neuron will consist of one axon that sends an action potential to the dendrites of connected neurons
and one or more dendrites that recieves action potentials from other neurons.
It will be set up by itself with one axon, and the manager will decide what other neurons get connected
*/

import java.util.ArrayList;

public class Neuron{

    private ArrayList<Dendrite> dendrites;
    private Axon axon;
    private String name;
    public enum Type{Inter, Motor, Sensory, Independant};

    Neuron(String name) {
        this.name = name;
        axon = new Axon();
        dendrites = new ArrayList<Dendrite>();
    }

    public void connectAxon(Neuron neuron, boolean isExcitatory) {
        axon.addDendrite(neuron.createDendrite(isExcitatory));//adds a dendrite created on neuron to the list axon keeps
    }

    public Dendrite createDendrite(boolean isExcitatory) {
        Dendrite dendrite = new Dendrite(this, Integer.toString(dendrites.size()));//makes a new dendrite on this neuron
        dendrite.setExcitatory(isExcitatory);
        dendrites.add(dendrite);
        return dendrite;
    }

    private boolean readyToFire() {
        for(int i = 0; i < dendrites.size(); i++) {
            if(!dendrites.get(i).getHasReceived()) {return false;}//if any dendrites have not recieved an action potential, return false
        }
        return true; //only runs when all dendrites have recieved action potential
    }

    private void resetDendrites() {//should be called after neuron has fired
        for(int i = 0; i < dendrites.size(); i++) {
            dendrites.get(i).resetDendrite();
        }
    }

    public void update() {
        if(readyToFire()) {
            fire(calculateActionPotential());
        }
    }

    private double calculateActionPotential() {
        double sum = 0;
        for(int i = 0; i < dendrites.size(); i++) {
            System.out.println("Adding " + dendrites.get(i).getActionPotential());
            sum += dendrites.get(i).getActionPotential();
            System.out.println("Got " + sum);
        }
        sum /= dendrites.size();
        if(sum < 0) {sum = 0;}//neurons should only fire a positive value
        if(sum > 1) {sum = 1;}
        System.out.println("Total: " + sum);
        return sum;
    }

    private void fire(double actionPotential) {
        axon.sendActionPotential(actionPotential);
        resetDendrites();
    }

    public void setAxon(Axon axon) {
        this.axon = axon;
    }

    public Axon getAxon() {
        return axon;
    }

    public Dendrite getDendrite(int index) {
        return dendrites.get(index);
    }

    public String getName() {
        return "Neuron #" + name;
    }

    public String toString() {
        return "Neuron:" +
        "\nNumber of Dendrites: " + dendrites.size() +
        "\nDendrites:" + //TODO loop through all dendrites
        "\nAxon:" + axon +
        "\nReady to fire? " + readyToFire();
    }
}