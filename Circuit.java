public class Circuit {

    private Node[][] layers; // Node array that this curcuit consists of, includes the outputs but NOT inputs.
    private double[][] connectionStrength; // Array of doubles that each output is multiplied by. Includes inputs to next but no outputs. Each array length is previous node layer size * next node layer size
    private double[][] thresholds; // The net threshold each node must pass to become active.
    private int inputs; // Number of input nodes (Not included in the layer 2d array).

    /**
     * A circuit consists of one "artificial" layer made up of inputs,
     * and specified layer sizes that make up normal layers (includes the output nodes).
     * @param layerSize The size of each layer (Must greater than or equal to 1)
     * @param inputs Number of inputs
     */
    Circuit(int inputs, int[] layerSize, double[][] thresholds, double[][]connectionStrength) {
        this.inputs = inputs;
        this.thresholds = thresholds;
        this.connectionStrength = connectionStrength;

        layers = new Node[layerSize.length][];
        for(int i = 0; i < layerSize.length; i++) { // Set each layer size
            layers[i] = new Node[layerSize[i]];
        }

        createNodes();
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
     * @return Array of ints (1 = active, 0 = inactive)
     */
    public int[] process(double[] inputValues) {
        clearCircuit();
        for(int y = 0; y < inputValues.length; y++) { // Place on "artificial" layer
            for(int y2 = 0; y2 < layers[0].length; y2++) { // Place on first node layer
                layers[0][y2].recieveSignal(inputValues[y] * connectionStrength[0][y2 + (layers[0].length * y)]); // Send the signal to each node in first layer
            }
        }

        int[] outputs = new int[layers[layers.length - 1].length]; // Create an int array with length of output layer
        for(int i = 0; i < outputs.length; i++) {
            outputs[i] = layers[layers.length - 1][i].getState(); // Fill array with the state of each output node
        }
        return outputs;
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

    /**
     * @return The 2D Node array that this circuit comprises of
     */
    public Node[][] getLayers() {
        return layers;
    }

    /**
     * @return Number of inputs this circuit has
     */
    public int getInputs() {
        return inputs;
    }

    /**
     * @return The 2D connection strength array that is assigned to nodes
     */
    public double[][] getConnectionStrengths() {
        return connectionStrength;
    }
}
