package ps.cmd;

import ps.PSApp;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToAddCurPtCurveToEdgeCmd extends XLoggableCmd {
    
    //constructor
    private PSCmdToAddCurPtCurveToEdgeCmd(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToAddCurPtCurveToEdgeCmd cmd = 
            new PSCmdToAddCurPtCurveToEdgeCmd(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        if (app.getPtCurveMgr().getCurPtCurve() != null) {
            app.getEdgeMgr().getCurEdge().getCmd().addCmdPtCurve(
                app.getPtCurveMgr().getCurPtCurve());
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
