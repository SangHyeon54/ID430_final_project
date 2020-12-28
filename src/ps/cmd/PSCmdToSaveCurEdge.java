package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSEdge;
import ps.PSGeneralNode;
import ps.PSNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToSaveCurEdge extends XLoggableCmd {
    private Point mScreenPt = null;
    private Point2D.Double mWorldPt = null;
    private PSNode mNode = null;

    //private constructor
    private PSCmdToSaveCurEdge(XApp app, Point pt, PSNode node) {
        super(app);
        this.mScreenPt = pt;
        this.mNode = node;
    }
    
    public static boolean execute(XApp app, Point pt, PSNode node) {
        PSCmdToSaveCurEdge cmd = new PSCmdToSaveCurEdge(app, pt, node);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        PSEdge mPSEdge = app.getEdgeMgr().getCurEdge();
        mPSEdge.setEndingNode(mNode);
        this.mNode.addEdgeEnd(mPSEdge);
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
