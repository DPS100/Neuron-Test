/*
Each axon will be given 1+ dendrites at construction
When a neuron fires, the axon will send the signal to all connected dendrites and update their neurons
*/
public class Axon {
    
    private Dendrite[] dendrites;
    private boolean isMotorAxon;

    Axon(Dendrite[] dendrites) {
        this.dendrites = dendrites;
        isMotorAxon = false;
    }

    Axon() {
        isMotorAxon = true;
    }

    public void sendActionPotential(double actionPotential) {
        if(!isMotorAxon) {
            for(int i = 0; i < dendrites.length; i++) {
                dendrites[i].setActionPotential(actionPotential);
            }
        } else {
            System.out.println("Motor Axon output:" + actionPotential);
        }
        
    }

    public String toString() {
        return "Axon: \nConnected dendrites: " + dendrites.length;
    }
}