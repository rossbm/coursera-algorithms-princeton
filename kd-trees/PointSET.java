/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Bag;
public class PointSET {
    private SET<Point2D> set;
    // construct an empty set of points
    public PointSET() {
        this.set = new SET<Point2D>();
    }
    // is the set empty?
    public boolean isEmpty() {
        return this.set.isEmpty();
    }
    // number of points in the set
    public int size() {
        return this.set.size();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        this.set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        return this.set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();

        Bag<Point2D> included = new Bag<Point2D>();

        for (Point2D p : this.set) {
            if (rect.contains(p)) included.add(p);
        }
        return included;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D q) {
        if (q == null) throw new java.lang.IllegalArgumentException();
        if (this.isEmpty()) return null;

        double minDist = Double.MAX_VALUE;
        double dist;
        Point2D closestPoint = q;

        for (Point2D p : this.set) {
            dist = p.distanceSquaredTo(q);
            if (dist == 0) return p;
            if (dist < minDist) {
                minDist = dist;
                closestPoint = p;
            }
        }
        return closestPoint;
    }
    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}