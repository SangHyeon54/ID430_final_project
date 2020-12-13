package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.util.ArrayList;

public class PSEdge {
    // constants
//    public static final double MIN_LENGTH = 10;
//    public static final double MAX_RADIUS = 100;

    // field
    private Point.Double mCenter = null;
    public Point.Double getCenter() {
        return this.mCenter;
    }
    
    private Point.Double mStartingPt = null;
    private Point.Double mEndingPt = null;
    
//    private double mLength;
//    public double getLength() {
//        return this.mLength;
//    }
    
    private PSNode mStartingNode;
    public PSNode getStartingNode() {
        return this.mStartingNode;
    }
    
    private PSNode mEndingNode;
    public PSNode getEndingNode() {
        return this.mEndingNode;
    }
    
    private ArrayList<PSPtCurve> mEdgeInput = null;
    public ArrayList<PSPtCurve> getEdgeInput() {
        return this.mEdgeInput;
    }
    
    private ArrayList<PSPtCurve> mEdgeCmd = null;
    public ArrayList<PSPtCurve> getEdgeCmd() {
        return this.mEdgeCmd;
    }
    
    //constructor
    public PSEdge (Point.Double pt) {
        this.mStartingPt = new Point.Double(pt.x, pt.y);
        this.mEndingPt = new Point.Double(pt.x, pt.y);
//        
//        super(pt.x - PSEdge.MIN_RADIUS, pt.y - PSEdge.MIN_RADIUS,
//                PSEdge.MIN_RADIUS * 2, PSEdge.MIN_RADIUS * 2);
//        this.mLength = PSEdge.MIN_LENGTH;
//        this.mCenter = new Point.Double(pt.x, pt.y);
        this.mEdgeInput = new ArrayList<PSPtCurve>();
        this.mEdgeCmd = new ArrayList<PSPtCurve>();
    }
    
    public void drawEdge(Graphics2D g2, Color c, Stroke s) {
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(new Line2D.Double(
            mStartingPt.x, mStartingPt.y, mEndingPt.x, mEndingPt.y));
        drawArrowHead(g2);
//        g2.dispose();
//        g2.draw((Shape) this);
    }
    
    private void drawArrowHead(Graphics2D g2) {
        Polygon arrowHead = new Polygon();
        AffineTransform tx = new AffineTransform();

        arrowHead.addPoint(0, 5);
        arrowHead.addPoint(-5, -5);
        arrowHead.addPoint(5, -5);

        tx.setToIdentity();
        double angle = Math.atan2(
            mEndingPt.y - mStartingPt.y, mEndingPt.x - mStartingPt.x);
        tx.translate(mEndingPt.x, mEndingPt.y);
        tx.rotate(angle - Math.PI / 2d);

        g2.setTransform(tx);
        g2.fill(arrowHead);
    }
    
    //update with a new point
    public void updateArrow(Point.Double pt) {
        this.mEndingPt = pt;
//        float radius = (float) pt.distance(this.mCenter.x, this.mCenter.y);
//        this.setFrame(mCenter.x - radius, mCenter.y - radius,
//            radius * 2, radius * 2);
//        this.mRadius = radius;
    }
    
//    public void addNamePtCurve(PSPtCurve pc) {
//        this.mName.add(pc);
//    }
//    
//    public void clearNamePtCurve() {
//        this.mName.clear();
//    }
}
