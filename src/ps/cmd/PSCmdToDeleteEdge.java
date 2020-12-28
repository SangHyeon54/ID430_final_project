package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSEdge;
import ps.PSEdgeMgr;
import ps.PSGeneralNode;
import ps.PSNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToDeleteEdge extends XLoggableCmd {
    private PSEdge mEdge = null;
    private PSNode mStartNode = null;
    private PSNode mEndNode = null;
    
    //private constructor
    private PSCmdToDeleteEdge(XApp app, PSEdge edge, PSNode startNode, 
        PSNode endNode) {
        super(app);
        this.mEdge = edge;
        this.mStartNode = startNode;
        this.mEndNode = endNode;
    }
    
    public static boolean execute(XApp app, PSEdge edge, PSNode startNode, 
        PSNode endNode) {
        PSCmdToDeleteEdge cmd = new PSCmdToDeleteEdge(app, edge, 
            startNode, endNode);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        // delete the edge info in the related nodes.
        this.mStartNode.removeEdgeStart(this.mEdge);
        this.mEndNode.removeEdgeEnd(this.mEdge);
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
