/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 19/08/20
 *  Description: A mutable data type that represents a set of points in the unit
 *  square
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rb;

        public Node(Point2D p, RectHV rect, Node lb, Node rb) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rb = rb;
        }

    }

    public KdTree() {
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    private void validate(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("argument can't be null");
        }
    }

    public void insert(Point2D p) {
        validate(p);
        // if tree is empty make this point the root
        if (isEmpty()) {
            root = new Node(p, new RectHV(0, 0, 1, 1), null, null);
            size += 1;
        }
        else {
            int height = 1;
            insert(p, root, height);
        }

    }

    public boolean contains(Point2D p) {
        validate(p);
        Node next = root;
        int height = 1;
        double pointXorY, nextXorY;
        while (next != null) {
            if (next.p.equals(p)) return true;
            if (height % 2 == 0) {
                pointXorY = p.y();
                nextXorY = next.p.y();
            }
            else {
                pointXorY = p.x();
                nextXorY = next.p.x();
            }
            if (pointXorY < nextXorY) next = next.lb;
            else next = next.rb;
            height += 1;
        }
        return false;
    }


    private void insert(Point2D p, Node node, int height) {
        if (p.equals(node.p)) {
            return;
        }

        double pointXorY, nodeXorY;
        if (height % 2 == 0) {
            pointXorY = p.y();
            nodeXorY = node.p.y();
        }
        else {
            pointXorY = p.x();
            nodeXorY = node.p.x();
        }

        if (pointXorY < nodeXorY) {
            // insert point in left subtree
            if (node.lb == null) {
                RectHV rect;
                if (height % 2 == 0) {
                    rect = new RectHV(node.rect.xmin(), node.rect.ymin(),
                                      node.rect.xmax(), node.p.y());
                }
                else {
                    rect = new RectHV(node.rect.xmin(), node.rect.ymin(),
                                      node.p.x(), node.rect.ymax());
                }
                node.lb = new Node(p, rect, null, null);
                size += 1;
            }
            else {
                insert(p, node.lb, height + 1);
            }
        }
        else {
            // if point is equal to or greater than the point
            // in current node, insert in right subtree
            if (node.rb == null) {
                RectHV rect;
                if (height % 2 != 0) {
                    rect = new RectHV(node.p.x(), node.rect.ymin(),
                                      node.rect.xmax(), node.rect.ymax());
                }
                else {
                    rect = new RectHV(node.rect.xmin(), node.p.y(),
                                      node.rect.xmax(), node.rect.ymax());
                }
                node.rb = new Node(p, rect, null, null);
                size += 1;
            }
            else {
                insert(p, node.rb, height + 1);
            }
        }


    }

    public void draw() {
        draw(root, 1);
    }

    private void draw(Node node, int height) {
        if (node == null) {
            return;
        }
        else {
            draw(node.lb, height + 1);
            draw(node.rb, height + 1);
        }

        StdDraw.setPenRadius(0.001);
        if (height % 2 == 0) {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        else {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }

        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.p.x(), node.p.y());


    }

    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        ArrayList<Point2D> points = new ArrayList<>();
        range(rect, root, points);
        return points;
    }

    private void range(RectHV rect, Node node, ArrayList<Point2D> points) {
        if (node == null) {
            return;
        }
        if (!rect.intersects(node.rect)) {
            return;
        }

        if (rect.contains(node.p)) {
            points.add(node.p);
        }

        range(rect, node.rb, points);
        range(rect, node.lb, points);
    }

    public Point2D nearest(Point2D p) {
        validate(p);
        double distance = Double.POSITIVE_INFINITY;

        return nearest(p, null, distance, root, 1);

    }

    private Point2D nearest(Point2D p, Point2D near, double dist, Node node, int height) {
        if (node == null) {
            return near;
        }

        if (dist < node.rect.distanceSquaredTo(p)) {
            return near;
        }

        if (dist > p.distanceSquaredTo(node.p)) {
            dist = p.distanceSquaredTo(node.p);
            near = node.p;
        }
        double pointXorY, nodeXorY;
        if (height % 2 == 0) {
            pointXorY = p.y();
            nodeXorY = node.p.y();
        }
        else {
            pointXorY = p.x();
            nodeXorY = node.p.x();
        }

        if (pointXorY < nodeXorY) {
            near = nearest(p, near, dist, node.lb, height + 1);
            Point2D temp;
            dist = p.distanceSquaredTo(near);
            temp = nearest(p, near, dist, node.rb, height + 1);

            if (p.distanceSquaredTo(temp) < dist) {
                near = temp;
            }
        }
        else {
            near = nearest(p, near, dist, node.rb, height + 1);
            dist = p.distanceSquaredTo(near);
            Point2D temp;
            temp = nearest(p, near, dist, node.lb, height + 1);

            if (p.distanceSquaredTo(temp) < dist) {
                near = temp;
            }
        }
        return near;
    }

    public static void main(String[] args) {

        KdTree tree = new KdTree();
        assert tree.size() == 0;
        assert tree.isEmpty();

        tree.insert(new Point2D(0.7, 0.2));
        assert tree.size() == 1;
        assert !tree.isEmpty();
        assert tree.contains(new Point2D(0.7, 0.2));
        assert !tree.contains(new Point2D(0, 0));

        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        assert tree.size() == 5;

        tree = new KdTree();
        StdOut.println(tree.size());
        tree.insert(new Point2D(0.7, 0.2));
        StdOut.println(tree.size());
        tree.insert(new Point2D(0.5, 0.4));
        StdOut.println(tree.size());
        tree.insert(new Point2D(0.2, 0.3));
        StdOut.println(tree.size());

    }
}
