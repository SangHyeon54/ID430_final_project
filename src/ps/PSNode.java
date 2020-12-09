package ps;



import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.Math;

public class PSNode extends Ellipse2D.Double {
    //field
    private Point.Double mCenter = null;
    private float mRadius = 0.0f;
    
    //constructor
    public PSNode (Point.Double pt) {
        super(pt.x, pt.y, 5.0f, 5.0f);
        this.mCenter = pt;
        this.mRadius = 5.0f;
    }
    
    //update with a new point
    public void updateEllipse(Point.Double pt) {
        float radius = (float) pt.distance(this.mCenter.x, this.mCenter.y);
        float height = (float) (radius / Math.sqrt(2.0f));
        this.setFrame(mCenter.x, mCenter.y, height, height);
    }
}
