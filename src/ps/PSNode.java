package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public abstract class PSNode {

    // field
    private Point.Double mCenter = null;
    public Point.Double getCenter() {
        return this.mCenter;
    }
    
    private double mRadius;
    public double getRadius() {
        return this.mRadius;
    }
    public void setRadius(double radius) {
        this.mRadius = radius;
    }
    
    private Ellipse2D.Double mBound;
    public Ellipse2D.Double getBound() {
        return this.mBound;
    }
    public void setBound(Ellipse2D.Double bound) {
        this.mBound = bound;
    }
    
    private boolean isQuasi = false;
    public boolean getIsQuasi() {
        return this.isQuasi;
    }
    public void changeQuasi() {
        this.isQuasi = !isQuasi;
    }
    
    private ArrayList<PSEdge> mEdgeStart;
    public  ArrayList<PSEdge> getEdgeStart() {
        return this.mEdgeStart;
    }
    public void addEdgeStart(PSEdge edge) {
        this.mEdgeStart.add(edge);
    }
    public void removeEdgeStart(PSEdge edge) {
        this.mEdgeStart.remove(edge);
    }
    
    private ArrayList<PSEdge> mEdgeEnd;
    public  ArrayList<PSEdge> getEdgeEnd() {
        return this.mEdgeEnd;
    }
    public void addEdgeEnd(PSEdge edge) {
        this.mEdgeEnd.add(edge);
    }
    public void removeEdgeEnd(PSEdge edge) {
        this.mEdgeEnd.remove(edge);
    }
    
    // for move node cmd
    // PointX,Y is distance from frame.x,y
    private double mMovePointX = 0;
    private double mMovePointY = 0;
    public void setMovePoint(Point.Double pt) {
        mMovePointX = pt.x - (this.mCenter.x - this.mRadius);
        mMovePointY = pt.y - (this.mCenter.y - this.mRadius);
    }
    
    private Point.Double prevCenter = null;
    public Point.Double getPrevCenter() {
        return this.prevCenter;
    }
    
    //constructor
    public PSNode (Point.Double pt) {
        this.mCenter = new Point.Double(pt.x, pt.y);
        this.mEdgeStart = new ArrayList<PSEdge>();
        this.mEdgeEnd = new ArrayList<PSEdge>();
    }
    
    public void drawNode(Graphics2D g2, Color c, Stroke s) {
        g2.setPaint(PSCanvas2D.COLOR_NODE_ELLIPSE_BG);
        g2.fill(this.mBound);
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(this.mBound);
    }
    
    // update ellipse radius with a new point
    public void updateRadius(Point.Double pt) {
        float radius = (float) pt.distance(this.mCenter.x, this.mCenter.y);
        this.mBound.setFrame(mCenter.x - radius, mCenter.y - radius,
            radius * 2, radius * 2);
        this.mRadius = radius;
    }
    
    // move the node to pt and fix with mMovePointX,Y
    public void moveNode(Point.Double pt) {
        this.prevCenter = this.mCenter;
        this.mBound.setFrame(pt.x - this.mMovePointX, pt.y - this.mMovePointY,
            this.mRadius * 2, this.mRadius * 2);
        this.mCenter = new Point.Double(pt.x - this.mMovePointX + this.mRadius, 
            pt.y - this.mMovePointY + this.mRadius);
        double dx = this.mCenter.x - this.prevCenter.x;
        double dy = this.mCenter.y - this.prevCenter.y;
        // move node contents in same distance
        moveEdgePoints(dx, dy);
    }
    
    // move related edge
    public void moveEdgePoints(double dx, double dy) {
        for (PSEdge edge : this.mEdgeStart) {
            edge.moveStartOfArrow(dx, dy);
            edge.moveStartingPt(dx, dy);
            edge.calcArrowEnd();
        }
        for (PSEdge edge : this.mEdgeEnd) {
            edge.moveEndingPt(dx, dy);
            edge.calcArrowEnd();
        }
    }
}
