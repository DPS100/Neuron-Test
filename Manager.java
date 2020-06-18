import java.util.Scanner;

public class Manager {
    Scanner scanner = new Scanner(System.in);
    String input;
    static int[] layers = new int[2];
    Neuron[] circuit = new Neuron[2];//TODO should calculate total neurons automatically
    static Neuron motor = new Neuron(1);
    static Dendrite manual = new Dendrite();
    static Neuron[] motorarr = new Neuron[]{motor};
    static Neuron sensory = new Neuron(manual, motorarr);

    public static void main(String[] args) {
        manual.setNeuron(sensory);
        manual.setActionPotential(0.5);
        //layers[0] = 1;
        //layers[1] = 1;//TODO do this automatically
    }

    /*public void makeCircuit() {//start with motor neuron(s), the go backwards through layers
        circuit[0] = new Neuron(1);
        circuit[1] = new Neuron(neuronsRecievengFromMe, 1);
    }*/
}