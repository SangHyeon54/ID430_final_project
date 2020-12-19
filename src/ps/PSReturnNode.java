package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.util.ArrayList;

public class PSReturnNode extends Ellipse2D.Double {
    
    // constants
    public static final double RADIUS = 30;

    // field
    private Point.Double mCenter = null;
    public Point.Double getCenter() {
        return this.mCenter;
    }
    
    private double mRadius;
    public double getRadius() {
        return this.mRadius;
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
    
    //constructor
    public PSReturnNode (Point.Double pt) {
        super(pt.x - PSReturnNode.RADIUS, pt.y - PSReturnNode.RADIUS,
                PSReturnNode.RADIUS * 2, PSReturnNode.RADIUS * 2);
        this.mRadius = PSReturnNode.RADIUS;
        this.mCenter = new Point.Double(pt.x, pt.y);
        this.mEdgeEnd = new ArrayList<PSEdge>();
    }
    
    public void drawReturnNode(Graphics2D g2, Color c, Stroke s) {
        g2.setPaint(PSCanvas2D.COLOR_NODE_ELLIPSE_BG);
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(this);
    }
    
    public void moveReturnNode(Point.Double pt) {
        this.prevCenter = this.mCenter;
        this.setFrame(pt.x - this.mMovePointX, pt.y - this.mMovePointY,
                this.mRadius * 2, this.mRadius * 2);
        this.mCenter = new Point.Double(pt.x - this.mMovePointX + this.mRadius, 
            pt.y - this.mMovePointY + this.mRadius);
        double dx = this.mCenter.x - this.prevCenter.x;
        double dy = this.mCenter.y - this.prevCenter.y;
        moveEdgePoints(dx, dy);
    }
    
    public void moveEdgePoints(double dx, double dy) {
        for (PSEdge edge : this.mEdgeEnd) {
            edge.moveEndingPt(dx, dy);
        }
    }
}
