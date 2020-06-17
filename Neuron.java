/*
NEURON
Each neuron will consist of one soma, one axon, and one or more dendrites.
It will be able to communicate with other neurons in a circuit.
It will be a sensory neuron, interneuron, or motor neuron depending on the manager.
*/
public class Neuron{

    private int numOfDendrites;
    private double[] dendrites;
    //private boolean[] isExcitatory;
    private boolean[] hasRecieved;
    
 
    Neuron(int numOfDendrites) {
        this.numOfDendrites = numOfDendrites;
        dendrites = new double[numOfDendrites];
        //isExcitatory = new boolean[numOfDendrites];
        hasRecieved = new boolean[numOfDendrites];
    }

    /*public void assignExcitatory(int dendriteIndex, boolean assignment) {
        isExcitatory[dendriteIndex] = assignment;
    }*/

    private void assignRecieved(int dendriteIndex, boolean assignment) {
        hasRecieved[dendriteIndex] = assignment;
    }

    private boolean readyToFire() {
        for(int i = 0; i < numOfDendrites; i++) {
            if(!hasRecieved[i]) {return false;}//if any dendrites have not recieved an action potential, return false
        }
        return true; //only runs when all dendrites have recieved action potential
    }

    private void resetDendrites() {//should only be called AFTER all dendrites are fired
        for(int i = 0; i < numOfDendrites; i++) {
            hasRecieved[i] = false;
        } 
    }

    public void recieveActionPotential(double actionPotential, int dendriteIndex) {
        dendrites[dendriteIndex] = actionPotential;
        hasRecieved[dendriteIndex] = true;
        update();
    }

    private void update() {
        if(readyToFire()) {
            fire(calculateActionPotential());
        }
    }

    private double calculateActionPotential() {
        double sum = 0;
        for(int i = 0; i < numOfDendrites; i++) {
            sum += dendrites[i];//TODO add logic for excitatory / inhibitory dendrites
        }
        return sum / numOfDendrites;
    }

    private void fire(double actionPotential) {
        System.out.println(actionPotential);
        resetDendrites();
    }
}