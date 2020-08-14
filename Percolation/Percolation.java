/* *****************************************************************************
 *  Author:              Prajjwal Juyal
 *  Last modified:       14/08/2020
 *
 *  A Percolation data type. Initializes an n-by-n grid and opens sites passed to
 *  it. Uses 2 WeightedQuickUnionUF objects to avoid backwash. The first wquf object
 *  only has one a top virtual site, while the other has both top and bottom virtual
 *  sites.
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF wqufa, wqufb;
    private int n;
    private int openSites, virtualTop, virtualBottom;

    // creates an  n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        grid = new boolean[n + 1][n + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                grid[i][j] = false;
            }
        }

        this.n = n;
        wqufa = new WeightedQuickUnionUF((n + 1) * (n + 1));
        wqufb = new WeightedQuickUnionUF((n + 1) * (n + 1));
        openSites = 0;

        // Taking two sites for a virtual top and virtual bottom. The system
        // percolates if these two virtual sites are connected.
        virtualTop = 0;
        virtualBottom = xyToz(0, n);
    }

    // mapping from a 2-dimensional (row, col) pair to a 1-dimensional union find object index
    private int xyToz(int row, int column) {
        return row * (n + 1) + column;
    }

    // is the given (row, col) pair valid?
    private boolean isValid(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            return false;
        }
        return true;
    }

    // opens this site (row, col) unless already open
    public void open(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException();
        }

        if (isOpen(row, col)) {
            return;
        }

        // open site and increment site count
        grid[row][col] = true;
        openSites++;

        // connecting it to adjacent open sites
        int currentSiteZ = xyToz(row, col);

        // connect to virtualTop if site belongs to first row, and to virtualBottom if site belongs to last row
        if (row == 1) {
            wqufa.union(currentSiteZ, virtualTop);
            wqufb.union(currentSiteZ, virtualTop);
        }
        if (row == n) {
            wqufb.union(currentSiteZ, virtualBottom);
        }

        // connect to open neighbors
        if ((row + 1 <= n) && isOpen(row + 1, col)) {
            wqufa.union(currentSiteZ, xyToz(row + 1, col));
            wqufb.union(currentSiteZ, xyToz(row + 1, col));
        }
        if ((row - 1 > 0) && isOpen(row - 1, col)) {
            wqufa.union(currentSiteZ, xyToz(row - 1, col));
            wqufb.union(currentSiteZ, xyToz(row - 1, col));
        }
        if ((col + 1 <= n) && isOpen(row, col + 1)) {
            wqufa.union(currentSiteZ, xyToz(row, col + 1));
            wqufb.union(currentSiteZ, xyToz(row, col + 1));

        }
        if ((col - 1 > 0) && isOpen(row, col - 1)) {
            wqufa.union(currentSiteZ, xyToz(row, col - 1));
            wqufb.union(currentSiteZ, xyToz(row, col - 1));
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid (row, col) pair");
        }
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid (row, col) pair");
        }

        return wqufa.find(xyToz(row, col)) == wqufa.find(virtualTop);
    }

    // returns number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // the second object with both virtual sites is used to check for percolation
    // efficiently
    public boolean percolates() {
        return wqufb.find(virtualTop) == wqufb.find(virtualBottom);
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);

        // testing constructor
        assert !percolation.grid[0][0];
        assert !percolation.grid[5][5];

        // testing xyToz
        assert percolation.xyToz(0, 0) == 0;
        assert percolation.xyToz(1, 1) == 7;
        assert percolation.xyToz(5, 5) == 35;

        // testing isValid
        assert !percolation.isValid(0, 0);
        assert percolation.isValid(1, 1);
        assert percolation.isValid(5, 5);
        assert !percolation.isValid(5, 100);

        // testing Open,n isOpen, isFull and numberOfOpenSites
        assert percolation.isFull(1, 1);
        assert percolation.isFull(5, 5);
        assert percolation.numberOfOpenSites() == 0;
        percolation.open(1, 1);
        percolation.open(5, 5);
        assert percolation.numberOfOpenSites() == 2;
        assert percolation.isOpen(1, 1);
        assert percolation.isOpen(5, 5);
        assert !percolation.isFull(5, 5);
    }
}
