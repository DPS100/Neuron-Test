package src.network;

import java.util.Random;

import src.network.InvalidCircuitParameter.ERROR;

public class Circuit{

    private static Random generator = new Random(1234);

    private final String id; // Unique ID
    public final int layers; // Number of layers
    public final int[] layerSizes; // Sizes of each layer{layer 0 size, layer 1 size, ..., layer n size}
    private double[][] weights; // Array of doubles that each output is multiplied by. [layers - 1][nodes in layer * nodes in next layer]
    private double[][] biases; // The threshold each node's net input must pass to become active. [layers][nodes in layer]

    /**
     * This constructor needs manual values, and will not generate it's own.
     * @param layerSizes Sizes of each layer{layer 0 size, layer 1 size, ..., layer n size}
     * @param weights Array of doubles that each output is multiplied by. [layers - 1][nodes in layer * nodes in next layer]
     * @param biases The threshold each node's net input must pass to become active. [layers][nodes in layer]
     * @param id Unique ID
     */
    Circuit(int[] layerSizes, double[][] weights, double[][] biases, String id) {
        this.layers = layerSizes.length;
        this.layerSizes = layerSizes;
        this.weights = weights;
        this.biases = biases;
        this.id = id;
    }

    /**
     * This constructor will generate it's own values
     * @param layerSizes Sizes of each layer{layer 0 size, layer 1 size, ..., layer n size}
     * @throws InvalidCircuitParameter
     */
    Circuit(int[] layerSizes, String id) throws InvalidCircuitParameter {
        this.layers = layerSizes.length;
        this.layerSizes = layerSizes;
        generateValues(layerSizes);
        this.id = id;
    }

    /**
     * Make this circuit generate it's own 2D arrays for thresholds and connection strengths
     * @param layerSizes The size of each layer (Must greater than or equal to 1)
     * @throws InvalidCircuitParameter
     */
    private void generateValues(int[] layerSizes) throws InvalidCircuitParameter {
        generateWeights(layerSizes);
        generateBiases(layerSizes);
    }

    private void generateWeights(int[] layerSizes) throws InvalidCircuitParameter {
        if(layerSizes.length < 0) throw new InvalidCircuitParameter(ERROR.CONSTRUCT, "Invalid layer length of 0");
        this.weights = new double[layers - 1][]; // Input layer has no weights
        for(int layer = 0; layer < layers - 1; layer++) {
            weights[layer] = new double[layerSizes[layer] * layerSizes[layer + 1]];
            for(int weight = 0; weight < weights[layer].length; weight++) {
                weights[layer][weight] = Math.random() * 2 - 1; // Between 1 and -1
            }
        }
    }

    private void generateBiases(int[] layerSizes) throws InvalidCircuitParameter {
        if(layerSizes.length < 0) throw new InvalidCircuitParameter(ERROR.CONSTRUCT, "Invalid layer length of 0");
        this.biases = new double[layers][];
        for(double[] layer : biases) {
            for(int node = 0; node < layer.length; node++) {
                layer[node] = 0.0; // Initialize all biases to 0
            }
        }
    }

    /**
     * See what the circuit outputs with a selection of input values.
     * @param inputValues Needs to be the same length as inputs.
     * @return Array of doubles
     * @throws InvalidCircuitParameter
     */
    public double[] process(double[] inputValues) throws InvalidCircuitParameter {
        if(inputValues.length != layerSizes[0]) {
            throw new InvalidCircuitParameter(ERROR.PROCESS, "Invalid number of inputs [" + inputValues.length + "] with input layer of size [" + layerSizes[0] + "]");
        }

        // Layer 1
        double[] layer1Out = new double[];
    }

    private double[] processLayerHelper(double[] inputs, double[] weights, double[] biases) {
        for()
    }

    private double processNodeHelper(double[] inputs, double[] weights, double bias) throws InvalidCircuitParameter {
        if(inputs.length != weights.length) {
            throw new InvalidCircuitParameter(ERROR.PROCESS, "num inputs [" + inputs.length + "] invalid with num weights [" + weights.length + "]");
        }

        double sum = 0;
        for(int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        sum -= bias;

        // Sigmoid on sum
        return sigmoid(sum);
    }

    public static double sigmoid(double x) {
        // Quick math bounds
        if(x > 10) return 1;
        else if(x < -10) return -1;

        // Actual sigmoid
        return 1 / (1 + Math.exp(-x));
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
