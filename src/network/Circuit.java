package src.network;

import java.util.Random;
import src.*;

public class Circuit{

    private static Random generator = new Random(1234);

    private String id; // Given when Node is created
    private int[] layerSize;
    private double[][] connectionStrength; // Array of doubles that each output is multiplied by. Includes inputs to next but no outputs. Each array length is previous node layer size * next node layer size
    private double[][] thresholds; // The net threshold each node must pass to become active.

    /**
     * This constructor needs manual values, and will not generate it's own.
     * A circuit consists of one "artificial" layer made up of inputs,
     * and specified layer sizes that make up normal layers (includes the output nodes).
     * @param inputs Number of inputs
     * @param layerSize The size of each layer (Must have at least one value)
     */
    Circuit(int inputs, int[] layerSize, double[][] thresholds, double[][] connectionStrength, String id) {
        this.inputs = inputs;
        this.layerSize = layerSize;
        setupDefaultValues(layerSize);

        this.thresholds = thresholds;
        this.connectionStrength = connectionStrength;

        createNodes();
        this.id = id;
    }

    /**
     * This constructor will generate it's own values
     * A circuit consists of one "artificial" layer made up of inputs,
     * and specified layer sizes that make up normal layers (includes the output nodes).
     * @param inputs Number of inputs
     * @param layerSize The size of each layer (Must greater than or equal to 1)
     */
    Circuit(int inputs, int[] layerSize, String id) {
        this.inputs = inputs;
        this.layerSize = layerSize;
        setupDefaultValues(layerSize);
        generateValues(layerSize);

        createNodes();
        this.id = id;
    }

    /**
     * For use with any Circuit constructor to initialize inputs and layersize
     * @param inputs Number of inputs
     * @param layerSize The size of each layer (Must greater than or equal to 1)
     */
    private void setupDefaultValues(int[] layerSize) {
        layers = new Node[layerSize.length][];
        for(int i = 0; i < layerSize.length; i++) { // Set each layer to the correct size
            layers[i] = new Node[layerSize[i]];
        }
    }

    /**
     * Make this circuit generate it's own 2D arrays for thresholds and connection strengths
     * @param inputs Number of inputs
     * @param layerSize The size of each layer (Must greater than or equal to 1)
     */
    private void generateValues(int[] layerSize) {
        generateThresholds(inputs, layerSize);
        generateConnectionStrengths(inputs, layerSize);
    }

    private void generateThresholds(int inputs, int[] layerSize) {
        double[][] thresholds = new double[layerSize.length][];
        for(int x = 0; x < layerSize.length; x++) { // Threshold at position [x,y] will have a random value (double) between 0 and 1 (including 0)
            thresholds[x] = new double[layerSize[x]];

            for(int y = 0; y < thresholds[x].length; y++) {
                thresholds[x][y] = Math.random();
            }
        }
        this.thresholds = thresholds;
    }

    private void generateConnectionStrengths(int inputs, int[] layerSize) {
        double[][] connectionStrength = new double[layerSize.length][];
        for(int i = 0; i < connectionStrength.length; i++) {
            if (i == 0) { // Connection between input and first layer
                connectionStrength[i] = new double[inputs * layerSize[i]]; // Total connections = inputs * first layer nodes
            } else { // Connection between current layer and next layer
                connectionStrength[i] = new double[layerSize[i - 1] * layerSize[i]]; // Total connections = current layer nodes * next layer nodes
            }
        }

        for(int x = 0; x < connectionStrength.length; x++) { // Connection Strength at position [x,y] will have a random value (double) between -1 and 1 (including -1)
            for(int y = 0; y < connectionStrength[x].length; y++) {
                connectionStrength[x][y] = Math.random() * 2 - 1;
            }
        }
        this.connectionStrength = connectionStrength;
    }

    /**
     * Creates each node in the circuit and connects them.
     * Should only be called by the constructor.
     */
    private void createNodes() {
        for(int x = layers.length - 1; x >= 0; x--) { // Place on x - axis. Works right to left to not overwrite previously adressed nodes
            for(int y = 0; y < layers[x].length; y++) { // Place on y - axis

                int nextLayerSize;
                if(x == layers.length - 1) { // Check if this is the output layer
                    nextLayerSize = 0; // Output layer has no outputs
                } else {
                    nextLayerSize = layers[x + 1].length;
                }

                Node[] connectedNodes = new Node[nextLayerSize]; // Array for current (x,y) node connections
                double[] connectedNodeStrengths = new double[nextLayerSize]; // Array for current (x,y) node connection strengths
                for(int y2 = 0; y2 < nextLayerSize; y2++) { // Place on next y - axis interval (y2)
                    connectedNodes[y2] = layers[x + 1][y2]; // Place the next layer node in connected node array
                    connectedNodeStrengths[y2] = connectionStrength[x + 1][y2 + (nextLayerSize * y)]; // Place the connection in connection array at position for this node -> next layer node
                }

                Node node;
                if(x == 0) { // First node layer relies on "artificial" input layer
                    node = new Node(connectedNodes, connectedNodeStrengths, inputs, thresholds[x][y]);
                } else {
                    node = new Node(connectedNodes, connectedNodeStrengths, layers[x-1].length, thresholds[x][y]);
                }
                
                layers[x][y] = node;
            }
        }
    }

