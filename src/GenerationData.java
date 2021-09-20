package src;

public record GenerationData(int generation, double[] fitness, Circuit parent) {
    public double max() {
        double best = 0;
        for(double i : fitness) {
            if (i > best) best = i;
        }
        return best;
    }

    public double min() {
        double worst = fitness[0];
        for(double i : fitness) {
            if (i < worst) worst = i;
        }
        return worst;
    }

    public double mean() {
        double total = 0;
        for(double i : fitness) {
            total += i;
        }
        return total / fitness.length;
    }

    public double median() {
        int size = fitness.length;
        if(size % 2 == 0) { // Even
            return (fitness[size / 2] + fitness[size / 2 - 1]) / 2.0;
        } else { // Odd
            return fitness[(size - 1) / 2];
        }
    }

    public double range() {
        return max() - min();
    }

    public double standardDeviation() {
        // Formula: Sqrt( Sum [{x-mu}^2] / N )
        double sum = 0;
        double mean = mean();
        for(double i : fitness) {
            sum += (i-mean) * (i-mean);
        }
        return Math.sqrt(sum / fitness.length);
    }

}
