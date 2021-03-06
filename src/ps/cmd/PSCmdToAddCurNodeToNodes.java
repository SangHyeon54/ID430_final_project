package ps.cmd;

import ps.PSApp;
import ps.PSGeneralNode;
import ps.PSReturnNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToAddCurNodeToNodes extends XLoggableCmd {
    
    // fields
    private int mNumOfNodesBef = Integer.MIN_VALUE;
    private int mNumOfNodesAft = Integer.MIN_VALUE;
    
    //constructor
    private PSCmdToAddCurNodeToNodes(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToAddCurNodeToNodes cmd = 
            new PSCmdToAddCurNodeToNodes(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        if (app.getNodeMgr().getCurNode() != null) {
            this.mNumOfNodesBef = 
                app.getNodeMgr().getGeneralNodes().size();
            if(app.getNodeMgr().getCurNode() instanceof PSGeneralNode) {
                app.getNodeMgr().getGeneralNodes().add(
                (PSGeneralNode) app.getNodeMgr().getCurNode());
            } else {
                app.getNodeMgr().getReturnNodes().add(
                (PSReturnNode) app.getNodeMgr().getCurNode());
            }
            
            this.mNumOfNodesAft = 
                app.getPtCurveMgr().getPtCurves().size();
            return true;
        }
        return false;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
