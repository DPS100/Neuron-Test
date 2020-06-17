/*
NEURON
Each neuron will consist of one or more dendrites that recieve action potentials (doubles) from other neurons.
It will be able to communicate with other assigned neurons through the manager.
*/
public class Neuron{

    private boolean isManualInput;
    private int numOfDendrites;
    private double[] dendrites;
    private boolean[] isExcitatory;
    private boolean[] hasRecieved;
    
    Neuron(int numOfDendrites) {
        isManualInput = false;//manual input is false by defauly
        this.numOfDendrites = numOfDendrites;
        dendrites = new double[numOfDendrites];
        isExcitatory = new boolean[numOfDendrites];
        setAllDendrites(true);//this is temporary code just to set all dendrites to excitatory
        hasRecieved = new boolean[numOfDendrites];
    }

    Neuron(int numOfDendrites, boolean isManualInput) {
        this.isManualInput = isManualInput;
        this.numOfDendrites = numOfDendrites;
        dendrites = new double[numOfDendrites];
        isExcitatory = new boolean[numOfDendrites];
        setAllDendrites(true);//this is temporary code just to set all dendrites to excitatory
        hasRecieved = new boolean[numOfDendrites];
    }

    private void setAllDendrites(boolean assignment) {
        for(int i = 0; i < numOfDendrites; i++) {
            assignExcitatory(i, assignment);
        }
    }

    public void assignExcitatory(int dendriteIndex, boolean assignment) {
        isExcitatory[dendriteIndex] = assignment;
    }

    private boolean readyToFire() {
        for(int i = 0; i < numOfDendrites; i++) {
            if(!hasRecieved[i]) {return false;}//if any dendrites have not recieved an action potential, return false
        }
        return true; //only runs when all dendrites have recieved action potential
    }

    private void resetDendrites() {//should be called after neuron has fired
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
            if(isExcitatory[i]) {sum += dendrites[i];}
            else {sum -= dendrites[i];}
        }
        if(sum < 0) {sum = 0;}//neurons should only fire a positive value
        return sum / numOfDendrites;
    }

    private void fire(double actionPotential) {
        System.out.println(actionPotential);
        System.out.println(this.toString());
        resetDendrites();
    }

    public String toString() {
        return "Number of Dendrites: " + numOfDendrites +
        "\nDendrite values: " + dendrites.toString() +
        "\nDendrite type: " + isExcitatory.toString() +
        "\nDendrite recieved:" + hasRecieved.toString() +
        "\nReady to fire? " + readyToFire();
    }
}