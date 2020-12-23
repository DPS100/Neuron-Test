public class Node {

    private final Node[] outputs;
    private final double[] outputStrengthFactor;
    private final Node[] inputs;
    private double[] inputStrengthFactor;
    private final double threshold;
    private boolean fired;
    private int state;

    public Node(Node[] outputs, double[] outputStrengthFactor, Node[] inputs, double threshold) {
        this.outputs = outputs;
        this.outputStrengthFactor = outputStrengthFactor;
        this.inputs = inputs;
        this.inputStrengthFactor = new double[inputs.length];
        this.threshold = threshold;
        this.fired = false;
    }

    private void process() {
        double acc = 0;
        for (int i = 0; i < inputs.length; i++) {
            acc += inputStrengthFactor[i];
        }

        if(acc >= threshold) {
            this.state = 1;
        } else {
            this.state = 0;
        }
    }

    private void sendSignals() {
        process();
        for(int i = 0; i < outputs.length; i++) {
            outputs[i].recieveSignal(state * outputStrengthFactor[i]);
        }
        this.setFired();
    }

    public void recieveSignal(double strength) {
        for(int i = 0; i < inputs.length; i++) {
            if(!inputs[i].isFired()) {
                inputStrengthFactor[i] = strength;
            }

            if(i == inputs.length - 1) {
                sendSignals();
            }
        }
    }

    public boolean isFired() {
        return fired;
    }

    public void setFired() {
        fired = true;
    }
}