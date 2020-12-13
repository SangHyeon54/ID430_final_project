package ps.cmd;

import ps.PSApp;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToClearCurEdgeCmd extends XLoggableCmd {
    
    //private constructor
    private PSCmdToClearCurEdgeCmd(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToClearCurEdgeCmd cmd = new PSCmdToClearCurEdgeCmd(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        app.getEdgeMgr().getCurEdge().getCmd().clearCmdPtCurve();
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
