/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {

    private Point[] points;
    private LineSegment[] segments;
    private int numberOfSegments;


    // finds all the line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException("no points found");
        }
        this.points = points.clone();
        validate();

        // calculating the line segments and populating the segments array
        ArrayList<LineSegment> ls = findSegments();
        numberOfSegments = ls.size();
        segments = new LineSegment[numberOfSegments];
        int i = 0;
        for (LineSegment l : ls) {
            segments[i++] = l;
        }
    }

    // calculates all the line segments in the given point set
    private ArrayList<LineSegment> findSegments() {
        ArrayList<LineSegment> lineSegments = new ArrayList<>();
        Point[] pointsClone = points.clone();
        ArrayList<Point> temp;

        for (int i = 0; i < points.length; i++) {
            Point p = pointsClone[i];
            Arrays.sort(points, p.slopeOrder());

            for (int j = 0; j < points.length - 2; j++) {
                temp = new ArrayList<>();
                double curSlope = p.slopeTo(points[j]);
                if (curSlope == p.slopeTo(points[j + 2])) {
                    temp.add(p);
                    temp.add(points[j]);
                    temp.add(points[j + 1]);
                    temp.add(points[j + 2]);
                    int jClone = j + 3;
                    while (jClone < points.length && curSlope == p.slopeTo(points[jClone])) {
                        temp.add(points[jClone]);
                        jClone += 1;
                    }

                    Collections.sort(temp);
                    if (temp.get(0).equals(p)) {
                        lineSegments.add(new LineSegment(temp.get(0), temp.get(temp.size() - 1)));
                    }
                    j = jClone - 1;
                }
            }
        }
        return lineSegments;
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

    // returns the count of the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // returns the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }


}
