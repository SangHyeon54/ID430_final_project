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
    private boolean isSelfLoopCondition = false;

    //private constructor
    private PSCmdToUpdateEdgeArrow(XApp app, Point pt, boolean cond) {
        super(app);
        this.mScreenPt = pt;
        this.isSelfLoopCondition = cond;
    }
    
    public static boolean execute(XApp app, Point pt, boolean cond) {
        PSCmdToUpdateEdgeArrow cmd = new PSCmdToUpdateEdgeArrow(app, pt, cond);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        PSEdge curEdge = app.getEdgeMgr().getCurEdge();
        mWorldPt = app.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        curEdge.updateArrow(mWorldPt, isSelfLoopCondition);
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
