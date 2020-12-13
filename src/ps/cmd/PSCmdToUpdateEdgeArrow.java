package ps.cmd;

import java.awt.Point;
import ps.PSApp;
import ps.PSEdge;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToUpdateEdgeArrow extends XLoggableCmd {
        //fields
    private Point mScreenPt = null;
    private Point.Double mWorldPt = null;

    //private constructor
    private PSCmdToUpdateEdgeArrow(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        PSCmdToUpdateEdgeArrow cmd = new PSCmdToUpdateEdgeArrow(app, pt);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        PSEdge curEdge = app.getEdgeMgr().getCurEdge();
//        Point2D.Double nodeCenter = curNode.getCenter();
        
//        this.mWorldPt = app.getXform().calcPtFromScreenToWorld(this.mScreenPt);
//        
//        Point CenterScreenPt = app.getXform().calcPtFromWorldToScreen(
//            nodeCenter);
//        
//        if (this.mWorldPt.distance(nodeCenter) < 
//            PSNode.MIN_RADIUS) {
//            return false;
//        }
//        if (this.mWorldPt.distance(nodeCenter) > 
//            PSNode.MAX_RADIUS) {
//            return false;
//        }
//
        mWorldPt = app.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        curEdge.updateArrow(mWorldPt);
        
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append(this.mScreenPt).append("\t");
        sb.append(this.mWorldPt).append("\t");
        return sb.toString();
    }
}
