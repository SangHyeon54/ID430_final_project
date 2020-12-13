package ps.cmd;

import ps.PSApp;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToAddCurEdgeToEdges extends XLoggableCmd {
    
    // fields
    private int mNumOfEdgesBef = Integer.MIN_VALUE;
    private int mNumOfEdgesAft = Integer.MIN_VALUE;
    
    //constructor
    private PSCmdToAddCurEdgeToEdges(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToAddCurEdgeToEdges cmd = 
            new PSCmdToAddCurEdgeToEdges(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        if (app.getEdgeMgr().getCurEdge() != null) {
            this.mNumOfEdgesBef = 
                app.getPtCurveMgr().getPtCurves().size();
            app.getEdgeMgr().getEdges().add(
                app.getEdgeMgr().getCurEdge());
            this.mNumOfEdgesAft = 
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