    /**
     * See what the circuit outputs with a selection of input values.
     * @param inputValues Needs to be the same length as inputs.
     * @return Array of doubles
     */
    public synchronized double[] process(double[] inputValues) {
        clearCircuit();
        for(int y = 0; y < inputValues.length; y++) { // Place on "artificial" layer
            for(int y2 = 0; y2 < layers[0].length; y2++) { // Place on first node layer
                layers[0][y2].recieveSignal(inputValues[y] * connectionStrength[0][y2 + (layers[0].length * y)]); // Send the signal to each node in first layer
            }
        }

        double[] outputs = new double[layers[layers.length - 1].length]; // Create an int array with length of output layer
        for(int i = 0; i < outputs.length; i++) {
            outputs[i] = layers[layers.length - 1][i].getOutputNodeResult(); // Fill array with the average of each output node
        }
        return outputs;
    }

    /**
     * Mutates this circuit
     * @param mutationChance Chance that circuit part will mutate
     * @param mutationRate Maximum change at which circuit could mutate (0 = no change, 1 = no limit to change)
     */
    public synchronized void mutate(double mutationChance, double mutationRate) {
		Main.debugLog("Mutating " + this.toString());
        if(mutationChance > 1) mutationChance = 1;
        if(mutationChance < 0) mutationChance = 0;
        if(mutationRate > 1) mutationRate = 1;
        if(mutationRate < 0) mutationRate = 0;
        mutateConnections(mutationChance, mutationRate);
        mutateThresholds(mutationChance, mutationRate);
        createNodes();
    }

    /**
     * Creates a cloned child, then mutates the child
     * @param mutationRate Rate at which child will mutate (0 = not at all, 1 = fully mutated)
     * @param childID Name of the new child
     * @return Mutated child
     */
    public synchronized Circuit createMutatedChild(double mutationChance, double mutationRate, String childID) {
        if(mutationRate > 1) mutationRate = 1;
        if(mutationRate < 0) mutationRate = 0;
        Circuit child = this.clone();
		child.setID(childID);
        child.mutate(mutationChance, mutationRate);
        return child;
    }

    private void mutateConnections(double mutationChance, double mutationRate) {
        for(int x = 0; x < connectionStrength.length; x++) {
            for(int y = 0; y < connectionStrength[x].length; y++) {
                if(Math.random() < mutationChance) {
                    //connectionStrength[x][y] = generateGaussianTransformed(3, 1.0/3.0, 0) * mutationRate; // from -1/1
                    //connectionStrength[x][y] = (Math.random() * 2 - 1) * mutationRate; // from -1/1
					connectionStrength[x][y] = connectionStrength[x][y] + (Math.random() * 2 - 1) * mutationRate;
					// Changes +/- (1 * mutationRate)
					//Main.debugLog("Generated " )
                }
            }
        }
		//Main.debugLog(connectionStrength[0][0]);
    }

    private void mutateThresholds(double mutationChance, double mutationRate) {
        for(int x = 0; x < thresholds.length; x++) {
            for(int y = 0; y < thresholds[x].length; y++) {
                if(Math.random() < mutationChance) {
                    //thresholds[x][y] = generateGaussianTransformed(3, 1.0/6.0, 1) * mutationRate; // from 0/1
                    //thresholds[x][y] = Math.random() * mutationRate; // from 0/1
					double change = (Math.random() * 2 - 1) * mutationRate;
					Main.debugLog("Changing " + this.toString() + " by " + change);
					thresholds[x][y] = thresholds[x][y] + change;
					// Changes +/- (1 * mutationRate)
                }
            }
        }
		//Main.debugLog(thresholds[0][0]);
    }

    /**
     * Returns a transformed and limited Gaussian value (A value of 3, 1/6, 1 will result in normal curve from 0-1)
     * @param standardCuttoff How many standard deviations away to limit values to
     * @param scaleFactor Scale of normal curve
     * @param translation Offset of normal curve
     */
    private double generateGaussianTransformed(double standardCuttoff, double scaleFactor, double translation) {
        double gaussian = generator.nextGaussian();
        if(gaussian > standardCuttoff) gaussian = standardCuttoff;
        else if(gaussian < -standardCuttoff) gaussian = -standardCuttoff;
        gaussian *= scaleFactor;
        gaussian += translation;
        return gaussian;
    }

    /**
     * Manually sets each node to unfired so the circuit can process again
     */
    private void clearCircuit() {
        for(int x = 0; x < layers.length; x++) {
            for(int y = 0; y < layers[x].length; y++) {
                layers[x][y].setCleared();
            }
        }
    }

    public void setID(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    /**
     * @return The 2D Node array that this circuit comprises of
     */
    public Node[][] getLayers() {
        return layers;
    }

    /**
     * @return The array of thresholds
     */
    public double[][] getThresholds() {
        return thresholds;
    }

    /**
     * @return Number of inputs this circuit has
     */
    public int getInputs() {
        return inputs;
    }

    public int getNumOutputs() {
        return layers[layers.length - 1].length;
    }

    /**
     * @return The 2D connection strength array that is assigned to nodes
     */
    public double[][] getConnectionStrengths() {
        return connectionStrength;
    }

    @Override
    public Circuit clone(){
        Circuit clone = new Circuit(inputs, layerSize.clone(), clone2DArray(thresholds), clone2DArray(connectionStrength), id);
        return clone;
    }

    public double[][] clone2DArray(double[][] arr) {
        double[][] newArr = new double[arr.length][];
        for(int i = 0; i < arr.length; i++) {
            newArr[i] = new double[arr[i].length];
            for(int j = 0; j < arr[i].length; j++) {
                newArr[i][j] = arr[i][j];
            }
        }
        return newArr;
    }
}
