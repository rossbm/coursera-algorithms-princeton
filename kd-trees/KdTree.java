/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean HORIZONTAL = true;
    // RT means right/top
    private static final boolean RT = true;
    private static RectHV unitRect = new RectHV(0.0, 0.0, 1.0, 1.0);
    private int size = 0;
    private Node root;

    // construct an empty set of points
    public KdTree() {

    }
    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }
    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        root = insert(root, p, unitRect, HORIZONTAL);
    }

    private Node insert(Node currentNode,
                        Point2D newPoint,
                        RectHV parentRect,
                        boolean orientation) {
        boolean direction;
        if (currentNode == null) {
            size++;
            return new Node(newPoint);
        }
        /* Make certain not to add duplicates */
        else if (currentNode.samePoint(newPoint)) {
            return currentNode;
        }
        direction = currentNode.compare(newPoint, orientation);
        // RT for when other node is bigger (horizontally or vertically)
        if (direction == RT) {
            currentNode.rt = insert(currentNode.rt,
                                    newPoint,
                                    currentNode.rtRect(parentRect, orientation),
                                    !orientation);
        }
        // LEFT when current node is bigger
        else {
            currentNode.lb = insert(currentNode.lb,
                                    newPoint,
                                    currentNode.lbRect(parentRect, orientation),
                                    !orientation);
        }
        return currentNode;
    }


    public Point2D nearest(Point2D queryPoint) {
        Querier query = new Querier(queryPoint);
        return query.search();
    }


    private class Querier {
        private Point2D queryPoint;
        private double closestDist = Double.POSITIVE_INFINITY;
        private Point2D closestPoint;

        public Querier(Point2D queryPoint) {
            this.queryPoint = queryPoint;
            this.closestPoint = root.p;
        }

        public Point2D search() {
            return search(root, unitRect, HORIZONTAL);
        }

        private Point2D search(Node currentNode, RectHV parentRect, boolean orientation) {
            if (currentNode == null) return closestPoint;
            double currentDist = currentNode.p.distanceSquaredTo(queryPoint);
            if (currentDist < closestDist) {
                closestPoint = currentNode.p;
                closestDist = currentDist;
            }
            RectHV lbRect = currentNode.lbRect(parentRect, orientation);
            RectHV rtRect = currentNode.rtRect(parentRect, orientation);

            double lbDist = lbRect.distanceSquaredTo(queryPoint);
            double rtDist = rtRect.distanceSquaredTo(queryPoint);

            if (lbDist < rtDist) {
                closestPoint = search(currentNode.lb, lbRect, !orientation);
                if (rtDist < closestDist) {
                    closestPoint = search(currentNode.rt, rtRect, !orientation);
                }
            }
            else {
                closestPoint = search(currentNode.rt, rtRect, !orientation);
                if (lbDist < closestDist) {
                    closestPoint = search(currentNode.lb, lbRect, !orientation);
                }
            }
            return closestPoint;
        }


    }


    // does the set contain point p?
    // TODO: implement a private GET style method...
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        return contains(root, p, HORIZONTAL);
    }

    private boolean contains(Node currentNode,
                             Point2D queryPoint,
                             boolean orientation) {
        boolean direction;
        if (currentNode == null) {
            return false;
        }
        else if (currentNode.samePoint(queryPoint)) {
            return true;
        }
        direction = currentNode.compare(queryPoint, orientation);
        // RT for when current node is smaller
        if (direction == RT) return contains(currentNode.rt, queryPoint, !orientation);
        // LEFT when current node is bigger
        else return contains(currentNode.lb, queryPoint, !orientation);
    }


    public void draw() {
        draw(root, unitRect, HORIZONTAL);
     }

    private void draw(Node node,
                      RectHV parentRect,
                      boolean orientation) {
        // Can only draw a point if it exists
        if (node != null) {

            // create children rectangles
            RectHV rtRect = node.rtRect(parentRect, orientation);
            RectHV lbRect = node.lbRect(parentRect, orientation);

            // draw children first, so that they will be overriden later
            draw(node.lb, lbRect, !orientation);
            draw(node.rt, rtRect, !orientation);

            if (orientation == HORIZONTAL) StdDraw.setPenColor(StdDraw.RED);
            else StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            // draw right/top rectangle
            rtRect.draw();
            lbRect.draw();

            // draw point last so it will be written overtop the rectangles
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();

        }

    }

    // unit testing of the methods (optional)
    // TODO: basic testing of insert and contains
    public static void main(String[] args) {
    }

    private static class Node {
        private Point2D p;
        // This rectagle is used for drawing and maybe other things
        // If orientation is HORIZONTAL, then the x points of the rectangle are
        // the same as the point, while the y points depend on the paren
        // if this node is a "LB" (left/bottom) child, then max y is equal to
        // y of parent node/point, while min y is equal to 0
        // if RT, then max y is 1 and min y is parent
        private Node lb;
        private Node rt;

        public Node(Point2D p) {
            this.p = p;
        }

        public boolean samePoint(Point2D op) {
            return p.equals(op);
        }

        public double x() {
            return this.p.x();
        }

        public double y() {
            return this.p.y();
        }

        public boolean compare(Point2D  that, boolean orientation) {
            if (orientation == HORIZONTAL) {
                return compareX(that);
            }
            else return compareY(that);
        }

        private boolean compareX(Point2D that) {
            return this.x() <= that.x();
        }

        private boolean compareY(Point2D that) {
            return this.y() <= that.y();
        }

        public RectHV rtRect(RectHV parentRect, boolean orientation) {
            if (orientation == HORIZONTAL) {
                return new RectHV(x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
            }
            else {
                return new RectHV(parentRect.xmin(), y(), parentRect.xmax(), parentRect.ymax());
            }
        }

        public RectHV lbRect(RectHV parentRect, boolean orientation) {
            if (orientation == HORIZONTAL) {
                return new RectHV(parentRect.xmin(), parentRect.ymin(), x(), parentRect.ymax());
            }
            else {
                 return new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), y());
            }
        }
    }


}