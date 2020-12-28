package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSEdge;
import ps.PSEdgeMgr;
import ps.PSGeneralNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToDeleteEdgeOfNode extends XLoggableCmd {
    private PSGeneralNode mNode = null;
    
    //private constructor
    private PSCmdToDeleteEdgeOfNode(XApp app, PSGeneralNode node) {
        super(app);
        this.mNode = node;
    }
    
    public static boolean execute(XApp app, PSGeneralNode node) {
        PSCmdToDeleteEdgeOfNode cmd = new PSCmdToDeleteEdgeOfNode(app, node);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        PSEdgeMgr edgeMgr = app.getEdgeMgr();
        for(PSEdge edge : this.mNode.getEdgeStart()) {
            edgeMgr.removeEdge(edge);
        }
        for(PSEdge edge : this.mNode.getEdgeEnd()) {
            edgeMgr.removeEdge(edge);
        }
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
