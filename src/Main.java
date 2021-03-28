package src;

public class Main extends Trainer {

    private final static int generationSize = 10;

    private Main(int size) {
        super(size);
    }

    @Override
    protected double evaluateFitness(Task task, double[] inputs) {
        double sum = 0;
        double[] results = readTask(task);
        for(int i = 0; i < results.length; i++) { // Temporary code to see if circuits evolve
            sum += results[i];
        }
        return sum;
    }
    
    public static void main(String[] args) {
        Main trainer = new Main(generationSize);
        trainer.sentinelLoop();
        new VisualManager(true, "Generation " + (trainer.getGeneration() - 1));
    }
}
