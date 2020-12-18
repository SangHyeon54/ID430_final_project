package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;

public class PSEdge {
    // constants
    public static final double INPUT_AND_NODE_OVERLAPPING_DISTANCE = 20;
    public static final int SELF_LOOP_DIAMETER = 50;
    public static final int GAP_BETW_SELF_LOOP_AND_NODE = 40;
//    public static final int ARROW_HEAD_LENGTH = 10;

    // field
    private Point.Double mCenter = null;
    public Point.Double getCenter() {
        return this.mCenter;
    }
    public void updateCenter() {
        double centerX = (mEndingPt.x + mStartOfArrow.x) / 2;
        double centerY = (mEndingPt.y + mStartOfArrow.y) / 2;
        this.mCenter = new Point.Double(centerX, centerY);
    }
    
    //MousePosition
    private Point.Double mStartingPt = null;
    private Point.Double mEndingPt = null;
    
    //self loop
    private boolean isSelfLoop = false;
    public boolean getSelfLoopCondition() {
        return isSelfLoop;
    }
    private Point.Double mSelfLoopPos = null;
    private double mSelfLoopStartAngle = 0;
    
    private Point.Double mInputPos = null;
    public Point.Double getInputPost() {
        return this.mInputPos;
    }
    
    private Point.Double mStartOfArrow = null;
    public Point.Double getStartOfArrow() {
        return this.mStartOfArrow;
    }
    
