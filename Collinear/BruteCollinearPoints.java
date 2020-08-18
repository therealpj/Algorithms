/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 17/08/20
 *  Description: Examines 4 points at a time and checks whether they all lie on
 *  same line segment, returning all such line segments
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private Point[] points;
    private int numberOfSegments;
    private ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points not provided");
        }

        this.points = points.clone();
        numberOfSegments = 0;

        validate();

        segments = new ArrayList<>();
        findSegments();
    }

    // ensure that no point is null and there are no duplicates
    private void validate() {
        // checking for null elements
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("points can't be null");
            }
        }

        // sorting points for duplicate checking
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            // comparing with previous point to check for duplicates
            if (i != 0 && points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException("there should be no duplicate points");
            }
        }
    }

    // find the line segments
    private void findSegments() {
        // using an arraylist as the number of line segments are unknown
        Point[] temp = new Point[4];
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {

                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {

                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) &&
                                points[i].slopeTo(points[k]) == points[i].slopeTo(points[l])) {
                            temp[0] = points[i];
                            temp[1] = points[j];
                            temp[2] = points[k];
                            temp[3] = points[l];
                            Arrays.sort(temp);
                            segments.add(new LineSegment(temp[0], temp[3]));
                            numberOfSegments++;
                        }
                    }
                }
            }
        }

    }

    // returns the number of segments in the given point set
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // returns the line segments formed by the given point set
    public LineSegment[] segments() {
        // returning the array of line segments
        LineSegment[] lineSegments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            lineSegments[i] = segments.get(i);
        }

        return lineSegments;
    }

    public static void main(String[] args) {

    }
}
