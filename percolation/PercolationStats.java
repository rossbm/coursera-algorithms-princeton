/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform trials independent experiments on an n-by-n grid
    private static final double STANDARD_DEVS = 1.96;
    private double mean = 0.0;
    private double std = 0.0;
    private final double[] percolationThresholds;
    private final int numTrials;

    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("Need a positive size of grid");
        if (trials <= 0) throw new IllegalArgumentException("Need a positive number of trials");
        percolationThresholds = new double[trials];
        numTrials = trials;
        for (int i = 0; i < trials; i++) {
            percolationThresholds[i] = oneTrial(n);
        }
    }

    private double oneTrial(int n) {
        Percolation percolation = new Percolation(n);
        boolean isOpen;
        boolean hasPercolated;
        int row = 1;
        int col = 1;
        do {
             do {
                col = StdRandom.uniform(n) + 1;
                row = StdRandom.uniform(n) + 1;
                isOpen = percolation.isOpen(row, col);
            } while (isOpen);
            percolation.open(row, col);
            hasPercolated = percolation.percolates();
          } while (!hasPercolated);
          return ((double) percolation.numberOfOpenSites()) / Math.pow((double) n, 2);
        }
    // sample mean of percolation threshold
    public double mean() {
        if (mean == 0) {
            mean = StdStats.mean(percolationThresholds);
        }
        return mean;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        if (std == 0) {
            std = StdStats.stddev(percolationThresholds);
        }
        return std;
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - STANDARD_DEVS * stddev() / Math.sqrt((double) numTrials);
    }
    public double confidenceHi() {

        return mean() + STANDARD_DEVS * stddev() / Math.sqrt((double) numTrials);
    }                 // high endpoint of 95% confidence interval

    public static void main(String[] args) {

        // Parse the string argument into an integer value.
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);


        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("std                     = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + "," + percolationStats.confidenceHi() + "]");

    }
}
