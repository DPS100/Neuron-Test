package src;

public class Main extends Trainer {

    private Main(int generationSize, int inputs, int[] layerSize, double mutationRate) {
        super(generationSize, inputs, layerSize, mutationRate);
    }

    /**
     * Custom evaluation
     */
    @Override
    protected double evaluateFitness(Task task, double[] inputs, double[] desiredOutput) {
        double[] results = readTask(task, layerSize[layerSize.length - 1]);
        double distance = 0;
        double maxDist = results.length; // Each output node has a max distance of 1
        for(int i = 0; i < results.length; i++) { // Optimizing for lowest distance to desired result
            distance += Math.abs(results[i] - desiredOutput[i]);
        }
        return Math.abs(maxDist - distance) / maxDist;
    }

    @Override
    protected void fillInputs(double[][][] circuitInputs) {
        for(int i = 0; i < circuitInputs.length; i++) { // Current circuit
            for(int j = 0; j < circuitInputs[i].length; j++) { // Current task
                for(int k = 0; k < circuitInputs[i][j].length; k++) { // Current input
                    //FIXME test values
                    if(k == 0) circuitInputs[i][j][k] = 0.1;
                    else circuitInputs[i][j][k] = 1.0;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        int generationSize = (int)Trainer.getDoubleFromUser("Enter the generation size: ");
        int inputs = 2; System.out.println("Enter number of inputs (currently locked): 2");
        int layers = (int)Trainer.getDoubleFromUser("Enter number of non-input layers (includes output): ");
        double mutationRate = Trainer.getDoubleFromUser("Enter the mutation rate (between 0 and 1, 0 results in no mutations and 1 results in full mutations): ");
        int[] layerSize = new int[layers];
        for(int i = 0; i < layers - 1; i++) {
            layerSize[i] = (int)Trainer.getDoubleFromUser("Enter layer " + (i + 1) + " size: ");
        } layerSize[layers - 1] = 1; System.out.println("Enter layer " + layers + " size (currently locked): 1");
        Main trainer = new Main(generationSize, inputs, layerSize, mutationRate);
        trainer.sentinelLoop();
        new VisualManager(true, "Generation " + (trainer.getGeneration() - 1));
    }
}
