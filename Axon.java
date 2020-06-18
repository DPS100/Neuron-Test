/*
Each axon will be given 1+ dendrites
When a neuron fires, the axon will send the signal to all connected dendrites and update their neurons
*/

import java.util.ArrayList;

public class Axon {
    
    private ArrayList<Dendrite> dendrites;

    Axon() {
        dendrites = new ArrayList<Dendrite>();
    }

    public void sendActionPotential(double actionPotential, String name) {
        if(dendrites.size() > 0) {//if no dendrites are present, this is a motor neuron
            for(int i = 0; i < dendrites.size(); i++) {
                dendrites.get(i).setActionPotential(actionPotential);
            }
        } else {
            System.out.println("Neuron " + name + " outputs " + actionPotential);
        }
    }

    public void addDendrite(Dendrite dendrite) {
        dendrites.add(dendrite);
    }

    public String toString() {
        return "Axon: \nConnected dendrites: " + dendrites.size();
    }
}