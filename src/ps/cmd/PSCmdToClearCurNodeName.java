package ps.cmd;

import ps.PSApp;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToClearCurNodeName extends XLoggableCmd {
    
    //private constructor
    private PSCmdToClearCurNodeName(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToClearCurNodeName cmd = new PSCmdToClearCurNodeName(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        app.getNodeMgr().getCurNode().clearNamePtCurve();
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
