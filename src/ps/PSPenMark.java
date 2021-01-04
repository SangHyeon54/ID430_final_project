package ps;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class PSPenMark {
    // constants
    public static final double MIN_DIST_BTWN_PTS = 2.0;
    
    // fields
    private ArrayList<Point> mPts = null;
    public ArrayList<Point> getPts() {
        return this.mPts;
    }
    private Rectangle mBoundingBox = null;
    public Rectangle getBoundingBox() {
        return this.mBoundingBox;
    }
    
    public PSPenMark(Point pt) {
        this.mPts = new ArrayList<Point>();
        this.mPts.add(pt);
        this.mBoundingBox = new Rectangle(pt.x, pt.y, 0, 0);
    }
    
    public boolean addPt(Point pt) {
        //make sure at least one point is created
        assert (this.mPts.size() > 0);
        Point lastPt = this.getLastPt();
        if (lastPt.distance(pt) < PSPenMark.MIN_DIST_BTWN_PTS) {
            return false;
        } else {
            this.mPts.add(pt);
            this.mBoundingBox.add(pt);
            return true;
        }
    }
    
    public Point getFirstPt() {
        return this.mPts.get(0);
    }
    
    public Point getLastPt() {
        int size = this.mPts.size();
        assert(size > 0);
        return this.mPts.get(size - 1);
    }
}