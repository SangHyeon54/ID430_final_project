package ps.cmd;

import ps.PSApp;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToClearCurEdgeInput extends XLoggableCmd {
    
    //private constructor
    private PSCmdToClearCurEdgeInput(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToClearCurEdgeInput cmd = new PSCmdToClearCurEdgeInput(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        app.getEdgeMgr().getCurEdge().getInput().clearInputPtCurve();
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
