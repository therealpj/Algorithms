import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // check the argument for null
    private void validate(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("argument can't be nul");
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.size() == 0;
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point in the set if not already
    public void insert(Point2D p) {
        validate(p);
        points.add(p);
    }

    // does the set contains point p
    public boolean contains(Point2D p) {
        validate(p);
        return points.contains(p);
    }

    // draw all the points to standard draw
    public void draw() {
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    //  check if a point is inside a rectangle
    private boolean inside(RectHV rect, Point2D p) {
        if (p.x() > rect.xmax() || p.x() < rect.xmin()) {
            return false;
        }

        if (p.y() > rect.ymax() || p.y() < rect.ymin()) {
            return false;
        }

        return true;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        ArrayList<Point2D> pointsInside = new ArrayList<>();

        for (Point2D p : points) {
            if (inside(rect, p)) {
                pointsInside.add(p);
            }
        }
        return pointsInside;
    }

    public Point2D nearest(Point2D p) {
        validate(p);
        if (points.isEmpty()) {
            return null;
        }

        if (points.size() == 1) {
            return points.first();
        }

        double minimumDistance = Double.POSITIVE_INFINITY;
        Point2D minimumDistancePoint = null;
        for (Point2D other : points) {
            if (other.equals(p)) {
                return other;
            }

            double distance = other.distanceTo(p);
            if (distance < minimumDistance) {
                minimumDistance = distance;
                minimumDistancePoint = other;
            }
        }
        return minimumDistancePoint;
    }
}
