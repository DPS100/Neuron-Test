public class Node {

    private final Node[] outputs;
    private final double[] outputStrengthFactor;
    private final int inputs;
    private double inputSum;
    private boolean[] inputHasFired;
    private final double threshold;
    private int state;

    /**
     * This constructor makes a node with 1 or more target connecting input and output nodes
     * @param outputs Array of nodes this node will send signals to
     * @param outputStrengthFactor The array of factors (between 0 - 1) that each signal will be multiplied by
     * @param inputs Number of nodes that will be sending signals to this node
     * @param threshold The sum of the inputs that will be needed to exceeded to fire 
     */
    Node(Node[] outputs, double[] outputStrengthFactor, int inputs, double threshold) {
        this.outputs = outputs;
        this.outputStrengthFactor = outputStrengthFactor;
        this.inputs = inputs;
        inputSum = 0;
        setUnfired(inputHasFired);
        this.threshold = threshold;
        state = -1;
    }

    /**
     * Method to set an entire array's contents to unfired.
     * Should only be called in the constructor on <inputHasFired>
     * @param targetArray Array whose contents should be set to unfired
     */
    private void setUnfired(boolean[] targetArray) {
        for(int i = 0; i < targetArray.length; i++) {
            targetArray[i] = false;
        }
    }

    /**
     * Method to process whether the inputs overcome the threshold.
     * Should only be called once all input nodes have fired
     */
    private void process() {
        if(inputSum >= threshold) { // Check if each input is enough to overcome threshold
            this.state = 1; // Over threshold, set state to active
        } else {
            this.state = 0; // Under threshold, set state to inactive
        }
    }

    /**
     * Method to send signal to each recieving node.
     * Should only be called once all input nodes have fired
     */
    private void sendSignals() {
        for(int i = 0; i < outputs.length; i++) { // Send each connected node a signal
            outputs[i].recieveSignal(state * outputStrengthFactor[i]); // Signal is this node's activity multiplied by the output strengh factor
        }
    }

    /**
     * Method to tell a node to recieve a specified signal
     */
    public void recieveSignal(double signalStrength) {
        inputSum += signalStrength;

        boolean acc = true;
        int i = 0;
        while(acc && i < inputs) { // Check to see if all inputs have fired
            if(inputHasFired[i] = false) {
                acc = false;
            }
            i++;
        }
        
        if(acc) { // If all inputs are completed
            process();
            sendSignals();
        }
    }

    /**
     * @return -1 for unprocessed, 0 for inactive, 1 for active
     */
    public int getState() {
        return state;
    }
}