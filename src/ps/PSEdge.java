package ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;

public class PSEdge {
    // constants
    // even though they are all private, put them here for easier modification
    private static final double INPUT_AND_NODE_OVERLAPPING_DISTANCE = 20;
    private static final int SELF_LOOP_DIAMETER = 50;
    private static final int GAP_BETW_SELF_LOOP_AND_NODE = 40;
    private static final int DIST_BETW_ARROW_AND_CMD = 10;
    private static final int ARROW_HEAD_LENGTH = 10;
    private static final double ARROW_HEAD_ANGLE = 0.5;

    // mouse position
    private Point.Double mStartingPt = null;
    private Point.Double mEndingPt = null; // same es end of arrow
    
    // actual location of where the arrow begins
    private Point.Double mStartOfArrow = null;
    public Point.Double getStartOfArrow() {
        return this.mStartOfArrow;
    }
    
    private Point.Double mCenter = null;
    public Point.Double getCenter() {
        return this.mCenter;
    }
    
    private PSEdgeInput mInput;
    public PSEdgeInput getInput() {
        return mInput;
    }
    
    private Point.Double mInputPos = null;
    public Point.Double getInputPos() {
        return this.mInputPos;
    }
    
    private PSEdgeCmd mCmd;
    public PSEdgeCmd getCmd() {
        return mCmd;
    }
    
    private PSNode mStartingNode;
    public PSNode getStartingNode() {
        return this.mStartingNode;
    }
    public void setStartingNode(PSNode node) {
        this.mStartingNode = node;
    }
    
    // Angle of Input in Starting Node
    private double mAngle;
    public double getAngle() {
        return this.mAngle;
    }
    
    private PSNode mEndingNode;
    public PSNode getEndingNode() {
        return this.mEndingNode;
    }
    public void setEndingNode(PSNode node) {
        this.mEndingNode = node;
    }
    
    //self loop
    private boolean isSelfLoop = false;
    public boolean getSelfLoopCondition() {
        return isSelfLoop;
    }
    private Point.Double mSelfLoopPos = null;
    private double mSelfLoopStartAngle = 0;
    
    // minor helping functions to calculate position
    public void updateCenter() {
        double centerX = (mEndingPt.x + mStartOfArrow.x) / 2;
        double centerY = (mEndingPt.y + mStartOfArrow.y) / 2;
        this.mCenter = new Point.Double(centerX, centerY);
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
        
        double input_x = pt.x - node.getCenter().x;
        double input_y = pt.y - node.getCenter().y;
        this.mAngle = Math.atan((double)input_y / input_x);
        System.out.println(this.mAngle);
    }
    
    public void drawEdge(Graphics2D g2, Color c, Stroke s) {
        int selfLoopArrowAngle = 270;
        
        g2.setColor(c);
        g2.setStroke(s);
        
        if (!isSelfLoop) { // normal arrow
            g2.draw(new Line2D.Double(
                mStartOfArrow.x, mStartOfArrow.y, mEndingPt.x, mEndingPt.y));
            drawArrowHead(g2);            
        } else { // self loop arrow
            calculateSelfLoopStartAngle();
            calculateSelfLoopPos();
            calculateCmdPosForSelfLoop();
            g2.drawArc((int)mSelfLoopPos.x, (int)mSelfLoopPos.y, 
                SELF_LOOP_DIAMETER, SELF_LOOP_DIAMETER,
                (int)mSelfLoopStartAngle, selfLoopArrowAngle);
            drawArrowHeadForSelfLoop(g2);
        }
        
        // when the edge is drawn and saved (when there is ending node),
        // draw the return scene info, input and cmd
        if (mEndingNode != null) {
            int posX = (int) Math.round(this.getCenter().x);
            int posY = (int) Math.round(this.getCenter().y);
            
            // input
            this.mInput.drawInput(g2, c, s);
            
            // cmd and return scene info
            // depending on self loop or not, draw cmd in different position
            if (isSelfLoop) {
                this.mCmd.updateEdgeCmd(new Point.Double(posX, posY));
            } else { // cmd on top of arrow and return scene on bottom
                this.mCmd.updateEdgeCmd(new Point.Double(
                    posX, posY - DIST_BETW_ARROW_AND_CMD));
                g2.drawString(this.getReturnScene(), 
                    posX - 20 , posY + DIST_BETW_ARROW_AND_CMD);
            }
            this.mCmd.drawCmd(g2, c, s);
        }        
    }
    
    private void drawArrowHead(Graphics2D g2) {
        double dx = mEndingPt.x - mStartingPt.x;
        double dy = mEndingPt.y - mStartingPt.y;
        double angleParallel = Math.atan2(dy, dx);
        
        double angle1 = angleParallel - ARROW_HEAD_ANGLE;
        double angle2 = angleParallel + ARROW_HEAD_ANGLE;
        double pt1x = mEndingPt.x - ARROW_HEAD_LENGTH * Math.cos(angle1);
        double pt1y = mEndingPt.y - ARROW_HEAD_LENGTH * Math.sin(angle1);
        double pt2x = mEndingPt.x - ARROW_HEAD_LENGTH * Math.cos(angle2);
        double pt2y = mEndingPt.y - ARROW_HEAD_LENGTH * Math.sin(angle2);
        
        g2.draw(new Line2D.Double(
            pt1x, pt1y, mEndingPt.x, mEndingPt.y));      
        g2.draw(new Line2D.Double(
            mEndingPt.x, mEndingPt.y, pt2x, pt2y));
    }
    
