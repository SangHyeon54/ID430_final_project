package ps;


import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class PSXform {
    //constants
    public static Point PIVOT_PT = new Point(100, 100);
    public static final double MIN_STARTING_ARM_LEN_FOR_SCALING = 100.0;
    
    // fields
    private Point mStartingScreenPt = null;
    public void setStartingScreenPt (Point pt) {
        this.mStartingScreenPt = pt;
        this.mStartXformFromWorldToScreen.setTransform(
        this.mCurXformFromWorldToScreen);
    }
    
    private AffineTransform mCurXformFromWorldToScreen = null;
    public AffineTransform getCurXformFromWorldToScreen() {
        return this.mCurXformFromWorldToScreen;
    }

    private AffineTransform mCurXformFromScreenToWorld = null;
    public AffineTransform getCurXformFromScreenToWorld() {
        return this.mCurXformFromScreenToWorld;
    }
    
    private AffineTransform mStartXformFromWorldToScreen = null;
    
    public PSXform() {
        this.mCurXformFromWorldToScreen = new AffineTransform();
        this.mCurXformFromScreenToWorld = new AffineTransform();
        this.mStartXformFromWorldToScreen = new AffineTransform();
    }
    
    // call whenever mCurXformWorldToScreen changes to have its corresponding
    // mCurXformFromScreenToWorld.
    public void updateCurXformFromScreenToWorld() {
        try {
            this.mCurXformFromScreenToWorld = this.mCurXformFromWorldToScreen.
                createInverse();
        } catch (NoninvertibleTransformException ex) {
            System.out.println("NoninvertibleTransformException");
        }
    }
    
    public Point calcPtFromWorldToScreen(Point2D.Double worldPt) {
        Point screenPt = new Point();
        this.mCurXformFromWorldToScreen.transform(worldPt, screenPt);
        return screenPt;
    }
    
    public Point2D.Double calcPtFromScreenToWorld(Point screenPt) {
        Point2D.Double worldPt = new Point2D.Double();
        this.mCurXformFromScreenToWorld.transform(screenPt, worldPt);
        return worldPt;
    }

    public boolean translateTo(Point pt) {
        if (this.mStartingScreenPt == null) {
            return false;
        }
        this.mCurXformFromWorldToScreen.setTransform(
            this.mStartXformFromWorldToScreen);
        this.updateCurXformFromScreenToWorld();
        
        Point2D.Double worldPt0 = this.calcPtFromScreenToWorld(
            this.mStartingScreenPt);
        Point2D.Double worldPt1 = this.calcPtFromScreenToWorld(pt);
        double dx = worldPt1.x - worldPt0.x;
        double dy = worldPt1.y - worldPt0.y;
        
        //translate the screen coorid system by (-dx, -dy)
        this.mCurXformFromWorldToScreen.translate(dx, dy);
        this.updateCurXformFromScreenToWorld();
        return true;
    }

    void setPivot(Point pt) {
        PIVOT_PT = new Point(pt.x, pt.y);;
    }

    public boolean zoomTo(Point pt) {
        if (this.mStartingScreenPt == null) {
            return false;
        }
        
        this.mCurXformFromWorldToScreen.setTransform(
            this.mStartXformFromWorldToScreen);
        //for scaling
        double d0 = PSXform.PIVOT_PT.distance(this.mStartingScreenPt);
        if (d0 < PSXform.MIN_STARTING_ARM_LEN_FOR_SCALING) {
            return false;
        }
        double d1 = PSXform.PIVOT_PT.distance(pt);
        double s = d1/d0;
        
        Point2D.Double worldPivotPt = this.calcPtFromScreenToWorld(PSXform.PIVOT_PT);
        
        // translate the screen coordinate system by (-dx , -dy)
        this.mCurXformFromWorldToScreen.translate(
            worldPivotPt.x, worldPivotPt.y);
        //scale the screen coordinate system by (1/s, 1/s);
        this.mCurXformFromWorldToScreen.scale(s, s);
        // translate the screen coordinate system by (dx, dy)
        this.mCurXformFromWorldToScreen.translate(
            -worldPivotPt.x, -worldPivotPt.y);
        
        this.updateCurXformFromScreenToWorld();
        
        return true;
    }

    public void home() {
        this.mCurXformFromWorldToScreen.setToIdentity();
        this.updateCurXformFromScreenToWorld();
    }
}
