package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class PSEdgeInput extends Ellipse2D.Double {    
    // constants
    public static final double RADIUS = 20;
    
    private Point.Double mCenter = null;
    public void setCenter(Point.Double pt) {
        this.mCenter = pt;
    }
    public Point.Double getCenter() {
        return this.mCenter;
    }
    
    private double mRadius;
    public double getRadius() {
        return this.mRadius;
    }
    
    private ArrayList<PSPtCurve> mInputPtCurve = null;
    public ArrayList<PSPtCurve> getInputPtCurve() {
        return this.mInputPtCurve;
    }
    
    //constructor
    public PSEdgeInput (Point.Double pt) {
        super(pt.x - PSEdgeInput.RADIUS, pt.y - PSEdgeInput.RADIUS,
                PSEdgeInput.RADIUS * 2, PSEdgeInput.RADIUS * 2);
        this.mCenter = pt;
        this.mRadius = PSEdgeInput.RADIUS;
        this.mInputPtCurve = new ArrayList<PSPtCurve>();
    }
    
    public void drawInput(Graphics2D g2, Color c, Stroke s) {     
        this.setFrame(mCenter.x - mRadius, mCenter.y - mRadius,
            mRadius * 2, mRadius * 2);   
        g2.setPaint(PSCanvas2D.COLOR_NODE_ELLIPSE_BG);
        g2.fill (this);
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(this);
    }
    
    public void addInputPtCurve(PSPtCurve pc) {
        this.mInputPtCurve.add(pc);
    }
    
    public void clearInputPtCurve() {
        this.mInputPtCurve.clear();
    }
    
    // move the ellipse and ptcurves in the ellipse
    public void moveInput(double dx, double dy) {
        this.setFrame(mCenter.x - mRadius + dx, mCenter.y - mRadius + dy,
            mRadius * 2, mRadius * 2);
        Point.Double newCenter = new Point.Double(mCenter.x + dx,
            mCenter.y + dy);
        this.setCenter(newCenter);
        for(PSPtCurve pc : mInputPtCurve) {
            pc.movePtCurve(dx, dy);
        }
    }
    
    public void moveInput(Point.Double pt) {
        this.setFrame(pt.x - mRadius, pt.y - mRadius, mRadius * 2, mRadius * 2);
        for(PSPtCurve pc : mInputPtCurve) {
            pc.movePtCurve(pt.x - this.getCenter().x,
                pt.y - this.getCenter().y);
        }
        this.setCenter(pt);
    }
}
