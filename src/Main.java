package src;

import src.network.*;
import src.scenarios.tictactoe.*;

public class Main implements Manager{

    public static boolean debug = false;
	public static boolean runMultiThreaded = false;
    
    public static void main(String[] args) {

        int generationSize = (int)Main.getDoubleFromUser("Enter the generation size: ");
        int inputs = 9;
        System.out.println("Enter number of inputs (currently locked): " + inputs);
        int layers = (int)getDoubleFromUser("Enter number of non-input layers (includes output): ");
        double mutationRate = getDoubleFromUser("Enter the mutation chance (between 0 and 1): ");
        double mutationChance = getDoubleFromUser("Enter the mutation rate (Maximum change at which circuit could mutate; 0 = no change, 1 = no limit to change): ");
        int[] layerSize = new int[layers];
        for(int i = 0; i < layers - 1; i++) {
            layerSize[i] = (int)getDoubleFromUser("Enter layer " + (i + 1) + " size: ");
        } layerSize[layers - 1] = 9; System.out.println("Enter layer " + layers + " size (currently locked): 9");
        TicTrainer t = new TicTrainer(generationSize, layerSize, mutationRate, mutationChance);
        t.sentinelLoop();

        int generation = t.getGeneration() - 1;
        new VisualManager(true, "Generation " + generation);
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

    public synchronized static void debugLog(String text) {
        if(debug) {
            System.out.println("(DEBUG) " + text);
        }
    }

    public synchronized static void debugLog(int text) {
        debugLog("" + text);
    }

    public synchronized static void debugLog(double text) {
        debugLog("" + text);
    }
}
