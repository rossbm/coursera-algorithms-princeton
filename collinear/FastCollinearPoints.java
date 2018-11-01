import edu.princeton.cs.algs4.LinkedBag;
import java.util.Arrays;

public class FastCollinearPoints {
    // Use a linked list, since don't know number of segments ahead of time
    private LinkedBag<LineSegment> segments = new LinkedBag<LineSegment>();
    // need a bunch of class wide variables, since modifying them from differen methods
    // this is the current origin point
    private Point originPoint;
    // current slope
    private double slope;
    private Point minPoint;
    private Point maxPoint;
    private int numPoints = 1;
    private boolean skip = false;
    private int originOrder;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] pointsOrg) {
        if (pointsOrg == null) throw new java.lang.IllegalArgumentException();
        // copy array, for soring and stuff
        int length = pointsOrg.length;

        Point[] points = new Point[length];
        for (int i = 0; i < length; i++) {
            if (pointsOrg[i] == null) throw new java.lang.IllegalArgumentException();
            points[i] = pointsOrg[i];
        }
        // now loop over pointsOrg
        // will sort points array twice
        // first by natural order, then by slopeOrder
        for (int i = 0; i < length; i++) {
            this.originPoint = pointsOrg[i];
            Arrays.sort(points);
            Arrays.sort(points, originPoint.slopeOrder());
            // now loop over the sorted points array;
            // reset numer of points;
            this.numPoints = 1;
            for (int j = 1; j < length; j++) {
                checkPoint(points[j]);
            }
            if (this.numPoints >= 4) {
                this.segments.add(new LineSegment(this.minPoint, this.maxPoint));
            }
        }

    }

    private void checkPoint(Point newPoint) {
        // used to check if same and also if needed
        originOrder = originPoint.compareTo(newPoint);
        // if same, throw exception;
        if (originOrder == 0) {
            throw new java.lang.IllegalArgumentException("duplicate point!");
        }
        // deal with new origin point
        // this needs to be before next statement for some reason.
        else if (this.numPoints == 1) {
            // this will set new slope
            // and reset max and min
            // and also disable skipping
            newSlope(newPoint);
        }
        // if a second point+, check slope
        else if (originPoint.slopeTo(newPoint) != this.slope) {
            newSlope(newPoint);
            return;
        }
        else if (this.maxPoint.compareTo(newPoint) == 0) {
            throw new java.lang.IllegalArgumentException("duplicate point!") ;
        }
        else if (!this.skip) {
            this.maxPoint = newPoint;
            this.numPoints += 1;
        }
    }

    private void newSlope(Point newPoint) {
        if (this.numPoints >= 4) {
            this.segments.add(new LineSegment(this.minPoint, this.maxPoint));
        }
        if (this.originOrder > 0) {
            this.skip = true;
        } else {
            this.skip = false;
        }
        this.slope = this.originPoint.slopeTo(newPoint);
        this.minPoint = this.originPoint;
        this.maxPoint = this.originPoint;
        this.numPoints = 2;
    }

    public int numberOfSegments() {
        return this.segments.size();
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