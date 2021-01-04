package ps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class PSPtCurve {
    //constant
    public static final double MIN_DIS_BTW_PTS = 2.0;
    //define member variables here
    private ArrayList<Point2D.Double> mPts = null;
    //getter function to get member variable
    public ArrayList<Point2D.Double> getPts() {
        return this.mPts;
    }
    
    private Rectangle2D.Double mBoundingBox = null;
    
    public Rectangle2D.Double getBoundingBox() {
        return this.mBoundingBox;
    }
    
    private Stroke mStroke = null;
    public Stroke getStroke() {
        return this.mStroke;
    }
    
    public void setStroke(Stroke s) {
        this.mStroke = s;
        return;
    }
    
    private Color mColor = null;
    public Color getColor() {
        return this.mColor;
    }
    
    public void setColor(Color c) {
        this.mColor = c;
        return;
    }
    
    //constructor
    public PSPtCurve(Point2D.Double pt, Color c, Stroke s) {
        this.mPts = new ArrayList<Point2D.Double>();
        this.mPts.add(pt);
        this.mBoundingBox = new Rectangle2D.Double(pt.x, pt.y, 0.0, 0.0);
        BasicStroke bs = (BasicStroke)s;
        this.mColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), 
            c.getAlpha());
        this.mStroke = new BasicStroke(bs.getLineWidth(), bs.getEndCap(),
            bs.getLineJoin());
    }
    
    public void addPt(Point2D.Double pt) {
        this.mPts.add(pt);
    }
    
    public void movePtCurve(double dx, double dy) {
        for (int i = 0; i < this.mPts.size(); i++) {
            Point2D.Double prevPt = mPts.get(i);
            Point2D.Double newPt = new Point2D.Double(prevPt.x + dx, 
                prevPt.y + dy);
            this.mPts.set(i, newPt);
        }
    }
}
