package src;

import src.network.Task;
import src.network.Trainer;
import src.network.VisualManager;
import src.scenarios.tictactoe.TicGame;

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
                /*
                for(int k = 0; k < circuitInputs[i][j].length; k++) { // Current input
                    //FIXME test values
                    if(j == 0) circuitInputs[i][j][k] = 1.0;
                    else circuitInputs[i][j][k] = 0.9;
                }
                */
                if(j == 0) {circuitInputs[i][j][0] = 1.0; circuitInputs[i][j][1] = 0.0;} // 1.0
                if(j == 1) {circuitInputs[i][j][0] = 0.5; circuitInputs[i][j][1] = 0.0;} // 1.0
                if(j == 2) {circuitInputs[i][j][0] = 0.1; circuitInputs[i][j][1] = 0.0;} // 1.0
                if(j == 3) {circuitInputs[i][j][0] = 0.9; circuitInputs[i][j][1] = 1.0;} // 0.0
                if(j == 4) {circuitInputs[i][j][0] = 0.4; circuitInputs[i][j][1] = 0.5;} // 0.0
                if(j == 5) {circuitInputs[i][j][0] = 0.05; circuitInputs[i][j][1] = 0.1;} // 0.0
            }
        }
    }
    
    public static void main(String[] args) {
        TicGame game = new TicGame(null, null);
        game.printBoard();
        game.makeMove(0, 0);
        game.printBoard();
        game.makeMove(2, 2);
        game.printBoard();
        if(args[0].equals("generate") || args[0].equals("-g")) {
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
            
        } else if(args[0].equals("display") || args[0].equals("-d")) {
            int generation = (int)Trainer.getDoubleFromUser("Enter target generation");
            new VisualManager(true, "Generation " + generation);
        }

    }
}
