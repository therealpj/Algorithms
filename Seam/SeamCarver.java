/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 24/08/20
 *  Description: A mutable data type used to perform seam carving on an image
 **************************************************************************** */


import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {

    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        validate(picture);
        this.picture = new Picture(picture);
        calculateEnergy();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of the picture
    public int width() {
        return picture.width();
    }

    // height of the picture
    public int height() {
        return picture.height();
    }

    // calculate energy for the picture
    private void calculateEnergy() {
        energy = new double[width()][height()];
        int w = width();
        int h = height();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                if (col == 0 || col == w - 1 || row == 0 || row == h - 1) {
                    energy[col][row] = 1000;
                }
                else {
                    int rgbLeft = picture.getRGB(col - 1, row);
                    int rgbRight = picture.getRGB(col + 1, row);
                    int rgbUp = picture.getRGB(col, row - 1);
                    int rgbDown = picture.getRGB(col, row + 1);
                    energy[col][row] = Math.sqrt(delta(rgbLeft, rgbRight) + delta(rgbDown, rgbUp));

                }
            }
        }
    }

    // energy of a pixel at column x and row y
    public double energy(int x, int y) {
        validateIndex(x, y);
        return energy[x][y];
    }

    // returns the component of a rgb int in order R, G, B per call
    private int nextComponent(int rgb, char c) {
        if (c == 'r') return (rgb >> 16 & 0xFF);
        else if (c == 'g') return (rgb >> 8) & 0xFF;
        else return rgb & 0xFF;
    }

    // returns the sum of squared differences between the individual RGB components of two rbg ints
    private int delta(int rgb1, int rgb2) {
        int r = nextComponent(rgb1, 'r') - nextComponent(rgb2, 'r');
        int g = nextComponent(rgb1, 'g') - nextComponent(rgb2, 'g');
        int b = nextComponent(rgb1, 'b') - nextComponent(rgb2, 'b');

        return (r * r) + (g * g) + (b * b);
    }

    // returns the index of vertical seam
    public int[] findVerticalSeam() {
        if (width() == 1) {
            int[] min = new int[height()];
            for (int i = 0; i < width(); i++)
                min[i] = 0;
            return min;
        }

        CustomTopological cs = new CustomTopological(energy);
        Stack<Integer> min = cs.pathTo(width() * height() + 1);
        min.pop();

        int[] minSeam = new int[min.size()];
        for (int i = 0; i < minSeam.length; i++) {
            minSeam[i] = zToxy(min.pop())[1];
        }
        return minSeam;
    }

    private void transpose() {
        int m = energy.length;
        int n = energy[0].length;

        double[][] transposedMatrix = new double[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                transposedMatrix[x][y] = energy[y][x];
            }
        }

        energy = transposedMatrix;
    }

    // returns the index of the horizontal seam
    public int[] findHorizontalSeam() {
        if (height() == 1) {
            int[] min = new int[width()];
            for (int i = 0; i < width(); i++)
                min[i] = 0;
            return min;
        }
        transpose();
        CustomTopological cs = new CustomTopological(energy);
        Stack<Integer> min = cs.pathTo(width() * height() + 1);
        min.pop();

        int[] minSeam = new int[min.size()];
        int j = 0;
        for (int i : min) {
            minSeam[j] = i % height();
            j += 1;
        }
        transpose();
        return minSeam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validate(seam);

        if (width() <= 1)
            throw new IllegalArgumentException("no more vertical seams to remove");

        if (seam.length != height()) throw new IllegalArgumentException("wrong seam length");
        int prev = seam[0];
        for (int i = 0; i < seam.length; i++) {
            if (Math.abs(seam[i] - prev) >= 2) {
                throw new IllegalArgumentException("wrong seam");
            }
            prev = seam[i];
        }


        Picture cutPicture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int cutCol = 0;
            for (int col = 0; col < width(); col++) {
                if (col != seam[row]) {
                    cutPicture.set(cutCol, row, picture.get(col, row));
                    cutCol += 1;
                }
            }
        }

        this.picture = cutPicture;
        calculateEnergy();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validate(seam);

        if (height() <= 1)
            throw new IllegalArgumentException("no more horizontal seams to remove");

        if (seam.length != width()) throw new IllegalArgumentException("wrong seam length");

        int prev = seam[0];
        for (int i = 0; i < seam.length; i++) {
            if (Math.abs(seam[i] - prev) >= 2) {
                throw new IllegalArgumentException("wrong seam");
            }
            prev = seam[i];
        }

        Picture cutPicture = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); col++) {
            int cutRow = 0;
            for (int row = 0; row < height(); row++) {
                if (row != seam[col]) {
                    cutPicture.set(col, cutRow, picture.get(col, row));
                    cutRow += 1;
                }
            }
        }

        this.picture = cutPicture;
        calculateEnergy();
    }

    // mapping from a 2-dimensional (row, col) pair to a 1-D index
    private int xyToz(int row, int column) {
        return row * width() + column;
    }

    // mapping from 1-dimensional z to 2-D (row, col) pair
    private int[] zToxy(int z) {
        int[] pair = new int[2];
        pair[0] = z / width();
        pair[1] = z % width();

        return pair;
    }

    private void validateIndex(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new IllegalArgumentException("pixel out of bounds");
    }

    private void validate(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("argument can't be null");
        }
    }

    public static void main(String[] args) {


        SeamCarver sc = new SeamCarver(new Picture(args[0]));
        StdOut.println(sc.picture.height());
        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.removeHorizontalSeam(sc.findHorizontalSeam());

        for (int i = 0; i < sc.width(); i++)
            StdOut.println(Arrays.toString(sc.energy[i]));

        StdOut.println(sc.picture.height());
    }
}
