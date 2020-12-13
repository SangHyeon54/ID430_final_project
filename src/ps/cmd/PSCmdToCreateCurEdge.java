package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSEdge;
import ps.PSNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToCreateCurEdge extends XLoggableCmd {
    private Point mScreenPt = null;
    private Point2D.Double mWorldPt = null;
    
    //private constructor
    private PSCmdToCreateCurEdge(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        PSCmdToCreateCurEdge cmd = new PSCmdToCreateCurEdge(app, pt);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        this.mWorldPt = app.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        PSEdge mPSEdge = new PSEdge(this.mWorldPt);
        app.getEdgeMgr().setCurEdge(mPSEdge);
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append(this.mScreenPt).append("\t");
        sb.append(this.mWorldPt);
        return sb.toString();
    }
}
