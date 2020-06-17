public class Manager {
    static Neuron sensory = new Neuron(1);
    public static void main(String[] args) {
        sensory.recieveActionPotential(1, 0);
    }
}