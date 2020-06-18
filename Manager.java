public class Manager {
    
    static Neuron neuron1 = new Neuron("1");
    static Neuron neuron2 = new Neuron("2");
    static Dendrite input;
    public static void main(String[] args) {
        connectNeurons();
        input.setActionPotential(1);
    }

    private static void connectNeurons() {
        input = neuron1.createDendrite(true);
        neuron1.connectAxon(neuron2, true);
        neuron2.connectAxon(neuron2, true);
    }
}