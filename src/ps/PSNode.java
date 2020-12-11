package ps;



import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.util.ArrayList;

public class PSNode extends Ellipse2D.Double {
    
    // constants
    public static final double MIN_RADIUS = 75;
    public static final double MAX_RADIUS = 150;

    // field
    private Point.Double mCenter = null;
    public Point.Double getCenter() {
        return this.mCenter;
    }
    
    private double mRadius = 75;
    public double getRadius() {
        return this.mRadius;
    }
    
    private ArrayList<PSPtCurve> mName = null;
    public ArrayList<PSPtCurve> getName() {
        return this.mName;
    }
    
    private boolean isQusai = false;
    public boolean getIsQusai() {
        return this.isQusai;
    }
    
    //constructor
    public PSNode (Point.Double pt) {
        super(pt.x - 75, pt.y - 75, 150.0, 150.0);
        this.mRadius = 75;
        this.mCenter = new Point.Double(pt.x, pt.y);
        this.mName = new ArrayList<PSPtCurve>();
    }
    
    public void drawNode(Graphics2D g2) {
        g2.setColor(PSCanvas2D.COLOR_NODE_ELLIPSE);
        g2.setStroke(PSCanvas2D.STROKE_NODE_ELLIPSE);
        g2.draw(this);
    }
    
    //update with a new point
    public void updateEllipse(Point.Double pt) {
        float radius = (float) pt.distance(this.mCenter.x, this.mCenter.y);
        this.setFrame(mCenter.x - radius, mCenter.y - radius,
            radius * 2, radius * 2);
        this.mRadius = radius;
    }
}
