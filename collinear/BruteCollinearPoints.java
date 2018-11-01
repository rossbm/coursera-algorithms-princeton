/**
 * class for  all line segments containing 4 points
 */
import edu.princeton.cs.algs4.LinkedBag;

import java.util.Arrays;


public class BruteCollinearPoints {
    // This defintely needs to be needs to be declared at class level rather
    // than at the local level
    // Needs to be a list since don't know length ahead of time
    private LinkedBag<LineSegment> segments = new LinkedBag<LineSegment>();

    // The constructor will find the line segments, rather than the getSegment
    // method, does this make sense?
    public BruteCollinearPoints(Point[] pointsOrg) {
        if (pointsOrg == null) { throw new java.lang.IllegalArgumentException(); }
        // which is what I do here
        int length = pointsOrg.length;
        //sorting, to deal with dups
        Point[] points = new Point[length];
        for (int i = 0; i < length; i++) {
            if (pointsOrg[i] == null) throw new java.lang.IllegalArgumentException();
            points[i] = pointsOrg[i];
        }
        Arrays.sort(points);
        // loop through all sets of 4 points
        for (int idx0 = 0; idx0 <= length - 4; idx0++) {
            for (int idx1 = idx0 + 1; idx1 <= length - 3; idx1++) {
                // With two points, can construct PointChecker object
                // Point checker object will store slope and max and min points
                PointChecker potentialSegment = new PointChecker(points[idx0], points[idx1]);
                for (int idx2 = idx1 + 1; idx2 < length; idx2++) {
                    // Check if new point has same slope as preexisting points
                    // If not, break to next part of loop
                    // Otherwise, continue to check 4th point
                    potentialSegment.checkPoint(points[idx2]);
                }
                if (potentialSegment.numPoints >= 4) {
                    this.segments.add(potentialSegment.getSegment());
                }
            }
        }
    }
    // the number of line segments
    public int numberOfSegments() {
        return this.segments.size();
    }

    private class PointChecker {
        private double slope;
        private Point minPoint;
        private Point maxPoint;
        private int numPoints = 2;
        public PointChecker(Point a, Point b) {
            if (a == null) {
                throw new java.lang.IllegalArgumentException();
            }
                this.slope = a.slopeTo(b);
            // initialize both min and max point to a, and then use pointOrder
            // method to determine where b slots in
            this.minPoint = a;
            this.maxPoint = a;
            pointOrder(b);
        }

        public void checkPoint(Point newPoint) {
            if (newPoint == null) {
                throw new java.lang.IllegalArgumentException();
            }
            double newSlope = minPoint.slopeTo(newPoint);
            if (newSlope == this.slope) {
                pointOrder(newPoint);
                this.numPoints++;
            }
        }
        private void pointOrder(Point newPoint) {
            int minOrder = this.minPoint.compareTo(newPoint);
            if (minOrder == 0) {
                throw new java.lang.IllegalArgumentException("duplicate point!");
            } else if (minOrder > 0) {
                this.minPoint = newPoint;
                return;
            }
            int maxOrder = this.maxPoint.compareTo(newPoint);
            if (maxOrder == 0) {
                throw new java.lang.IllegalArgumentException("duplicate point!");
            } else if (maxOrder < 0) {
                this.maxPoint = newPoint;
            }
        }
        public LineSegment getSegment() {
            return new LineSegment(this.minPoint, this.maxPoint);
        }

    }

    // get the line segments
    public LineSegment[] segments() {
        int arrayLength = numberOfSegments();
        LineSegment[] segmentArray = new LineSegment[arrayLength];
        int i = 0;
        for (LineSegment element : this.segments) {
            segmentArray[i++] = element;
        }
        return segmentArray;
    }
}
