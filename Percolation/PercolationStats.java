/* *****************************************************************************
 *  Author:              Prajjwal Juyal
 *  Last modified:       14/08/2020
 *
 *  Runs trials number of independent trials on an n-by-n grid.
 * Opens up a random site on the grid and checks if the system percolates.
 * The fraction of open sites when the system percolates is stored for each trial.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int trials;
    private double[] fractionOfOpenSites;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        fractionOfOpenSites = new double[trials];

        Percolation percolation;
        int row, col;
        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }

            fractionOfOpenSites[i] = (percolation.numberOfOpenSites() * 1.0) / (n * n);

        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractionOfOpenSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractionOfOpenSites);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);

        String confidence = "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]";
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
