public class Circuit {

    private Neuron[][] neurons;

    Circuit(int numNeurons, int layers, int[] layerSizes) {
        neurons = new Neuron[layers][];
        fillNeurons(layers, layerSizes);
        connectNeurons();
    }

    private void fillNeurons(int layers, int[] layerSizes) {
        for(int i = 0; i < layers; i++) {
            neurons[i] = new Neuron[layerSizes[i]];
        }
    }

    private void connectNeurons() {
        for(int i = 0; i < neurons.length - 1; i++) {
            for(int j = 0; j < neurons[i].length; j++) {
                
            }
        }
    }
}