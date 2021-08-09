package src;

public class Main extends Trainer {

    private Main(int generationSize, int inputs, int[] layerSize, double mutationRate) {
        super(generationSize, inputs, layerSize, mutationRate);
    }

    /**
     * Adds up all the output nodes, higher sum = better score
     */
    @Override
    protected double evaluateFitness(Task task, double[] inputs) {
        double sum = 0;
        double[] results = readTask(task, layerSize[layerSize.length - 1]);
        for(int i = 0; i < results.length; i++) {
            sum += results[i];
        }
        return sum;
    }

    /**
     * Fills each input with 1.0
     */
    @Override
    protected void fillInputs(double[][] circuitInputs) {
        for(int i = 0; i < circuitInputs.length; i++) {
            for(int j = 0; j < circuitInputs[i].length; j++) {
                circuitInputs[i][j] = 1.0;
            }
        }
    }
    
    public static void main(String[] args) {
        int generationSize = (int)Trainer.getDoubleFromUser("Enter the generation size: ");
        int inputs = 2; System.out.println("Enter number of inputs (currently locked): 2");
        int layers = (int)Trainer.getDoubleFromUser("Enter number of non-input layers (includes output): ");
        double mutationRate = Trainer.getDoubleFromUser("Enter the mutation rate (between 0 and 1, 0 results in no mutations and 1 results in full mutations): ");
        int[] layerSize = new int[layers];
        for(int i = 0; i < layers; i++) {
            layerSize[i] = (int)Trainer.getDoubleFromUser("Enter layer " + (i + 1) + " size: ");
        }
        Main trainer = new Main(generationSize, inputs, layerSize, mutationRate);
        trainer.sentinelLoop();
        new VisualManager(true, "Generation " + (trainer.getGeneration() - 1));
    }
}
