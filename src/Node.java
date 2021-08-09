package src;

/**
 * Also referred to as a neuron, Nodes are objects that can process inputs and connect together to form an input
 */
public class Node{

    public enum NodeState{ 
        UNPROCESSED(-1),
        INACTIVE(0),
        ACTIVE(1);

        public final int value;

        private NodeState(int value) {
            this.value = value;
        }
    }

    private final Node[] outputs; // Node array this node will output to
    private double[] outputStrengthFactors; // Strengths of the outputs this node will send
    private final int inputs; // Number of nodes sending signals to this node
    private double inputSum;
    private boolean[] inputHasFired;
    private double threshold;
    private NodeState state;
    

    /**
     * This constructor makes a node with 1 or more target connecting input and output nodes
     * @param outputs Array of nodes this node will send signals to
     * @param outputStrengthFactor The array of factors (between -1 and 1) that each signal will be multiplied by
     * @param inputs Number of nodes that will be sending signals to this node
     * @param threshold The sum of the inputs that will be needed to exceeded to fire 
     */
    Node(Node[] outputs, double[] outputStrengthFactor, int inputs, double threshold) {
        this.outputs = outputs;
        this.outputStrengthFactors = outputStrengthFactor;
        this.inputs = inputs;
        inputSum = 0;
        inputHasFired = new boolean[inputs];
        setUnfired(inputHasFired);
        this.threshold = threshold;
        state = NodeState.UNPROCESSED;
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
        if(inputSum / inputs >= threshold) { // Check if each input is enough to overcome threshold
            this.state = NodeState.ACTIVE;
        } else {
            this.state = NodeState.INACTIVE;
        }
    }

    /**
     * Method to send signal to each recieving node.
     * Should only be called once all input nodes have fired
     */
    private void sendSignals() {
        if(outputs.length > 0) { // Only works if there is > 1 output node
            for(int i = 0; i < outputs.length; i++) { // Send each connected node a signal
                outputs[i].recieveSignal(state.value * outputStrengthFactors[i]); // Signal is this node's state (0 or 1) multiplied by the output strengh factor
            }
        }
    }

    /**
     * Method to tell a node to recieve a specified signal
     * @param signalStrength Strength of the signal this node should recieve
     */
    public void recieveSignal(double signalStrength) {
        int i = 0;
        fillInputArraySlot:
        while(i < inputs) { // Find next open spot in input fired array, and change it to true
            if(inputHasFired[i] == false) {
                inputHasFired[i] = true;
                inputSum += signalStrength; // Update signal strength
                break fillInputArraySlot;
            }
            i++;
        }

        i = 0;
        boolean shouldProcess = true;
        checkAllFired:
        while(i < inputs) { // Check to see if all inputs have fired
            if(inputHasFired[i] == false) {
                shouldProcess = false;
                break checkAllFired;
            }
            i++;
        }
        
        if(shouldProcess) { // If all inputs are completed
            process();
            sendSignals();
        }
    }

    /**
     * @return 0 if inactive, or the current output otherwise
     */
    public double getOutputNodeResult() {
        if(state == NodeState.INACTIVE) {
            return 0;
        } else {
            return inputSum / inputs;
        }
    }

    /**
     * @return Current state of this node.
     */
    public NodeState getState() {
        return state;
    }

    /**
     * @return The threshold this node must be greater than or equal to to be active
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * @return the value from -1/1 for the percentage strength of all the inputs
     */
    public double getInputPercentage() {
        return inputSum/inputs;
    }

    /**
     * @param index Index of the output node
     * @return Strength factor for specified output node
     */
    public double getOutputStrengthFactor(int index) {
        return outputStrengthFactors[index];
    }

    /**
     * @param threshold The threshold this node must be greater than or equal to to be active
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    /**
     * @param index Index of the output node
     * @param strengthFactor Strength factor for specified output node
     */
    public void setOutputStrengthFactor(int index, double strengthFactor) {
        outputStrengthFactors[index] = strengthFactor;
    }

    /**
     * Lets this node recieve new inputs
     */
    public void setCleared() {
        inputSum = 0;
        for(int i = 0; i < inputHasFired.length; i++) {
            inputHasFired[i] = false;
        }
    }
}
