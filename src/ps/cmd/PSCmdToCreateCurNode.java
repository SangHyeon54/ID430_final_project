package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSGeneralNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToCreateCurNode extends XLoggableCmd {
    private Point mScreenPt = null;
    private Point2D.Double mWorldPt = null;
    
    //private constructor
    private PSCmdToCreateCurNode(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        PSCmdToCreateCurNode cmd = new PSCmdToCreateCurNode(app, pt);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        this.mWorldPt = app.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        PSGeneralNode mPSNode = new PSGeneralNode(this.mWorldPt);
        app.getNodeMgr().setCurNode(mPSNode);
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
