package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.Math;
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
    
    private boolean isInvisible = false;
    public void setVisible() {
        this.isInvisible = false;
    }
    public void setInvisible() {
        this.isInvisible = true;
    }
    
    private double mRadius;
    public double getRadius() {
        return this.mRadius;
    }
    
    private ArrayList<PSPtCurve> mInputCurve = null;
    public ArrayList<PSPtCurve> getInputCurve() {
        return this.mInputCurve;
    }
    
    private boolean isQuasi = false;
    public boolean getIsQuasi() {
        return this.isQuasi;
    }
    public void changeQuasi() {
        this.isQuasi = !isQuasi;
    }
    
    //constructor
    public PSEdgeInput (Point.Double pt) {
        super(pt.x - PSEdgeInput.RADIUS, pt.y - PSEdgeInput.RADIUS,
                PSEdgeInput.RADIUS * 2, PSEdgeInput.RADIUS * 2);
        this.mCenter = pt;
        this.mRadius = PSEdgeInput.RADIUS;
        this.mInputCurve = new ArrayList<PSPtCurve>();
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
        this.mInputCurve.add(pc);
    }
    
    public void clearInputPtCurve() {
        this.mInputCurve.clear();
    }
}
