package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.util.ArrayList;

public class PSReturnNode extends PSNode {
    
    // constants
    public static final double RADIUS = 20;
    
    //constructor
    public PSReturnNode (Point.Double pt) {
        super(pt);
        Ellipse2D.Double bound = new Ellipse2D.Double(
            pt.x - PSReturnNode.RADIUS, pt.y - PSReturnNode.RADIUS,
            PSReturnNode.RADIUS * 2, PSReturnNode.RADIUS * 2);
        this.setBound(bound);
        this.setRadius(PSReturnNode.RADIUS);
    }
    
    // move the node to pt and fix with mMovePointX,Y
    @Override
    public void moveNode(Point.Double pt) {
        super.moveNode(pt);
        double dx = this.getCenter().x - this.getPrevCenter().x;
        double dy = this.getCenter().y - this.getPrevCenter().y;
    }
}
