public class Circuit {

    private Node[][] layers; // Includes the outputs
    private double[][] connectionStrength;
    private double[][] thresholds;
    private double[] inputs;

    /**
     * A circuit consists of one "artificial" layer made up of inputs,
     * and specified layer sizes that make up normal layers (includes the output nodes).
     * @param layerSize The size of each layer
     * @param inputs Number of inputs
     */
    Circuit(int[] layerSize, double[] inputs) {
        this.inputs = inputs;

        layers = new Node[layerSize.length][];
        for(int i = 0; i < layerSize.length; i++) {
            layers[i] = new Node[layerSize[i]];
        }

        connectionStrength = new double[layerSize.length][];
        for(int i = 0; i < connectionStrength.length; i++) {
            if (i == 0) { // Connection between input and first layer
                connectionStrength[i] = new double[inputs.length * layers[i].length]; // Total connections = inputs * first layer nodes
            } else { // Connection between current layer and next layer
                connectionStrength[i] = new double[layers[i - 1].length * layers[i].length]; // Total connections = current layer nodes * next layer nodes
            }
        }

        createNodes();
    }   

    /**
     * Creates each node in the circuit and connects them.
     * Should only be called by the constructor.
     */
    private void createNodes() {
        for(int x = layers.length - 1; x > 0; x--) { // Place on x - axis. Works right to left to not overwrite previously adressed nodes
            for(int y = 0; y < layers[x].length; y++) { // Place on y - axis
                int nextLayerSize = layers[x + 1].length;
                Node[] connectedNodes = new Node[nextLayerSize]; // Array for current (x,y) node connections
                double[] connectedNodeStrengths = new double[nextLayerSize]; // Array for current (x,y) node connection strengths
                for(int y2 = 0; y2 < nextLayerSize; y2++) { // Place on next y - axis interval (y2)
                    connectedNodes[y2] = layers[x + 1][y2]; // Place the (x+1, y2) node in node array
                    connectedNodeStrengths[y2] = connectionStrength[x + 1][y2 + (y * y2)]; // Place the (x+1, y2 + (y * y2))) connection in connection array
                }

                Node node;
                if(x == 0) { // First node layer relies on "artificial" input layer
                    node = new Node(connectedNodes, connectedNodeStrengths, inputs.length, thresholds[x][y]);
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
        for(int y = 0; y < inputValues.length; y++) { // Place on "artificial" layer
            for(int y2 = 0; y2 < layers[0].length; y2++) { // Place on first node layer
                layers[0][y].recieveSignal(inputValues[y] * connectionStrength[0][y2 + (y * y2)]); // Send the signal to each node in first layer
            }
        }

        int[] outputs = new int[layers[layers.length - 1].length]; // Create an int array with length of output layer
        for(int i = 0; i < outputs.length; i++) {
            outputs[i] = layers[layers.length - 1][i].getState(); // Fill array with the state of each output node
        }
        return outputs;
    }
}
