package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.util.ArrayList;

public class PSGeneralNode extends PSNode {
    
    // constants
    public static final double MIN_RADIUS = 30;
    public static final double MAX_RADIUS = 100;
    
    // fields
    private ArrayList<PSPtCurve> mName = null;
    public ArrayList<PSPtCurve> getName() {
        return this.mName;
    }
    
    //constructor
    public PSGeneralNode (Point.Double pt) {
        super(pt);
        Ellipse2D.Double bound = new Ellipse2D.Double(
            pt.x - PSGeneralNode.MIN_RADIUS, pt.y - PSGeneralNode.MIN_RADIUS,
            PSGeneralNode.MIN_RADIUS * 2, PSGeneralNode.MIN_RADIUS * 2);
        this.setBound(bound);
        this.setRadius(PSGeneralNode.MIN_RADIUS);
        this.mName = new ArrayList<PSPtCurve>();
    }
    
    // move the node to pt and fix with mMovePointX,Y
    @Override
    public void moveNode(Point.Double pt) {
        super.moveNode(pt);
        double dx = this.getCenter().x - this.getPrevCenter().x;
        double dy = this.getCenter().y - this.getPrevCenter().y;
        moveNamePtCurves(dx, dy);
    }
    
    // move name drawing in node
    public void moveNamePtCurves(double dx, double dy) {
        for (PSPtCurve pc : this.mName) {
            pc.movePtCurve(dx, dy);
        }
    }
    
    public void addNamePtCurve(PSPtCurve pc) {
        this.mName.add(pc);
    }
    
    public void clearNamePtCurve() {
        this.mName.clear();
    }
}
