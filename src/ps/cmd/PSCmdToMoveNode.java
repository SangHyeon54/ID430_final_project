package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSGeneralNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToMoveNode extends XLoggableCmd{
    
    private Point mScreenPt = null;
    private Point.Double mWorldPt = null;
    private PSGeneralNode mNode = null;
    
    //private constructor
    private PSCmdToMoveNode(XApp app, Point pt, PSGeneralNode node) {
        super(app);
        this.mScreenPt = pt;
        this.mNode = node;
    }
    
    public static boolean execute(XApp app, Point pt, PSGeneralNode node) {
        PSCmdToMoveNode cmd = 
            new PSCmdToMoveNode(app, pt, node);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;        
        this.mWorldPt = app.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        this.mNode.moveNode(mWorldPt);
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append(this.mNode).append("\t");
        sb.append(this.mScreenPt).append("\t");
        sb.append(this.mWorldPt).append("\t");
        return sb.toString();
    }
    
}
