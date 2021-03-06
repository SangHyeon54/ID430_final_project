package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class PSEdgeCmd extends Rectangle2D.Double {    
    // constants
    public static final double WIDTH = 150;
    public static final double HEIGHT = 50;
    
    private Point.Double mCorneredCenter = null;
    public void setCorneredCenter(Point.Double pt) {
        this.mCorneredCenter = pt;
    }
    public Point.Double getCorneredCenter() {
        return this.mCorneredCenter;
    }
    
    private boolean isVisible = false;
    public void setVisible() {
        this.isVisible = true;
    }
    public void setInvisible() {
        this.isVisible = false;
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
        this.isVisible = true;
    }
    
    public void drawCmd(Graphics2D g2, Color c, Stroke s) {   
        //draw only when it is visible
        if (this.isVisible) {
            this.setFrame(mCorneredCenter.x, mCorneredCenter.y,
            WIDTH, HEIGHT);   
            g2.setPaint(PSCanvas2D.COLOR_CUR_EDGE_BG);
            g2.fill (this);
            g2.setColor(c);
            g2.setStroke(s);
            g2.draw(this);
        }
    }
    
    // update the Cmd Position in center between start and end pt
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
    
    // move Cmd Position half of point's distance.
    // because one of points is moved at once.
    public void moveCmd(double dx, double dy) {
        for(PSPtCurve pc : this.mCmdPtCurve) {
            pc.movePtCurve(dx/2, dy/2);
        }
    }
}
