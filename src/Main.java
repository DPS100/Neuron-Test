package src;

import src.network.*;
import src.scenarios.tictactoe.*;

public class Main implements Manager{
    
    public static void main(String[] args) {
		if(args.length == 0) {
			args = new String [1];
			args[0] = "-g";
		}
        if(args[0].equals("generate") || args[0].equals("-g")) {
            int generationSize = (int)Main.getDoubleFromUser("Enter the generation size: ");
            int inputs = 9;
			System.out.println("Enter number of inputs (currently locked): " + inputs);
            int layers = (int)getDoubleFromUser("Enter number of non-input layers (includes output): ");
            double mutationRate = getDoubleFromUser("Enter the mutation rate (between 0 and 1, 0 results in no mutations and 1 results in full mutations): ");
            int[] layerSize = new int[layers];
            for(int i = 0; i < layers - 1; i++) {
                layerSize[i] = (int)getDoubleFromUser("Enter layer " + (i + 1) + " size: ");
            } layerSize[layers - 1] = 1; System.out.println("Enter layer " + layers + " size (currently locked): 1");
			TicTrainer t = new TicTrainer(generationSize, layerSize, mutationRate);
			t.sentinelLoop();
			
            
        } else if(args[0].equals("display") || args[0].equals("-d")) {
            int generation = (int)getDoubleFromUser("Enter target generation");
            new VisualManager(true, "Generation " + generation);
        }
    }

	/**
     * Attempt to get user to enter a double. Will end the program on unknown error, 
     * but handle uncastable string by prompting user again.
     * 
     * @param text Question to prompt the user with
     * @return User answer in double form
     */
    public static double getDoubleFromUser(String text){
        double value = 0.0;
        boolean valid = false;
        while(!valid) {
            try {
                value = Double.parseDouble(System.console().readLine(text));
                valid = true;
            } catch(NumberFormatException e) {
                System.out.println("Could not cast input to double. Please try again.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An unknown error occurred. Terminating program.");
                System.exit(1);
            }
        }
        return value;
    }
}