    public void moveStartOfArrow(double dx, double dy) {
        double newX = this.mStartOfArrow.x + dx;
        double newY = this.mStartOfArrow.y + dy;
        this.mStartOfArrow = new Point.Double(newX, newY);
        this.updateCenter();
        this.mInput.moveInput(dx, dy);
        this.mCmd.moveCmd(dx, dy);
    }
    public void moveEndingPt(double dx, double dy) {
        double newX = this.mEndingPt.x + dx;
        double newY = this.mEndingPt.y + dy;
        this.mEndingPt = new Point.Double(newX, newY);
        this.updateCenter();
        this.mCmd.moveCmd(dx, dy);
    }
    public void moveStartingPt(double dx, double dy) {
        double newX = this.mStartingPt.x + dx;
        double newY = this.mStartingPt.y + dy;
        this.mStartingPt = new Point.Double(newX, newY);
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
    
    //constructor
    public PSEdge (Point.Double pt, PSNode node) {
        this.mStartingPt = new Point.Double(pt.x, pt.y);
        this.mEndingPt = new Point.Double(pt.x, pt.y);
        this.mCenter = new Point.Double(pt.x, pt.y);
        this.mInput = new PSEdgeInput(pt);
        this.mCmd = new PSEdgeCmd(pt);
        this.mStartingNode = node;
        this.isSelfLoop = false;
        
        this.calculateStartOfArrow();
        this.calculateInputPos();
        this.mInput.setCenter(mInputPos);
    }
    
    public void drawEdge(Graphics2D g2, Color c, Stroke s) {
        g2.setColor(c);
        g2.setStroke(s);
        
        if (!isSelfLoop) {
            g2.draw(new Line2D.Double(
                mStartOfArrow.x, mStartOfArrow.y, mEndingPt.x, mEndingPt.y));
            drawArrowHead(g2);            
        } else {
            calculateSelfLoopStartAngle();
            calculateSelfLoopPos();
            g2.drawArc((int)mSelfLoopPos.x, (int)mSelfLoopPos.y, 
                SELF_LOOP_DIAMETER, SELF_LOOP_DIAMETER,
                (int)mSelfLoopStartAngle, 270);
            drawArrowHeadForSelfLoop(g2);
            calculateCmdPosForSelfLoop();
        }
        
        // if there is ending scene, then it means the edge is drawn and saved.
        // draw the return scene info and input
        if (mEndingNode != null) {
            int posX = (int) Math.round(this.getCenter().x);
            int posY = (int) Math.round(this.getCenter().y);

            this.mInput.drawInput(g2, c, s);
               //lets refactor this part lator its so messy
            if (isSelfLoop) {
                this.mCmd.updateEdgeCmd(new Point.Double(posX, posY));
            } else {
                this.mCmd.updateEdgeCmd(new Point.Double(posX, posY - 10));
                g2.drawString(this.getReturnScene(), posX, posY + 10);
            }
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
    public void updateArrow(Point.Double pt, boolean cond) {
        this.isSelfLoop = cond;

        double centerX = (mEndingPt.x + mStartOfArrow.x) / 2;
        double centerY = (mEndingPt.y + mStartOfArrow.y) / 2;

        this.mEndingPt = pt;
        this.mCenter = new Point.Double(centerX, centerY);
        
        if (isSelfLoop) {
            System.out.println(mStartingNode.getCenter());
            System.out.println(mStartingNode.getCenterX());
        }
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
    
    private void calculateSelfLoopStartAngle() {
        double angle = Math.atan2(mStartOfArrow.y - mStartingNode.getCenterY(), 
            mStartOfArrow.x - mStartingNode.getCenterX());
        
        this.mSelfLoopStartAngle = 225 - Math.toDegrees(angle) ;
    }

    private void calculateSelfLoopPos() {
        double angle = Math.atan2(mStartOfArrow.y - mStartingNode.getCenterY(), 
            mStartOfArrow.x - mStartingNode.getCenterX());

        //corner position of the self loop
        double posX = mStartOfArrow.x - SELF_LOOP_DIAMETER / 2
            + GAP_BETW_SELF_LOOP_AND_NODE * Math.cos(angle);
        double posY = mStartOfArrow.y - SELF_LOOP_DIAMETER / 2
            + GAP_BETW_SELF_LOOP_AND_NODE * Math.sin(angle);
//        double posX = mStartOfArrow.x - r;
//        double posY = mStartOfArrow.y - r;

        this.mSelfLoopPos = new Point.Double(posX, posY);
    }
    
    private void drawArrowHeadForSelfLoop(Graphics2D g2) {
        double angleParallel = Math.atan2(mStartOfArrow.y - mStartingNode.getCenterY(), 
            mStartOfArrow.x - mStartingNode.getCenterX()) - 4 * Math.PI / 5;
        double anglePt0 = Math.atan2(mStartOfArrow.y - mStartingNode.getCenterY(), 
            mStartOfArrow.x - mStartingNode.getCenterX()) - 315 * Math.PI / 400;
        
        double angle1 = angleParallel - 0.5;
        double angle2 = angleParallel + 0.5;
        double length = 10;
        double pt0x = mStartOfArrow.x - 29 * Math.cos(anglePt0);
        double pt0y = mStartOfArrow.y - 29 * Math.sin(anglePt0);
        
        double pt1x = pt0x - length * Math.cos(angle1);
        double pt1y = pt0y - length * Math.sin(angle1);
        double pt2x = pt0x - length * Math.cos(angle2);
        double pt2y = pt0y - length * Math.sin(angle2);
        
        g2.draw(new Line2D.Double(
            pt1x, pt1y, pt0x, pt0y));      
        g2.draw(new Line2D.Double(
            pt0x, pt0y, pt2x, pt2y));
    }
    
    private void calculateCmdPosForSelfLoop() {
        double angle = Math.atan2(mStartOfArrow.y - mStartingNode.getCenterY(), 
            mStartOfArrow.x - mStartingNode.getCenterX());
        double d = SELF_LOOP_DIAMETER + GAP_BETW_SELF_LOOP_AND_NODE;

        //corner position of the self loop
        double posX = mStartOfArrow.x
            + d * Math.cos(angle);
        double posY = mStartOfArrow.y
            + d * Math.sin(angle);
//        double posX = mStartOfArrow.x - r;
//        double posY = mStartOfArrow.y - r;

        this.mCenter = new Point.Double(posX, posY);
    }
}
