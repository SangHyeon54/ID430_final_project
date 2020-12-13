package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.util.ArrayList;

public class PSEdge {
    // constants
    public static final double INPUT_AND_NODE_OVERLAPPING_DISTANCE = 3;

    // field
    private Point.Double mCenter = null;
    public Point.Double getCenter() {
        return this.mCenter;
    }
    
    //MousePosition
    private Point.Double mStartingPt = null;
    private Point.Double mEndingPt = null;
    
    private Point.Double mInputPos = null;
    public Point.Double getInputPost() {
        return this.mInputPos;
    }
    
    private Point.Double mStartOfArrow = null;
    public Point.Double getStartOfArrow() {
        return this.mStartOfArrow;
    }
    
    private PSNode mStartingNode;
    public PSNode getStartingNode() {
        return this.mStartingNode;
    }
    public void setStartingNode(PSNode node) {
        this.mStartingNode = node;
    }
    
    private PSNode mEndingNode;
    public PSNode getEndingNode() {
        return this.mEndingNode;
    }
    public void setEndingNode(PSNode node) {
        this.mEndingNode = node;
    }
    
    private PSEdgeInput mInput;
    public PSEdgeInput getInput() {
        return mInput;
    }
    
    private PSEdgeCmd mCmd;
    public PSEdgeCmd getCmd() {
        return mCmd;
    }
//    
//    private ArrayList<PSPtCurve> mEdgeInput = null;
//    public ArrayList<PSPtCurve> getEdgeInput() {
//        return this.mEdgeInput;
//    }
    
//    private ArrayList<PSPtCurve> mEdgeCmd = null;
//    public ArrayList<PSPtCurve> getEdgeCmd() {
//        return this.mEdgeCmd;
//    }
    
    //constructor
    public PSEdge (Point.Double pt, PSNode node) {
        this.mStartingPt = new Point.Double(pt.x, pt.y);
        this.mEndingPt = new Point.Double(pt.x, pt.y);
        this.mCenter = new Point.Double(pt.x, pt.y);
//        this.mEdgeInput = new ArrayList<PSPtCurve>();
        this.mInput = new PSEdgeInput(pt);
        this.mCmd = new PSEdgeCmd(pt);
        this.mStartingNode = node;
        
        this.calculateStartOfArrow();
        this.calculateInputPos();
        this.mInput.setCenter(mInputPos);
    }
    
    public void drawEdge(Graphics2D g2, Color c, Stroke s) {
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(new Line2D.Double(
            mStartOfArrow.x, mStartOfArrow.y, mEndingPt.x, mEndingPt.y));
        drawArrowHead(g2);
        
        // if there is ending scene, then it means the edge is drawn and saved.
        // draw the return scene info and input
        if (mEndingNode != null) {
            int posX = (int) Math.round(this.getCenter().x);
            int posY = (int) Math.round(this.getCenter().y);

            g2.drawString(this.getReturnScene(), posX, posY + 10);
            this.mInput.drawInput(g2, c, s);
            this.mCmd.updateEdgeCmd(new Point.Double(posX, posY - 10));
            this.mCmd.drawCmd(g2, c, s);
        }        
    }
    
    private void drawArrowHead(Graphics2D g2) {
        double dx = mEndingPt.x - mStartingPt.x;
        double dy = mEndingPt.y - mStartingPt.y;
        double angleParallel = Math.atan2(dy, dx);
        
        double angle1 = angleParallel - 0.5;
        double angle2 = angleParallel + 0.5;
        double length = 10;
        double pt1x = mEndingPt.x - length * Math.cos(angle1);
        double pt1y = mEndingPt.y - length * Math.sin(angle1);
        double pt2x = mEndingPt.x - length * Math.cos(angle2);
        double pt2y = mEndingPt.y - length * Math.sin(angle2);
        
        g2.draw(new Line2D.Double(
            pt1x, pt1y, mEndingPt.x, mEndingPt.y));      
        g2.draw(new Line2D.Double(
            mEndingPt.x, mEndingPt.y, pt2x, pt2y));
    }
    
    //update with a new point
    public void updateArrow(Point.Double pt) {
        double centerX = (mEndingPt.x + mStartOfArrow.x) / 2;
        double centerY = (mEndingPt.y + mStartOfArrow.y) / 2;
        
        this.mEndingPt = pt;
        this.mCenter = new Point.Double(centerX, centerY);
    }
    
    private String getReturnScene() {
        String mString;
        boolean startingNodeIsQuasi = this.mStartingNode.getIsQuasi();
        boolean endingNodeIsQuasi = this.mEndingNode.getIsQuasi();
        
        if (startingNodeIsQuasi && endingNodeIsQuasi) {
            mString = "R = this.R";
        } else if (!startingNodeIsQuasi && endingNodeIsQuasi) {
            mString = "R = this";
        } else {
            mString = "R = null";
        }
        
        return mString;
    }
    
    private void calculateStartOfArrow() {
        double cx = mStartingNode.getCenterX();
        double cy = mStartingNode.getCenterY();
        
        double r = mStartingNode.getRadius();
        double angle = Math.atan2(mStartingPt.y - cy, 
            mStartingPt.x - cx);
        
        double posX = cx + r * Math.cos(angle);
        double posY = cy + r * Math.sin(angle);      
        
        this.mStartOfArrow = new Point.Double(posX, posY);
    }
    
    private void calculateInputPos() {
        double distance = mInput.getRadius() 
            - INPUT_AND_NODE_OVERLAPPING_DISTANCE;
        double angle = Math.atan2(mStartOfArrow.y - mEndingPt.y, 
            mStartOfArrow.x - mEndingPt.x);
        
        double posX = mStartOfArrow.x + distance * Math.cos(angle);
        double posY = mStartOfArrow.y + distance * Math.sin(angle);      
        
        this.mInputPos = new Point.Double(posX, posY);
    }
    
//    // a helper function to calculate the position that is apart from
//    // start of arrow by the input distance and return the position
//    private Point.Double calcPtApartFromStartOfArrowBy(double distance) {
//        double angle = Math.atan2(mEndingPt.y - mStartingPt.y, 
//            mEndingPt.x - mStartingPt.x);
//        
//        double posX = mStartOfArrow.x + distance * Math.cos(angle);
//        double posY = mStartOfArrow.y + distance * Math.sin(angle);      
//        
//        return new Point.Double(posX, posY);
//    }
}
