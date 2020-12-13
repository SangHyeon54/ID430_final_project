package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.Math;
import java.util.ArrayList;

public class PSEdgeCmd extends Rectangle2D.Double {    
    // constants
    public static final double WIDTH = 100;
    public static final double HEIGHT = 30;
    
    private Point.Double mCorneredCenter = null;
    public void setCorneredCenter(Point.Double pt) {
        this.mCorneredCenter = pt;
    }
    public Point.Double getCorneredCenter() {
        return this.mCorneredCenter;
    }
    
    private boolean isInvisible = false;
    public void setVisible() {
        this.isInvisible = false;
    }
    public void setInvisible() {
        this.isInvisible = true;
    }
    
    private ArrayList<PSPtCurve> mCmdPtCurve = null;
    public ArrayList<PSPtCurve> getCmdPtCurve() {
        return this.mCmdPtCurve;
    }
    
    //constructor
    public PSEdgeCmd (Point.Double pt) {
        super(pt.x, pt.y, WIDTH, HEIGHT);
        this.mCorneredCenter = pt;
        this.mCmdPtCurve = new ArrayList<PSPtCurve>();
    }
    
    public void drawCmd(Graphics2D g2, Color c, Stroke s) {     
        this.setFrame(mCorneredCenter.x, mCorneredCenter.y,
            WIDTH, HEIGHT);   
        g2.setPaint(PSCanvas2D.COLOR_NODE_ELLIPSE_BG);
        g2.fill (this);
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(this);
    }
    
    public void updateEdgeCmd(Point.Double pt) {
        this.mCorneredCenter.x = pt.x - WIDTH / 2;
        this.mCorneredCenter.y = pt.y - HEIGHT;
        this.setFrame(mCorneredCenter.x, mCorneredCenter.y,
            WIDTH, HEIGHT);
    }
    
    
    public void addCmdPtCurve(PSPtCurve pc) {
        this.mCmdPtCurve.add(pc);
    }
    
    public void clearCmdPtCurve() {
        this.mCmdPtCurve.clear();
    }
}
