package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSNode;
import ps.PSXform;
import ps.scenario.PSDrawNodeScenario;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToUpdateNodeRadius extends XLoggableCmd {
        //fields
    private Point mScreenPt = null;
    private Point.Double mWorldPt = null;

    //private constructor
    private PSCmdToUpdateNodeRadius(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        PSCmdToUpdateNodeRadius cmd = new PSCmdToUpdateNodeRadius(app, pt);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        PSNode curNode = app.getNodeMgr().getCurNode();
        Point2D.Double nodeCenter = 
            app.getNodeMgr().getCurNode().getCenter();
        Point CenterScreenPt = app.getXform().calcPtFromWorldToScreen(
            nodeCenter);
        
        if (this.mScreenPt.distance(CenterScreenPt) < 
            PSNode.MIN_RADIUS) {
            return false;
        }
        if (this.mScreenPt.distance(CenterScreenPt) > 
            PSNode.MAX_RADIUS) {
            return false;
        }

        this.mWorldPt = app.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        curNode.updateEllipse(mWorldPt);
        
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