    private void drawArrowHeadForSelfLoop(Graphics2D g2) {                
        double tangentAngle = Math.atan2(
            mStartOfArrow.y - mStartingNode.getCenter().y, 
            mStartOfArrow.x - mStartingNode.getCenter().x) - 4 * Math.PI / 5;
        double anglePt0 = Math.atan2(mStartOfArrow.y 
            - mStartingNode.getCenter().y, 
            mStartOfArrow.x - mStartingNode.getCenter().x) - 63 * Math.PI / 80;
        
        double angle1 = tangentAngle - ARROW_HEAD_ANGLE;
        double angle2 = tangentAngle + ARROW_HEAD_ANGLE;
        
        double pt0x = mStartOfArrow.x - 29 * Math.cos(anglePt0);
        double pt0y = mStartOfArrow.y - 29 * Math.sin(anglePt0);
        double pt1x = pt0x - ARROW_HEAD_LENGTH * Math.cos(angle1);
        double pt1y = pt0y - ARROW_HEAD_LENGTH * Math.sin(angle1);
        double pt2x = pt0x - ARROW_HEAD_LENGTH * Math.cos(angle2);
        double pt2y = pt0y - ARROW_HEAD_LENGTH * Math.sin(angle2);
        
        g2.draw(new Line2D.Double(
            pt1x, pt1y, pt0x, pt0y));      
        g2.draw(new Line2D.Double(
            pt0x, pt0y, pt2x, pt2y));
    }

    //update with a new point
    public void updateArrow(Point.Double pt, boolean cond) {
        this.mEndingPt = pt;
        this.isSelfLoop = cond;
  
        double centerX = (mEndingPt.x + mStartOfArrow.x) / 2;
        double centerY = (mEndingPt.y + mStartOfArrow.y) / 2;
        this.mCenter = new Point.Double(centerX, centerY);
    }
    
    private String getReturnScene() {
        String mString;
        boolean startingNodeIsQuasi = this.mStartingNode.getIsQuasi();
        boolean endingNodeIsQuasi = this.mEndingNode.getIsQuasi();
        
        // the return scene depends on mode of starting and ending node
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
        double cx = mStartingNode.getCenter().x;
        double cy = mStartingNode.getCenter().y;
        
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
    
    private void calculateSelfLoopPos() {
        double angle = Math.atan2(mStartOfArrow.y - mStartingNode.getCenter().y, 
            mStartOfArrow.x - mStartingNode.getCenter().x);

        //corner position of the self loop
        double posX = mStartOfArrow.x - SELF_LOOP_DIAMETER / 2
            + GAP_BETW_SELF_LOOP_AND_NODE * Math.cos(angle);
        double posY = mStartOfArrow.y - SELF_LOOP_DIAMETER / 2
            + GAP_BETW_SELF_LOOP_AND_NODE * Math.sin(angle);

        this.mSelfLoopPos = new Point.Double(posX, posY);
    }
    
    private void calculateSelfLoopStartAngle() {
        // depending on where the start of arrow is,
        // the input angle to create arc for self loop is different
        double angle = Math.atan2(mStartOfArrow.y - mStartingNode.getCenter().y, 
            mStartOfArrow.x - mStartingNode.getCenter().x);
        
        this.mSelfLoopStartAngle = 225 - Math.toDegrees(angle) ;
    }
    
    private void calculateCmdPosForSelfLoop() {
        double angle = Math.atan2(mStartOfArrow.y - mStartingNode.getCenter().y, 
            mStartOfArrow.x - mStartingNode.getCenter().x);
        double d = SELF_LOOP_DIAMETER + GAP_BETW_SELF_LOOP_AND_NODE;

        //corner position of the self loop
        double posX = mStartOfArrow.x
            + d * Math.cos(angle);
        double posY = mStartOfArrow.y
            + d * Math.sin(angle);

        this.mCenter = new Point.Double(posX, posY);
    }
    
    public void cutArrowEnd(double distance) {
        double dx = this.mEndingPt.x - this.mStartingPt.x;
        double dy = this.mEndingPt.y - this.mStartingPt.y;
        
        double cutX = dx / Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) * 
            distance;
        double cutY = dy / Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) * 
            distance;
        
        Point.Double newEndingPt = new Point.Double(this.mEndingPt.x - cutX, 
            this.mEndingPt.y - cutY);
        this.mEndingPt = newEndingPt;
    }
    
    public void calcArrowEnd() {
        this.mEndingPt = this.mEndingNode.getCenter();
        this.cutArrowEnd(this.mEndingNode.getRadius());
    }
    
    public void calcArrowStart(Point.Double pt) {
        double dx = pt.x - this.getStartingNode().getCenter().x;
        double dy = pt.y - this.getStartingNode().getCenter().y;
        this.mAngle = Math.atan((double)dy / dx);
        
        double new_x = this.mStartingNode.getCenter().x + Math.cos(this.mAngle);
        double new_y = this.mStartingNode.getCenter().y + Math.sin(this.mAngle);
        Point.Double new_pt = new Point.Double(new_x, new_y);
        
        this.mInput.moveInput(new_pt);
    }
}
