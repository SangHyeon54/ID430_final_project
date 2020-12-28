package ps.cmd;

import ps.PSApp;
import ps.PSGeneralNode;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToAddCurPtCurveToNodeName extends XLoggableCmd {
    
    //constructor
    private PSCmdToAddCurPtCurveToNodeName(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToAddCurPtCurveToNodeName cmd = 
            new PSCmdToAddCurPtCurveToNodeName(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        if (app.getPtCurveMgr().getCurPtCurve() != null) {
            PSGeneralNode cNode = (PSGeneralNode) app.getNodeMgr().getCurNode();
            cNode.addNamePtCurve(app.getPtCurveMgr().getCurPtCurve());
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
